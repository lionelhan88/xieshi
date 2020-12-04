package com.lessu.xieshi.module.mis.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BasePageListAdapter;
import com.lessu.xieshi.bean.LoadState;
import com.lessu.xieshi.module.mis.bean.MisHySearchResultData;
import com.lessu.xieshi.module.mis.listener.AdapterItemClickListener;
import com.lessu.xieshi.module.mis.viewmodel.MisSearchViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by ljs
 * on 2020/11/26
 */
public class MisMemberSearchListAdapter extends BasePageListAdapter<MisHySearchResultData.ListContentBean> {

    private static final int TYPE_FOOTER_VIEW = 1;
    private static final int TYPE_NORMAL = 0;
    private boolean hasFooter = false;
    private LoadState loadState;
    private MisSearchViewModel viewModel;

    private static DiffUtil.ItemCallback<MisHySearchResultData.ListContentBean> mDiffCallBack =
            new DiffUtil.ItemCallback<MisHySearchResultData.ListContentBean>() {
                @Override
                public boolean areItemsTheSame(@NonNull MisHySearchResultData.ListContentBean oldItem,
                                               @NonNull MisHySearchResultData.ListContentBean newItem) {
                    return oldItem.getMemberId().equals(newItem.getMemberId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull MisHySearchResultData.ListContentBean oldItem,
                                                  @NonNull MisHySearchResultData.ListContentBean newItem) {
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
    public void bindWithCommonItemView(PageListCommonViewHolder vh, MisHySearchResultData.ListContentBean contentBean) {
        TextView HYMemberId = vh.getView(R.id.tv_mis_hynum);
        TextView MemberName = vh.getView(R.id.tv_mis_hyname);
        HYMemberId.setText(contentBean.getMemberId());
        MemberName.setText(contentBean.getMemberName());
    }
}
