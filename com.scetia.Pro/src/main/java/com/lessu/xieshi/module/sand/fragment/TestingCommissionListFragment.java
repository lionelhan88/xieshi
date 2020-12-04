package com.lessu.xieshi.module.sand.fragment;

import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.TestingCommissionListAdapter;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * created by ljs
 * on 2020/11/10
 */
public class TestingCommissionListFragment extends BaseFragment {
    @BindView(R.id.sand_common_rv)
    RecyclerView sandCommonRv;
    @BindView(R.id.sand_common_refresh)
    SmartRefreshLayout sandCommonRefresh;
    @BindView(R.id.sand_common_fab)
    FloatingActionButton sandCommonFab;
    private TestingCommissionListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_common_layout;
    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.testing_commission_list_title_text));
        listAdapter = new TestingCommissionListAdapter();
        sandCommonRv.setAdapter(listAdapter);
        sandCommonRv.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        sandCommonFab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.actionCommissionListToDetail));
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            Navigation.findNavController(view).navigate(R.id.actionCommissionListToDetail);
        });
    }

    @Override
    protected void initData() {
        for (int i = 0; i <= 10; i++) {
            TestingCommissionBean bean = new TestingCommissionBean();
            bean.setS1("人工砂");
            bean.setS2("张三");
            bean.setS3("2020-11-01");
            bean.setS4("上海市检测技术单位");
            bean.setS5("张三");
            bean.setS6("2020-11-14");
            listAdapter.addData(bean);
        }
    }
}
