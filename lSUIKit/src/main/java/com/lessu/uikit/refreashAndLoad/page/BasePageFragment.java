package com.lessu.uikit.refreashAndLoad.page;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lessu.net.ApiConnection;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.page.PageController;
import com.lessu.uikit.refreashAndLoad.RefreshAndLoadMoreBaseFragment;
import com.lessu.uikit.tableview.SimpleClassListAdapter;

import java.util.List;

/**
 * Created by lessu on 14-7-3.
 */
public abstract class BasePageFragment<T extends View> extends RefreshAndLoadMoreBaseFragment implements PageController.PageControllerDelegate{
    protected   PageController                pageController;
    protected   BaseAdapter                   adapter;

    protected abstract ApiMethodDescription onPageGetApiMethodDescription   ();
    protected abstract void                 onPageToInit                    (PageController pageController);
    protected abstract T                    onPageCreateCell                (int position);
    protected abstract void                 onPageCellSetData               (int position, T cell, Object data);


    protected boolean testMode  = false;
    protected int testCount     = 10;
    protected String testJson   = "{}";
    public BasePageFragment() {
        super();
        pageController = new PageController();

        final BasePageFragment<T> self = this;

        adapter = new SimpleClassListAdapter<T>(){

            @Override
            public int getCount() {
                if (testMode){
                    return testCount;
                }
                return pageController.getList().size();
            }

            @Override
            public T newInstance(int position) {
                return self.onPageCreateCell(position);
            }

            @Override
            public void setData(int position, T cell) {
                if (testMode){
                    self.onPageCellSetData(position, cell, new JsonParser().parse(testJson));
                }else {
                    self.onPageCellSetData(position, cell, pageController.getList().get(position));
                }
            }
        };

        pageController.setDelegate(this);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        pageController.setApiMethod(onPageGetApiMethodDescription());
        onPageToInit(pageController);
        pageController.refreshNoMerge();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        pageController.refresh();

    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        pageController.nextPage();
    }

    public void onRefreshSuccess(List list, JsonElement result) {
        adapter.notifyDataSetChanged();
        setCanLoadMore(pageController.hasMore());
        finishRefresh();
    }

    @Override
    public void onNextSuccess(List list, JsonElement result) {
        adapter.notifyDataSetChanged();
        setCanLoadMore(pageController.hasMore());
        finishLoad();
    }

    @Override
    public void onRefreshFailed(ApiError error){
        finishRefresh();
    }

    @Override
    public void onNextFailed(ApiError error){
        finishLoad();
    }

    @Override
    public boolean beforeNextPageRequest(ApiConnection nextPageConnection) {
        return true;
    }

    @Override
    public boolean beforeRefreshRequest(ApiConnection refreshConnection) {
        return true;
    }
}
