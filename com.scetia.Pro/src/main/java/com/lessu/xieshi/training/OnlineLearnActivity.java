package com.lessu.xieshi.training;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.LongString;
import com.lessu.xieshi.bean.PushToDx;
import com.lessu.xieshi.http.RetrofitManager;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
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
    private com.tencent.smtt.sdk.WebView onlineWebView;
    private FrameLayout fullScreenView;
    private View customerView;
    private IX5WebChromeClient.CustomViewCallback mCallBack;
    private RelativeLayout rl_web_view_full_title;
    private ImageView imageBack;
    private ImmersionBar immersionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_learn);
        setTitle("在线课程");
        navigationBar.setBackgroundColor(0xFF3598DC);
        initView();
        initData();
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
    private void initView() {

        //禁止屏幕休眠
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        onlineWebView =  findViewById(R.id.online_web_view);
        fullScreenView = (FrameLayout) findViewById(R.id.full_screen_view);
        rl_web_view_full_title = (RelativeLayout) findViewById(R.id.rl_web_view_full_title);
        imageBack = (ImageView) findViewById(R.id.online_web_view_full_back);
        final com.tencent.smtt.sdk.WebSettings settings = onlineWebView.getSettings();
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
        //支持js事件
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        onlineWebView.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {
                // TODO Auto-generated method stub
                disableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onX5ButtonClicked() {
                disableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onCustomButtonClicked() {
              disableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onLiteWndButtonClicked() {
                disableX5FullscreenFunc();
               // FullScreenActivity.this.enableLiteWndFunc();
            }

            @JavascriptInterface
            public void onPageVideoClicked() {
                disableX5FullscreenFunc();
               // FullScreenActivity.this.enablePageVideoFunc();
            }
        }, "Android");
        onlineWebView.setWebViewClient(new WebViewClient(){

          /*  @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //这里返回false，页面重定向后也会返回上一级页面
                return false;
            }*/

            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(LSAlert.progressDialog==null){
                    LSAlert.showProgressHud(OnlineLearnActivity.this,"正在加载...");
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
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

        onlineWebView.setWebChromeClient(new WebChromeClient(){
            @Nullable
            @Override
            public View getVideoLoadingProgressView() {
                return super.getVideoLoadingProgressView();
            }

            @Override
            public void onShowCustomView(final View view, final IX5WebChromeClient.CustomViewCallback callback) {
                super.onShowCustomView(view,callback);
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
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击了返回键
                noFullScreen();
            }
        });
    }
    private void initData() {
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void receiverPushToDx(PushToDx pushToDx){
        EventBus.getDefault().removeStickyEvent(pushToDx);
        getGuid2(pushToDx);
    }
    private void disableX5FullscreenFunc() {
        if (onlineWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            onlineWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }
    /**
     * 不再全屏
     */
    private void noFullScreen(){
        if(customerView!=null){
            if(mCallBack!=null){
                mCallBack.onCustomViewHidden();
                mCallBack = null;
            }
            fullScreen();
            fullScreenView.setVisibility(View.GONE);
            rl_web_view_full_title.setVisibility(View.GONE);
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
        RetrofitManager.getInstance().getService().updateUserCourse(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<BaseResponse<PushToDx>>() {
                    @Override
                    public void success(BaseResponse<PushToDx> pushToDxBaseResponse) {
                        String guid =pushToDxBaseResponse.getRecord().getGuid();
                        String userId = pushToDx.getUserId();
                        String date = sdf.format(new Date());
                        String paramstr = "guid="+guid+"&timestamp="+date+"&userId="+userId+"&secret=Rpa00Wcw9yaI";
                        String sign = LongString.md5(paramstr).toUpperCase();
                        String indexUrl ="https://bgtj.o-learn.cn/thirdparty/jzjc/appInterfaceApi/enterCourseList?"
                                +"guid="+guid+"&userId="+userId+"&timestamp="+date+"&sign="+sign;

                        onlineWebView.loadUrl(indexUrl);
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
    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 当用户点击home建退出软件再打开时，要判断之前是否是再全屏状态观看视频，如果是
         * 就在打开软件时继续隐藏系统导航栏
         */
        if(customerView!=null){
            immersionBar.fullScreen(true).hideBar(BarHide.FLAG_HIDE_BAR).init();
        }
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