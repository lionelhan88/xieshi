package com.lessu.uikit.refreashAndLoad;

import androidx.fragment.app.Fragment;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by lessu on 14-7-3.
 */
public abstract class RefreshAndLoadMoreBaseFragment extends Fragment {

    protected PullToRefreshBase refreshBase;

//    private PullToRefreshBase
    @Override
    public void onStart() {
        super.onStart();
        refreshBase.setMode(PullToRefreshBase.Mode.BOTH);
        refreshBase.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                onRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                onLoadMore();
            }
        });
    }


    public void setCanLoadMore(boolean can){
        if (can){
            refreshBase.setMode(PullToRefreshBase.Mode.BOTH);
        }else{
            refreshBase.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
    }
    public void finishRefresh(){
        refreshBase.onRefreshComplete();
    }
    public void finishLoad(){
        refreshBase.onRefreshComplete();
    }
    public void onRefresh(){
//        listView.onRefreshComplete();
    }
    public void onLoadMore(){
//        listView.onRefreshComplete();

    }


}
