package com.lessu.xieshi.module.todaystatistics;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.bean.TodayStatisticsBean;

/**
 * created by ljs
 * on 2020/12/7
 */
public class ToadyStatisticsListAdapter extends BaseQuickAdapter<TodayStatisticsBean.JsonContentBean.ItemListBean, BaseViewHolder> {
    public ToadyStatisticsListAdapter() {
        super(R.layout.today_statistics_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodayStatisticsBean.JsonContentBean.ItemListBean item) {
        helper.setText(R.id.tv_item1,item.getItemName());
        helper.setText(R.id.tv_item2,item.getItemSampleCount()+"");
        helper.setText(R.id.tv_item3,item.getItemSampleFinishCount()+"");
        helper.setText(R.id.tv_item4,item.getItemBhgCount()+"");
        helper.setText(R.id.tv_item5,item.getItemNoExam()+"");
    }
}
