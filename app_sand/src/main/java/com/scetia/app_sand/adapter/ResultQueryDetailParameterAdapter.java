package com.scetia.app_sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scetia.app_sand.R;
import com.scetia.app_sand.bean.SandParameterResultBean;

/**
 * created by ljs
 * on 2020/11/13
 */
public class ResultQueryDetailParameterAdapter extends BaseQuickAdapter<SandParameterResultBean, BaseViewHolder> {
    public ResultQueryDetailParameterAdapter() {
        super(R.layout.sand_result_query_detail_paramter_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SandParameterResultBean item) {
        helper.setText(R.id.result_query_detail_paramter_item_name,item.getParameterName()==null?"暂无":item.getParameterName());
        helper.setText(R.id.result_query_detail_paramter_item_result,item.getParameterResult()==null?"暂无":item.getParameterResult());
    }
}
