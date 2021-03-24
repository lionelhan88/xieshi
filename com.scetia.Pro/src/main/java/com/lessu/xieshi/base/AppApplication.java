package com.lessu.xieshi.base;

import android.content.Context;

import androidx.multidex.MultiDex;
import com.baidu.mapapi.SDKInitializer;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.network.ConstantApi;
import com.scetia.Pro.baseapp.ShareableApplication;
import com.lessu.net.ApiBase;
import com.lessu.net.ApiConnection;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.SPUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;


public class AppApplication extends ShareableApplication {
	public static String muidstr;
	public static boolean isGLY=false;

	@Override
	public void init() {
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
	private void initSmartRefresh(){
		SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setDrawableSize(20f));
		SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(20f));
	}
	/**
	 * 初始化腾讯x5内核浏览器
	 */
	private void initQBWeb(){
		QbSdk.setDownloadWithoutWifi(true);
		QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
			@Override
			public void onCoreInitFinished() {

			}

			@Override
			public void onViewInitFinished(boolean b) {
				LogUtil.showLogE("X5WebView初始化==="+b);
			}
		});
	}
	/**
	 * 初始化api接口服务
	 */
	private void initApi(){
		ApiConnection.DefaultStandardDataKey 		= "Data";
		ApiConnection.DefaultStandardErrorCodeKey	= "Code";
		ApiConnection.DefaultStandardMessageKey 	= "Message";
		ApiConnection.DefaultStandardSuccessKey		= "Success";
		//每次进入程序都要重新默认为电信服务
		SPUtil.setSPLSUtil(Constants.Setting.SERVICE, ConstantApi.XS_TELECOM_BASE_URL);
		ApiBase.sharedInstance().apiUrl = ConstantApi.XS_TELECOM_BASE_URL;
	}
}
