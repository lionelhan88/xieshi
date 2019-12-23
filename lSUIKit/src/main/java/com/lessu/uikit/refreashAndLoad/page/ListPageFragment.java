package com.lessu.uikit.refreashAndLoad.page;

import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.page.PageController;

/**
 * Created by lessu on 14-7-8.
 */
public abstract class ListPageFragment<T extends View> extends BasePageFragment<T>  {

    public void setListView(PullToRefreshListView listView) {

        listView.setAdapter(adapter);
        refreshBase = listView;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onCellClick(i, (T) view,pageController.getList().get(i-1));
            }
        });
    }
    public PullToRefreshListView getListView(){
        return (PullToRefreshListView) refreshBase;
    }

    public void onCellClick(int position,T cell,Object data){

    }
}
