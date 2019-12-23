package com.lessu.xieshi.scan;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lessu.navigation.BarButtonItem;
import com.lessu.xieshi.R;
import com.lessu.xieshi.XieShiSlidingMenuActivity;
import com.raylinks.ModuleControl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectblueActivity extends XieShiSlidingMenuActivity implements View.OnClickListener {

    private static ModuleControl moduleControl = new ModuleControl();
    private byte flagCrc;
    private static String sConn;
    private static String sPower;
    private static String sFrequency;
    private boolean connFlag = false;


    private Button switchBT;
    //private Button searchDevices;
    private BluetoothAdapter bluetoothAdapter;
    private ListView unbondDevices;
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
        handleButtonItem.setOnClickMethod(this,"serachlanya");
        navigationBar.setRightBarItem(handleButtonItem);
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(moduleControl.UhfReaderDisconnect()){
            System.out.println("断开成功。。。");
        }else{
            connFlag = false;
            System.out.println("断开失败。。。");
        }
        Toast.makeText(this,"请连接设备！",Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        switchBT = (Button) this.findViewById(R.id.openBluetooth_tb);
        unbondDevices = (ListView) this.findViewById(R.id.unbondDevices);
        bondDevices = (ListView) this.findViewById(R.id.bondDevices);
        switchBT.setOnClickListener(this);
    }


    private void initData() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initIntentFilter();
        if (BlueisOpen()) {
            System.out.println("蓝牙有开");
            switchBT.setBackgroundResource(R.drawable.kai);
        }
        if (!BlueisOpen()) {
            System.out.println("蓝牙没开!");
            switchBT.setBackgroundResource(R.drawable.guan);
            //this.searchDevices.setEnabled(false);
        }
    }

    private boolean BlueisOpen() {
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

    private void initIntentFilter() {
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
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        ProgressDialog progressDialog = null;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    addBandDevices(device);

                    addBondDevicesToListView();
                } else {
                    addUnbondDevices(device);
                    addUnbondDevicesToListView();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //progressDialog = ProgressDialog.show(context, "请稍等...",
                //"搜索蓝牙设备中...", true);
                startAnima();


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                System.out.println("设备搜索完毕");
                endAnima();
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    System.out.println("--------打开蓝牙-----------");
                    switchBT.setBackgroundResource(R.drawable.kai);
                    //searchDevices.setEnabled(true);
                    bondDevices.setEnabled(true);
                    unbondDevices.setEnabled(true);
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    System.out.println("--------关闭蓝牙-----------");
                    Toast.makeText(ConnectblueActivity.this,"蓝牙断开了",Toast.LENGTH_SHORT).show();;
                    switchBT.setBackgroundResource(R.drawable.guan);
                    //searchDevices.setEnabled(false);
                    bondDevices.setEnabled(false);
                    unbondDevices.setEnabled(false);
                }
            }

        }

    };
    /**
     * 添加未绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addUnbondDevices(BluetoothDevice device) {
        System.out.println("未绑定设备名称" + device.getName());
        if (!unbondDeviceslist.contains(device)) {
            unbondDeviceslist.add(device);
        }
    }

    /**
     *添加已绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addBandDevices(BluetoothDevice device) {
        System.out.println("已绑定设备名称" + device.getName());
        if (!bondDeviceslist.contains(device)) {
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
                //intent.setClass(BluetoothActivity.this, PrintDataActivity.class);
                intent.setClass(ConnectblueActivity.this, PrintDataActivity.class);
                final String deviceMAC = device.getAddress();
                String name = device.getName();
                if(deviceMAC == "")
                    return;
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConnectblueActivity.this);
                    builder.setMessage("确认连接：\n\n" + name);
                    builder.setTitle("提示");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //打开蓝牙连接
                            byte[] bStatus = new byte[1];
                            if(moduleControl.UhfReaderConnect(deviceMAC, bStatus, flagCrc)){
                                connFlag = true;
                                System.out.println("连接成功。。。");



                                byte[] bPower = new byte[1];
                                if(moduleControl.UhfGetPower(bPower, flagCrc)){
                                    byte power = (byte) (bPower[0] & 0x7F);
                                    sPower = "功率：" + String.valueOf(power) + "dBm";
                                }else{
                                    sPower = "读取功率失败";
                                }

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {

                                    e.printStackTrace();
                                }
                            }else{
                                sConn = "连接失败";
                            }
                            if(connFlag)
                            {
                                Toast.makeText(getApplicationContext(), sConn+"\n"+sPower+"\n" +sFrequency, 0).show();
                            }else{
                                Toast.makeText(getApplicationContext(), sConn, 0).show();
                            }

                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.create().show();
                }
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
        unbondDevices.setAdapter(simpleAdapter);

        // 为每个item绑定监听，用于设备间的配对
        unbondDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                try {
                    Method createBondMethod = BluetoothDevice.class
                            .getMethod("createBond");
                    createBondMethod
                            .invoke(unbondDeviceslist.get(arg2));
                    // 将绑定好的设备添加的已绑定list集合
                    bondDeviceslist.add(unbondDeviceslist.get(arg2));
                    // 将绑定好的设备从未绑定list集合中移除
                    unbondDeviceslist.remove(arg2);
                    addBondDevicesToListView();
                    addUnbondDevicesToListView();
                } catch (Exception e) {
                    System.out.println("配对失败。。。。。。。");
                    Toast.makeText(ConnectblueActivity.this, "配对失败!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.openBluetooth_tb:
                if (!BlueisOpen()) {
                    //蓝牙关闭的情况
                    System.out.println("蓝牙关闭的情况");
                    openBluetooth();
                } else {
                    //蓝牙打开的情况
                    System.out.println("蓝牙打开的情况");
                    closeBluetooth();
                }
                break;
            //case R.id.searchDevices:
//
//				bondDeviceslist.clear();
//				unbondDeviceslist.clear();
//				bluetoothAdapter.startDiscovery();
            //break;
        }
    }
    public void serachlanya(){
        if(BlueisOpen()) {

            System.out.println("搜索蓝牙开始。。。。。。。。。。。。。。。。。");
            initAnima();
            bondDeviceslist.clear();
            unbondDeviceslist.clear();
            bluetoothAdapter.startDiscovery();
        }else{
            Toast.makeText(ConnectblueActivity.this,"请打开蓝牙",Toast.LENGTH_SHORT).show();
        }
    }

    private void initAnima() {
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.5f, 180f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 360f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2);
        objectAnimator = ObjectAnimator.ofPropertyValuesHolder(handleButtonItem, pvhRotation);
    }

    public void startAnima(){
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }
    public void endAnima(){
        objectAnimator.end();
    }


}
