package com.lessu.xieshi.module.mis.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.bean.SealManageBean;


/**
 * created by Lollipop
 * on 2021/3/3
 */
public class SealMatterDetailAnnexListAdapter extends BaseQuickAdapter<SealManageBean.YzFjInfoBean, BaseViewHolder> {
    public SealMatterDetailAnnexListAdapter() {
        super(R.layout.seal_matter_annex_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SealManageBean.YzFjInfoBean item) {
        helper.setText(R.id.seal_matter_annex_list_item_title,item.getFileName());
    }
}
