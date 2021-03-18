package com.lessu.xieshi.module.sand.adapter;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.view.ImageViewWithDelete;


/**
 * created by ljs
 * on 2020/10/27
 */
public class TakePhotosAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private OnSrcClickListener onSrcClickListener;
    private DelClickListener delClickListener;
    public TakePhotosAdapter() {
        super(R.layout.sand_manage_take_photos_item);
        addData("");
    }

    public interface OnSrcClickListener{
        void onScrClick(ImageViewWithDelete imageViewWithDelete, String photoPath);
    }
    public interface DelClickListener{
        void onDelClick(int imageIndex);
    }

    public void setOnSrcClickListener(OnSrcClickListener onSrcClickListener) {
        this.onSrcClickListener = onSrcClickListener;
    }

    public void setDelClickListener(DelClickListener delClickListener) {
        this.delClickListener = delClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        final ImageViewWithDelete imageViewWithDelete = helper.getView(R.id.sand_manage_take_photos_item_close_img);
        imageViewWithDelete.setImageUrl(item);
        imageViewWithDelete.setCloseImageViewClick(new ImageViewWithDelete.CloseImageViewClick() {
            @Override
            public void closeListener() {
                String item1 = getItem(getData().size() - 1);
                if(!TextUtils.isEmpty(item1)){
                    addData("");
                }
                delClickListener.onDelClick(helper.getAdapterPosition());
                remove(helper.getAdapterPosition());
            }

            @Override
            public void srcClickListener(View v) {
                onSrcClickListener.onScrClick(imageViewWithDelete,item);
            }
        });
    }
}
