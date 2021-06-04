package com.lessu.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.lessu.uikit.R;
import com.scetia.Pro.common.Util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Created by lessu on 14-7-31.
 */
public abstract class NavigationActivity extends FragmentActivity {
    protected NavigationBar navigationBar;
    private ImmersionBar immersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //强制竖屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        //绑定控件
        ButterKnife.bind(this);
        //沉浸式标题栏
        initImmersionBar();
        observerData();
        initView();
        initData();
        if(isRegisterEventBus()){
            EventBus.getDefault().register(this);
        }
    }
    protected boolean isRegisterEventBus(){
        return false;
    }
    /**
     * 添加顶部标题栏菜单按钮
     */
    protected NavigationBar initNavigationBar() {
        //为页面添加返回按钮
        NavigationBar navigationBar = new NavigationBar(this);
        BarButtonItem handleButtonItem = new BarButtonItem(this, R.drawable.back);
        handleButtonItem.setOnClickListener(view -> leftNavBarClick());
        navigationBar.setLeftBarItem(handleButtonItem);
        navigationBar.setBackgroundColor(ContextCompat.getColor(this, R.color.top_bar_background));
        return navigationBar;
    }

    protected abstract int getLayoutId();

    /**
     * 监听数据变化，更新UI
     */
    protected void observerData() {

    }

    /**
     * 初始化控件
     */
    protected void initView() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

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
        navigationBar = initNavigationBar();
        if (navigationBar != null) {
            layoutView.addView(navigationBar);
        }
        layoutView.addView(view);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (params != null) {
            super.setContentView(layoutView, params);
        } else {
            super.setContentView(layoutView);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (title.length() > 9) {
            navigationBar.setTitle(title.subSequence(0, 9) + "...");
        } else {
            navigationBar.setTitle(title);
        }
    }

    /**
     * 左上角返回按钮
     */
    protected void leftNavBarClick() {
        finish();
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void initImmersionBar() {
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(this)
                    .titleBar(getTitleBar())
                    .navigationBarColor(R.color.light_gray)
                    .navigationBarDarkIcon(true)
                    ;
            immersionBar.init();
        }
    }

    protected View getTitleBar() {
        return navigationBar;
    }

    protected void startOtherActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startOtherActivity(Class<? extends Activity> cls,boolean finish) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if (finish) finish();
    }

    @Override
    protected void onDestroy() {
        if (immersionBar!=null){
            immersionBar.getBarParams().titleBarView = null;
        }
        if(isRegisterEventBus()){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
    /**
     * 设置SearchView下划线透明
     **/
    protected void setUnderLinearTransparent(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            if (mView != null) {
                mView.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 不允许APP的字体随系统改变
     *
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
            int[] l = {0, 0};
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
