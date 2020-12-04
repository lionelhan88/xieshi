package com.lessu.xieshi.module.sand.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;

/**
 * created by ljs
 * on 2020/11/10
 */
public class TestingCommissionListAdapter extends BaseQuickAdapter<TestingCommissionBean, BaseViewHolder> {
    public TestingCommissionListAdapter() {
        super(R.layout.testing_commission_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestingCommissionBean item) {
        helper.setText(R.id.testing_commission_item_sample_name,item.getS1());
        helper.setText(R.id.testing_commission_item_sampling_user,item.getS2());
        helper.setText(R.id.testing_commission_item_sampling_date,item.getS3());
        helper.setText(R.id.testing_commission_item_company_name,item.getS4());
        helper.setText(R.id.testing_commission_item_commission_user,item.getS5());
        helper.setText(R.id.testing_commission_item_date,item.getS6());
    }
}
