package com.lessu.xieshi.module.sand.fragment;

import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.TestingCompanyQueryResultListAdapter;
import com.lessu.xieshi.module.sand.bean.TestingManageBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/9
 */
public class SandInfoMaintainTestingManageFragment extends BaseFragment {

    @BindView(R.id.sand_common_rv)
    RecyclerView sandCommonRv;
    @BindView(R.id.sand_common_refresh)
    SmartRefreshLayout sandCommonRefresh;
    @BindView(R.id.sand_common_fab)
    FloatingActionButton sandCommonFab;
    private TestingCompanyQueryResultListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_common_layout;
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.sand_manage_info_testing_manage_text));
        listAdapter = new TestingCompanyQueryResultListAdapter(false);
        sandCommonRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sandCommonRv.setAdapter(listAdapter);
    }

    @Override
    protected void initData() {
        for (int i = 0; i <= 10; i++) {
            TestingManageBean bean = new TestingManageBean();
            bean.setTestingCompanyName("上海技术检测公司上海技术检测公司上海技术检测公司上海技术检测公司上海技术检测公司" + i);
            bean.setTestingCompanyArea("静安区");
            bean.setTestingCompanyUser("张三");
            bean.setTestingCompanyPhone("15512345678");
            bean.setTestingCompanyAddress("上海市中山南二路777弄");
            listAdapter.addData(bean);
        }
    }

    @OnClick({R.id.sand_common_fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sand_common_fab:
                Navigation.findNavController(view).navigate(R.id.actionTestingMangeToTestingQueryResult);
                break;
        }
    }
}
