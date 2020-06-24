package com.lessu.xieshi.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fhm on 2019/12/25.
 */

public class TestActivity extends NavigationActivity implements View.OnClickListener{
    private DrawerLayout topDl;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private BiaoShiSaoMiaoFragment biaoshiFragment;
    private ShenQingShangBaoFragment shangBaoFragment;
    private MyPagerAdapter adapter;
    private LinearLayout llShangBao;
    private LinearLayout llBiaoShi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2_layout);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("标识扫描");
        initView();

    }
    private void initView(){
        BarButtonItem menuButtonitem = new BarButtonItem(this, R.drawable.icon_navigation_menu);
        menuButtonitem.setOnClickMethod(this, "menuButtonDidClick");
        navigationBar.setLeftBarItem(menuButtonitem);
        topDl = (DrawerLayout) findViewById(R.id.topDl);
        viewPager = (ViewPager) findViewById(R.id.print_data_view_pager);
        llShangBao = (LinearLayout) findViewById(R.id.ll_push_message);
        llBiaoShi = (LinearLayout) findViewById(R.id.ll_biaoshi);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        fragments = new ArrayList<>();
        biaoshiFragment = BiaoShiSaoMiaoFragment.newInstance();
        shangBaoFragment=ShenQingShangBaoFragment.newInstance();
        fragments.add(biaoshiFragment);
        fragments.add(shangBaoFragment);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);


        llShangBao.setOnClickListener(this);
        llBiaoShi.setOnClickListener(this);
    }
    public void menuButtonDidClick() {
        if(topDl.isDrawerOpen(GravityCompat.START)){
            topDl.closeDrawers();
        }else{
            topDl.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_biaoshi:
                viewPager.setCurrentItem(0,false);
                this.setTitle("标识扫描");
                topDl.closeDrawers();
                break;
            case R.id.ll_push_message:
                viewPager.setCurrentItem(1,false);
                this.setTitle("申请上报");
                topDl.closeDrawers();
                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
