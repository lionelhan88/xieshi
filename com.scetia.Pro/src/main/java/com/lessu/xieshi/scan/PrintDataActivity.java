package com.lessu.xieshi.scan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Changezifu;
import com.lessu.xieshi.Utils.JieMi;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.Utils.UriUtils;
import com.lessu.xieshi.customView.DragLayout;
import com.raylinks.Function;
import com.raylinks.ModuleControl;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintDataActivity extends NavigationActivity implements View.OnClickListener {

    ModuleControl moduleControl = new ModuleControl();
    Function fun = new Function();
    private static byte flagCrc;
    private boolean isConnection = false;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public static BluetoothSocket bluetoothSocket = null;
    private static final UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    public BluetoothDevice device;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    private ArrayList<String> Tal = new ArrayList<>();
    private ArrayList<String> Xal = new ArrayList<>();
    private MyAdaptertiaoma madaptertiaoma;
    private MyAdapterxinpian madapterxinpian;
    private LinearLayout ll_shenqingshangbao;
    private LinearLayout ll_shenhexiazai;
    private LinearLayout ll_rukuchakan;
    private LinearLayout ll_shebeixinxi;
    private SeekBar sb_scan;
    private SwipeMenuListView lv_tiaoma;
    private SwipeMenuListView lv_xinpian;
    private SwipeMenuCreator creator;
    private DragLayout dl;
    private TextView tv_baocun;
    private TextView tv_duqv;
    private TextView tv_qingchu;
    private TextView tv_shujvjiaohu;
    private TextView tv_tiaoma_num;
    private TextView tv_xinpian_num;
    private String sPower;
    private String uidStr;
    private boolean cuowulianjie;
    private boolean lianjieshibai;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printdata_layout);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("标识扫描");

        //设置侧滑菜单
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onDrag(float percent) {

            }
        });

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
    }


    private void initView() {
        ll_shenqingshangbao = (LinearLayout) findViewById(R.id.ll_shenqingshangbao);
        ll_shenhexiazai = (LinearLayout) findViewById(R.id.ll_shenhexiazai);
        ll_rukuchakan = (LinearLayout) findViewById(R.id.ll_rukuchakan);
        ll_shebeixinxi = (LinearLayout) findViewById(R.id.ll_shebeixinxi);
        sb_scan = (SeekBar) findViewById(R.id.sb_scan);
        sb_scan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tv_baocun = (TextView) findViewById(R.id.tv_baocun);
        tv_duqv = (TextView) findViewById(R.id.tv_duqv);
        tv_qingchu = (TextView) findViewById(R.id.tv_qingchu);
        tv_shujvjiaohu = (TextView) findViewById(R.id.tv_shujvjiaohu);
        tv_tiaoma_num = (TextView) findViewById(R.id.tv_tiaoma_num);
        tv_xinpian_num = (TextView) findViewById(R.id.tv_xinpian_num);


        lv_tiaoma = (SwipeMenuListView) findViewById(R.id.lv_tiaoma);
        lv_xinpian = (SwipeMenuListView) findViewById(R.id.lv_xinpian);

        lv_tiaoma.setMenuCreator(creator);
        lv_xinpian.setMenuCreator(creator);
        lv_tiaoma.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Tal.remove(position);
                        madaptertiaoma.notifyDataSetChanged();
                        tv_tiaoma_num.setText(Tal.size() + "");
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        lv_xinpian.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        Xal.remove(position);
                        madapterxinpian.notifyDataSetChanged();
                        tv_xinpian_num.setText(Xal.size() + "");


                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        lv_tiaoma.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lv_xinpian.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        madaptertiaoma = new MyAdaptertiaoma();
        madapterxinpian = new MyAdapterxinpian();
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
        tv_tiaoma_num.setText(Tal.size() + "");
        tv_xinpian_num.setText(Xal.size() + "");

        ll_shenqingshangbao.setOnClickListener(this);
        ll_shenhexiazai.setOnClickListener(this);
        ll_rukuchakan.setOnClickListener(this);
        ll_shebeixinxi.setOnClickListener(this);
        tv_baocun.setOnClickListener(this);
        tv_duqv.setOnClickListener(this);
        tv_qingchu.setOnClickListener(this);
        tv_shujvjiaohu.setOnClickListener(this);
    }

    private void initData() {
        //这里打开对话框如果设备编号为空的话
        // check(getDeviceAddress());
        device = bluetoothAdapter.getRemoteDevice(getDeviceAddress());
        //设置当前设备名称
        //deviceName.setText(device.getName());
        // 一上来就先连接蓝牙设备
        new Thread(new Runnable() {
            @Override
            public void run() {
                getuidd();
                if (Shref.getString(PrintDataActivity.this, getDeviceAddress(), null) == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            while (cuowulianjie) {
                                LogUtil.showLogD("在断开。。。。。。。。。。。。。。");
                                duankai();
                            }
                            showt("请返回重新获取设备编号");
                        }
                    });
                }
                boolean flag =connect();
                if (!flag) {
                    // 连接失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Shref.setString(PrintDataActivity.this, "deviceaddress", null);
                            Toast.makeText(PrintDataActivity.this, "连接失败，检查蓝牙是否打开，请返回重新连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //finish();
                } else {
                    // 连接成功z
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showt("获取设备号成功");
                        }
                    });
                    read();
                    //startReceive();
                }
            }
        }).start();
    }

    /**
     * 获取设备编号
     */
    private void getuidd() {
        getUid();
        while (Shref.getString(PrintDataActivity.this, getDeviceAddress(), null) == null) {
            if (cuowulianjie) {
                if (j < 5) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showt("正在获取设备号。。。。。");
                        }
                    });
                    xunuid();
                } else {
                    j = 1;
                    while (cuowulianjie) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(PrintDataActivity.this, "正在获取设备号。。。。。", Toast.LENGTH_LONG).show();
                                //MyToast.showShort("正在获取设备号。。。。。");
                                showt("正在获取设备号。。。。。");
                            }
                        });
                        System.out.println("在断开。。。。。。。。。。。。。。");
                        duankai();
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


    public void menuButtonDidClick() {
        if (dl.getStatus() != DragLayout.Status.Close) {
            dl.close();
        } else {
            dl.open();
        }
    }

    /**
     * 获得从上一个Activity传来的蓝牙地址
     *
     * @return String
     */
    private String getDeviceAddress() {
        // 直接通过Context类的getIntent()即可获取Intent
        Intent intent = this.getIntent();
        // 判断deviceaddress
        if (Shref.getString(PrintDataActivity.this, "deviceaddress", null) == null) {
            Shref.setString(PrintDataActivity.this, "deviceaddress", intent.getStringExtra("deviceAddress"));
           LogUtil.showLogD("null......" + intent.getStringExtra("deviceAddress"));
            return intent.getStringExtra("deviceAddress");
        } else {
            String deviceaddress = Shref.getString(PrintDataActivity.this, "deviceaddress", null);
            LogUtil.showLogD("不为null......" + intent.getStringExtra("deviceAddress"));
            return deviceaddress;
        }
    }

    /**
     * 连接蓝牙设备
     */

    public boolean connect() {
        if (!isConnection) {
            try {
                System.out.println("111111111111");
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
                System.out.println("222222222222");
                bluetoothSocket.connect();
                isConnection = true;
                if (bluetoothAdapter.isDiscovering()) {
                    System.out.println("关闭适配器!");
                    bluetoothAdapter.isDiscovering();
                }
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PrintDataActivity.this, PrintDataActivity.this.device.getName() + "连接成功!",
                            Toast.LENGTH_SHORT).show();
                }
            });

            return true;
        } else {
            return true;
        }
    }


    @Override
    protected void onDestroy() {
        LogUtil.showLogD("断开蓝牙设备连接");
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void startReceive() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                read();
            }
        }.start();
    }

    /**
     * 接收数据条码！！！！！！！！！！！！！！！！
     */
    private void read() {
        if (this.isConnection) {
            System.out.println("开始打印！！");
            try {
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
                String Ts;
                String Ts1 = null;
                String Ts2 = null;

                boolean saolebiedetiaoma = false;
                byte[] buffer = new byte[1024];
                byte[] buffer2 = new byte[1024];

                byte[] bLenUii = new byte[1];
                byte[] bUii = new byte[255];

                //这里会一直等待读取
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int bytes;
                    do {
                        bytes = this.inputStream.read(buffer);
                    } while (bytes <= 0);

                    buffer2 = (byte[]) buffer.clone();
                    for (int i = 0; i < buffer.length; ++i) {
                        buffer[i] = 0;
                    }
                    String ceshishujv = new String(buffer2, 0, bytes);
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                    Matcher m = p.matcher(ceshishujv);
                    ceshishujv = m.replaceAll("");
                    Log.e("PrintDataActivity", "是不是二维码" + ceshishujv + ceshishujv.matches("^[0-9]*$"));
                    //条形码
                    if (ceshishujv != null && !ceshishujv.equals("")) {
                        if (ceshishujv.matches("^[0-9]*$")) {
                            System.out.println("是二维码");
                            if (ceshishujv.length() > 10) {
                            } else if (ceshishujv.length() < 10) {
                                if (Ts1 == null || Ts1.equals("")) {
                                    Ts1 = new String(buffer2, 0, ceshishujv.length());
                                    if (saolebiedetiaoma) {
                                        Ts1 = null;
                                        Ts2 = null;
                                        Ts = null;
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
                                        }
                                        Ts1 = null;
                                        Ts2 = null;
                                        Ts = null;
                                        Wenjian.baocunauto(Tal, Xal);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                madaptertiaoma.notifyDataSetChanged();
                                                tv_tiaoma_num.setText(Tal.size() + "");
                                            }
                                        });
                                    } else {
                                        Ts1 = null;
                                        Ts2 = null;
                                        Ts = null;
                                        saolebiedetiaoma = true;
                                    }

                                }
                            } else if (ceshishujv.length() == 10) {
                                Ts = new String(buffer2, 0, 10);
                                String substring = Ts.substring(0, 1);
                                if (!Tal.contains(Ts) && substring.equals("1")) {
                                    Tal.add(0,Ts);
                                }
                                Ts = null;
                                System.out.println("Ts...." + Ts);
                                Wenjian.baocunauto(Tal, Xal);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        madaptertiaoma.notifyDataSetChanged();
                                        tv_tiaoma_num.setText(Tal.size() + "");
                                    }
                                });
                            }
                            //是芯片
                        } else {
                            System.out.println("是芯片...." + buffer2);
                            String s = Changezifu.Bytes2HexString(buffer2);
                            System.out.println(s);
                            String jiexinpian = JieMi.jiexinpian(s);
                            if (jiexinpian != null) {
                                if (!Xal.contains(jiexinpian)) {
                                    Xal.add(0,jiexinpian);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Wenjian.baocunauto(Tal, Xal);
                                        }
                                    });
                                    System.out.println(Xal);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        madapterxinpian.notifyDataSetChanged();
                                        tv_xinpian_num.setText(Xal.size() + "");
                                    }
                                });
                            }
                        }
                    }
                }
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PrintDataActivity.this, "连接断开了，请重新连接", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PrintDataActivity.this, "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_shenqingshangbao:
                if (uidStr != null) {
                    String s1 = "";
                    for (int i = 0; i < Tal.size(); i++) {
                        s1 = s1 + Tal.get(i) + ";";
                    }
                    for (int i = 0; i < Xal.size(); i++) {
                        s1 = s1 + Xal.get(i) + ";";
                    }
                    Intent intent1 = new Intent();
                    intent1.putExtra("talxal", s1);
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
                System.out.println("保存。。。。。。。。。。。。。" + Tal);
                Wenjian.baocun(Tal, Xal);
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
                tv_tiaoma_num.setText(Tal.size() + "");
                madapterxinpian.notifyDataSetChanged();
                tv_xinpian_num.setText(Xal.size() + "");
                break;
            case R.id.tv_shujvjiaohu:
                getUid();
                if (moduleControl.UhfReaderDisconnect()) {
                    System.out.println("断开成功");
                }
                String s = "";
                for (int i = 0; i < Tal.size(); i++) {
                    s = s + Tal.get(i) + ";";
                }
                for (int i = 0; i < Xal.size(); i++) {
                    s = s + Xal.get(i) + ";";
                }
                //uidStr =" 35ffd0054843353230782243";
                //AppApplication.muidstr=uidStr;
                //Shref.setString(PrintDataActivity.this, "huiyuanhao", "0000");
                if (uidStr != null) {
                    Intent intentshujvjiaohu = new Intent();
                    intentshujvjiaohu.putExtra("uidstr", uidStr);
                    intentshujvjiaohu.putExtra("TALXAL", s);
                    intentshujvjiaohu.setClass(PrintDataActivity.this, ShujvjiaohuActivity.class);
                    startActivity(intentshujvjiaohu);
                } else {
                    Toast.makeText(PrintDataActivity.this, "设备编号为空", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    private void check() {
        if (Shref.getString(PrintDataActivity.this, getDeviceAddress(), null) != null) {
            uidStr = Shref.getString(PrintDataActivity.this, getDeviceAddress(), null);
            AppApplication.muidstr = uidStr;
            System.out.println("sherf里取得。。。。。" + uidStr);
        } else {
            //加上一个对话框，提示用户
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("正在加载请稍后");
            pd.show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        PrintDataActivity.bluetoothSocket.close();
                        byte[] bStatus = new byte[1];
                        if (moduleControl.UhfReaderConnect(getDeviceAddress(), bStatus, flagCrc)) {
                            System.out.println("ffff连接成功");
                            byte[] bUid = new byte[12];
                            if (moduleControl.UhfGetReaderUID(bUid, flagCrc)) {
                                uidStr = fun.bytesToHexString(bUid, 12);
                                System.out.println("ifuidStr...." + uidStr);
                                Shref.setString(PrintDataActivity.this, getDeviceAddress(), uidStr);
                                AppApplication.muidstr = uidStr;
                                if (pd != null) {
                                    pd.dismiss();//关闭
                                }
                                getHuiyuan();
                                if (moduleControl.UhfReaderDisconnect()) {
                                    device = bluetoothAdapter.getRemoteDevice(getDeviceAddress());
                                    boolean flag = connect();
                                    if (flag == false) {
                                        // 连接失败
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Shref.setString(PrintDataActivity.this, "deviceaddress", null);
                                                Toast.makeText(PrintDataActivity.this, "连接失败，检查蓝牙是否打开，请返回重新连接", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        // 连接成功
                                        read();
                                    }
                                    System.out.println("断开成功");
                                }

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PrintDataActivity.this, "未能获取手持机标识号", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {
                            System.out.println("连接失败");
                            return;
                        }
                    } catch (Exception e) {
                    }
                    if (pd != null) {
                        pd.dismiss();//关闭
                    }
                }
            }.start();
        }
    }

    private void getHuiyuan() {
        if (AppApplication.muidstr != null) {
            String nameSpace = "http://tempuri.org/";
            String methodName = "CheckHm";
            String endPoint = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
            String soapAction = "http://tempuri.org/CheckHm";
            SoapObject soapObject = new SoapObject(nameSpace, methodName);
            soapObject.addProperty("hMIdStr", AppApplication.muidstr);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObject);
            HttpTransportSE transport = new HttpTransportSE(endPoint);
            try {
                transport.call(soapAction, envelope);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SoapObject object = (SoapObject) envelope.bodyIn;
            System.out.println("啦啦啦。。。" + object.toString());
            if (object.toString().equals("CheckHmResponse{}")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PrintDataActivity.this, "还未绑定会员编号", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                final String result = object.getProperty(0).toString();
                Shref.setString(PrintDataActivity.this, "huiyuanhao", result);
                System.out.println("object..." + object);
                System.out.println("result...." + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        LSAlert.showDialog(PrintDataActivity.this, "编号", "设备编号：" + AppApplication.muidstr + "\n" + "会员号：" + result, "确定", "取消", new LSAlert.DialogCallback() {
                            @Override
                            public void onConfirm() {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onCancel() {
                                // TODO Auto-generated method stub

                            }
                        });
                    }
                });
            }
        }
    }

    int i = 1;

    /**
     * 获取设备编号
     */
    private void getUid() {
        System.out.println("getuid...." + i++);
        if (Shref.getString(PrintDataActivity.this, getDeviceAddress(), null) != null) {
            uidStr = Shref.getString(PrintDataActivity.this, getDeviceAddress(), null);
            AppApplication.muidstr = uidStr;
            System.out.println("sherf里取得。。。。。" + uidStr);
        } else {
            try {
                if (bluetoothSocket != null) {
                    PrintDataActivity.bluetoothSocket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] bStatus = new byte[1];
            if (moduleControl.UhfReaderConnect(getDeviceAddress(), bStatus, flagCrc)) {
                cuowulianjie = true;
                System.out.println("ffff连接成功");
                xunuid();

            } else {
                System.out.println("连接失败");
                lianjieshibai = true;
                duankai();
                return;
            }
        }
    }

    int j = 1;

    private void xunuid() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showt("正在获取设备号。。。。。");
            }
        });
        System.out.println("在获取。。。" + j++);
        byte[] bUid = new byte[12];
        if (moduleControl.UhfGetReaderUID(bUid, flagCrc)) {
            uidStr = fun.bytesToHexString(bUid, 12);
            System.out.println("ifuidStr...." + uidStr);
            Shref.setString(PrintDataActivity.this, getDeviceAddress(), uidStr);
            AppApplication.muidstr = uidStr;
            while (cuowulianjie) {
                System.out.println("在断开。。。。。。。。。。。。。。");
                duankai();
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PrintDataActivity.this, "未能获取手持机标识号", Toast.LENGTH_SHORT).show();
                    //getUid();
                }
            });
        }
    }

    private void duankai() {
        if (moduleControl.UhfReaderDisconnect()) {
            cuowulianjie = false;
            System.out.println("断开成功错误连接。。。。。");
            return;
        } else {
            duankai();
        }
    }

    class MyAdaptertiaoma extends BaseAdapter {

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


    class MyAdapterxinpian extends BaseAdapter {

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = "";
            if(uri!=null){
                path=UriUtils.getPath(this,uri);
            }
            if(path==null){
                Toast.makeText(this,"文件路径为空！",Toast.LENGTH_LONG).show();
                return;
            }
            File file = new File(path);
            String duqv = Wenjian.duqv(this,file);
            if (duqv.length() != 0 && duqv != null) {
                if (duqv.contains("xinpian")) {
                    String[] split = duqv.split("xinpian");
                    if (split.length > 0) {
                        String[] splitTiaoma = split[0].split(",");
                        Tal.clear();
                        Xal.clear();
                        System.out.println("split[0]..........." + split[0]);
                        for (int i = 0; i < splitTiaoma.length; i++) {
                            if (splitTiaoma[i].length() == 10) {
                                Tal.add(splitTiaoma[i]);
                            } else {
                                Tal.clear();
                            }
                        }
                        if (split.length > 1) {
                            String[] splitxinpian = split[1].split(",");
                            for (int i = 0; i < splitxinpian.length; i++) {
                                Xal.add(splitxinpian[i]);
                            }
                        }
                        madaptertiaoma.notifyDataSetChanged();
                        tv_tiaoma_num.setText(Tal.size() + "");

                        madapterxinpian.notifyDataSetChanged();
                        tv_xinpian_num.setText(Xal.size() + "");
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


    static Toast mToast = null;

    public void showt(String msg) {

        if (null == mToast) {
            //mToast=new Toast(AppApplication.getAppContext());
            mToast = Toast.makeText(PrintDataActivity.this, msg, Toast.LENGTH_SHORT);
            // mToast.setGravity(Gravity.BOTTOM, 0, 350);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        while (cuowulianjie) {
            duankai();
        }
        if (cuowulianjie) {
            System.out.println("if ... 返回键");
            return false;
        } else {
            System.out.println("else... 返回键");
            return super.onKeyDown(keyCode, event);
        }


    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
////        if (keyCode == KeyEvent.KEYCODE_BACK )
////        {
////            while (cuowulianjie) {
////                duankai();
////            }
////            return false;
////        }else{
////            return false;
////        }
//    }


}