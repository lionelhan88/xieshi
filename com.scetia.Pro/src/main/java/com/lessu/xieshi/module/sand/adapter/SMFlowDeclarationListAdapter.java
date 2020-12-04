package com.lessu.xieshi.module.sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;

/**
 * created by ljs
 * on 2020/10/28
 */
public class SMFlowDeclarationListAdapter extends BaseQuickAdapter<FlowDeclarationBean, BaseViewHolder> {

    public SMFlowDeclarationListAdapter() {
        super(R.layout.sm_flow_declaration_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, FlowDeclarationBean item) {
        helper.setText(R.id.sm_flow_declaration_item_ship_name,item.getS1());
        helper.setText(R.id.sm_flow_declaration_item_sand,item.getS2());
        helper.setText(R.id.sm_flow_declaration_sale_user,item.getS3());
        helper.setText(R.id.sm_flow_declaration_sale_number,item.getS4());
        helper.setText(R.id.sm_flow_declaration_declare_date,item.getS5());
        if(item.isS6()){
            helper.setText(R.id.sm_flow_declaration_declare_commission_state,"已委托");
            helper.setTextColor(R.id.sm_flow_declaration_declare_commission_state,
                    mContext.getResources().getColor(R.color.green_dark));
        }else{
            helper.setText(R.id.sm_flow_declaration_declare_commission_state,"未委托");
            helper.setTextColor(R.id.sm_flow_declaration_declare_commission_state,
                    mContext.getResources().getColor(R.color.red_dark));
        }
    }
}
