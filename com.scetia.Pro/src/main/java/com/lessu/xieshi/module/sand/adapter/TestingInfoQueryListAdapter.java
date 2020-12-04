package com.lessu.xieshi.module.sand.adapter;

import android.view.View;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

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
    public TestingInfoQueryListAdapter() {
        super(R.layout.testing_info_query_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestingQueryResultBean item) {
       /* helper.setText(R.id.testing_info_query_item_ship_name,item.getS2());
        helper.setText(R.id.testing_info_query_item_commission,item.getS3());
        helper.setText(R.id.testing_info_query_item_sample_name,item.getS4());
        helper.setText(R.id.testing_info_query_item_sample_state,item.getS5());*/

        helper.setText(R.id.result_query_item_sample_name, item.getS1());
        helper.setText(R.id.result_query_item_sample_state, item.getS2());
        helper.setText(R.id.result_query_item_commission_date, item.getS3());
        helper.setText(R.id.result_query_item_commission_company, item.getS4());
        helper.setText(R.id.result_query_item_ship_name, item.getS5());
        LabelView view = helper.getView(R.id.result_query_item_label_view);
        if(item.getS6()==0){
            view.setLabelContextText("合格");
            view.setLabelBackground(R.color.green_dark);
        }else if(item.getS6()==1){
            view.setLabelContextText("不合格");
            view.setLabelBackground(R.color.red_dark);
        }else{
            view.setVisibility(View.INVISIBLE);
        }
    }
}
