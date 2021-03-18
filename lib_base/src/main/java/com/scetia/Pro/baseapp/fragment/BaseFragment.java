package com.scetia.Pro.baseapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.components.ImmersionFragment;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationBar;
import com.scetia.Pro.baseapp.R;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public abstract class BaseFragment extends ImmersionFragment {
    public NavigationBar navigationBar;
    private Unbinder unbinder;
    protected View contentView;
    protected ImmersionBar immersionBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(isEnableHandleBack()) {
            @Override
            public void handleOnBackPressed() {
                leftNavBarClick(contentView);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        navigationBar = createTopBarView();
        contentView = createContentView(inflater, container);
        LinearLayout rootView = createRootView();
        if (navigationBar != null) {
            rootView.addView(navigationBar);
        }
        rootView.addView(contentView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    /**
     * 创建顶部标题栏
     *
     * @return
     */
    protected NavigationBar createTopBarView() {
        NavigationBar navigationBar = new NavigationBar(requireContext());
        navigationBar.setBackgroundColor(topBackgroundColor(R.color.top_bar_background));
        //为页面添加返回按钮
        BarButtonItem handleButtonItem = new BarButtonItem(requireContext(), R.drawable.back);
        handleButtonItem.setOnClickListener(this::leftNavBarClick);
        navigationBar.setLeftBarItem(handleButtonItem);
        return navigationBar;
    }

    /**
     * 创建内容的容器View
     *
     * @return
     */
    private LinearLayout createRootView() {
        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    /**
     * 创建显示具体内容的View
     *
     * @param inflater
     * @param container
     * @return
     */
    private View createContentView(LayoutInflater inflater, ViewGroup container) {
        View contentView = inflater.inflate(getLayoutId(), container, false);
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

    /**
     * 设置标题栏背景颜色
     * @param colorRes 颜色的资源值
     * @return
     */
    protected int topBackgroundColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(requireActivity(), colorRes);
    }

    /**
     * 设置标题
     * @param title 标题内容
     */
    protected void setTitle(String title) {
        navigationBar.setTitle(title);
    }

    /**
     * 点击返回按键（包括自定义的返回和物理返回按键）
     * @param view
     */
    public void leftNavBarClick(View view) {
        Navigation.findNavController(view).navigateUp();
    }

    @Override
    public void initImmersionBar() {
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(this)
                    .titleBar(getImmersionBarNavigation())
                    .navigationBarColor(getNavigationBarColor())
                    .navigationBarDarkIcon(navigationBarDarkIcon());
            immersionBar.init();
        }
    }

    protected View getImmersionBarNavigation() {
        return navigationBar;
    }

    /**
     * 底部系统导航栏的背景色
     *
     * @return
     */
    protected int getNavigationBarColor() {
        return R.color.light_gray;
    }

    protected boolean navigationBarDarkIcon() {
        return true;
    }

    //初始化swipeRefresh刷新样式
    protected void setSwipeRefresh(SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.setColorSchemeResources(R.color.blue_light1, R.color.blue_normal1, R.color.blue_normal2);
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
        //这里需要注意，使用沉浸式状态栏框架 ImmersionBar，fragment销毁时要将immersionBar内部持有的 titleBarView赋值为null，
        //否则因为titleBarView 持有fragment的引用，导致fragment无法被正常回收导致内存泄漏
        if (immersionBar != null) {
            immersionBar.getBarParams().titleBarView = null;
        }
        unbinder.unbind();
        super.onDestroy();
    }

    /**
     * 是否监听物理返回按键
     * @return 默认不监听
     */
    protected boolean isEnableHandleBack(){
        return true;
    }

}
