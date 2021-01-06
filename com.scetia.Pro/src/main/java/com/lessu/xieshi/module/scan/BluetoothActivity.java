package com.lessu.xieshi.module.scan;

import android.Manifest;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.good.permission.util.PermissionUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class BluetoothActivity extends XieShiSlidingMenuActivity {
    private Switch blueSwitch;
    private BluetoothAdapter bluetoothAdapter;
    private ListView unbindDevices;
    private ListView bondDevices;
    private ArrayList<BluetoothDevice> unBondDevices = new ArrayList<>();
    private ArrayList<BluetoothDevice> bondDeviceslist = new ArrayList<>();
    private BarButtonItem handleButtonItem;
    private ObjectAnimator objectAnimator;
    public static final int BLUETOOTH_REQUEST_CODE=1;
    public static final int GPS_REQUEST_CODE=2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);
        this.setTitle("蓝牙连接");
        handleButtonItem = new BarButtonItem(this , R.drawable.shuaxinl );
        handleButtonItem.setOnClickMethod(this,"searchBlueDevices");
        navigationBar.setRightBarItem(handleButtonItem);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        blueSwitch.postDelayed(() -> searchBlueDevices(),300);
    }

    /**
     * 初始化按钮
     */
    private void initView() {
        blueSwitch = this.findViewById(R.id.switch_bluetooth);
        unbindDevices = this.findViewById(R.id.unbondDevices);
        bondDevices = this.findViewById(R.id.bondDevices);
        blueSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!blueSwitch.isPressed()){
                return;
            }
            if(isChecked){
                openBluetooth();
            }else{
                closeBluetooth();
            }
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initIntentFilter();
    }

    /**
     * 进入页面，先判断有没有授权定位权限，然后判断有没有开启gps，
     * 再判断有没有开启蓝牙，最后开始搜索
     */
    @PermissionNeed(Manifest.permission.ACCESS_FINE_LOCATION)
    public void searchBlueDevices(){
        if(isOpenGps()) {
            //如果开启了定位权限和gps,开始进行蓝牙判断
            initBluetooth();
        }else{
            //如果没有打开定位权限，引导用户去打开定位
            LSAlert.showDialog(this, "提示", "搜索蓝牙需要打开GPS", "去设置",
                    "取消",
                    new LSAlert.DialogCallback() {
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,GPS_REQUEST_CODE);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }

    /**
     * 是否打开了gps
     * @return
     */
    private boolean isOpenGps(){
        //android 6.0以后蓝牙搜索需要开启定位权限
        if(PermissionUtil.isOverMarshmallow()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return true;
    }

    /**
     * 初始化打开蓝牙
     */
    private void initBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            //蓝牙已经打开了
            blueSwitch.setChecked(true);
            blueToothStartDiscovery();
        }else {
            //如果蓝牙没有打开，提示用户是否打开蓝牙
            openBluetooth();
            blueSwitch.setChecked(true);
        }
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST_CODE);
    }

    /**
     * 开始搜索蓝牙
     */
    private void blueToothStartDiscovery(){
        if (objectAnimator != null) return;
        bondDeviceslist.clear();
        unBondDevices.clear();
        if (simpleAdapter != null && bondData != null) {
            bondData.clear();
            simpleAdapter.notifyDataSetChanged();
        }
        bluetoothAdapter.startDiscovery();
        ToastUtil.showShort("正在搜索蓝牙...");
    }
    /**
     * 需要定位权限
     */
    @PermissionDenied
    private void openLocation(int requestCode){
        LSAlert.showDialog(this, "提示", "搜索蓝牙需要打开定位权限", "去设置", "取消",
                new LSAlert.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        PermissionSettingPage.start(BluetoothActivity.this,true);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        bluetoothAdapter.disable();
    }

    public void initIntentFilter() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //注册广播接收器，接受并处理搜索结果
        registerReceiver(receiver, intentFilter);
    }
    /**
     * 蓝牙广播接收器
     */
    public  BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device==null) return;
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    //发现设备，已经被绑定过的
                    addBondDevices(device);
                    addBondDevicesToListView();
                } else {
                    //发现设备，未被绑定过
                    addUnbindDevices(device);
                    addUnbondDevicesToListView();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                startAnimator();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                endAnimator();
                ToastUtil.showShort("设备搜索完毕");
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    System.out.println("--------打开蓝牙-----------");
                    blueSwitch.setChecked(true);
                    bondDevices.setEnabled(true);
                    unbindDevices.setEnabled(true);
                    searchBlueDevices();
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    System.out.println("--------关闭蓝牙-----------");
                    Toast.makeText(BluetoothActivity.this,"蓝牙断开了",Toast.LENGTH_SHORT).show();
                    blueSwitch.setChecked(false);
                    bondDevices.setEnabled(false);
                    unbindDevices.setEnabled(false);
                }
            }
        }
    };
    /**
     * 添加未绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addUnbindDevices(BluetoothDevice device) {
        if (device.getName()!=null&&!unBondDevices.contains(device)) {
            unBondDevices.add(device);
        }
    }

    /**
     *添加已绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addBondDevices(BluetoothDevice device) {
        if (device.getName()!=null&&!bondDeviceslist.contains(device)) {
            bondDeviceslist.add(device);
        }
    }

    /**
     * 添加已绑定蓝牙设备到ListView
     */
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, Object>> bondData;
    private void addBondDevicesToListView() {
        bondData = new ArrayList<>();
        int count = bondDeviceslist.size();
        System.out.println("已绑定设备数量" + count);
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("deviceName", bondDeviceslist.get(i).getName());
            bondData.add(map);// 把item项的数据加到data中
        }
        String[] from = { "deviceName" };
        int[] to = { R.id.device_name };
        simpleAdapter = new SimpleAdapter(this, bondData,
                R.layout.bonddevice_item, from, to);
        // 把适配器装载到listView中
        bondDevices.setAdapter(simpleAdapter);

        bondDevices.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            BluetoothDevice device = bondDeviceslist.get(arg2);
            Intent intent = new Intent();
            intent.putExtra("deviceAddress", device.getAddress());
            if(AppApplication.isGLY){
                intent.setClass(BluetoothActivity.this, PrintDataActivity.class);
            }else{
                intent.setClass(BluetoothActivity.this, YangpinshibieActivity.class);
            }
            startActivity(intent);
            bluetoothAdapter.cancelDiscovery();

        });

    }

    /**
     *添加未绑定蓝牙设备到ListView
     */
    private void addUnbondDevicesToListView() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int count = unBondDevices.size();
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deviceName", unBondDevices.get(i).getName());
            data.add(map);// 把item项的数据加到data中
        }
        String[] from = { "deviceName" };
        int[] to = { R.id.undevice_name };
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
                R.layout.unbonddevice_item, from, to);

        //把适配器装载到listView中
        unbindDevices.setAdapter(simpleAdapter);
        // 为每个item绑定监听，用于设备间的配对
        unbindDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                try {
                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                    createBondMethod.invoke(unBondDevices.get(arg2));
                    // 将绑定好的设备添加的已绑定list集合
                    bondDeviceslist.add(unBondDevices.get(arg2));
                    // 将绑定好的设备从未绑定list集合中移除
                    unBondDevices.remove(arg2);
                    addBondDevicesToListView();
                    addUnbondDevicesToListView();
                } catch (Exception e) {
                    ToastUtil.showShort("配对失败");
                }
            }
        });
    }


    /**
     * 初始化右上角按钮动画
     */
    private void initAnimator() {
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.5f, 180f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 360f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
        objectAnimator = ObjectAnimator.ofPropertyValuesHolder(handleButtonItem, pvhRotation);
    }

    /**
     * 右上角搜索按钮开启旋转动画
     */
    public void startAnimator() {
        if (objectAnimator == null) {
            initAnimator();
        }
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    /**
     * 结束右上角按钮旋转动画
     */
    public void endAnimator(){
        if(objectAnimator!=null) {
            objectAnimator.end();
            objectAnimator=null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_REQUEST_CODE) {//拒绝打开蓝牙
            if (resultCode == RESULT_CANCELED) {
                blueSwitch.setChecked(false);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        endAnimator();
        if(bluetoothAdapter!=null) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(receiver);
    }
}
