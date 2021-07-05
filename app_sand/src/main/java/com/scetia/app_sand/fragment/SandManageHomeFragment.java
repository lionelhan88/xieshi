package com.scetia.app_sand.fragment;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import com.scetia.Pro.baseapp.fragment.BaseFragment;
import com.scetia.Pro.common.CommonQRCode;
import com.scetia.app_sand.R;
import com.scetia.app_sand.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/10/26
 */
public class SandManageHomeFragment extends BaseFragment {

    @BindView(R2.id.sand_manage_flow_declaration)
    ConstraintLayout sandManageFlowDeclaration;
    @BindView(R2.id.sand_manage_testing_commission)
    ConstraintLayout sandManageTestingCommission;
    @BindView(R2.id.sand_manage_info_query)
    ConstraintLayout sandManageInfoQuery;
    @BindView(R2.id.sand_manage_provider_search)
    ConstraintLayout sandManageProviderSearch;
    @BindView(R2.id.sand_manage_sales_manage)
    ConstraintLayout sandManageSalesManage;
    @BindView(R2.id.sand_manage_testing_manage)
    ConstraintLayout sandManageTestingManage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_manage;
    }

    @Override
    protected void initView() {
        setTitle("用砂管理");
        contentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void leftNavBarClick(View view) {
       requireActivity().finish();
    }

    @OnClick({R2.id.sand_manage_provider_search, R2.id.sand_manage_sales_manage, R2.id.sand_manage_testing_manage,
            R2.id.sand_manage_flow_declaration, R2.id.sand_manage_testing_commission, R2.id.sand_manage_info_query
    ,R2.id.sand_manage_info_qrcode})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.sand_manage_provider_search) {
            Navigation.findNavController(view).navigate(R.id.actionManageToProviderList);
        } else if (id == R.id.sand_manage_sales_manage) {
            Navigation.findNavController(view).navigate(R.id.actionManageToSalesManage);
        } else if (id == R.id.sand_manage_testing_manage) {
            Navigation.findNavController(view).navigate(R.id.actionManageToTestingManage);
        } else if (id == R.id.sand_manage_flow_declaration) {
            Navigation.findNavController(view).navigate(R.id.actionManageToSMFlowDeclaration);
        } else if (id == R.id.sand_manage_testing_commission) {
            Navigation.findNavController(view).navigate(R.id.actionManageToTestingCommission);
        } else if (id == R.id.sand_manage_info_query) {
            Navigation.findNavController(view).navigate(R.id.actionManageToTestingQuery);
        } else if (id == R.id.sand_manage_info_qrcode) {
            CommonQRCode.with(requireActivity()).setIsBarCode(true).show();
        }
    }
}
