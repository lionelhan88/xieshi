package com.lessu.xieshi.module.unqualified.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.lessu.xieshi.R;
import com.scetia.Pro.baseapp.basepage.BasePageListAdapter;
import com.scetia.Pro.baseapp.basepage.PageListCommonViewHolder;
import com.lessu.xieshi.module.unqualified.bean.ConstructionData;
import com.lessu.xieshi.module.unqualified.viewmodel.ConstructionListViewModel;

/**
 * created by ljs
 * on 2020/12/15
 */
public class ConstructionListAdapter extends BasePageListAdapter<ConstructionData.ConstructionBean> {
    private ConstructionListViewModel viewModel;
    private static DiffUtil.ItemCallback<ConstructionData.ConstructionBean> diff
            = new DiffUtil.ItemCallback<ConstructionData.ConstructionBean>() {
        @Override
        public boolean areItemsTheSame(@NonNull ConstructionData.ConstructionBean oldItem, @NonNull ConstructionData.ConstructionBean newItem) {
            return oldItem.getReportId().equals(newItem.getReportId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ConstructionData.ConstructionBean oldItem, @NonNull ConstructionData.ConstructionBean newItem) {
            return oldItem==newItem;
        }
    };

    public ConstructionListAdapter(ConstructionListViewModel viewModel) {
        super(diff, R.layout.un_construction_list_item);
        this.viewModel = viewModel;
    }

    @Override
    public void footerItemClick() {
        viewModel.retry();
    }

    @Override
    public void bindWithCommonItemView(PageListCommonViewHolder vh, ConstructionData.ConstructionBean constructionBean) {
        TextView projectNameTextView = vh.getView(R.id.ProjectNameTextView);
        TextView memberIdTextView = vh.getView(R.id.MemberIdTextView);
        TextView reportIDTextView = vh.getView(R.id.ReportIDTextView);
        TextView linkmanTextView = vh.getView(R.id.LinkmanTextView);
        TextView telTextView = vh.getView(R.id.TelTextView);
        TextView buildUnitTextView = vh.getView(R.id.BuildUnitTextView);
        TextView constructionUnitTextView = vh.getView(R.id.ConstructionUnitTextView);
        TextView supervisorUnitTextView = vh.getView(R.id.SupervisorUnitTextView);
        TextView superviserKeyTextView = vh.getView(R.id.SuperviserKeyTextView);
        TextView modifyTimeTextView = vh.getView(R.id.ModifyTimeTextView);
        TextView isSuperviserTextView = vh.getView(R.id.IsSuperviserTextView);

        projectNameTextView.setText(constructionBean.getProjectName());//工程名称
        memberIdTextView.setText(constructionBean.getMemberId());//会员编号
        reportIDTextView.setText(constructionBean.getReportId());//报告编号
        linkmanTextView.setText(constructionBean.getLinkman());//联系人
        telTextView.setText(constructionBean.getTel());//联系电话
        buildUnitTextView.setText(constructionBean.getBuildUnit());//建设单位
        constructionUnitTextView.setText(constructionBean.getConstructionUnit());//施工单位
        supervisorUnitTextView.setText(constructionBean.getSupervisorUnit());//监督单位
        superviserKeyTextView.setText(constructionBean.getSuperviserKey());//质监站
        modifyTimeTextView.setText(constructionBean.getModifyTime());//编辑日期
        isSuperviserTextView.setText(constructionBean.getIsSuperviser());//是否监督抽检

    }

}
