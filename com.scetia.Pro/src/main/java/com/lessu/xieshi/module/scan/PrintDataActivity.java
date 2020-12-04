package com.lessu.xieshi.module.scan;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.Utils.FileUtil;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.JieMi;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.Utils.LongString;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.Utils.UriUtils;
import com.lessu.xieshi.view.DragLayout;
import com.raylinks.Function;
import com.raylinks.ModuleControl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintDataActivity extends NavigationActivity implements View.OnClickListener {

    ModuleControl moduleControl = new ModuleControl();
    Function fun = new Function();
    /**
     * 正在读取设备返回值的标识
     */
    private  boolean isReading = true;
    private static byte flagCrc;
    private boolean isConnection = false;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public static BluetoothSocket bluetoothSocket = null;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public BluetoothDevice device;
    private static InputStream inputStream;
    private SoundPool soundPool;
    private ArrayList<String> Tal = new ArrayList<>();
    private ArrayList<String> Xal = new ArrayList<>();
    private TMAdapter madaptertiaoma;
    private XPAdapter madapterxinpian;
    private SwipeMenuCreator creator;
    private DragLayout dl;
    private TextView tv_tiaoma_num;
    private TextView tv_xinpian_num;
    private String uidStr;
    private int j = 1;
    /**
     * 标识是jar方法连接的蓝牙
     */
    private boolean uhfBlueConnect;
    private boolean lianjieshibai;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printdata_layout);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("标识扫描");
        //设置侧滑菜单
        dl = findViewById(R.id.dl);
        BarButtonItem menuButtonitem = new BarButtonItem(this, R.drawable.icon_navigation_menu);
        menuButtonitem.setOnClickMethod(this, "menuButtonDidClick");
        navigationBar.setLeftBarItem(menuButtonitem);
        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setWidth((130));
                deleteItem.setIcon(R.drawable.shanchu);
                menu.addMenuItem(deleteItem);
            }
        };
        initView();
        initData();
        initSoundPool();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        LinearLayout ll_shenqingshangbao = findViewById(R.id.ll_shenqingshangbao);
        LinearLayout ll_shenhexiazai = findViewById(R.id.ll_shenhexiazai);
        LinearLayout ll_rukuchakan = findViewById(R.id.ll_rukuchakan);
        LinearLayout ll_shebeixinxi = findViewById(R.id.ll_shebeixinxi);
        SeekBar sb_scan = findViewById(R.id.sb_scan);

        TextView tv_baocun = findViewById(R.id.tv_baocun);
        TextView tv_duqv = findViewById(R.id.tv_duqv);
        TextView tv_qingchu = findViewById(R.id.tv_qingchu);
        TextView tv_shujvjiaohu = findViewById(R.id.tv_shujvjiaohu);
        tv_tiaoma_num = findViewById(R.id.tv_tiaoma_num);
        tv_xinpian_num = findViewById(R.id.tv_xinpian_num);


        SwipeMenuListView lv_tiaoma = findViewById(R.id.lv_tiaoma);
        SwipeMenuListView lv_xinpian = findViewById(R.id.lv_xinpian);

        lv_tiaoma.setMenuCreator(creator);
        lv_xinpian.setMenuCreator(creator);
        lv_tiaoma.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    Tal.remove(position);
                    madaptertiaoma.notifyDataSetChanged();
                    tv_tiaoma_num.setText(String.valueOf(Tal.size()));
                }
                return false;
            }
        });
        lv_xinpian.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {// delete
                    Xal.remove(position);
                    madapterxinpian.notifyDataSetChanged();
                    tv_xinpian_num.setText(String.valueOf(Xal.size()));
                }
                return false;
            }
        });

        lv_tiaoma.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lv_xinpian.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        madaptertiaoma = new TMAdapter();
        madapterxinpian = new XPAdapter();
        lv_tiaoma.setAdapter(madaptertiaoma);
        lv_tiaoma.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                //长按删除当前项
                AlertDialog.Builder builder = new AlertDialog.Builder(PrintDataActivity.this);
                builder.setTitle("提示");
                builder.setMessage("是否删除"+Tal.get(pos)+"?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Tal.remove(pos);
                        dialogInterface.dismiss();
                        madaptertiaoma.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return false;
            }
        });
        lv_xinpian.setAdapter(madapterxinpian);
        //已扫条码数
        tv_tiaoma_num.setText(String.valueOf(Tal.size()));
        //已读芯片数
        tv_xinpian_num.setText(String.valueOf(Xal.size()));

        ll_shenqingshangbao.setOnClickListener(this);
        ll_shenhexiazai.setOnClickListener(this);
        ll_rukuchakan.setOnClickListener(this);
        ll_shebeixinxi.setOnClickListener(this);
        tv_baocun.setOnClickListener(this);
        tv_duqv.setOnClickListener(this);
        tv_qingchu.setOnClickListener(this);
        tv_shujvjiaohu.setOnClickListener(this);
    }

    /**
     * 左上角滑出抽屉按钮事件
     */
    public void menuButtonDidClick() {
        if (dl.getStatus() != DragLayout.Status.Close) {
            dl.close();
        } else {
            dl.open();
        }
    }
    /**
     * 初始化数据
     */
    private void initData() {
        // 一上来就先连接设备
        device = bluetoothAdapter.getRemoteDevice(getDeviceAddress());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设备连接成功，开始读取设备的uid
                startGetUid();
                if (Shref.getString(PrintDataActivity.this, getDeviceAddress(), null) == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (uhfBlueConnect) {
                                uhfReaderDisconnect();
                            }
                            ToastUtil.showShort("请返回重新获取设备编号");
                        }
                    });
                }
                boolean flag =connect();
                if (!flag) {
                    // 连接失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Shref.setString(PrintDataActivity.this, Shref.BLUETOOTH_DEVICE, null);
                            ToastUtil.showShort("连接失败，检查蓝牙是否打开，请返回重新连接");
                        }
                    });
                } else {
                    // 蓝牙连接成功并且成功读取到设备编号
                    toastAlert("获取设备号成功");
                    read();
                }
            }
        }).start();
    }

    /**
     * 初始化提示音播放
     */
    private void initSoundPool(){
        //当前系统的SDK版本大于等于21(Android 5.0)时
        if(Build.VERSION.SDK_INT > 21){
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频数量
            builder.setMaxStreams(2);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_SYSTEM);//STREAM_MUSIC
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        }else{
            soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
        }
        //已经存在
        soundPool.load(PrintDataActivity.this,R.raw.scan_existence,1);
        soundPool.load(PrintDataActivity.this,R.raw.scan_success,1);
    }
    /**
     * 播放提示音
     * @param isExistence 播放哪类提示音
     */
    private void playSoundPool(boolean isExistence){
        if(isExistence){
            soundPool.play(1,1,1,0,0,1);
        }else{
            soundPool.play(2,1,1,0,0,1);
        }
    }
    /**
     * 获取设备编号
     */
    private void startGetUid() {
        toastAlert("正在获取设备号...");
        getUid();
        while (Shref.getString(PrintDataActivity.this, getDeviceAddress(), null) == null) {
            if (uhfBlueConnect) {
                if (j < 5) {
                    getBluetoothDeviceId();
                } else {
                    j = 1;
                    while (uhfBlueConnect) {
                        toastAlert("正在获取设备号...");
                        uhfReaderDisconnect();
                    }
                }
            } else {
                getUid();
                if (lianjieshibai) {
                    return;
                }
            }
        }
    }
    /**
     * 获取设备编号
     */
    private void getUid() {
        if (Shref.getString(PrintDataActivity.this, getDeviceAddress(), null) != null) {
            uidStr = Shref.getString(PrintDataActivity.this, getDeviceAddress(), null);
            AppApplication.muidstr = uidStr;
            LogUtil.showLogE("从本地缓存获取的设备uid=="+uidStr);
            return;
        }
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            /*
              这里使用jar包中提供的方法连接蓝牙，因为要获取设备uid，必须要使用jar包
              中提供的方法，获取uid之前先要UhfReaderConnect连接蓝牙才可以
             */
            byte[] bStatus = new byte[1];
            if (moduleControl.UhfReaderConnect(getDeviceAddress(), bStatus, flagCrc)) {
                uhfBlueConnect = true;
                //jar包方法连接成功，开始获取设备uid
                getBluetoothDeviceId();
            } else {
                lianjieshibai = true;
                uhfReaderDisconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取蓝牙设备的uid
     * 因为有新旧两种设备，所以后两个获取uid的方法
     */
    private void getBluetoothDeviceId() {
        toastAlert("正在获取设备号...");
        byte[] bUid = new byte[12];
        String newUidName;
        j++;
        if (moduleControl.UhfGetReaderUID(bUid, flagCrc)) {
            //先判断是否是旧设备，如果成功获取到uid，则是旧设备
            uidStr = fun.bytesToHexString(bUid, 12);
            LogUtil.showLogE("设备uid=="+uidStr);
            Shref.setString(PrintDataActivity.this, getDeviceAddress(), uidStr);
            AppApplication.muidstr = uidStr;
            if (uhfBlueConnect) {
                //获取uid成功后，要断开jar方式连接的蓝牙，准备自定义方式连接设备
                uhfReaderDisconnect();
            }
        } else if(!(newUidName=getNewUhfUID2()).equals("")){
            //之前的方法没有获取到uid，此时成功获取uid，则是新设备
            uidStr = newUidName;
            LogUtil.showLogE("设备uid=="+uidStr);
            Shref.setString(PrintDataActivity.this, getDeviceAddress(), uidStr);
            AppApplication.muidstr = uidStr;
            if (uhfBlueConnect) {
                //获取uid成功后，要断开jar方式连接的蓝牙，准备自定义方式连接设备
                uhfReaderDisconnect();
            }
        } else {
            if(j>=5) {
                toastAlert("未能获取手持机标识号");
            }
        }
    }

    /**
     * 获取新设备的uid方法
     * @return uidName 返回值uid
     */
    private String  getNewUhfUID2(){
        String uidName="";
        try {
            //获取设备uid命令
            byte[] writData = {(byte) 0xA0,0x03,0x01, 0x68, (byte) 0xF4};
            moduleControl.SendDataToBT(writData,writData.length);
            Thread.sleep(800);
            byte[] readBuffer = new byte[17];
            moduleControl.RecvDataFromBT(readBuffer);
            String backData = fun.bytesToHexString(readBuffer, readBuffer.length).toUpperCase();
            //读取蓝牙设备返回的数据，转换为16进制字符串
            String uidHexStr = backData.substring(8,backData.lastIndexOf("FFFFFFF9")).trim();
            //16进制转为十进制ascii码字符串
            uidName = LongString.hexStr2Str(uidHexStr);
            LogUtil.showLogE(uidName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uidName;
    }

    /**
     * 获取蓝牙地址
     * @return String
     */
    private String getDeviceAddress() {
        Intent intent = this.getIntent();
        String deviceAdd = Shref.getString(PrintDataActivity.this, Shref.BLUETOOTH_DEVICE, null);
        //是否存在已经缓存的地址，如果没有缓存的地址，重新获取上一个页面传来的地址
        if (deviceAdd == null) {
            deviceAdd = intent.getStringExtra("deviceAddress");
            Shref.setString(PrintDataActivity.this, Shref.BLUETOOTH_DEVICE, deviceAdd);
            LogUtil.showLogD("deviceAddress为null==" +deviceAdd);
        }
        return deviceAdd;
    }

    /**
     * 连接蓝牙设备
     */
    public boolean connect() {
        if (!isConnection) {
            try {
                //如果在搜寻蓝牙设备就关闭搜寻
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.isDiscovering();
                }
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                isConnection = true;
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnection = false;
                        Toast.makeText(PrintDataActivity.this, "连接失败!", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            toastAlert(device.getName()+ "连接成功!");
        }
        return true;
    }

    /**
     * 这个方法是断开jar包方法中连接的蓝牙
     */
    private void uhfReaderDisconnect() {
        if (moduleControl.UhfReaderDisconnect()) {
            uhfBlueConnect = false;
        } else {
            uhfReaderDisconnect();
        }
    }


    /**
     * 读取蓝牙设备扫描到的芯片或者条码数据
     */
    private void read() {
        if (this.isConnection) {
            System.out.println("开始打印！！");
            try {
                inputStream = bluetoothSocket.getInputStream();
                String Ts;
                String Ts1 = null;
                String Ts2;

                boolean saolebiedetiaoma = false;
                byte[] buffer = new byte[1024];
                byte[] buffer2;
                //这里会一直等待读取
                while (isReading) {
                    int bytes;
                    do {
                        bytes = inputStream.read(buffer);
                    } while (bytes <= 0);
                    buffer2 = (byte[]) buffer.clone();
                    //清空缓存
                    Arrays.fill(buffer, (byte) 0);
                    //将缓冲区读取的字节数组转为字符串
                    String ceshishujv;
                    ceshishujv = new String(buffer2, 0, buffer2.length);
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                    Matcher m = p.matcher(ceshishujv);
                    ceshishujv = m.replaceAll("").trim();
                    //条形码
                    if (!ceshishujv.equals("")) {
                        if (ceshishujv.matches("^[0-9]*$")) {
                            System.out.println("是条形码");
                            if (ceshishujv.length() < 10) {
                                if (Ts1 == null || Ts1.equals("")) {
                                    Ts1 = new String(buffer2, 0, ceshishujv.length());
                                    if (saolebiedetiaoma) {
                                        Ts1 = null;
                                        saolebiedetiaoma = false;
                                    }
                                } else {
                                    Ts2 = new String(buffer2, 0, ceshishujv.length());
                                    Log.e("PrintDataActivity", "Ts1..else..." + Ts1 + "，Ts2..else..." + Ts2);
                                    Ts = Ts1 + Ts2;
                                    String substring = Ts.substring(0, 1);
                                    if (Ts.length() == 10 && substring.equals("1")) {
                                        if (!Tal.contains(Ts)) {
                                            Tal.add(0,Ts);
                                            playSoundPool(false);
                                        }else{
                                            playSoundPool(true);
                                        }
                                        Ts1 = null;
                                        FileUtil.baocunauto(Tal, Xal);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                madaptertiaoma.notifyDataSetChanged();
                                                tv_tiaoma_num.setText(String.valueOf(Tal.size()));
                                            }
                                        });

                                    } else {
                                        Ts1 = null;
                                        saolebiedetiaoma = true;
                                    }
                                }
                            } else if (ceshishujv.length() == 10) {
                                if(!Tal.contains(ceshishujv)) {
                                    Tal.add(0, ceshishujv);
                                    playSoundPool(false);
                                }else{
                                    playSoundPool(true);
                                }
                                FileUtil.baocunauto(Tal, Xal);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        madaptertiaoma.notifyDataSetChanged();
                                        tv_tiaoma_num.setText(String.valueOf(Tal.size()));
                                    }
                                });
                            }
                            //是芯片
                        } else {
                            String s = LongString.bytes2HexString(buffer2);
                            System.out.println(s);
                            String jiexinpian = JieMi.jiexinpian(s);
                            if (jiexinpian != null) {
                                if (!Xal.contains(jiexinpian)) {
                                    Xal.add(0,jiexinpian);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            FileUtil.baocunauto(Tal, Xal);
                                        }
                                    });
                                    playSoundPool(false);
                                }else{
                                    playSoundPool(true);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        madapterxinpian.notifyDataSetChanged();
                                        tv_xinpian_num.setText(String.valueOf(Xal.size()));
                                    }
                                });
                            }
                        }
                    }
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort( "连接断开了，请重新连接");
                        finish();
                    }
                });
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort( "设备未连接，请重新连接！");
                    finish();
                }
            });
        }
    }

    /**
     * 弹出提示
     */
    private void toastAlert(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShort(message);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_shenqingshangbao:
                if (uidStr != null) {
                    StringBuilder s1 = new StringBuilder();
                    for (int i = 0; i < Tal.size(); i++) {
                        s1.append(Tal.get(i)).append(";");
                    }
                    for (int i = 0; i < Xal.size(); i++) {
                        s1.append(Xal.get(i)).append(";");
                    }
                    Intent intent1 = new Intent();
                    intent1.putExtra("talxal", s1.toString());
                    //传入uid
                    intent1.putExtra("uidstr",uidStr);
                    intent1.setClass(PrintDataActivity.this, ShenqingshangbaoActivity.class);
                    startActivity(intent1);
                } else {
                    Toast.makeText(PrintDataActivity.this, "设备编号为空", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ll_shenhexiazai:
                if (uidStr != null) {
                    Intent intent = new Intent(PrintDataActivity.this, ShenhexiazaiActivity.class);
                    intent.putExtra("uidstr",uidStr);
                    startActivity(intent);
                } else {
                    Toast.makeText(PrintDataActivity.this, "设备编号为空", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ll_rukuchakan:
                startActivity(new Intent(PrintDataActivity.this, RukuchakanActivity.class));
                break;
            case R.id.ll_shebeixinxi:
                if (uidStr == null) {
                    initData();
                }
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidStr);
                intent.setClass(PrintDataActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_baocun:
                FileUtil.saveScanFile(Tal, Xal);
                Toast.makeText(PrintDataActivity.this, "保存成功。", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_duqv:
                Intent intentread = new Intent(Intent.ACTION_GET_CONTENT);
                intentread.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intentread.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intentread, 1);
                break;
            case R.id.tv_qingchu:
                Xal.clear();
                Tal.clear();
                madaptertiaoma.notifyDataSetChanged();
                tv_tiaoma_num.setText(String.valueOf(Tal.size()) );
                madapterxinpian.notifyDataSetChanged();
                tv_xinpian_num.setText(String.valueOf(Xal.size()));
                break;
            case R.id.tv_shujvjiaohu:
                getUid();
                if (moduleControl.UhfReaderDisconnect()) {
                    System.out.println("断开成功");
                }
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < Tal.size(); i++) {
                    s.append(Tal.get(i)).append(";");
                }
                for (int i = 0; i < Xal.size(); i++) {
                    s.append(Xal.get(i)).append(";");
                }
                if (uidStr != null) {
                    Intent intentshujvjiaohu = new Intent();
                    intentshujvjiaohu.putExtra("uidstr", uidStr);
                    intentshujvjiaohu.putExtra("TALXAL", s.toString());
                    intentshujvjiaohu.setClass(PrintDataActivity.this, ShujvjiaohuActivity.class);
                    startActivity(intentshujvjiaohu);
                } else {
                    Toast.makeText(PrintDataActivity.this, "设备编号为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = "";
            if (uri != null) {
                path = UriUtils.getPath(this, uri);
            }
            if (path == null) {
                Toast.makeText(this, "文件路径为空！", Toast.LENGTH_LONG).show();
                return;
            }
            File file = new File(path);
            String duqv = FileUtil.readFile(this, file);
            if (duqv.length() != 0) {
                if (duqv.contains("xinpian")) {
                    String[] split = duqv.split("xinpian");
                    if (split.length > 0) {
                        String[] splitTiaoma = split[0].split(",");
                        Tal.clear();
                        Xal.clear();
                        System.out.println("split[0]..........." + split[0]);
                        for (String s : splitTiaoma) {
                            if (s.length() == 10) {
                                Tal.add(s);
                            } else {
                                Tal.clear();
                            }
                        }
                        if (split.length > 1) {
                            String[] splitxinpian = split[1].split(",");
                            Xal.addAll(Arrays.asList(splitxinpian));
                        }
                        madaptertiaoma.notifyDataSetChanged();
                        tv_tiaoma_num.setText(String.valueOf(Tal.size()));

                        madapterxinpian.notifyDataSetChanged();
                        tv_xinpian_num.setText(String.valueOf(Xal.size()));
                    } else {
                        Toast.makeText(PrintDataActivity.this, "所选文件无内容", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PrintDataActivity.this, "所选文件不符合", Toast.LENGTH_SHORT).show();
                }
            }
            Toast.makeText(PrintDataActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private class TMAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Tal.size();
        }

        @Override
        public Object getItem(int i) {
            return Tal.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHoldertiaoma holder;
            if (view == null) {
                holder = new ViewHoldertiaoma();
                view = View.inflate(PrintDataActivity.this, R.layout.item_scanlistview, null);
                holder.tv = (TextView) view.findViewById(R.id.tv_scanlistview);
                view.setTag(holder);
            } else {
                holder = (ViewHoldertiaoma) view.getTag();
            }
            holder.tv.setText(Tal.get(i));
            return view;
        }
    }

    static class ViewHoldertiaoma {
        TextView tv;
    }


    private class XPAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Xal.size();
        }

        @Override
        public Object getItem(int i) {
            return Xal.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolderxinpian holder;
            if (view == null) {
                holder = new ViewHolderxinpian();
                view = View.inflate(PrintDataActivity.this, R.layout.item_scanlistview, null);
                holder.tv = (TextView) view.findViewById(R.id.tv_scanlistview);
                view.setTag(holder);
            } else {
                holder = (ViewHolderxinpian) view.getTag();
            }
            holder.tv.setText(Xal.get(i));
            return view;
        }
    }

    static class ViewHolderxinpian {
        TextView tv;
    }


    @Override
    protected void onDestroy() {
        isReading = false;
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(soundPool!=null){
            soundPool.release();
        }
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (uhfBlueConnect) {
            uhfReaderDisconnect();
        }
        return super.onKeyDown(keyCode, event);
    }
}