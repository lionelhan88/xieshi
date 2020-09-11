package com.lessu.navigation;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.kongzueupdatesdk.BuildConfig;
import com.kongzue.kongzueupdatesdk.UpdateInfo;
import com.kongzue.kongzueupdatesdk.UpdateUtil;
import com.lessu.ShareableApplication;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.MyUpdateUtil;
import com.lessu.uikit.R;
import com.lessu.uikit.views.LSAlert;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lessu on 14-7-31.
 */
public class NavigationActivity extends FragmentActivity {
    public NavigationBar navigationBar;
    private boolean isFirstLoad = true;
    private BarButtonItem handleButtonItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ShareableApplication.activities.add(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setNavigationBar(new NavigationBar(this));
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks.get(0).numActivities > 1){
            BarButtonItem backButtonItem = BarButtonItem.backBarButtonItem(this);
            backButtonItem.titleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leftNavBarClick();
                }
            });
        }
        //为页面添加返回按钮
        handleButtonItem = new BarButtonItem(this,R.drawable.back);
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftNavBarClick();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        isFirstLoad = true;
        initImmersionBar();
    }
    @Override
    protected void onStart() {
    	super.onStart();
    	isFirstLoad = false;
    }

    protected void leftNavBarClick(){
        finish();
    }
    @Override
    public void setContentView(int layoutResID) {
        this.setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        this.setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        LinearLayout layoutView = new LinearLayout(this);
        layoutView.setOrientation(LinearLayout.VERTICAL);
        layoutView.addView(getNavigationBar());
        layoutView.addView(view);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (params!=null) {
            super.setContentView(layoutView, params);
        }else{
            super.setContentView(layoutView);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
    	super.setTitle(title);
    	if(title.length() > 9){
    		getNavigationBar().setTitle(title.subSequence(0, 9) + "...");
    	}else{
    		getNavigationBar().setTitle(title);
    	}
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void initImmersionBar(){
        ImmersionBar.with(this).titleBar(navigationBar)
                .navigationBarColor(R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
    }
    public void setTitleView(View view){
    	getNavigationBar().setTitleView(view);
    }
    public void setLeftNavigationItem(String title,View.OnClickListener listener){
    	BarButtonItem barButtonItem = new BarButtonItem(this, title);
    	barButtonItem.setOnClickListener(listener);
    	getNavigationBar().setLeftBarItem(barButtonItem);
    }
    public void setRightNavigationItem(String title,View.OnClickListener listener){
    	BarButtonItem barButtonItem = new BarButtonItem(this, title);
    	barButtonItem.setOnClickListener(listener);
    	getNavigationBar().setRightBarItem(barButtonItem);
    }

	public NavigationBar getNavigationBar() {
		return navigationBar;
	}

	public void setNavigationBar(NavigationBar navigationBar) {
		this.navigationBar = navigationBar;
	}

	public boolean isFirstLoad() {
		return isFirstLoad;
	}

    /**
     * 更新app回调接口
     */
	public interface UpdateAppCallback{
        //不去下载
        void updateCancel();
    }
    /**
     * 检查更新
     */
    protected void getUpdate(final boolean isShowDialog, final UpdateAppCallback updateAppCallback) {
        HashMap<String, Object> updateparams = new HashMap<String, Object>();
        updateparams.put("PlatformType", "1");//1为安卓
        updateparams.put("SystemType", "2");//2为内部版
        EasyAPI.apiConnectionAsync(this, isShowDialog, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), updateparams, new EasyAPI.ApiFastSuccessCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                String versionName = null;
                try {
                    versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
                    System.out.println("versionName.." + versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                JsonObject json = result.getAsJsonObject().get("Data").getAsJsonArray().get(0).getAsJsonObject();
                String serviceVersion = json.get("Version").getAsString();
                //是否强制更新标识
                final int isMustBeUpdate = json.get("Update_Flag").getAsInt();
                String[] localVersionArray = versionName.split("\\.");
                String[] serviceVersionArray = serviceVersion.split("\\.");
                int localCount = localVersionArray.length;
                int serviceCount = serviceVersionArray.length;
                int count = localCount;
                if (localCount > serviceCount) {
                    count = serviceCount;
                }
                boolean updateFlag = false;
                try {
                    for (int i = 0; i < count; i++) {
                        if (Integer.parseInt(localVersionArray[i]) < Integer.parseInt(serviceVersionArray[i])) {
                            updateFlag = true;
                        }
                    }
                } catch (Exception e) {
                    updateFlag = false;
                }
                if (updateFlag) {
                    final String urlString = json.get("Update_Url").getAsString();
                    String description="";
                    if(isMustBeUpdate==1){
                        //强制更新
                        description = "更新内容:\r\n" + json.get("Description").getAsString()
                                +"\r\n此更新为强制更新，必须更新后尚可继续使用，如暂时不更新点取消退出程序！\r\n"+ "是否立即前往更新？";
                    }else{
                        description = "更新内容:\r\n" + json.get("Description").getAsString() + "是否立即前往更新？";
                    }
                    isShowUpdate(serviceVersion, description, urlString, isMustBeUpdate == 1, new MyUpdateUtil.OnDownloadListener() {
                        @Override
                        public void onStart(long downloadId) {

                        }

                        @Override
                        public void onDownloading(long downloadId, int progress) {

                        }

                        @Override
                        public void onSuccess(long downloadId) {

                        }

                        @Override
                        public void onCancel(long downloadId) {
                            if (isMustBeUpdate == 1) {
                                updateAppCallback.updateCancel();
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isMustBeUpdate == 1) {
                                updateAppCallback.updateCancel();
                            }
                        }
                    });
                }else{
                    if (isShowDialog)
                    LSAlert.showAlert(NavigationActivity.this,"已经是最新版本了");
                }
            }

        });
    }
    /**
     * 是否显示更新提示框
     * @param version
     * @param description
     * @param apkUrl
     * @param isMustBeUpdate
     */
	public void isShowUpdate(String version, String description, String apkUrl, boolean isMustBeUpdate, MyUpdateUtil.OnDownloadListener downloadListener,
                             DialogInterface.OnClickListener listener){
	    UpdateInfo info =new  UpdateInfo();
	    info.setInfo(description)
                .setVer(version)
                .setDownloadUrl(apkUrl);
        MyUpdateUtil updateUtil = new MyUpdateUtil(this);
        updateUtil.showNormalUpdateDialog(info,"检查到更新"+info.getVer(),null,"下载","取消",isMustBeUpdate,listener);
        MyUpdateUtil.updateTitle = "发现更新";
        MyUpdateUtil.progressDialogTitle ="建设检测";
        MyUpdateUtil.progressDescription = "正在更新...";
        updateUtil.setOnDownloadListener(downloadListener);
    }

    /**
     * 不允许APP的字体随系统改变
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration newConfig = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        if (resources != null && newConfig.fontScale != 1) {
            newConfig.fontScale = 1;
            if (Build.VERSION.SDK_INT >= 17) {
                Context configurationContext = createConfigurationContext(newConfig);
                resources = configurationContext.getResources();
                displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
            } else {
                resources.updateConfiguration(newConfig, displayMetrics);
            }
        }
        return resources;
    }
}
