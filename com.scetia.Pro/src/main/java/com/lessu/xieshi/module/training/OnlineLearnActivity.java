package com.lessu.xieshi.module.training;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.LongString;
import com.lessu.xieshi.http.service.TraningApiService;
import com.lessu.xieshi.module.training.bean.PushToDx;
import com.scetia.Pro.network.manage.TrainRetrofit;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class OnlineLearnActivity extends NavigationActivity {
    private X5WebView onlineWebView;
    private FrameLayout fullScreenView;
    private View customerView;
    private IX5WebChromeClient.CustomViewCallback mCallBack;
    private ImmersionBar immersionBar;
    private ProgressBar progressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_online_learn;
    }

    @Override
    protected void leftNavBarClick() {
        if(onlineWebView.canGoBack()){
            onlineWebView.goBack();
        }else{
            finish();
        }
    }

    @Override
    protected void initImmersionBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.titleBar(navigationBar).init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        setTitle("在线课程");
        //禁止屏幕休眠
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        onlineWebView =  findViewById(R.id.online_web_view);
        fullScreenView =  findViewById(R.id.full_screen_view);
        progressBar = findViewById(R.id.progress);
        onlineWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                //这里返回false，页面重定向后也会返回上一级页面
                return false;
            }

            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView  view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                isAnimtor=true;
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView  view, String url) {
                super.onPageFinished(view, url);
                if(!isAnimtor){
                    startDismissAnimation(50);
                }
                progressBar.setVisibility(View.GONE);
                //页面加载结束
                //编写 javaScript方法
                if(url.contains("enterCourseList")) {
                    //去除页面自带的标题栏
                    String javascript = "javascript:function hideOther() {" +
                            "document.getElementsByTagName('div')[1].style.display='none';}";
                    view.loadUrl(javascript);
                    view.loadUrl("javascript:hideOther();");
                }
                LSAlert.dismissProgressHud();
            }
        });

        onlineWebView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                int curProgress = progressBar.getProgress();
                startProgressAnimation(curProgress,i);
                progressBar.setProgress(i);
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                setTitle(s);
            }
            @Override
            public void onShowCustomView(final View view, final IX5WebChromeClient.CustomViewCallback callback) {
                super.onShowCustomView(view,callback);
                //如果当前设备不支持x5内核，则执行自定义的全屏模式
                customerView = view;
                mCallBack = callback;
                fullScreenView.setVisibility(View.VISIBLE);
                onlineWebView.setVisibility(View.GONE);
                fullScreen();
                fullScreenView.addView(view);
            }

            @Override
            public void onHideCustomView() {
                noFullScreen();
                super.onHideCustomView();

            }
        });
    }
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void receiverPushToDx(PushToDx pushToDx){
        EventBus.getDefault().removeStickyEvent(pushToDx);
        getGuid2(pushToDx);
    }
    /**
     * 不再全屏
     * 如果手机支持x5内核，不需要执行此方法
     */
    private void noFullScreen(){
        if(customerView!=null){
            if(mCallBack!=null){
                mCallBack.onCustomViewHidden();
                mCallBack = null;
            }
            fullScreen();
            fullScreenView.setVisibility(View.GONE);
            onlineWebView.setVisibility(View.VISIBLE);
            customerView = null;
        }
    }

    /**
     * 请求获取Guid
     */
    private void getGuid2(final PushToDx pushToDx){
        LSAlert.showProgressHud(this,"正在获取课程...");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        //登录在线培训平台
        pushToDx.setTimestamp(date);
        String paramstr = "certificateNo="+pushToDx.getCertificateNo()+"&fullName="+
                pushToDx.getFullName()+"&timestamp="+pushToDx.getTimestamp()+"&userId="
                +pushToDx.getUserId()+"&secret=Rpa00Wcw9yaI";
        //对字符串进行md5加密
        String sign = LongString.md5(paramstr).toUpperCase();
        pushToDx.setSign(sign);
        pushToDx.setGuid("");
        Gson gson = new Gson();
        String params=gson.toJson(pushToDx);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),params);
        TrainRetrofit.getInstance().getService(TraningApiService.class).updateUserCourse(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<TrainingResultData<PushToDx>>() {
                    @Override
                    public void success(TrainingResultData<PushToDx> pushToDxTrainingResultData) {
                        String guid = pushToDxTrainingResultData.getRecord().getGuid();
                        String userId = pushToDx.getUserId();
                        String date = sdf.format(new Date());
                        String paramstr = "guid="+guid+"&timestamp="+date+"&userId="+userId+"&secret=Rpa00Wcw9yaI";
                        String sign = LongString.md5(paramstr).toUpperCase();
                        String indexUrl ="https://bgtj.o-learn.cn/thirdparty/jzjc/appInterfaceApi/enterCourseList?"
                                +"guid="+guid+"&userId="+userId+"&timestamp="+date+"&sign="+sign;
                        onlineWebView.loadUrl(indexUrl);
                        LSAlert.dismissProgressHud();
                    }

                    @Override
                    public void failure(String errorMsg) {
                        LSAlert.dismissProgressHud();
                        LSAlert.showAlert(OnlineLearnActivity.this,errorMsg);
                    }
                });
    }
    /**
     * 全屏显示
     */
    private void fullScreen(){
        int orientation = getResources().getConfiguration().orientation;
        //如果是竖排则变为横排
        if(orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            navigationBar.setVisibility(View.GONE);
            //全屏显示并且隐藏状态栏和导航栏
            immersionBar.fullScreen(true).hideBar(BarHide.FLAG_HIDE_BAR).init();
        }else{
            //如果是横排，则变为竖排
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            navigationBar.setVisibility(View.VISIBLE);
            //退出全屏并且显示导航栏和状态栏
           immersionBar.fullScreen(false).hideBar(BarHide.FLAG_SHOW_BAR).init();
        }
    }

    /**
     * 开启增长动画
     * @param currentProgress
     * @param newProgress
     */
    private boolean isAnimtor = false;
    private void startProgressAnimation(int currentProgress,int newProgress){
        ObjectAnimator startAnimator = ObjectAnimator.ofInt(progressBar,"progress",currentProgress,newProgress);
        startAnimator.setDuration(500);
        startAnimator.setInterpolator(new DecelerateInterpolator());
        startAnimator.start();
    }
    private void startDismissAnimation(int currentProgress){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(progressBar,"alpha",1.0f,0.0f);
        objectAnimator.setDuration(1500);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(View.GONE);
                isAnimtor = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 当用户点击home建退出软件再打开时，要判断之前是否是再全屏状态观看视频，如果是
         * 就在打开软件时继续隐藏系统导航栏
         */
        onlineWebView.onResume();
        if(customerView!=null){
            immersionBar.fullScreen(true).hideBar(BarHide.FLAG_HIDE_BAR).init();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onlineWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        onlineWebView.destroy();
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            //点击了系统的返回按钮
            if(customerView!=null){
                //此时还在全屏状态，先退出全屏状态
                noFullScreen();
                return true;
            }else if(onlineWebView.canGoBack()){
                onlineWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}