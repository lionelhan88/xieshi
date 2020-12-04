package com.lessu.xieshi.module.sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.SandProviderBean;

/**
 * created by ljs
 * on 2020/11/6
 */
public class SandProviderListAdapter extends BaseQuickAdapter<SandProviderBean, BaseViewHolder> {
    public SandProviderListAdapter() {
        super(R.layout.sand_provider_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SandProviderBean item) {
        helper.setText(R.id.sand_provider_list_item_code,item.getRecordCardCode());
        helper.setText(R.id.sand_provider_list_item_name,item.getRecordCardName());
    }
}
