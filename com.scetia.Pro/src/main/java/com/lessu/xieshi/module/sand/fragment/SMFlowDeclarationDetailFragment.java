package com.lessu.xieshi.module.sand.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lessu.BaseFragment;
import com.lessu.xieshi.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/10/28
 */
public class SMFlowDeclarationDetailFragment extends BaseFragment {

    @BindView(R.id.flow_declaration_detail_record_number)
    TextView flowDeclarationDetailRecordNumber;
    @BindView(R.id.flow_declaration_detail_record_name)
    TextView flowDeclarationDetailRecordName;
    @BindView(R.id.flow_declaration_detail_ship_name)
    TextView flowDeclarationDetailShipName;
    @BindView(R.id.flow_declaration_detail_sand_name)
    TextView flowDeclarationDetailSandName;
    @BindView(R.id.flow_declaration_detail_sand_spec)
    TextView flowDeclarationDetailSandSpec;
    @BindView(R.id.flow_declaration_detail_wharf_name)
    EditText flowDeclarationDetailWharfName;
    @BindView(R.id.flow_declaration_detail_sale_user)
    TextView flowDeclarationDetailSaleUser;
    @BindView(R.id.flow_declaration_detail_sale_number)
    EditText flowDeclarationDetailSaleNumber;
    @BindView(R.id.bt_flow_declaration_detail_save)
    Button btFlowDeclarationDetailSave;
    @BindView(R.id.bt_flow_declaration_detail_declaration)
    Button btFlowDeclarationDetailDeclaration;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sm_flow_declaration_detail;
    }

    @Override
    protected void initView() {
        setTitle("流向申报");
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.bt_flow_declaration_detail_save, R.id.bt_flow_declaration_detail_declaration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_flow_declaration_detail_save:
                break;
            case R.id.bt_flow_declaration_detail_declaration:
                break;
        }
    }
}
