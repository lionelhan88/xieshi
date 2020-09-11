package com.lessu.xieshi.scan;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by ljs
 * on 2020/9/2
 */
public class PrintDataHomeActivity extends NavigationActivity {

    @BindView(R.id.print_data_home_frameLayout)
    FrameLayout printDataHomeFrameLayout;
    @BindView(R.id.sb_scan)
    SeekBar sbScan;
    @BindView(R.id.print_data_home_drawer)
    DrawerLayout printDataHomeDrawer;
    @BindView(R.id.ll_biaoshisaomiao)
    LinearLayout llBiaoshisaomiao;
    @BindView(R.id.ll_shenqingshangbao)
    LinearLayout llShenqingshangbao;
    @BindView(R.id.ll_shenhexiazai)
    LinearLayout llShenhexiazai;
    @BindView(R.id.ll_rukuchakan)
    LinearLayout llRukuchakan;
    @BindView(R.id.ll_shebeixinxi)
    LinearLayout llShebeixinxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_data_home);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("标识扫描");
        BarButtonItem menuButtonitem = new BarButtonItem(this, R.drawable.icon_navigation_menu);
        menuButtonitem.setOnClickMethod(this, "menuButtonDidClick");
        navigationBar.setLeftBarItem(menuButtonitem);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        printDataHomeDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
    
    public void menuButtonDidClick() {
        if (printDataHomeDrawer.isDrawerOpen(GravityCompat.START)) {
            printDataHomeDrawer.closeDrawer(GravityCompat.START);
        } else {
            printDataHomeDrawer.openDrawer(GravityCompat.START);
        }
    }
}
