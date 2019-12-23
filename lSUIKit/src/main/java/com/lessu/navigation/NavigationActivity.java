package com.lessu.navigation;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.lessu.uikit.R;

import java.util.List;

/**
 * Created by lessu on 14-7-31.
 */
public class NavigationActivity extends FragmentActivity {
    public NavigationBar navigationBar;
    private boolean isFirstLoad = true;
    private BarButtonItem handleButtonItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setNavigationBar(new NavigationBar(this));
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
//        LSAlert.showAlert(this,"提示",String.format("%2d",tasks.get(0).numActivities));
        if (tasks.get(0).numActivities > 1){
            BarButtonItem backButtonItem = BarButtonItem.backBarButtonItem(this);
            //getNavigationBar().setLeftBarItem(backButtonItem);
            backButtonItem.titleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        //为页面添加返回按钮
        handleButtonItem = new BarButtonItem(this,R.drawable.back);
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        isFirstLoad = true;
    }
    @Override
    protected void onStart() {
    	super.onStart();
    	isFirstLoad = false;
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
    private void setHeight(View view){
        //获取actionbar的高度
        TypedArray actionBarSize  = obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        float height = actionBarSize.getDimension(0,0);
        //toolBar的top值
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        double statusHeight = getStatusBarHeight(this);
        lp.height = (int) (statusHeight+height);
        Log.e("hieght",height+statusHeight+"");
        view.setPadding(0, (int) statusHeight,0,0);
        navigationBar.setLayoutParams(lp);
    }
    private double getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
