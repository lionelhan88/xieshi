package com.lessu.xieshi.module.scan;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.utils.FileUtil;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.Decrypt;
import com.lessu.xieshi.module.scan.adapter.PrintDataNoListAdapter;
import com.lessu.xieshi.module.scan.util.BluetoothHelper;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.lessu.xieshi.utils.LongString;
import com.lessu.xieshi.utils.ToastUtil;
import com.scetia.Pro.common.photo.UriUtils;
import com.lessu.xieshi.view.DragLayout;
import com.raylinks.Function;
import com.raylinks.ModuleControl;
import com.scetia.Pro.common.Util.SPUtil;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class PrintDataActivity extends NavigationActivity implements View.OnClickListener {

    ModuleControl moduleControl = new ModuleControl();
    Function fun = new Function();
    //正在读取设备返回值的标识
    private boolean isReading = true;
    private static byte flagCrc;
    private boolean isConnection = false;
    private SoundPool soundPool;
    private ArrayList<String> Tal = new ArrayList<>();
    private ArrayList<String> Xal = new ArrayList<>();
    private SwipeMenuCreator creator;
    private String uidStr;
    private int j = 1;
    // 标识是jar方法连接的蓝牙
    private boolean uhfBlueConnect;
    private boolean lianjieshibai;
    private boolean isFirstIn = true;
    private PrintDataNoListAdapter barCodeNoListAdapter;
    private PrintDataNoListAdapter chipNoListAdapter;
    @BindView(R.id.dl)
    DragLayout dl;
    @BindView(R.id.tv_tiaoma_num)
    TextView tvBarCodeNum;
    @BindView(R.id.tv_xinpian_num)
    TextView tvChipNum;
    @BindView(R.id.lv_tiaoma)
    SwipeMenuListView barCodeListView;
    @BindView(R.id.lv_xinpian)
    SwipeMenuListView chipListView;
    @Override
    protected int getLayoutId() {
        return R.layout.printdata_layout;
    }

    @Override
    protected void initView() {
        setTitle("标识扫描");
        BarButtonItem menuButtonItem = new BarButtonItem(this, R.drawable.icon_navigation_menu);
        menuButtonItem.setOnClickMethod(this, "menuButtonDidClick");
        navigationBar.setLeftBarItem(menuButtonItem);
        creator = menu -> {
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setWidth((130));
            deleteItem.setIcon(R.drawable.shanchu);
            menu.addMenuItem(deleteItem);
        };
        barCodeListView.setMenuCreator(creator);
        chipListView.setMenuCreator(creator);
        barCodeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {
                    Tal.remove(position);
                    barCodeNoListAdapter.notifyDataSetChanged();
                    tvBarCodeNum.setText(String.valueOf(Tal.size()));
                }
                return false;
            }
        });
        chipListView.setOnMenuItemClickListener((position, menu, index) -> {
            if (index == 0) {// delete
                Xal.remove(position);
                chipNoListAdapter.notifyDataSetChanged();
                tvChipNum.setText(String.valueOf(Xal.size()));
            }
            return false;
        });

        barCodeListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        chipListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        barCodeNoListAdapter = new PrintDataNoListAdapter(Tal);
        chipNoListAdapter = new PrintDataNoListAdapter(Xal);
        barCodeListView.setAdapter(barCodeNoListAdapter);
        chipListView.setAdapter(chipNoListAdapter);
        //已扫条码数
        tvBarCodeNum.setText(String.valueOf(Tal.size()));
        //已读芯片数
        tvChipNum.setText(String.valueOf(Xal.size()));

        LinearLayout ll_shenqingshangbao = findViewById(R.id.ll_shenqingshangbao);
        LinearLayout ll_shenhexiazai = findViewById(R.id.ll_shenhexiazai);
        LinearLayout ll_rukuchakan = findViewById(R.id.ll_rukuchakan);
        LinearLayout ll_shebeixinxi = findViewById(R.id.ll_shebeixinxi);
        TextView tv_baocun = findViewById(R.id.tv_baocun);
        TextView tv_duqv = findViewById(R.id.tv_duqv);
        TextView tv_qingchu = findViewById(R.id.tv_qingchu);
        TextView tv_shujvjiaohu = findViewById(R.id.tv_shujvjiaohu);
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
    @Override
    protected void initData() {
        initSoundPool();
        BluetoothHelper.getInstance().getBluetoothDevice(getDeviceAddress());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设备连接成功，开始读取设备的uid
                startGetUid();
                if (SPUtil.getSPConfig(getDeviceAddress(), "").equals("")) {
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
                boolean flag = connect();
                if (!flag) {
                    // 连接失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SPUtil.setSPConfig(SPUtil.BLUETOOTH_DEVICE, "");
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
    private void initSoundPool() {
        //当前系统的SDK版本大于等于21(Android 5.0)时
        if (Build.VERSION.SDK_INT > 21) {
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
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
        }
        //已经存在
        soundPool.load(PrintDataActivity.this, R.raw.scan_existence, 1);
        soundPool.load(PrintDataActivity.this, R.raw.scan_success, 1);
    }

    /**
     * 播放提示音
     *
     * @param isExistence 播放哪类提示音
     */
    private void playSoundPool(boolean isExistence) {
        if (isExistence) {
            soundPool.play(1, 1, 1, 0, 0, 1);
        } else {
            soundPool.play(2, 1, 1, 0, 0, 1);
        }
    }

    /**
     * 获取设备编号
     */
    private void startGetUid() {
        toastAlert("正在获取设备号...");
        getUid();
        while (SPUtil.getSPConfig(getDeviceAddress(), "").equals("")) {
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
        if (!SPUtil.getSPConfig(getDeviceAddress(), "").isEmpty()) {
            uidStr = SPUtil.getSPConfig(getDeviceAddress(), "");
            AppApplication.muidstr = uidStr;
            return;
        }

        try {
            BluetoothHelper.getInstance().closeSocket();
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
            LogUtil.showLogE("设备uid==" + uidStr);
            SPUtil.setSPConfig(getDeviceAddress(), uidStr);
            AppApplication.muidstr = uidStr;
            if (uhfBlueConnect) {
                //获取uid成功后，要断开jar方式连接的蓝牙，准备自定义方式连接设备
                uhfReaderDisconnect();
            }
        } else if (!(newUidName = getNewUhfUID2()).equals("")) {
            //之前的方法没有获取到uid，此时成功获取uid，则是新设备
            uidStr = newUidName;
            LogUtil.showLogE("设备uid==" + uidStr);
            SPUtil.setSPConfig(getDeviceAddress(), uidStr);
            AppApplication.muidstr = uidStr;
            if (uhfBlueConnect) {
                //获取uid成功后，要断开jar方式连接的蓝牙，准备自定义方式连接设备
                uhfReaderDisconnect();
            }
        } else {
            if (j >= 5) {
                toastAlert("未能获取手持机标识号");
            }
        }
    }

    /**
     * 获取新设备的uid方法
     *
     * @return uidName 返回值uid
     */
    private String getNewUhfUID2() {
        String uidName = "";
        try {
            //获取设备uid命令
            byte[] writData = {(byte) 0xA0, 0x03, 0x01, 0x68, (byte) 0xF4};
            moduleControl.SendDataToBT(writData, writData.length);
            Thread.sleep(800);
            byte[] readBuffer = new byte[17];
            moduleControl.RecvDataFromBT(readBuffer);
            String backData = fun.bytesToHexString(readBuffer, readBuffer.length).toUpperCase();
            //读取蓝牙设备返回的数据，转换为16进制字符串
            //  A00F0168 42502D323030303031 FFFFFF09,判断位数
            if (backData.length() == 34 && backData.startsWith("A00F0168")) {
                String uidHexStr = backData.substring(8, 26).trim();
                //16进制转为十进制ascii码字符串
                uidName = LongString.hexStr2Str(uidHexStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uidName;
    }

    /**
     * 获取蓝牙地址
     *
     * @return String
     */
    private String getDeviceAddress() {
        Intent intent = this.getIntent();
        String deviceAdd = SPUtil.getSPConfig(SPUtil.BLUETOOTH_DEVICE, "");
        //是否存在已经缓存的地址，如果没有缓存的地址，重新获取上一个页面传来的地址
        if (deviceAdd.isEmpty()) {
            deviceAdd = intent.getStringExtra("deviceAddress");
            SPUtil.setSPConfig(SPUtil.BLUETOOTH_DEVICE, deviceAdd);
        }
        return deviceAdd;
    }

    /**
     * 连接蓝牙设备
     */
    public boolean connect() {
        if (!isConnection) {
            String deviceName = BluetoothHelper.getInstance().connectDevice();
            if (deviceName == null) {
                runOnUiThread(() -> {
                    isConnection = false;
                    Toast.makeText(PrintDataActivity.this, "连接失败!", Toast.LENGTH_SHORT).show();
                });
                return false;
            }
            toastAlert(deviceName + "连接成功!");

            isConnection = true;
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
            try {
                InputStream inputStream = BluetoothHelper.getInstance().getInputStream();
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
                    buffer2 = buffer.clone();
                    //清空缓存
                    Arrays.fill(buffer, (byte) 0);
                    //将缓冲区读取的字节数组转为字符串
                    String ceshishujv;
                    ceshishujv = new String(buffer2, 0, buffer2.length);
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                    Matcher m = p.matcher(ceshishujv);
                    ceshishujv = m.replaceAll("").trim();
                    if (ceshishujv.isEmpty()) {
                        continue;
                    }
                    //条形码
                    if (ceshishujv.matches("^[0-9]*$")) {
                        if (ceshishujv.length() < 10) {
                            if (Ts1 == null || Ts1.equals("")) {
                                Ts1 = new String(buffer2, 0, ceshishujv.length());
                                if (saolebiedetiaoma) {
                                    Ts1 = null;
                                    saolebiedetiaoma = false;
                                }
                            } else {
                                Ts2 = new String(buffer2, 0, ceshishujv.length());
                                LogUtil.showLogE("PrintDataActivity==>" + Ts1 + "..else..." + Ts1 + "，Ts2..else..." + Ts2);
                                Ts = Ts1 + Ts2;
                                String substring = Ts.substring(0, 1);
                                if (Ts.length() == 10 && substring.equals("1")) {
                                    if (!Tal.contains(Ts)) {
                                        Tal.add(0, Ts);
                                        playSoundPool(false);
                                    } else {
                                        playSoundPool(true);
                                    }
                                    Ts1 = null;
                                    FileUtil.baocunauto(Tal, Xal);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            barCodeNoListAdapter.notifyDataSetChanged();
                                            tvBarCodeNum.setText(String.valueOf(Tal.size()));
                                        }
                                    });

                                } else {
                                    Ts1 = null;
                                    saolebiedetiaoma = true;
                                }
                            }
                        } else if (ceshishujv.length() == 10) {
                            if (!Tal.contains(ceshishujv)) {
                                Tal.add(0, ceshishujv);
                                playSoundPool(false);
                            } else {
                                playSoundPool(true);
                            }
                            FileUtil.baocunauto(Tal, Xal);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    barCodeNoListAdapter.notifyDataSetChanged();
                                    tvBarCodeNum.setText(String.valueOf(Tal.size()));
                                }
                            });
                        }
                        //是芯片
                    } else {
                        String s = LongString.bytes2HexString(buffer2);
                        String jiexinpian = Decrypt.decodeChip(s);
                        if (jiexinpian != null) {
                            if (!Xal.contains(jiexinpian)) {
                                Xal.add(0, jiexinpian);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        FileUtil.baocunauto(Tal, Xal);
                                    }
                                });
                                playSoundPool(false);
                            } else {
                                playSoundPool(true);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chipNoListAdapter.notifyDataSetChanged();
                                    tvChipNum.setText(String.valueOf(Xal.size()));
                                }
                            });
                        }
                    }
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort("连接断开了，请重新连接");
                        finish();
                    }
                });
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort("设备未连接，请重新连接！");
                    finish();
                }
            });
        }
    }

    /**
     * 弹出提示
     */
    private void toastAlert(final String message) {
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
                    intent1.putExtra("uidstr", uidStr);
                    intent1.setClass(PrintDataActivity.this, ShenqingshangbaoActivity.class);
                    startActivity(intent1);
                } else {
                    Toast.makeText(PrintDataActivity.this, "设备编号为空", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ll_shenhexiazai:
                if (uidStr != null) {
                    Intent intent = new Intent(PrintDataActivity.this, ReviewDownloadActivity.class);
                    intent.putExtra("uidstr", uidStr);
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
                barCodeNoListAdapter.notifyDataSetChanged();
                tvBarCodeNum.setText(String.valueOf(Tal.size()));
                chipNoListAdapter.notifyDataSetChanged();
                tvChipNum.setText(String.valueOf(Xal.size()));
                break;
            case R.id.tv_shujvjiaohu:
                getUid();
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
                    intentshujvjiaohu.setClass(PrintDataActivity.this, DataInteractionActivity.class);
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
                ToastUtil.showShort("文件路径为空！");
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
                        LogUtil.showLogE("split[0]..........." + split[0]);
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
                        barCodeNoListAdapter.notifyDataSetChanged();
                        tvBarCodeNum.setText(String.valueOf(Tal.size()));

                        chipNoListAdapter.notifyDataSetChanged();
                        tvChipNum.setText(String.valueOf(Xal.size()));
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

    @Override
    protected void onStart() {
        super.onStart();
        isReading = true;
        if (!isFirstIn) {
            new Thread() {
                @Override
                public void run() {
                    read();
                }
            }.start();
        }
        isFirstIn = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isReading = false;
    }

    @Override
    protected void onDestroy() {
        isReading = false;
        BluetoothHelper.getInstance().closeInputStream();
        BluetoothHelper.getInstance().closeSocket();
        if (soundPool != null) {
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