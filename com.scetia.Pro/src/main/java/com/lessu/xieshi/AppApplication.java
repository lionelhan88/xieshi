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
	public final static int APP_STATUS_KILLED = 0; // 表示应用是被杀死后在启动的
	public final static int APP_STATUS_NORMAL = 1; // 表示应用时正常的启动流程
	public static int APP_STATUS = APP_STATUS_KILLED; // 记录App的启动状态
	@Override
	public void onCreate() {
		super.onCreate();
		mcontext=this;
		DensityUtil.initInstance(this);
		//cc_edit
		LSUtil.setValueStatic("service", "telecom");
		QbSdk.setDownloadWithoutWifi(true);
		QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
			@Override
			public void onCoreInitFinished() {

			}

			@Override
			public void onViewInitFinished(boolean b) {
			}
		});
		SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
			@NonNull
			@Override
			public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
				//设置底部加载样式为经典样式
				ClassicsHeader classicsHeader = new ClassicsHeader(context).setDrawableSize(20f);
				return classicsHeader;
			}
		});
		ApiConnection.DefaultStandardDataKey 		= "Data";
		ApiConnection.DefaultStandardErrorCodeKey	= "Code";
		ApiConnection.DefaultStandardMessageKey 	= "Message";
		ApiConnection.DefaultStandardSuccessKey		= "Success";
		ShareableApplication.sharedApplication = this;
		SDKInitializer.initialize(this);

		config.serviceMap.put("telecom","www.scetia.com/scetia.app.ws");
		config.serviceMap.put("unicom","www.schetia.com/scetia.app.ws");
		if (LSUtil.valueStatic("service")== null || LSUtil.valueStatic("service").isEmpty()){
			LSUtil.setValueStatic("service", "telecom");
		}

		ApiBase.sharedInstance().apiUrl = config.serviceMap.get(LSUtil.valueStatic("service"));


		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				//.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)//线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				//.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100) //缓存的文件数量

				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.writeDebugLogs() // Remove for release app
				.build();//开始构建

		ImageLoader.getInstance().init(config);//全局初始化此配置
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	/**
	 * 重新初始化应用界面，清空当前Activity棧，并启动欢迎页面
	 */
	public static void reInitApp() {
		Intent intent = new Intent(getAppContext(), LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		getAppContext().startActivity(intent);
	}
	public static Context getAppContext(){
		return mcontext;
	}
	public static void  exit(){
		for (Activity activity :activities){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
		activities.clear();
		//System.exit(0);
	}
}
