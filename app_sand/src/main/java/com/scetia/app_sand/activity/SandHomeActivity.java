package com.scetia.app_sand.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lessu.navigation.NavigationActivity;
import com.lessu.navigation.NavigationBar;
import com.scetia.app_sand.R;

/**
 * created by ljs
 * on 2020/10/26
 */
@Route(path = "/app_sand/sandHomeActivity")
public class SandHomeActivity extends NavigationActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sand_home;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected NavigationBar initNavigationBar() {
        return null;
    }

    @Override
    protected void initImmersionBar() {

    }
}
