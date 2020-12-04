package com.lessu.xieshi.module.sand.fragment;

import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.SMFlowDeclarationListAdapter;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/10/28
 */
public class SMFlowDeclarationListFragment extends BaseFragment {

    @BindView(R.id.sand_manage_flow_declaration_rv)
    RecyclerView sandManageFlowDeclarationRv;
    @BindView(R.id.sand_manage_flow_declaration_refresh)
    SmartRefreshLayout sandManageFlowDeclarationRefresh;
    @BindView(R.id.sand_manage_flow_declaration_fab)
    FloatingActionButton sandManageFlowDeclarationFab;
    private SMFlowDeclarationListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sm_flow_declaration_list;
    }

    @Override
    protected void initView() {
        setTitle("流向申报记录");
        initRecyclerView();
    }

    @Override
    protected void initData() {
        for (int i = 0; i <= 10; i++) {
            FlowDeclarationBean bean = new FlowDeclarationBean();
            bean.setS1("0063");
            bean.setS2("人工砂");
            bean.setS3("混凝土公司");
            if (i % 2 == 0) {
                bean.setS4("1000");
                bean.setS6(true);
            } else {
                bean.setS4("50");
                bean.setS6(false);
            }
            bean.setS5("2020-11-01");
            listAdapter.addData(bean);
        }
    }

    private void initRecyclerView() {
        listAdapter = new SMFlowDeclarationListAdapter();
        sandManageFlowDeclarationRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sandManageFlowDeclarationRv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Navigation.findNavController(view).navigate(R.id.actionSMFlowDeclarationListToDetail);
            }
        });
    }

    @OnClick(R.id.sand_manage_flow_declaration_fab)
    public void onViewClicked(View view) {
        Navigation.findNavController(view).navigate(R.id.actionSMFlowDeclarationListToDetail);
    }
}
