package com.lessu.xieshi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.lessu.ShareableApplication;
import com.lessu.foundation.DensityUtil;
import com.lessu.foundation.LSUtil;
import com.lessu.net.ApiBase;
import com.lessu.net.ApiConnection;
import com.lessu.xieshi.login.LoginActivity;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;

import java.util.Stack;

public class AppApplication extends ShareableApplication{
	public static boolean isfirst=true;
	private static AppApplication mcontext;
	public static String muidstr;
	public static boolean isGLY=false;
	@Override
	public void onCreate() {
		super.onCreate();
		mcontext=this;
		DensityUtil.initInstance(this);
		LSUtil.setValueStatic("service", "telecom");
		initQBWeb();
		initSmartRefresh();
		//初始化百度地图SDk
		SDKInitializer.initialize(this);
		ShareableApplication.sharedApplication = this;
		initApi();
		initImageLoader();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	/**
	 * 初始化smartRefresh
	 */
	private void initSmartRefresh(){
		SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
			@NonNull
			@Override
			public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
				//设置底部加载样式为经典样式
				return new ClassicsHeader(context).setDrawableSize(20f);
			}
		});
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
	 * 初始化ImageLoader
	 */
	private void initImageLoader(){
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				//.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)//线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100) //缓存的文件数量
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.writeDebugLogs() // Remove for release app
				.build();//开始构建
		ImageLoader.getInstance().init(config);//全局初始化此配置
	}

	/**
	 * 初始化api接口服务
	 */
	private void initApi(){
		ApiConnection.DefaultStandardDataKey 		= "Data";
		ApiConnection.DefaultStandardErrorCodeKey	= "Code";
		ApiConnection.DefaultStandardMessageKey 	= "Message";
		ApiConnection.DefaultStandardSuccessKey		= "Success";

		config.serviceMap.put("telecom","www.scetia.com/scetia.app.ws");
		config.serviceMap.put("unicom","www.schetia.com/scetia.app.ws");
		if (LSUtil.valueStatic("service")== null || LSUtil.valueStatic("service").isEmpty()){
			LSUtil.setValueStatic("service", "telecom");
		}
		ApiBase.sharedInstance().apiUrl = config.serviceMap.get(LSUtil.valueStatic("service"));
	}
	public static Context getAppContext(){
		return mcontext;
	}

	/**
	 * 退出程序，清空栈
	 */
	public static void  exit(){
		for (Activity activity :activities){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
		activities.clear();
	}
}
