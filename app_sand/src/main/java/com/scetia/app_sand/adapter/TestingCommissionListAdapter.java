package com.scetia.app_sand.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scetia.app_sand.R;
import com.scetia.app_sand.bean.TestingCommissionBean;

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
        helper.setText(R.id.testing_commission_item_sample_name,item.getSampleName());//	建设用砂名称
        helper.setText(R.id.testing_commission_item_sampling_user,item.getSampler());//	取样员
        helper.setText(R.id.testing_commission_item_sampling_date,item.getSamplingTime());//取样日期
        helper.setText(R.id.testing_commission_item_company_name,item.getConsignUnit());//委托单位 即备案企业名称
        helper.setText(R.id.testing_commission_item_commission_user,item.getPrincipal());//委托人
        helper.setText(R.id.testing_commission_item_date,item.getCommissionDate());//委托日期
        helper.addOnClickListener(R.id.right);
        helper.addOnClickListener(R.id.commission_list_item_content);
    }
}
