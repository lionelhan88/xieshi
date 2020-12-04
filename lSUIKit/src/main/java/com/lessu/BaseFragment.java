package com.lessu;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationBar;
import com.lessu.uikit.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    public NavigationBar navigationBar;

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =inflater.inflate(getLayoutId(),container,false);
        setNavigationBar(new NavigationBar(getActivity()));
        //为页面添加返回按钮
        BarButtonItem handleButtonItem = new BarButtonItem(getActivity(), R.drawable.back);
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftNavBarClick();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        LinearLayout layoutView = new LinearLayout(requireActivity());
        layoutView.setOrientation(LinearLayout.VERTICAL);
        navigationBar.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.top_bar_background));
        layoutView.addView(navigationBar);
        layoutView.addView(view);
        unbinder=ButterKnife.bind(this,view);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initImmersionBar();
        initData();
    }
    public void setNavigationBar(NavigationBar navigationBar) {
        this.navigationBar = navigationBar;
    }
    protected void setTitle(String title){
        navigationBar.setTitle(title);
    }

    public void leftNavBarClick(){
        requireActivity().onBackPressed();
    }

    protected  void initImmersionBar(){
        ImmersionBar.with(this).titleBarMarginTop(navigationBar)
                .statusBarColorInt(getResources().getColor(R.color.top_bar_background))
                .navigationBarColor(R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
