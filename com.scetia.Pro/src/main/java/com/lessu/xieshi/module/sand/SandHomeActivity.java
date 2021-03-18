package com.lessu.xieshi.module.sand;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.lessu.navigation.NavigationActivity;
import com.lessu.navigation.NavigationBar;
import com.lessu.xieshi.R;
import com.scetia.Pro.baseapp.custome_nav.CustomNavHostFragment;

/**
 * created by ljs
 * on 2020/10/26
 */
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
