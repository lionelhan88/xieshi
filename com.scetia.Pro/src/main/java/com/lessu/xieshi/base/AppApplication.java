package com.lessu.xieshi.base;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import com.baidu.mapapi.SDKInitializer;
import com.lessu.ShareableApplication;
import com.lessu.foundation.LSUtil;
import com.lessu.net.ApiBase;
import com.lessu.net.ApiConnection;
import com.lessu.xieshi.Utils.Common;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;


public class AppApplication extends ShareableApplication{
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
		SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
			//设置顶部加载样式为经典样式
			return new ClassicsHeader(context).setDrawableSize(20f);
		});
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
		LSUtil.setValueStatic("service", "telecom");
		Common.serviceMap.put("telecom","www.scetia.com/scetia.app.ws");
		Common.serviceMap.put("unicom","www.schetia.com/scetia.app.ws");
		ApiBase.sharedInstance().apiUrl = Common.serviceMap.get(LSUtil.valueStatic("service"));
	}
}
