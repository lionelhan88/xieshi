package com.lessu.xieshi.module.unqualified.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.lessu.xieshi.R;
import com.scetia.Pro.baseapp.page.BasePageListAdapter;
import com.scetia.Pro.baseapp.basepage.PageListCommonViewHolder;
import com.lessu.xieshi.module.unqualified.bean.TestingReportData;
import com.lessu.xieshi.module.unqualified.viewmodel.TestingReportListViewModel;

/**
 * created by ljs
 * on 2020/12/15
 */
public class TestingReportListAdapter extends BasePageListAdapter<TestingReportData.TestingReportBean> {
    private TestingReportListViewModel viewModel;
    private static DiffUtil.ItemCallback<TestingReportData.TestingReportBean> diff = new DiffUtil.ItemCallback<TestingReportData.TestingReportBean>() {
        @Override
        public boolean areItemsTheSame(@NonNull TestingReportData.TestingReportBean oldItem, @NonNull TestingReportData.TestingReportBean newItem) {
            return oldItem.getReport_ID().equals(newItem.getReport_ID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TestingReportData.TestingReportBean oldItem, @NonNull TestingReportData.TestingReportBean newItem) {
            return oldItem==newItem;
        }
    };

    public TestingReportListAdapter(TestingReportListViewModel viewModel) {
        super(diff, R.layout.test_report_item);
        this.viewModel = viewModel;
    }

    @Override
    public void footerItemClick() {
        viewModel.retry();
    }

    @Override
    public void bindWithCommonItemView(PageListCommonViewHolder vh, TestingReportData.TestingReportBean testingReportBean) {
        TextView reportId = vh.getView(R.id.Report_IDTextView);//报告编号
        TextView projectName = vh.getView(R.id.ProJectNameTextView);//工程名称
        TextView identifyingCode = vh.getView(R.id.IdentifyingCodeTextView);//校验码
        TextView uqExecState = vh.getView(R.id.UqExecStatusTextView);//处理状态
        reportId.setText(testingReportBean.getReport_ID());
        projectName.setText(testingReportBean.getProJectName());
        identifyingCode.setText(testingReportBean.getIdentifyingCode());
        int uqExecStatus = testingReportBean.getUqExecStatus();
        switch (uqExecStatus){
            case 0:
                uqExecState.setText("未处理");
                break;
            case 1:
                uqExecState.setText("已处理");
                break;
            case 2:
                uqExecState.setText("处理中");
                break;
        }
    }
}
