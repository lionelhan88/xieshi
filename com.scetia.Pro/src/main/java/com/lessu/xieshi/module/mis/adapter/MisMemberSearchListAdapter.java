package com.lessu.xieshi.module.mis.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BasePageListAdapter;
import com.lessu.xieshi.module.mis.bean.MisMemberSearchResultData;
import com.lessu.xieshi.module.mis.viewmodel.MisSearchViewModel;

/**
 * created by ljs
 * on 2020/11/26
 */
public class MisMemberSearchListAdapter extends BasePageListAdapter<MisMemberSearchResultData.ListContentBean> {
    private MisSearchViewModel viewModel;
    private static DiffUtil.ItemCallback<MisMemberSearchResultData.ListContentBean> mDiffCallBack =
            new DiffUtil.ItemCallback<MisMemberSearchResultData.ListContentBean>() {
                @Override
                public boolean areItemsTheSame(@NonNull MisMemberSearchResultData.ListContentBean oldItem,
                                               @NonNull MisMemberSearchResultData.ListContentBean newItem) {
                    return oldItem.getMemberId().equals(newItem.getMemberId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull MisMemberSearchResultData.ListContentBean oldItem,
                                                  @NonNull MisMemberSearchResultData.ListContentBean newItem) {
                    return oldItem == newItem;
                }
            };

    public MisMemberSearchListAdapter(MisSearchViewModel viewModel) {
        super(mDiffCallBack,R.layout.mis_hy_item);
        this.viewModel = viewModel;
    }

    @Override
    public void footerItemClick() {
        //点击底部加载按钮刷新
        viewModel.reFresh();
    }


    @Override
    public void bindWithCommonItemView(PageListCommonViewHolder vh, MisMemberSearchResultData.ListContentBean contentBean) {
        TextView HYMemberId = vh.getView(R.id.tv_mis_hynum);
        TextView MemberName = vh.getView(R.id.tv_mis_hyname);
        HYMemberId.setText(contentBean.getMemberId());
        MemberName.setText(contentBean.getMemberName());
    }
}
