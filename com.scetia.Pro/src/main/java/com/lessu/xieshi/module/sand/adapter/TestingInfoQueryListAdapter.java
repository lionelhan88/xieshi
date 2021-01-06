package com.lessu.xieshi.module.sand.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.TestingQueryResultBean;
import com.lessu.xieshi.view.LabelView;

/**
 * created by ljs
 * on 2020/11/2
 */
public class TestingInfoQueryListAdapter extends BaseQuickAdapter<TestingQueryResultBean, BaseViewHolder> {
    private static final String QUALIFIED_RESULT = "合格";
    private static final String UN_QUALIFIED_RESULT = "不合格";
    public TestingInfoQueryListAdapter() {
        super(R.layout.testing_info_query_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestingQueryResultBean item) {

        helper.setText(R.id.result_query_item_sample_name, item.getSampleName());
        helper.setText(R.id.result_query_item_sample_state, item.getSampleStatusName());
        helper.setText(R.id.result_query_item_commission_date, item.getConsignCreateDate());
        helper.setText(R.id.result_query_item_commission_company, item.getCustomerUnitMemberName());
        helper.setText(R.id.result_query_item_ship_name, item.getShipName());
        LabelView view = helper.getView(R.id.result_query_item_label_view);
        if(item.getSampleRegular().equals(QUALIFIED_RESULT)){
            view.setLabelContextText("合格");
            view.setLabelBackground(R.color.green_dark);
        }else if(item.getSampleRegular().equals(UN_QUALIFIED_RESULT)){
            view.setLabelContextText("不合格");
            view.setLabelBackground(R.color.red_dark);
        }else{
            view.setVisibility(View.INVISIBLE);
        }
    }
}
