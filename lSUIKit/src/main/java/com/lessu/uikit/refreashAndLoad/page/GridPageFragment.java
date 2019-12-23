package com.lessu.uikit.refreashAndLoad.page;

import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by lessu on 14-7-8.
 */
public abstract class GridPageFragment<T extends View> extends BasePageFragment<T> {

    public void setGridView(PullToRefreshGridView gridView) {
        gridView.setAdapter(adapter);
        refreshBase = gridView;
    }
    public PullToRefreshGridView getGridView(){
        return (PullToRefreshGridView) refreshBase;
    }

}
