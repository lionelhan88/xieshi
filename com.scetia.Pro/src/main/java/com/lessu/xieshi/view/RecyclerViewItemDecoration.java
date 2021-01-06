package com.lessu.xieshi.view;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * created by ljs
 * on 2020/12/3
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private int itemDiverHeight;

    public RecyclerViewItemDecoration( int itemDiverHeight) {
        this.itemDiverHeight = itemDiverHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = itemDiverHeight;
    }
}
