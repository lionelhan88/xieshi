package com.lessu.navigation;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.ShareableApplication;
import com.lessu.uikit.R;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lessu on 14-7-31.
 */
public class NavigationActivity extends FragmentActivity {
    public NavigationBar navigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ShareableApplication.activities.add(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setNavigationBar(new NavigationBar(this));
        addTitleBarHandle();
        super.onCreate(savedInstanceState);
        initImmersionBar();
    }

    /**
     * 添加顶部标题栏菜单按钮
     */
    private void addTitleBarHandle(){
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
        BarButtonItem handleButtonItem = new BarButtonItem(this, R.drawable.back);
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftNavBarClick();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        navigationBar.setBackgroundColor(ContextCompat.getColor(this,R.color.top_bar_background));
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

    protected void startOtherActivity(Class<? extends Activity> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
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

        if (newConfig.fontScale != 1) {
            newConfig.fontScale = 1;
            Context configurationContext = createConfigurationContext(newConfig);
            resources = configurationContext.getResources();
            displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
        }
        return resources;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
