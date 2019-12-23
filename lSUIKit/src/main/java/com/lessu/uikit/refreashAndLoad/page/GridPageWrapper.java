package com.lessu.uikit.refreashAndLoad.page;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by lessu on 14-7-8.
 */
public abstract class GridPageWrapper<T extends View> extends BasePageWrapper<T> implements AdapterView.OnItemClickListener {

    public GridPageWrapper(Context incontext) {
		super(incontext);
		// TODO Auto-generated constructor stub
	}

	public void wrap(PullToRefreshGridView gridView) {
        super.wrap(gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }
//    public PullToRefreshGridView getListView(){
//        return (PullToRefreshGridView) refreshBase;
//    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    	
        onCellClick(i, (T) view,pageController.getList().get(i - 1));
    }

    public void onCellClick(int position,T cell,Object data){

    }
}
