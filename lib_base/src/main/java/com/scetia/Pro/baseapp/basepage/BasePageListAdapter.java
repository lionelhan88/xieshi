package com.scetia.Pro.baseapp.basepage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.baseapp.R;
import com.scetia.Pro.baseapp.listener.AdapterItemClickListener;

/**
 * created by ljs
 * on 2020/12/2
 */
public abstract class BasePageListAdapter<T> extends PagedListAdapter<T, RecyclerView.ViewHolder> {
    private static final int TYPE_FOOTER_VIEW = 1;
    private static final int TYPE_NORMAL = 0;
    private static int commonLayoutId;
    protected LoadState loadState;
    private boolean hasFooter;
    private AdapterItemClickListener<T> adapterItemClickListener;
    protected BasePageListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback, @LayoutRes int layoutId) {
        super(diffCallback);
        commonLayoutId = layoutId;
    }

    public void setAdapterItemClickListener(AdapterItemClickListener<T> adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    /**
     * 底部加载提示按钮的点击事件
     */
    public abstract void footerItemClick();

    public void setLoadState(LoadState loadState) {
        this.loadState = loadState;
        //初始化加载不要显示底部加载提示，有另外的全屏加载提示
        if (loadState != LoadState.LOAD_INIT) {
            showFooter();
        } else {
            hideFooter();
        }
    }

    /**
     * 显示底部的加载提示
     */
    private void showFooter(){
        if(hasFooter){
            notifyItemChanged(getItemCount()-1);
        }else{
            hasFooter = true;
            notifyItemInserted(getItemCount()-1);
        }
    }

    /**
     * 隐藏底部的加载提示
     */
    private void hideFooter(){
        if(hasFooter){
            hasFooter =false;
            notifyItemRemoved(getItemCount()-1);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_FOOTER_VIEW&&hasFooter){
            FooterViewHolder viewHolder = FooterViewHolder.newInstance(parent);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    footerItemClick();
                }
            });
            return viewHolder;
        }
        return PageListCommonViewHolder.newInstance(parent,commonLayoutId);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_FOOTER_VIEW&&holder instanceof FooterViewHolder){
            //返回底部加载提示
            FooterViewHolder viewHolder = (FooterViewHolder) holder;
            viewHolder.bindWithLoadState(loadState);
        }else{
            PageListCommonViewHolder viewHolder = (PageListCommonViewHolder) holder;
            T t = getItem(position);
            bindWithCommonItemView(viewHolder,t);
            //设置点击事件的回调
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(adapterItemClickListener !=null){
                        adapterItemClickListener.onItemClickListener(position,t);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        //需要添加底部的加载提示
        return super.getItemCount()+(hasFooter?1:0);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1&&hasFooter){
            return TYPE_FOOTER_VIEW;
        }else{
            return TYPE_NORMAL;
        }
    }
    public abstract void bindWithCommonItemView(PageListCommonViewHolder vh,T t);

    static class FooterViewHolder extends RecyclerView.ViewHolder{
        ContentLoadingProgressBar loadProgress;
        TextView loadText;
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            loadProgress = itemView.findViewById(R.id.list_footer_load_progress);
            loadText = itemView.findViewById(R.id.list_footer_load_text);
        }

        public static FooterViewHolder newInstance(ViewGroup parent){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_footer__layout, parent, false);
            return new FooterViewHolder(itemView);
        }

        public void bindWithLoadState(LoadState loadState){
            //根据不同的状态显示不同的提示
            switch (loadState) {
                case EMPTY:
                    loadProgress.setVisibility(View.GONE);
                    loadText.setText("没有符合查询条件的数据");
                    itemView.setClickable(false);
                    break;
                case NO_MORE:
                    //没有更多数据了
                    loadProgress.setVisibility(View.GONE);
                    loadText.setText("没有更多数据了");
                    itemView.setClickable(false);
                    break;
                case FAILURE:
                    loadProgress.setVisibility(View.GONE);
                    loadText.setText("加载失败，点击重试");
                    itemView.setClickable(true);
                    break;
                case LOADING:
                    loadProgress.setVisibility(View.VISIBLE);
                    loadText.setText("正在加载数据...");
                    itemView.setClickable(false);
                    break;
            }
        }
    }
}
