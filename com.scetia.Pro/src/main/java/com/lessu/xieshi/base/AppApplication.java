package com.lessu.xieshi.base;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.SDKInitializer;
import com.lessu.net.ApiBase;
import com.lessu.net.ApiConnection;
import com.lessu.xieshi.BuildConfig;
import com.scetia.Pro.baseapp.ShareableApplication;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.network.ConstantApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;

import static cn.bingoogolapple.qrcode.core.BGAQRCodeUtil.isDebug;


public class AppApplication extends ShareableApplication {
    public static String muidstr;
    public static boolean isGLY = false;

    @Override
    public void init() {
        initARouter();
        initQBWeb();
        initSmartRefresh();
        //初始化百度地图SDK
        SDKInitializer.initialize(this);
        initApi();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * 规定smartRefresh的样式风格
     */
    private void initSmartRefresh() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setDrawableSize(20f));
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(20f));
    }

    /**
     * 初始化腾讯x5内核浏览器
     */
    private void initQBWeb() {
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                LogUtil.showLogE("X5WebView初始化===" + b);
            }
        });
    }

    /**
     * 初始化api接口服务
     */
    private void initApi() {
        ApiConnection.DefaultStandardDataKey = "Data";
        ApiConnection.DefaultStandardErrorCodeKey = "Code";
        ApiConnection.DefaultStandardMessageKey = "Message";
        ApiConnection.DefaultStandardSuccessKey = "Success";
        //每次进入程序都要重新默认为电信服务
        SPUtil.setSPLSUtil(Constants.Setting.SERVICE, ConstantApi.XS_TELECOM_BASE_URL);
        ApiBase.sharedInstance().apiUrl = ConstantApi.XS_TELECOM_BASE_URL;
    }


    /**
     * 初始化ARouter路由SDK
     */
    private void initARouter(){
        if (BuildConfig.DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this);
    }
}
