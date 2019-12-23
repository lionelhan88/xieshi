package com.lessu.xieshi.login;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.BaseActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.SettingActivity;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.ImageloaderUtil;
import com.lessu.xieshi.Utils.SavePic;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.construction.ConstructionListActivity;
import com.lessu.xieshi.dataauditing.DataAuditingActivity;
import com.lessu.xieshi.dataexamine.DataExamineActivity;
import com.lessu.xieshi.map.ProjectListActivity;
import com.lessu.xieshi.scan.BluetoothActivity;
import com.lessu.xieshi.scan.PrintDataActivity;
import com.lessu.xieshi.scan.YangpinshibieActivity;
import com.lessu.xieshi.tianqi.activitys.TianqiActivity;
import com.lessu.xieshi.tianqi.bean.Hourbean;
import com.lessu.xieshi.tianqi.utils.Contenttianqi;
import com.lessu.xieshi.todaystatistics.AdminTodayStatisticsActivity;
import com.lessu.xieshi.todaystatistics.TodayStatisticsActivity;
import com.lessu.xieshi.unqualified.UnqualifiedSearchActivity;
import com.lessu.xieshi.uploadpicture.UploadPictureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirstActivity extends BaseActivity {


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
    private LinearLayout ll_tianqi;

    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getLogin();
        getUpdate();

        initView();

        gettianqi();

        initData();

    }

    /**
     * 获取天气数据
     */
    private void gettianqi() {
        //19-5-29更改首页天气数据现实和ios保持统一
        mLocationClient = new LocationClient(getApplicationContext());
        mBDLocationListener = new MyBDLocationListener();
        // 注册监听
        mLocationClient.registerLocationListener(mBDLocationListener);
        getLocation();
      /*  HashMap<String, Object> params = new HashMap<String, Object>();
        String gettoken = Contenttianqi.gettoken();
        params.put("Token", gettoken);
        EasyAPI.apiConnectionAsync(FirstActivity.this, true, false, ApiMethodDescription.get("/ServiceWeather.asmx/GetDay"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                System.out.println(result);
                Tenbean tenbean = GsonUtil.JsonToObject(result.toString(), Tenbean.class);
                Tenbean.DataBean data = tenbean.getData();
                List<Tenbean.DataBean.ItemsBean> items = data.getItems();
                Tenbean.DataBean.ItemsBean todaybean = items.get(0);
                //2019-05-29 获取城市名称
                String cityName = data.getCity_name();
                String todaytemp = todaybean.getTempe();
                String todayweather = todaybean.getWeather();
                Tqpicbean gettodayweather = WeatherUtil.getweather(todayweather);
//                if( todaytemp.contains("~")){
//                    todaytemp= todaytemp.replace("~", "/");
//                }
                tv_tianqi.setText(gettodayweather.tianqimiaoshu + "     " + todaytemp);
            }

            @Override
            public String onFailed(ApiError error) {
                return null;
            }
        });*/
    }

    /**
     * 获取当前时间点的天气情况
     * @param token
     * @param longgitude
     * @param latiformat
     */
    private void getHourWeather(final String cityName, String token, String longgitude, String latiformat){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", token);
        params.put("JD", longgitude);
        params.put("WD", latiformat);
        System.out.println("params.............."+params);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceWeather.asmx/GetHour"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                System.out.println(result);
                Hourbean hourbean = GsonUtil.JsonToObject(result.toString(), Hourbean.class);
                List<Hourbean.DataBean> data = hourbean.getData();
                Hourbean.DataBean dataBean = data.get(0);
                String wthr = dataBean.getWthr();
                tv_tianqi.setText(cityName+" > "+wthr);
                tv_temp.setText(dataBean.getTemp()+"℃");
            }
            @Override
            public String onFailed(ApiError error) {
                System.out.println("shibai......"+error.errorMeesage);
                return null;
            }
        });
    }
    /** 获得所在位置经纬度及详细地址 */
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
                final String city = location.getCity().substring(0,location.getCity().length()-1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getHourWeather(city,Contenttianqi.gettoken(), longformat, latiformat);
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
    private void getLogin() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("UserName", Shref.getString(FirstActivity.this, Common.USERNAME, ""));
        // params.put("UserName", "www");
        params.put("PassWord", Shref.getString(FirstActivity.this, Common.PASSWORD, ""));
        params.put("DeviceId", Shref.getString(FirstActivity.this, Common.DEVICEID, ""));
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/UserLogin"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                System.out.println(result);
            }

            @Override
            public String onFailed(final ApiError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error.errorCode == 3000) {
                            System.out.println(error.errorCode);
                            System.out.println(error.errorDomain);
                            System.out.println(error.errorMeesage);
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

    private void getUpdate() {
        HashMap<String, Object> updateparams = new HashMap<String, Object>();
        updateparams.put("PlatformType", "1");//1为安卓
        updateparams.put("SystemType", "2");//2为内部版
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), updateparams, new EasyAPI.ApiFastSuccessCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                String versionName = null;
                try {
                    versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
                    System.out.println("versionName.." + versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                JsonObject json = result.getAsJsonObject().get("Data").getAsJsonArray().get(0).getAsJsonObject();
                String serviceVersion = json.get("Version").getAsString();
                String[] localVersionArray = versionName.split("\\.");
                String[] serviceVersionArray = serviceVersion.split("\\.");
                int localCount = localVersionArray.length;
                int serviceCount = serviceVersionArray.length;
                int count = localCount;
                if (localCount > serviceCount) {
                    count = serviceCount;
                }
                Boolean updateFlag = false;
                try {
                    for (int i = 0; i < count; i++) {
                        if (Integer.parseInt(localVersionArray[i]) < Integer.parseInt(serviceVersionArray[i])) {
                            updateFlag = true;
                            AppApplication.isupdate = true;
                        }
                    }
                } catch (Exception e) {
                    updateFlag = false;
                    AppApplication.isupdate = false;
                }
                if (updateFlag) {
                    final String urlString = json.get("Update_Url").getAsString();
                    String description = "更新内容:\r\n" + json.get("Description").getAsString() + "是否立即前往更新";
                    LSAlert.showDialog(FirstActivity.this, "检查到新版本", description, "确定", "取消", new LSAlert.DialogCallback() {

                        @Override
                        public void onConfirm() {
                            // TODO Auto-generated method stub
                            downLoadFile(urlString);
//								if (updatefile == null){
//									LSAlert.showAlert(SettingActivity.this, "未成功下载更新软件，请稍后再试");
//									return;
//								}
//								else{
//									installApk(updatefile);
//								}
                        }

                        @Override
                        public void onCancel() {
                            // TODO Auto-generated method stub

                        }
                    });
                }
            }

        });
    }

    protected void downLoadFile(String httpUrl) {
        // TODO Auto-generated method stub
        final Uri uri = Uri.parse(httpUrl);
        final Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    private void initView() {
        ll_tianqi = (LinearLayout) findViewById(R.id.ll_tianqi);
        tv_tianqi = (TextView) findViewById(R.id.tv_tianqi);
        iv_tianqi = (ImageView) findViewById(R.id.iv_tianqi);
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
        setHeight(ll_tianqi);

        ll_tianqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstActivity.this, TianqiActivity.class));
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
                                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //"android.media.action.IMAGE_CAPTURE";
                                            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                                            //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                            startActivityForResult(intent, 0);
                                        } else {
                                            Intent intent = new Intent();
                    /* �?��Pictures画面Type设定为image */
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

    private void initData() {
        String picname = Shref.getString(FirstActivity.this, Common.PICNAME, null);
        if (picname == "" || picname == null) {
            iv_touxiang.setImageResource(R.drawable.touxiang);
        } else {
            ImageLoader.getInstance().displayImage(picname, iv_touxiang, ImageloaderUtil.imageconfig());
        }
        ArrayList<LinearLayout> llal = new ArrayList<LinearLayout>();
        llal.add(ll_seccion1);
        llal.add(ll_seccion2);
        llal.add(ll_seccion3);
        Intent intent = getIntent();
        final String userPower = intent.getStringExtra("userpower");
        String username = intent.getStringExtra("username");
        tv_yonghuming.setText(username);
        System.out.println("FirstActivity......." + userPower);
        if (userPower.equals("00010010100000")) {//j20623 279162 见证人
            System.out.println("zhelili");
            ll_seccion1.setVisibility(View.VISIBLE);
            ll_seccion2.setVisibility(View.VISIBLE);
            ll_seccion3.setVisibility(View.VISIBLE);
            ll_seccion4.setVisibility(View.VISIBLE);
            ll_seccion5.setVisibility(View.VISIBLE);
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
            ll_seccion3.setVisibility(View.VISIBLE);
            ll_seccion4.setVisibility(View.VISIBLE);
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
            ll_seccion5.setVisibility(View.VISIBLE);
            ll_seccion6.setVisibility(View.VISIBLE);

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
            ll_seccion1.setVisibility(View.VISIBLE);
            ll_seccion2.setVisibility(View.VISIBLE);
            ll_seccion3.setVisibility(View.VISIBLE);
            ll_seccion4.setVisibility(View.VISIBLE);
            ll_seccion5.setVisibility(View.VISIBLE);
            ll_seccion6.setVisibility(View.VISIBLE);
            iv_seccion1.setImageResource(R.drawable.shujushenhe1);
            tv_seccion1.setText("记录审核");
            iv_seccion2.setImageResource(R.drawable.baogaopizhun);
            tv_seccion2.setText("报告批准");
            iv_seccion3.setImageResource(R.drawable.jinritongji1);
            tv_seccion3.setText("统计信息");
            iv_seccion4.setImageResource(R.drawable.shouchijisaomiao);
            tv_seccion4.setText("手持机扫描");
            iv_seccion5.setImageResource(R.drawable.xitongshezhi);
            tv_seccion5.setText("系统设置");
            iv_seccion6.setImageResource(R.drawable.tuichu);
            tv_seccion6.setText("重新登录");


            ll_seccion1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FirstActivity.this, DataAuditingActivity.class);
                    startActivity(intent);
                }
            });
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
                        //intent.putExtra("deviceAddress", Shref.getString(FirstActivity.this,"deviceaddress",null));
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
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inTempStorage = new byte[100 * 1024];
                    opts.inPreferredConfig = Bitmap.Config.RGB_565;
//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
                    opts.inPurgeable = true;
//5.设置位图缩放比例
//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
                    opts.inSampleSize = 4;
//6.设置解码位图的尺寸信息
                    opts.inInputShareable = true;
//7.解码位图
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg", opts);
                    SavePic.savePhotoToSDCard(bitmap, Environment.getExternalStorageDirectory().getAbsolutePath() + "/image", timename);
                    String s = "file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/image/" + timename + ".jpge";
                    System.out.println(s);
                    ImageLoader.getInstance().displayImage(s, iv_touxiang, ImageloaderUtil.imageconfig());
                    Shref.setString(FirstActivity.this, Common.PICNAME, s);
                    break;
                case 1:
                    ContentResolver cr = this.getContentResolver();
                    Uri uri = intent.getData();
                    try {
                        String decode = URLDecoder.decode(String.valueOf(uri), "UTF-8");
                        System.out.println(decode);
                        if (uri != null) {
                            System.out.println("走到这里。。。。。。。。。" + decode);
                            ImageLoader.getInstance().displayImage(decode, iv_touxiang, ImageloaderUtil.imageconfig());
                            Shref.setString(FirstActivity.this, Common.PICNAME, decode);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }

    }



}
