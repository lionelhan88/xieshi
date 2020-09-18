package com.lessu.xieshi.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.ColorUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.SettingActivity;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.ImageloaderUtil;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.Utils.PermissionUtils;
import com.lessu.xieshi.Utils.PicSize;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.Utils.UriUtils;
import com.lessu.xieshi.construction.ConstructionListActivity;
import com.lessu.xieshi.dataauditing.DataAuditingActivity;
import com.lessu.xieshi.dataexamine.DataExamineActivity;
import com.lessu.xieshi.map.ProjectListActivity;
import com.lessu.xieshi.meet.MeetingListActivity;
import com.lessu.xieshi.scan.BluetoothActivity;
import com.lessu.xieshi.scan.PrintDataActivity;
import com.lessu.xieshi.scan.PrintDataHomeActivity;
import com.lessu.xieshi.scan.YangpinshibieActivity;
import com.lessu.xieshi.tianqi.activitys.TianqiActivity;
import com.lessu.xieshi.tianqi.bean.Hourbean;
import com.lessu.xieshi.tianqi.utils.Contenttianqi;
import com.lessu.xieshi.todaystatistics.AdminTodayStatisticsActivity;
import com.lessu.xieshi.todaystatistics.TodayStatisticsActivity;
import com.lessu.xieshi.training.TrainingActivity;
import com.lessu.xieshi.unqualified.UnqualifiedSearchActivity;
import com.lessu.xieshi.uploadpicture.UploadPictureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FirstActivity extends NavigationActivity {
    private ImageView iv_touxiang;
    private TextView tv_yonghuming;
    private LinearLayout ll_seccion1;
    private LinearLayout ll_seccion2;
    private LinearLayout ll_seccion3;
    private LinearLayout ll_seccion4;
    private LinearLayout ll_seccion5;
    private TextView tv_seccion1;
    private ImageView iv_seccion1;
    private TextView tv_seccion2;
    private ImageView iv_seccion2;
    private TextView tv_seccion3;
    private ImageView iv_seccion3;
    private TextView tv_seccion4;
    private ImageView iv_seccion4;
    private TextView tv_seccion5;
    private ImageView iv_seccion5;
    private LinearLayout ll_seccion6;
    private ImageView iv_seccion6;
    private TextView tv_seccion6;
    private ImageView iv_tianqi;
    private TextView tv_tianqi;
    private TextView tv_temp;
    private RelativeLayout ll_tianqi;
    private ImageView firstSet;

    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private static final String permission =Manifest.permission.ACCESS_FINE_LOCATION;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        navigationBar.setVisibility(View.GONE);
        getUpdate();
        initView();
        initData();
        ImmersionBar.with(this).titleBarMarginTop(ll_tianqi)
              .navigationBarColor(R.color.light_gray)
                .navigationBarDarkIcon(true).init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtils.requestPermission(this, new PermissionUtils.permissionResult() {
            @Override
            public void hasPermission(List<String> granted, boolean isAll) {
                gettianqi();
            }
        },permission);
    }

    @Override
    protected void initImmersionBar() {

    }

    /**
     * 获取天气数据
     */
    private void gettianqi() {
        //19-5-29更改首页天气数据现实和ios保持统一
        if(mLocationClient==null) {
            mLocationClient = new LocationClient(getApplicationContext());
            mBDLocationListener = new MyBDLocationListener();
            // 注册监听
            mLocationClient.registerLocationListener(mBDLocationListener);
        }
        getLocation();
    }

    /**
     * 获取当前时间点的天气情况
     *
     * @param token
     * @param longgitude
     * @param latiformat
     */
    private void getHourWeather(final String cityName, String token, String longgitude, String latiformat) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", token);
        params.put("JD", longgitude);
        params.put("WD", latiformat);
        System.out.println("params.............." + params);
        EasyAPI.apiConnectionAsync(this, false, false, ApiMethodDescription.get("/ServiceWeather.asmx/GetHour"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                System.out.println(result);
                Hourbean hourbean = GsonUtil.JsonToObject(result.toString(), Hourbean.class);
                List<Hourbean.DataBean> data = hourbean.getData();
                Hourbean.DataBean dataBean = data.get(0);
                String wthr = dataBean.getWthr();
                tv_tianqi.setText(cityName + " > " + wthr);
                tv_temp.setText(dataBean.getTemp() + "℃");
            }

            @Override
            public String onFailed(ApiError error) {
               LogUtil.showLogE("获取天气数据失败：" + error.errorMeesage);
                return null;
            }
        });
    }
    /**
     * 获得所在位置经纬度及详细地址
     */
    public void getLocation() {
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();

    }
    // 2019-05-29获取地址坐标
    private class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 非空判断
            if (location != null) {
                DecimalFormat df = new DecimalFormat("######0.00");
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                final String latiformat = df.format(latitude);
                final String longformat = df.format(longitude);
                final String city = location.getCity().substring(0, location.getCity().length() - 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getHourWeather(city, Contenttianqi.gettoken(), longformat, latiformat);
                    }
                });
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 打开软件重新登陆
     */
    private void getLogin() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("UserName", Shref.getString(FirstActivity.this, Common.USERNAME, ""));
        params.put("PassWord", Shref.getString(FirstActivity.this, Common.PASSWORD, ""));
        params.put("DeviceId", Shref.getString(FirstActivity.this, Common.DEVICEID, ""));
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/UserLogin"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                System.out.println(result);
                boolean success = result.getAsJsonObject().get("Success").getAsBoolean();
                if(success){
                    JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
                    String userPower = json.get("UserPower").getAsString();
                    String token = GsonValidate.getStringByKeyPath(json, "Token", "");
                    String MemberInfoStr = GsonValidate.getStringByKeyPath(json, "MemberInfoStr", "");
                    String PhoneNumber = GsonValidate.getStringByKeyPath(json, "PhoneNumber", "");
                    String userId = GsonValidate.getStringByKeyPath(json, "UserId", "");
                    Shref.setString(FirstActivity.this, Common.USERPOWER, userPower);
                    Shref.setString(FirstActivity.this, Common.USERID, userId);
                    Shref.setString(FirstActivity.this, Common.MEMBERINFOSTR, MemberInfoStr);
                    LSUtil.setValueStatic("Token", token);
                    initMenu(userPower);
                }else{
                    LSAlert.showAlert(FirstActivity.this, "提示", "当前登录账户用户名或密码错误！\n是否重新登录？"
                            , "确定", false, new LSAlert.AlertCallback() {
                                @Override
                                public void onConfirm() {
                                    loginOut();
                                }
                            });
                }

            }

            @Override
            public String onFailed(final ApiError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error.errorCode == 3000) {
                            LogUtil.showLogE(String.valueOf(error.errorCode));
                            LogUtil.showLogE(error.errorDomain);
                            LogUtil.showLogE(error.errorMeesage);
                            new AlertDialog.Builder(FirstActivity.this).setTitle("权限改变").setMessage("请重新登陆").show();
                            new AlertDialog.Builder(FirstActivity.this)
                                    .setTitle("权限改变")
                                    .setMessage("您的权限有所改变，请重新登陆")
                                    .setPositiveButton("重新登陆",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialoginterface, int i) {
                                                    //按钮事件
                                                    Shref.setString(FirstActivity.this, Common.USERPOWER, "");
                                                    startActivity(new Intent(FirstActivity.this, LoginActivity.class));
                                                    finish();
                                                }
                                            }).show();
                        }
                    }
                });
                return null;
            }
        });

    }
    /**
     * 退出登录
     */
    private void loginOut(){
        Intent intentTUICHU = new Intent();
        intentTUICHU.setClass(FirstActivity.this, LoginActivity.class);
        intentTUICHU.putExtra("exit", true);
        intentTUICHU.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentTUICHU);
        finish();
    }

    /**
     * 检查更新
     */
    private void getUpdate() {
        getUpdate(false,new UpdateAppCallback() {
            @Override
            public void updateCancel() {
                AppApplication.exit();
            }
        });
    }

    private void initView() {
        ll_tianqi = (RelativeLayout) findViewById(R.id.ll_tianqi);
        tv_tianqi = (TextView) findViewById(R.id.tv_tianqi);
        iv_tianqi = (ImageView) findViewById(R.id.iv_tianqi);
        firstSet = (ImageView)findViewById(R.id.first_set);
        tv_temp = (TextView) findViewById(R.id.first_tv_tmp);
        iv_touxiang = (ImageView) findViewById(R.id.iv_touxiang);
        tv_yonghuming = (TextView) findViewById(R.id.tv_yonghuming);
        ll_seccion1 = (LinearLayout) findViewById(R.id.ll_seccion1);
        tv_seccion1 = (TextView) findViewById(R.id.tv_seccion1);
        iv_seccion1 = (ImageView) findViewById(R.id.iv_seccion1);
        ll_seccion2 = (LinearLayout) findViewById(R.id.ll_seccion2);
        tv_seccion2 = (TextView) findViewById(R.id.tv_seccion2);
        iv_seccion2 = (ImageView) findViewById(R.id.iv_seccion2);
        ll_seccion3 = (LinearLayout) findViewById(R.id.ll_seccion3);
        iv_seccion3 = (ImageView) findViewById(R.id.iv_seccion3);
        tv_seccion3 = (TextView) findViewById(R.id.tv_seccion3);
        ll_seccion4 = (LinearLayout) findViewById(R.id.ll_seccion4);
        iv_seccion4 = (ImageView) findViewById(R.id.iv_seccion4);
        tv_seccion4 = (TextView) findViewById(R.id.tv_seccion4);
        ll_seccion5 = (LinearLayout) findViewById(R.id.ll_seccion5);
        iv_seccion5 = (ImageView) findViewById(R.id.iv_seccion5);
        tv_seccion5 = (TextView) findViewById(R.id.tv_seccion5);

        ll_seccion6 = (LinearLayout) findViewById(R.id.ll_seccion6);
        iv_seccion6 = (ImageView) findViewById(R.id.iv_seccion6);
        tv_seccion6 = (TextView) findViewById(R.id.tv_seccion6);
        //该功能还未开放！！！！！！
        ll_seccion6.setVisibility(View.GONE);
        iv_tianqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.requestPermission(FirstActivity.this, new PermissionUtils.permissionResult() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        startActivity(new Intent(FirstActivity.this, TianqiActivity.class));
                    }
                },permission);

            }
        });
        firstSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到设置页面
                Intent intentXITONG = new Intent(FirstActivity.this, SettingActivity.class);
                startActivity(intentXITONG);
            }
        });

        iv_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(FirstActivity.this)
                        .setTitle("图片选择")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[]{"拍照", "相册"}, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                            Uri imageUri=null;
                                            File saveImagePath = new File(Environment.getExternalStorageDirectory(),"image.jpg");
                                            //android7.0以后，相机拍照不能直接获取uri，需要用fileprovider
                                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                                                imageUri = FileProvider.getUriForFile(FirstActivity.this,
                                                        getPackageName()+".fileProvider",saveImagePath);
                                                //添加临时权限
                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            }else{
                                                imageUri = Uri.fromFile(saveImagePath);
                                            }
                                            //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                            startActivityForResult(intent, 0);
                                        } else {
                                            Intent intent = new Intent();
                                           /* Pictures画面Type设定为image */
                                            intent.setType("image/*");
                                           /* 使用Intent.ACTION_GET_CONTENT这个Action */
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                               /* 取得相片后返回本画面 */
                                            startActivityForResult(intent, 1);
                                        }
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    /**
     * 初始化菜单
     * @param newPower
     */
    private void initMenu(String newPower){
        final String userPower = newPower.substring(0,14);
        char userPower2 =newPower.charAt(15);
        LogUtil.showLogD("FirstActivity......." + userPower);
        if (userPower.equals("00010010100000")) {//j20623 279162 见证人
            ll_seccion1.setVisibility(View.VISIBLE);
            ll_seccion2.setVisibility(View.VISIBLE);
            ll_seccion3.setVisibility(View.VISIBLE);
            ll_seccion4.setVisibility(View.INVISIBLE);
            ll_seccion5.setVisibility(View.INVISIBLE);
            ll_seccion6.setVisibility(View.INVISIBLE);
            iv_seccion1.setImageResource(R.drawable.yangpinchaxun);
            tv_seccion1.setText("样品查询");
            iv_seccion2.setImageResource(R.drawable.tupianshangchuan);
            tv_seccion2.setText("现场图片上传");
            iv_seccion3.setImageResource(R.drawable.yangpinshibie);
            tv_seccion3.setText("样品识别");
            iv_seccion4.setImageResource(R.drawable.xitongshezhi);
            tv_seccion4.setText("系统设置");
            iv_seccion5.setImageResource(R.drawable.tuichu);
            tv_seccion5.setText("重新登录");


            ll_seccion1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstActivity.this, ConstructionListActivity.class);
                    startActivity(intent);
                }
            });
            ll_seccion2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstActivity.this, UploadPictureActivity.class);
                    startActivity(intent);
                }
            });
            ll_seccion3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    if (Shref.getString(FirstActivity.this, "deviceaddress", null) != null) {
                        System.out.println(Shref.getString(FirstActivity.this, "deviceaddress", ""));
                        intent.setClass(FirstActivity.this, YangpinshibieActivity.class);
                    } else {
                        AppApplication.isGLY = false;
                        intent.setClass(FirstActivity.this, BluetoothActivity.class);
                    }
                    startActivity(intent);
                }
            });
            ll_seccion4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentXITONG = new Intent(FirstActivity.this, SettingActivity.class);
                    startActivity(intentXITONG);
                }
            });
            ll_seccion5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentTUICHU = new Intent();
                    intentTUICHU.setClass(FirstActivity.this, LoginActivity.class);
                    intentTUICHU.putExtra("exit", true);
                    intentTUICHU.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentTUICHU);
                    finish();
                }
            });
        }
        if (userPower.equals("00010000100000")) {//Q13503 123456 取样人
            ll_seccion1.setVisibility(View.VISIBLE);
            ll_seccion2.setVisibility(View.VISIBLE);
            ll_seccion3.setVisibility(View.INVISIBLE);
            ll_seccion4.setVisibility(View.INVISIBLE);
            ll_seccion5.setVisibility(View.INVISIBLE);
            ll_seccion6.setVisibility(View.INVISIBLE);
            iv_seccion1.setImageResource(R.drawable.yangpinchaxun);
            tv_seccion1.setText("样品查询");
            iv_seccion2.setImageResource(R.drawable.yangpinshibie);
            tv_seccion2.setText("样品识别");
            iv_seccion3.setImageResource(R.drawable.xitongshezhi);
            tv_seccion3.setText("系统设置");
            iv_seccion4.setImageResource(R.drawable.tuichu);
            tv_seccion4.setText("重新登录");

            ll_seccion1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstActivity.this, ConstructionListActivity.class);
                    startActivity(intent);
                }
            });

            ll_seccion2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    if (Shref.getString(FirstActivity.this, "deviceaddress", null) != null) {
                        //intent.putExtra("deviceAddress", Shref.getString(FirstActivity.this,"deviceaddress",null));
                        System.out.println(Shref.getString(FirstActivity.this, "deviceaddress", ""));
                        intent.setClass(FirstActivity.this, YangpinshibieActivity.class);
                    } else {
                        AppApplication.isGLY = false;
                        intent.setClass(FirstActivity.this, BluetoothActivity.class);
                    }
                    startActivity(intent);
                }
            });

            ll_seccion3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentXITONG = new Intent(FirstActivity.this, SettingActivity.class);
                    startActivity(intentXITONG);
                }
            });
            ll_seccion4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentTUICHU = new Intent();
                    intentTUICHU.setClass(FirstActivity.this, LoginActivity.class);
                    intentTUICHU.putExtra("exit", true);
                    intentTUICHU.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentTUICHU);
                    finish();
                }
            });


        }
        if (userPower.equals("10001101100000")) {//gly 1319 质监站人员
            ll_seccion1.setVisibility(View.VISIBLE);
            ll_seccion2.setVisibility(View.VISIBLE);
            ll_seccion3.setVisibility(View.VISIBLE);
            ll_seccion4.setVisibility(View.VISIBLE);
            ll_seccion5.setVisibility(View.INVISIBLE);
            ll_seccion6.setVisibility(View.INVISIBLE);

            iv_seccion1.setImageResource(R.drawable.jinritongji1);
            tv_seccion1.setText("工地查询");
            iv_seccion2.setImageResource(R.drawable.xinxichaxun);
            tv_seccion2.setText("不合格信息查询");
            iv_seccion3.setImageResource(R.drawable.gongchengchaxun1);
            tv_seccion3.setText("基桩静载");
            iv_seccion4.setImageResource(R.drawable.yangpinshibie);
            tv_seccion4.setText("样品识别");
            iv_seccion5.setImageResource(R.drawable.xitongshezhi);
            tv_seccion5.setText("系统设置");
            iv_seccion6.setImageResource(R.drawable.tuichu);
            tv_seccion6.setText("重新登录");


            ll_seccion1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Class tempClass = null;
                    if (userPower.charAt(0) == '0') {
                        tempClass = TodayStatisticsActivity.class;
                        Intent intent = new Intent(FirstActivity.this, tempClass);
                        startActivity(intent);
                    } else {
                        tempClass = AdminTodayStatisticsActivity.class;
                        Intent intent = new Intent(FirstActivity.this, tempClass);
                        //intent.putExtra("diyici",true);
                        startActivity(intent);
                    }
                }
            });
            ll_seccion2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstActivity.this, UnqualifiedSearchActivity.class);
                    startActivity(intent);
                }
            });
            ll_seccion3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstActivity.this, ProjectListActivity.class);
                    startActivity(intent);
                }
            });
            ll_seccion4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    if (Shref.getString(FirstActivity.this, "deviceaddress", null) != null) {
                        System.out.println(Shref.getString(FirstActivity.this, "deviceaddress", ""));
                        intent.setClass(FirstActivity.this, YangpinshibieActivity.class);
                    } else {
                        AppApplication.isGLY = false;
                        intent.setClass(FirstActivity.this, BluetoothActivity.class);
                    }
                    startActivity(intent);
                }
            });
            ll_seccion5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentXITONG = new Intent(FirstActivity.this, SettingActivity.class);
                    startActivity(intentXITONG);
                }
            });
            ll_seccion6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentTUICHU = new Intent();
                    intentTUICHU.setClass(FirstActivity.this, LoginActivity.class);
                    intentTUICHU.putExtra("exit", true);
                    intentTUICHU.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentTUICHU);
                    finish();
                }
            });


        }
        if (userPower.equals("01101000000000")) {//t9990001 1 检测人员
            /**
             * 如果登录的账号中有"Meet"开头的，才显示会议菜单按钮，其他隐藏
             */
            if(Shref.getString(this,Common.USERNAME,"").toUpperCase().startsWith("MEET")){
                ll_seccion1.setVisibility(View.VISIBLE);
                ll_seccion2.setVisibility(View.INVISIBLE);
                ll_seccion3.setVisibility(View.INVISIBLE);
                ll_seccion4.setVisibility(View.INVISIBLE);
                ll_seccion5.setVisibility(View.INVISIBLE);
                ll_seccion6.setVisibility(View.INVISIBLE);
                iv_seccion1.setImageResource(R.drawable.home_meeting_bg);
                tv_seccion1.setText("会议安排");
                ll_seccion1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentMeeting = new Intent(FirstActivity.this, MeetingListActivity.class);
                        intentMeeting.putExtra("type_user",1);
                        startActivity(intentMeeting);
                    }
                });
            }else{
                ll_seccion1.setVisibility(View.VISIBLE);
                ll_seccion2.setVisibility(View.VISIBLE);
                ll_seccion3.setVisibility(View.VISIBLE);
                ll_seccion4.setVisibility(View.VISIBLE);
                ll_seccion5.setVisibility(View.VISIBLE);
                ll_seccion6.setVisibility(View.INVISIBLE);
                iv_seccion1.setImageResource(R.drawable.shujushenhe1);
                tv_seccion1.setText("记录审核");
                ll_seccion1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FirstActivity.this, DataAuditingActivity.class);
                        startActivity(intent);
                    }
                });
            }
            iv_seccion2.setImageResource(R.drawable.baogaopizhun);
            tv_seccion2.setText("报告批准");
            iv_seccion3.setImageResource(R.drawable.jinritongji1);
            tv_seccion3.setText("统计信息");
            iv_seccion4.setImageResource(R.drawable.shouchijisaomiao);
            tv_seccion4.setText("手持机扫描");
            if (userPower2 == '1') {
                iv_seccion5.setImageResource(R.drawable.zaixianjiaoyu);
            }
            tv_seccion5.setText("在线培训");
            iv_seccion6.setImageResource(R.drawable.home_meeting_bg);
            tv_seccion6.setText("会议安排");


            ll_seccion2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstActivity.this, DataExamineActivity.class);
                    startActivity(intent);
                }
            });
            ll_seccion3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Class tempClass = null;
                    if (userPower.charAt(0) == '0') {
                        tempClass = TodayStatisticsActivity.class;
                        Intent intent = new Intent(FirstActivity.this, tempClass);
                        startActivity(intent);
                    } else {
                        tempClass = AdminTodayStatisticsActivity.class;
                        Intent intent = new Intent(FirstActivity.this, tempClass);
                        intent.putExtra("diyici", true);
                        startActivity(intent);
                    }
                }
            });
            ll_seccion4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    if (Shref.getString(FirstActivity.this, "deviceaddress", null) != null) {
                        System.out.println(Shref.getString(FirstActivity.this, "deviceaddress", ""));
                        intent.setClass(FirstActivity.this, PrintDataActivity.class);
                    } else {
                        AppApplication.isGLY = true;
                        intent.setClass(FirstActivity.this, BluetoothActivity.class);
                    }
                    startActivity(intent);
                }
            });
            ll_seccion5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentXITONG = new Intent(FirstActivity.this, TrainingActivity.class);
                    startActivity(intentXITONG);
                }
            });
            ll_seccion6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentMeeting = new Intent(FirstActivity.this, MeetingListActivity.class);
                    intentMeeting.putExtra("type_user",1);
                    startActivity(intentMeeting);
                }
            });
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String picname = Shref.getString(FirstActivity.this, Common.PICNAME, null);
        if ( picname == null||picname.equals("")) {
            iv_touxiang.setImageResource(R.drawable.touxiang);
        } else {
            ImageLoader.getInstance().displayImage(picname, iv_touxiang, ImageloaderUtil.imageconfig());
        }
        ArrayList<LinearLayout> llal = new ArrayList<LinearLayout>();
        llal.add(ll_seccion1);
        llal.add(ll_seccion2);
        llal.add(ll_seccion3);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        tv_yonghuming.setText(username);
        getLogin();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    String timename = String.valueOf(System.currentTimeMillis());
                    final String srcImagePath = Environment.getExternalStorageDirectory()+"/image.jpg";
                    final String outImagePath = Environment.getExternalStorageDirectory()+"/image/"+timename+".jpg";
                    LSAlert.showProgressHud(FirstActivity.this,"正在获取...");
                    Observable.create(new ObservableOnSubscribe<Boolean>() {
                        @Override
                        public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                            //在子线程中进行图片压缩
                            PicSize.compressAndOutPath(srcImagePath,outImagePath,1024);
                            emitter.onNext(true);
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            String s = "file://" +outImagePath;
                            System.out.println(s);
                            ImageLoader.getInstance().displayImage(s, iv_touxiang, ImageloaderUtil.imageconfig());
                            Shref.setString(FirstActivity.this, Common.PICNAME, s);
                            LSAlert.dismissProgressHud();
                        }
                    });
                    break;
                case 1:
                    Uri uri = intent.getData();
                    String dataColumn = UriUtils.getPath(this, uri);
                    if(!dataColumn.contains("file://")){
                        dataColumn = "file://"+dataColumn;
                    }
                    ImageLoader.getInstance().displayImage(dataColumn, iv_touxiang, ImageloaderUtil.imageconfig());
                    Shref.setString(FirstActivity.this, Common.PICNAME, dataColumn);
                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        if(mLocationClient!=null){
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
            mBDLocationListener=null;
            mLocationClient=null;
        }
        super.onDestroy();
    }
    private long time=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-time>2000){
                time = System.currentTimeMillis();
                ToastUtil.showShort("再次点击退出程序");
                return true;
            }else{
                AppApplication.exit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
