package com.lessu.uikit.refreashAndLoad;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by lessu on 14-7-8.
 */
public class RefreshAndLoadMoreGridFragment extends RefreshAndLoadMoreBaseFragment {

    public PullToRefreshGridView getListView() {
        return (PullToRefreshGridView) refreshBase;
    }

    public void setListView(PullToRefreshGridView gridView) {
        this.refreshBase = gridView;
    }

}
