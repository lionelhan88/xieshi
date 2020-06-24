package com.lessu.xieshi.scan;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Changezifu;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.XieShiSlidingMenuActivity;
import com.lessu.xieshi.bean.Project;
import com.lessu.xieshi.bean.PushToDx;
import com.lessu.xieshi.bean.TrainingUserInfo;
import com.lessu.xieshi.http.RetrofitManager;
import com.lessu.xieshi.training.ApiObserver;
import com.lessu.xieshi.training.BaseResponse;
import com.lessu.xieshi.training.ScanEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ScanActivity extends XieShiSlidingMenuActivity implements QRCodeView.Delegate {

    private TrainingUserInfo curTrainingInfo;
    private ZXingView zXingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("扫码登录");
        zXingView = (ZXingView) findViewById(R.id.zxingview);
        zXingView.setDelegate(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //打开摄像头预览
        zXingView.startCamera();
        //显示扫描框，并开始识别
        zXingView.startSpotAndShowRect();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky =true)
    public void receiverTrainingUserInfo(ScanEvent scanEvent){
        curTrainingInfo = scanEvent.getTrainingUserInfo();
        EventBus.getDefault().removeStickyEvent(scanEvent);
    }
    @Override
    public void onScanQRCodeSuccess(String result) {
        /**
         * 接受到扫描返回的数据，开始请求登陆
         */
        if(!result.equals("")){
            if(result.contains("chinanetLearning")){
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
                String sign = Changezifu.md5(paramstr).toUpperCase();
                pushToDx.setSign(sign);
                pushToDx.setGuid(guid);
                Gson gson = new Gson();
                String params=gson.toJson(pushToDx);
                RequestBody body = RequestBody.create(MediaType.parse("application/json"),params);
                RetrofitManager.getInstance().getService().updateUserCourse(body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ApiObserver<BaseResponse<PushToDx>>() {
                            @Override
                            public void success(BaseResponse<PushToDx> baseResponse) {
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
            }else {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("s1", result);
                //传入UserId
                params.put("s2", Shref.getString(this, Common.USERID, ""));
                //传入userName
                params.put("s3", Shref.getString(this, Common.USERNAME, ""));
                LogUtil.showLogD(params.toString());
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
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LSAlert.showAlert(this,"打开相机出错！");
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
