package com.lessu.xieshi.module.unqualified.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.lessu.xieshi.base.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.unqualified.datasource.TestingReportListDataFactory;
import com.lessu.xieshi.module.unqualified.bean.TestingReportData;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/12/15
 */
public class TestingReportListViewModel extends BaseViewModel {
    private TestingReportListDataFactory factory = new TestingReportListDataFactory();
    private LiveData<LoadState> pageLoadState = Transformations.switchMap(factory.getDataSourceLiveData(),
            input -> input.getLoadState());
    private PagedList.Config config = new PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build();
    private LiveData<PagedList<TestingReportData.TestingReportBean>> pagedListLiveData
            = new LivePagedListBuilder<>(factory, config).build();

    public TestingReportListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoadState> getPageLoadState() {
        return pageLoadState;
    }

    public LiveData<PagedList<TestingReportData.TestingReportBean>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    /**
     * 加载失败时重新加载
     */
    public void retry() {
        factory.getDataSourceLiveData().getValue().getRetry().invoke();
    }

    /**
     * 传入查询的参数
     *
     * @param params
     */
    public void setQueryKey(HashMap<String, Object> params) {
        factory.setParams(params);
    }
}
