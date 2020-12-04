package com.lessu.xieshi.module.scan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.Utils.LongString;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.lessu.xieshi.bean.PushToDx;
import com.lessu.xieshi.bean.TrainingUserInfo;
import com.lessu.xieshi.http.TrainRetrofit;
import com.lessu.xieshi.module.training.ApiObserver;
import com.lessu.xieshi.module.training.TrainingResultData;
import com.lessu.xieshi.module.training.ScanEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ScanActivity extends XieShiSlidingMenuActivity implements QRCodeView.Delegate {

    private TrainingUserInfo curTrainingInfo;
    private ZXingView zXingView;
    private String scanType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        navigationBar.setBackgroundColor(0x80000000);
        this.setTitle("扫一扫");
        zXingView =  findViewById(R.id.zxingview);
        zXingView.setDelegate(this);
        EventBus.getDefault().register(this);
        if(getIntent()!=null){
            scanType = getIntent().getStringExtra(Common.SCAN_TYPE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //打开摄像头预览
        zXingView.startCamera();
        //不显示扫描框，全屏识别，并开始识别
        zXingView.startSpotAndShowRect();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky =true)
    public void receiverTrainingUserInfo(ScanEvent scanEvent){
        curTrainingInfo = scanEvent.getTrainingUserInfo();
        EventBus.getDefault().removeStickyEvent(scanEvent);
    }
    @Override
    public void onScanQRCodeSuccess(String result) {
        startVibrate();
        if(result.equals("")){
            LSAlert.showAlert(this,"扫码的内容为空！");
            zXingView.startSpot();
            return;
        }
        if(getIntent().getStringExtra(Common.SCAN_TYPE)!=null&&!result.contains("ScetiaMeetingCode")){
            LSAlert.showAlert(this, "提示", "无效签到扫描，请联系协会工作人员！",
                    "确定", false, new LSAlert.AlertCallback() {
                        @Override
                        public void onConfirm() {
                            finish();
                        }
                    });
            return;
        }
        /**
         * 接受到扫描返回的数据，开始请求登陆
         */
            if(result.contains("chinanetLearning")){
                //如果用户
                if(curTrainingInfo==null){
                    LSAlert.showAlert(this, "提示", "请使用在线培训页面的扫码进行登录！", "确定",
                            new LSAlert.AlertCallback() {
                                @Override
                                public void onConfirm() {
                                    finish();
                                }
                            });
                    return;
                }
                LSAlert.showProgressHud(this,"正在登陆...");
                String guid = result.substring(result.lastIndexOf(":")+1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String date = sdf.format(new Date());
                //登录在线培训平台
                PushToDx pushToDx = curTrainingInfo.getPushToDx();
                pushToDx.setCertificateNo(curTrainingInfo.getCertificateNo());
                pushToDx.setFullName(curTrainingInfo.getFullName());
                pushToDx.setTimestamp(date);
                String paramstr = "certificateNo="+pushToDx.getCertificateNo()+"&fullName="+
                        pushToDx.getFullName()+"&guid="+guid+"&timestamp="+pushToDx.getTimestamp()+"&userId="
                        +pushToDx.getUserId()+"&secret=Rpa00Wcw9yaI";
                //对字符串进行md5加密
                String sign = LongString.md5(paramstr).toUpperCase();
                pushToDx.setSign(sign);
                pushToDx.setGuid(guid);
                Gson gson = new Gson();
                String params=gson.toJson(pushToDx);
                RequestBody body = RequestBody.create(MediaType.parse("application/json"),params);
                TrainRetrofit.getInstance().getService().updateUserCourse(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ApiObserver<TrainingResultData<PushToDx>>() {
                            @Override
                            public void success(TrainingResultData<PushToDx> trainingResultData) {
                                LSAlert.dismissProgressHud();
                                LSAlert.showAlert(ScanActivity.this, "提示", "登陆成功", "确定", new LSAlert.AlertCallback() {
                                    @Override
                                    public void onConfirm() {
                                        //登陆成功，返回上层页面
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void failure(String errorMsg) {
                                zXingView.startSpot();
                                LSAlert.dismissProgressHud();
                                LSAlert.showAlert(ScanActivity.this,errorMsg);
                            }
                        });
            }else if(result.contains("ScetiaMeetingCode")){
                if(getIntent().getStringExtra(Common.SCAN_TYPE)==null){
                    LSAlert.showAlert(this, "提示", "请在会议现场页面中，使用右上角扫码签到",
                            "确定", false, new LSAlert.AlertCallback() {
                                @Override
                                public void onConfirm() {
                                    finish();
                                }
                            });
                    return;
                }
                //这是会议签到的扫码内容
                //当前是会议扫码签到
                Intent intent = new Intent();
                intent.putExtra("scanResult",result.substring(result.indexOf(":")+1));
                setResult(RESULT_OK,intent);
                finish();
            } else {
                HashMap<String, Object> params = new HashMap<>();
                params.put("s1", result);
                //传入UserId
                params.put("s2", Shref.getString(this, Common.USERID, ""));
                //传入userName
                params.put("s3", Shref.getString(this, Common.USERNAME, ""));
                EasyAPI.apiConnectionAsync(this, true, false,
                        ApiMethodDescription.get("/ServiceMis.asmx/ScanLogin"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                            @Override
                            public void onSuccessJson(JsonElement result) {
                                LogUtil.showLogD(result.toString());
                                //处理返回数据
                                boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                                if (isSuccess) {
                                    LSAlert.showAlert(ScanActivity.this, "提示", "登陆成功", "确定", new LSAlert.AlertCallback() {
                                        @Override
                                        public void onConfirm() {
                                            //登陆成功，返回上层页面
                                            finish();
                                        }
                                    });
                                } else {
                                    LSAlert.showAlert(ScanActivity.this, "提示", "登陆失败！", "确定", new LSAlert.AlertCallback() {
                                        @Override
                                        public void onConfirm() {
                                            //登录失败，开启再次扫描
                                            zXingView.startSpot();
                                        }
                                    });
                                }
                            }

                            @Override
                            public String onFailed(ApiError error) {
                                LSAlert.showAlert(ScanActivity.this, "提示", "登陆失败！", "确定", new LSAlert.AlertCallback() {
                                    @Override
                                    public void onConfirm() {
                                        //登录失败，开启再次扫描
                                        zXingView.startSpot();
                                    }
                                });
                                return null;
                            }
                        });
            }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LSAlert.showAlert(this,"打开相机出错！");
    }

    /**
     * 开启震动
     */
    private void startVibrate(){
        //开始震动
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    @Override
    protected void onStop() {
        super.onStop();
        zXingView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //销毁二维码扫描控件
        zXingView.onDestroy();
    }
}
