package com.lessu.uikit.refreashAndLoad.page;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lessu.net.ApiConnection;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.page.PageController;
import com.lessu.uikit.refreashAndLoad.RefreshAndLoadMoreBase;
import com.lessu.uikit.tableview.SimpleClassListAdapter;
import com.lessu.uikit.views.LSAlert;

import java.util.List;

/**
 * Created by lessu on 14-7-3.
 */
public abstract class BasePageWrapper<T extends View> extends RefreshAndLoadMoreBase implements PageController.PageControllerDelegate {

    protected PageController pageController;
    protected BaseAdapter adapter;

    protected abstract ApiMethodDescription onPageGetApiMethodDescription();

    protected abstract void onPageToInit(PageController pageController);

    protected abstract T onPageCreateCell(int position);

    protected abstract void onPageCellSetData(int position, T cell, Object data);


    protected boolean testMode = false;
    protected int testCount = 10;
    protected Context context;

    protected String testJson = "{}";

    public BasePageWrapper(Context incontext) {
        super();
        context = incontext;
        pageController = new PageController();
        pageController.setApiMethod(onPageGetApiMethodDescription());

        final BasePageWrapper<T> self = this;

        adapter = new SimpleClassListAdapter<T>() {

            @Override
            public int getCount() {
                if (testMode) {
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
                if (testMode) {
                    self.onPageCellSetData(position, cell, new JsonParser().parse(testJson));
                } else {
                    self.onPageCellSetData(position, cell, pageController.getList().get(position));
                }
            }
        };

        pageController.setDelegate(this);

    }

    public void wrap(PullToRefreshBase baseView) {
        super.wrap(baseView);
        onPageToInit(pageController);
        onRefresh();

    }

    public void refresh() {
        super.onRefresh();
        LSAlert.showProgressHud(context, "加载中");
        pageController.refresh();

    }

    public void refreshNoMerge() {
        super.onRefresh();
        LSAlert.showProgressHud(context, "加载中");
        pageController.refreshNoMerge();

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        LSAlert.showProgressHud(context, "加载中");
        pageController.refresh();

    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        LSAlert.showProgressHud(context, "加载中");
        pageController.nextPage();
    }

    public void shuaxin() {
        adapter.notifyDataSetChanged();
        finishRefresh();
        setCanLoadMore(pageController.hasMore());
    }


    public void onRefreshSuccess(List list, JsonElement result) {
        LSAlert.dismissProgressHud();

        adapter.notifyDataSetChanged();
        finishRefresh();
        setCanLoadMore(pageController.hasMore());
    }

    @Override
    public void onNextSuccess(List list, JsonElement result) {
        LSAlert.dismissProgressHud();

        adapter.notifyDataSetChanged();
        finishLoad();
        setCanLoadMore(pageController.hasMore());
    }

    @Override
    public void onRefreshFailed(ApiError error) {
        LSAlert.dismissProgressHud();
        finishRefresh();
    }

    @Override
    public void onNextFailed(ApiError error) {
        LSAlert.dismissProgressHud();
        finishLoad();
        setCanLoadMore(pageController.hasMore());
    }

    @Override
    public boolean beforeNextPageRequest(ApiConnection nextPageConnection) {
        return true;
    }

    @Override
    public boolean beforeRefreshRequest(ApiConnection refreshConnection) {
        return true;
    }


    public String getTestJson() {
        return testJson;
    }

    public void setTestJson(String testJson) {
        this.testJson = testJson;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public PageController getPageController() {
        return pageController;
    }

    public void setPageController(PageController pageController) {
        this.pageController = pageController;
    }

}
