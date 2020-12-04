package com.lessu.xieshi.module.sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;

/**
 * created by ljs
 * on 2020/11/6
 */
public class SandSalesManageListAdapter extends BaseQuickAdapter<SandSalesTargetBean, BaseViewHolder> {
    public SandSalesManageListAdapter() {
        super(R.layout.sand_sales_manage_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SandSalesTargetBean item) {
        helper.setText(R.id.sales_manage_list_item_nature,item.getGetSalesTargetNature());
        helper.setText(R.id.sales_manage_list_item_name,item.getSalesTargetName());
    }
}
