package com.lessu.uikit.refreashAndLoad;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by lessu on 14-7-3.
 */
public class RefreshAndLoadMoreListFragment extends RefreshAndLoadMoreBaseFragment {

    public PullToRefreshListView getListView() {
        return (PullToRefreshListView) refreshBase;
    }

    public void setListView(PullToRefreshListView listView) {
        this.refreshBase = listView;
    }

}
