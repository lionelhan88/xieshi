package com.lessu.xieshi.module.sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;

/**
 * created by ljs
 * on 2020/11/13
 */
public class ResultQueryDetailParamterAdapter extends BaseQuickAdapter<FlowDeclarationBean, BaseViewHolder> {
    public ResultQueryDetailParamterAdapter() {
        super(R.layout.sand_result_query_detail_paramter_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, FlowDeclarationBean item) {
        helper.setText(R.id.result_query_detail_paramter_item_name,item.getS1());
        helper.setText(R.id.result_query_detail_paramter_item_testing,item.getS2());
        helper.setText(R.id.result_query_detail_paramter_item_result,item.getS3());
    }
}
