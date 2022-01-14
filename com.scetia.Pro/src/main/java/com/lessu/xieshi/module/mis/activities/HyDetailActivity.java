package com.lessu.xieshi.module.mis.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.fragment.MemberSearchInfoFragment;
import com.lessu.xieshi.module.mis.fragment.QualificationLevelFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HyDetailActivity extends NavigationActivity {
    @BindView(R.id.member_search_info_vp)
    ViewPager memberSearchInfoVP;
    @BindView(R.id.member_search_info_tab_layout)
    TabLayout tabLayout;
    private List<Fragment> fragments;
    private final String[] titles=new String[]{"基本信息","资质等级"};
    @Override
    protected int getLayoutId() {
        return R.layout.activity_hydetail;
    }

    @Override
    protected void initView() {
        setTitle("会员信息");
        fragments = new ArrayList<>();
        fragments.add(new MemberSearchInfoFragment());
        fragments.add(new QualificationLevelFragment());
        memberSearchInfoVP.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        tabLayout.setupWithViewPager(memberSearchInfoVP);
    }
}
