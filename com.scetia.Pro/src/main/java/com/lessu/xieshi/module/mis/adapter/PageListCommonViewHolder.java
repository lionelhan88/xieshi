package com.lessu.xieshi.module.mis.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by ljs
 * on 2020/12/2
 */
public class PageListCommonViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;
    public PageListCommonViewHolder(@NonNull View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }
    public static PageListCommonViewHolder newInstance(ViewGroup parent, @LayoutRes int commonLayoutId){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(commonLayoutId,parent,false);
        return new PageListCommonViewHolder(itemView);
    }
    /**
     * 获取当前view
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View>  T getView(@IdRes int viewId){
        View view = views.get(viewId);
        if(view==null){
            view = itemView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T) view;
    }


}
