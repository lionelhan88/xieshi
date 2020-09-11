package com.lessu.xieshi.scan;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.lessu.navigation.BarButtonItem;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.XieShiSlidingMenuActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class BluetoothActivity extends XieShiSlidingMenuActivity {
    private Switch blueSwitch;
    private BluetoothAdapter bluetoothAdapter;
    private ListView unbindDevices;
    private ListView bondDevices;
    private ArrayList<BluetoothDevice> unbondDeviceslist = new ArrayList<BluetoothDevice>();
    private ArrayList<BluetoothDevice> bondDeviceslist = new ArrayList<BluetoothDevice>();
    private BarButtonItem handleButtonItem;
    private ObjectAnimator objectAnimator;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_layout);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("蓝牙连接");
        handleButtonItem = new BarButtonItem(this , R.drawable.shuaxinl );
        handleButtonItem.setOnClickMethod(this,"searchBlueDevices");
        navigationBar.setRightBarItem(handleButtonItem);
        initView();
        initData();
    }

    /**
     * 初始化按钮
     */
    private void initView() {
        blueSwitch = (Switch) this.findViewById(R.id.switch_bluetooth);
        unbindDevices = (ListView) this.findViewById(R.id.unbondDevices);
        bondDevices = (ListView) this.findViewById(R.id.bondDevices);
        blueSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!blueSwitch.isPressed()){
                    return;
                }
                if(isChecked){
                    openBluetooth();
                }else{
                    closeBluetooth();
                }
            }
        });
    }

    /**
     * 初始化打开蓝牙
     */
    private void initData() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initIntentFilter();
        if (BlueIsOpen()) {
            //蓝牙已经打开了，直接搜索蓝牙设备
            blueSwitch.setChecked(true);
            searchBlueDevices();
        }else {
            //如果蓝牙没有打开，提示用户是否打开蓝牙
            openBluetooth();
            blueSwitch.setChecked(true);
        }
    }

    /**
     * 检测蓝牙是否打开
     * @return
     */
    private boolean BlueIsOpen() {
        return this.bluetoothAdapter.isEnabled();
    }
    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
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
        if (device.getName()!=null&&!unbondDeviceslist.contains(device)) {
            unbondDeviceslist.add(device);
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
    private void addBondDevicesToListView() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int count = bondDeviceslist.size();
        System.out.println("已绑定设备数量" + count);
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deviceName", bondDeviceslist.get(i).getName());
            data.add(map);// 把item项的数据加到data中
        }
        String[] from = { "deviceName" };
        int[] to = { R.id.device_name };
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
                R.layout.bonddevice_item, from, to);
        // 把适配器装载到listView中
        bondDevices.setAdapter(simpleAdapter);

        bondDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
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

            }
        });

    }

    /**
     *添加未绑定蓝牙设备到ListView
     */
    private void addUnbondDevicesToListView() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int count = unbondDeviceslist.size();
        System.out.println("未绑定设备数量" + count);
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deviceName",unbondDeviceslist.get(i).getName());
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
                    createBondMethod.invoke(unbondDeviceslist.get(arg2));
                    // 将绑定好的设备添加的已绑定list集合
                    bondDeviceslist.add(unbondDeviceslist.get(arg2));
                    // 将绑定好的设备从未绑定list集合中移除
                    unbondDeviceslist.remove(arg2);
                    addBondDevicesToListView();
                    addUnbondDevicesToListView();
                } catch (Exception e) {
                    ToastUtil.showShort("配对失败");
                }
            }
        });
    }

    /**
     * 开始搜索蓝牙
     */
    public void searchBlueDevices(){
        if(BlueIsOpen()) {
            if(objectAnimator!=null) return;
            bondDeviceslist.clear();
            unbondDeviceslist.clear();
            bluetoothAdapter.startDiscovery();
            ToastUtil.showShort("正在搜索蓝牙...");
        }else{
            openBluetooth();
        }
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
        if(requestCode==1){
            //拒绝打开蓝牙
            if(resultCode==RESULT_CANCELED){
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
