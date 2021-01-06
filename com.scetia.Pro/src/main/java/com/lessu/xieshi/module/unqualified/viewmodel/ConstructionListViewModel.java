package com.lessu.xieshi.module.unqualified.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.data.LoadState;
import com.lessu.xieshi.module.unqualified.datasource.ConstructionListDataFactory;
import com.lessu.xieshi.module.unqualified.bean.ConstructionData;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/12/15
 */
public class ConstructionListViewModel extends BaseViewModel {
    private ConstructionListDataFactory factory = new ConstructionListDataFactory();
    private LiveData<LoadState> pageState = Transformations.switchMap(factory.getDataSourceLiveData(),
            input -> input.getLoadState());
    private PagedList.Config config = new PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build();
    private LiveData<PagedList<ConstructionData.ConstructionBean>> pagedListLiveData
             = new LivePagedListBuilder<>(factory,config)
            .build();



    public ConstructionListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoadState> getPageState() {
        return pageState;
    }

    public LiveData<PagedList<ConstructionData.ConstructionBean>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    /**
     * 传入请求的参数值
     */
    public void setParams(HashMap<String,Object> params){
        factory.setParams(params);
    }

    /**
     * 加载失败时，重新尝试加载
     */
    public void retry(){
        factory.getDataSourceLiveData().getValue().getRetry().invoke();
    }
}
