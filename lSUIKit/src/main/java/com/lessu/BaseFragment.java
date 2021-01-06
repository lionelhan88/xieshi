package com.lessu;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationBar;
import com.lessu.uikit.R;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    public NavigationBar navigationBar;
    private Unbinder unbinder;
    private LinearLayout rootView;
    protected View contentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            navigationBar = getTopBarView();
            rootView = getRootView(navigationBar);
            contentView = createContentView(inflater, container);
            initImmersionBar();
            initView();
            initData();
        } else {
            initImmersionBar();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isRegisterEventBus() && !EventBusUtil.isRegistered(this)) {
            EventBusUtil.register(this);
        }
    }

    protected NavigationBar getTopBarView() {
        NavigationBar navigationBar = new NavigationBar(requireActivity());
        navigationBar.setBackgroundColor(topBackgroundColor(R.color.top_bar_background));
        //为页面添加返回按钮
        BarButtonItem handleButtonItem = new BarButtonItem(requireActivity(), R.drawable.back);
        handleButtonItem.setOnClickListener(this::leftNavBarClick);
        navigationBar.setLeftBarItem(handleButtonItem);
        return navigationBar;
    }

    private LinearLayout getRootView(NavigationBar navigationBar) {
        LinearLayout linearLayout = new LinearLayout(requireActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(navigationBar);
        return linearLayout;
    }

    private View createContentView(LayoutInflater inflater, ViewGroup container) {
        FrameLayout frameLayout = new FrameLayout(requireActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(layoutParams);
        View contentView = inflater.inflate(getLayoutId(), container, false);
        frameLayout.addView(contentView);
        rootView.addView(frameLayout);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }


    //布局ID
    protected abstract int getLayoutId();

    //初始化控件
    protected abstract void initView();

    //初始化数据
    protected void initData() {

    }

    //设置头部标题栏背景
    protected int topBackgroundColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(requireActivity(), colorRes);
    }

    //设置同步标题
    protected void setTitle(String title) {
        navigationBar.setTitle(title);
    }


    public void leftNavBarClick(View view) {
        Navigation.findNavController(view).navigateUp();
    }

    //设置沉浸式状态栏
    protected void initImmersionBar() {
        ImmersionBar.with(this).titleBarMarginTop(navigationBar)
                .statusBarColorInt(getResources().getColor(R.color.top_bar_background))
                .navigationBarColor(R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
    }

    //初始化swipeRefresh刷新样式
    protected void setSwipeRefresh(SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.setColorSchemeResources(R.color.blue_light1, R.color.blue_normal1, R.color.blue_normal2);
    }

    //是否注册EventBus
    protected boolean isRegisterEventBus() {
        return false;
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

}
