package com.lessu.uikit.refreashAndLoad.page;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by lessu on 14-7-8.
 */
public abstract class ListPageWrapper<T extends View> extends BasePageWrapper<T> implements AdapterView.OnItemClickListener {

    public ListPageWrapper(Context incontext) {
		super(incontext);
	}
	public void wrap(PullToRefreshListView listView) {
        super.wrap(listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    public PullToRefreshListView getListView(){
        return (PullToRefreshListView) refreshBase;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        onCellClick(i, (T) view,pageController.getList().get(i - 1));
    }

    public void onCellClick(int position,T cell,Object data){

    }
}
