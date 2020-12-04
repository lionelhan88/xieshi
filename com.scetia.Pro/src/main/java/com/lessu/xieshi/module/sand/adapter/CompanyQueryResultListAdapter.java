package com.lessu.xieshi.module.sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;

/**
 * created by ljs
 * on 2020/11/6
 */
public class CompanyQueryResultListAdapter extends BaseQuickAdapter<SandSalesTargetBean, BaseViewHolder> {
    public CompanyQueryResultListAdapter() {
        super(R.layout.company_query_result_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SandSalesTargetBean item) {
        helper.setText(R.id.company_query_result_list_item_select_nature,item.getGetSalesTargetNature());
        helper.setText(R.id.company_query_result_list_item_select_name,item.getSalesTargetName());
        if(item.isSelect()){
            helper.setImageResource(R.id.company_query_result_list_item_select,R.drawable.icon_chosen);
        }else{
            helper.setImageResource(R.id.company_query_result_list_item_select,R.drawable.icon_unchosen);
        }
    }
}
