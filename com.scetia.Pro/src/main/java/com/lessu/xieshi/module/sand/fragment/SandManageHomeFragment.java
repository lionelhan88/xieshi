package com.lessu.xieshi.module.sand.fragment;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import com.lessu.BaseFragment;
import com.lessu.xieshi.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/10/26
 */
public class SandManageHomeFragment extends BaseFragment {

    @BindView(R.id.sand_manage_flow_declaration)
    ConstraintLayout sandManageFlowDeclaration;
    @BindView(R.id.sand_manage_testing_commission)
    ConstraintLayout sandManageTestingCommission;
    @BindView(R.id.sand_manage_info_query)
    ConstraintLayout sandManageInfoQuery;
    @BindView(R.id.sand_manage_provider_search)
    ConstraintLayout sandManageProviderSearch;
    @BindView(R.id.sand_manage_sales_manage)
    ConstraintLayout sandManageSalesManage;
    @BindView(R.id.sand_manage_testing_manage)
    ConstraintLayout sandManageTestingManage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_manage;
    }

    @Override
    protected void initView() {
        setTitle("用砂管理");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.sand_manage_provider_search, R.id.sand_manage_sales_manage, R.id.sand_manage_testing_manage, R.id.sand_manage_flow_declaration, R.id.sand_manage_testing_commission, R.id.sand_manage_info_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sand_manage_provider_search:
                Navigation.findNavController(view).navigate(R.id.actionManageToProviderList);
                break;
            case R.id.sand_manage_sales_manage:
                Navigation.findNavController(view).navigate(R.id.actionManageToSalesManage);
                break;
            case R.id.sand_manage_testing_manage:
                Navigation.findNavController(view).navigate(R.id.actionManageToTestingManage);
                break;
            case R.id.sand_manage_flow_declaration:
                Navigation.findNavController(view).navigate(R.id.actionManageToSMFlowDeclaration);
                break;
            case R.id.sand_manage_testing_commission:
                Navigation.findNavController(view).navigate(R.id.actionManageToTestingCommission);
                break;
            case R.id.sand_manage_info_query:
                Navigation.findNavController(view).navigate(R.id.actionManageToTestingQuery);
                break;
        }
    }
}
