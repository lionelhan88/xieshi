package com.lessu.xieshi.training;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Changezifu;
import com.lessu.xieshi.bean.PushToDx;
import com.lessu.xieshi.http.RetrofitManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class OnlineLearnActivity extends NavigationActivity {
    private WebView onlineWebView;
    private FrameLayout fullScreenView;
    private View customerView;
    private WebChromeClient.CustomViewCallback mCallBack;
    private RelativeLayout rl_web_view_full_title;
    private ImageView imageBack;
    private PowerManager.WakeLock wakeLock;
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

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        //禁止屏幕休眠
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        onlineWebView = (WebView) findViewById(R.id.online_web_view);
        fullScreenView = (FrameLayout) findViewById(R.id.full_screen_view);
        rl_web_view_full_title = (RelativeLayout) findViewById(R.id.rl_web_view_full_title);
        imageBack = (ImageView) findViewById(R.id.online_web_view_full_back);
        final WebSettings settings = onlineWebView.getSettings();
        //支持js事件
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        onlineWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //这里返回false，页面重定向后也会返回上一级页面
                return false;
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
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(title);
                super.onReceivedTitle(view, title);
            }

            @Nullable
            @Override
            public View getVideoLoadingProgressView() {
                return super.getVideoLoadingProgressView();
            }

            @Override
            public void onShowCustomView(final View view, final CustomViewCallback callback) {
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
        String sign = Changezifu.md5(paramstr).toUpperCase();
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
                        String sign = Changezifu.md5(paramstr).toUpperCase();
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
        View decorView = getWindow().getDecorView();
        int orientation = getResources().getConfiguration().orientation;
        //如果是竖排则变为横排
        if(orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(FLAG_FULLSCREEN,FLAG_FULLSCREEN);
            navigationBar.setVisibility(View.GONE);
            //横屏时隐藏系统底部导航栏
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }else{
            //如果是横排，则变为竖排
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().clearFlags(FLAG_FULLSCREEN);
            navigationBar.setVisibility(View.VISIBLE);
            //竖屏时显示系统底部导航栏
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void alphaAnimation(float start,float end,final Runnable runnable){
        Animator animator = ObjectAnimator.ofFloat(fullScreenView,"alpha",start,end);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //动画结束
                runnable.run();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 当用户点击home建退出软件再打开时，要判断之前是否是再全屏状态观看视频，如果是
         * 就在打开软件时继续隐藏系统导航栏
         */
        if(customerView!=null){
            View decorView = getWindow().getDecorView();
            //横屏时隐藏系统底部导航栏
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
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