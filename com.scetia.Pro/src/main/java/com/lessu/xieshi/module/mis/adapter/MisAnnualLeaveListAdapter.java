package com.lessu.xieshi.module.mis.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.bean.MisAnnualLeaveData;
import com.lessu.xieshi.module.mis.viewmodel.MisAnnualLeaveViewModel;
import com.scetia.Pro.baseapp.basepage.BasePageListAdapter;
import com.scetia.Pro.baseapp.basepage.PageListCommonViewHolder;

/**
 * created by ljs
 * on 2020/12/2
 */
public class MisAnnualLeaveListAdapter extends BasePageListAdapter<MisAnnualLeaveData.AnnualLeaveBean> {
    private static final String AGREE_STATE = "已批准";
    private static final String Uploading_STATE = "申请中";
    private MisAnnualLeaveViewModel viewModel;
    private static DiffUtil.ItemCallback<MisAnnualLeaveData.AnnualLeaveBean> diffCallback =
            new DiffUtil.ItemCallback<MisAnnualLeaveData.AnnualLeaveBean>() {
                @Override
                public boolean areItemsTheSame(@NonNull MisAnnualLeaveData.AnnualLeaveBean oldItem, @NonNull MisAnnualLeaveData.AnnualLeaveBean newItem) {
                    return oldItem.getMId() == newItem.getMId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull MisAnnualLeaveData.AnnualLeaveBean oldItem, @NonNull MisAnnualLeaveData.AnnualLeaveBean newItem) {
                    return oldItem == newItem;
                }
            };

    public MisAnnualLeaveListAdapter(MisAnnualLeaveViewModel viewModel) {
        super(diffCallback, R.layout.mis_nj_item);
        this.viewModel = viewModel;
    }

    @Override
    public void footerItemClick() {
        viewModel.reTry();
    }

    @Override
    public void bindWithCommonItemView(PageListCommonViewHolder vh, MisAnnualLeaveData.AnnualLeaveBean annualLeaveBean) {
        //绑定itemLayout的控件
        LinearLayout llBottom = vh.getView(R.id.ll_disanhang);
        TextView tvAnnualLeaveName = vh.getView(R.id.tv_nj_name);
        TextView tvLeaveTime = vh.getView(R.id.tv_nj_qingjia);
        TextView tvLeaveReason = vh.getView(R.id.tv_nj_reason);
        TextView tvLeaveUploadTime = vh.getView(R.id.tv_nj_shenqing);
        TextView tvLeaveAgreeTime = vh.getView(R.id.tv_nj_pztime);
        TextView tvLeaveAgreeLeader = vh.getView(R.id.tv_nj_bmpz);
        TextView tvLeaveState = vh.getView(R.id.tv_nj_status);

        tvAnnualLeaveName.setText(annualLeaveBean.getXm());
        tvLeaveTime.setText(annualLeaveBean.getLeaveTime());
        tvLeaveReason.setText(annualLeaveBean.getLeaveReason());
        tvLeaveUploadTime.setText(annualLeaveBean.getApplyTime());
        tvLeaveAgreeTime.setText(annualLeaveBean.getDepartTime());
        tvLeaveAgreeLeader.setText(annualLeaveBean.getDepartApprove());

        String state = annualLeaveBean.getStatus();
        if(state.equals(AGREE_STATE)){
            //已批准
            tvLeaveState.setText(AGREE_STATE);
            tvLeaveState.setTextColor(0xff73bf47);
            llBottom.setVisibility(View.VISIBLE);
        }else if (state.equals(Uploading_STATE)){
            tvLeaveState.setText("待审批");
            tvLeaveState.setTextColor(0xff268beb);
            llBottom.setVisibility(View.GONE);
        }

    }
}
