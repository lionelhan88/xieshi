package com.lessu.xieshi.module.mis.adapter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.bean.EvaluationComparisonBean;

import java.util.List;

/**
 * created by Lollipop
 * on 2021/4/15
 */
public class EvaluationComparisonListAdapter extends BaseQuickAdapter<EvaluationComparisonBean.EvaluationComparisonItem, BaseViewHolder> {

    public EvaluationComparisonListAdapter() {
        super(R.layout.evaluation_comparison_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, EvaluationComparisonBean.EvaluationComparisonItem item) {
        helper.setText(R.id.evaluation_comparison_item_member_no,item.getS1());//会员号
        helper.setText(R.id.evaluation_comparison_item_state,item.getS2Text());//状态
        helper.setText(R.id.evaluation_comparison_item_member_name,item.getS11());//单位名称
        helper.setText(R.id.evaluation_comparison_item_apply_type,item.getS14Text());//申请类型
        helper.setText(R.id.evaluation_comparison_item_type, TextUtils.isEmpty(item.getS7Text())?"-":item.getS7Text());//类型
        helper.setText(R.id.evaluation_comparison_item_expired_date,item.getS4());//证书过期日期
        helper.setText(R.id.evaluation_comparison_item_remarks,item.getS8());//备注
    }
}
