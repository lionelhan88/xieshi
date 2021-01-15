package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.base.BaseViewModel;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;
import com.lessu.xieshi.module.sand.datasource.SandSalesQueryListDataFactory;
import com.lessu.xieshi.module.sand.repository.SandSalesQueryListRepository;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/21
 */
public class SanSalesQueryListViewModel extends BaseViewModel {
    private SandSalesQueryListRepository repository = new SandSalesQueryListRepository();
    private SandSalesQueryListDataFactory factory = new SandSalesQueryListDataFactory(repository);
    private LiveData<LoadState> pageState = Transformations.switchMap(factory.getDataSourceLiveData(),
            input -> input.getLoadState());
    private PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build();
    private LiveData<PagedList<SandSalesTargetBean>> pagedListLiveData =
            new LivePagedListBuilder<>(factory,config).build();
    public SanSalesQueryListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoadState> getPageState() {
        return pageState;
    }

    public LiveData<PagedList<SandSalesTargetBean>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    /**
     * 失败重试
     */
    public void retry(){
        factory.getDataSourceLiveData().getValue().getRetry().invoke();
    }

    /**
     * 刷新数据
     */
    public void refresh(){
        factory.getDataSourceLiveData().getValue().invalidate();
    }

    /**
     * 查询销售对象对应的性质的数据
     * @param queryType
     */
    public void setQueryType(String queryType){
        factory.setQueryUnitType(queryType);
        factory.getDataSourceLiveData().getValue().invalidate();
    }

    /**
     * 查询关键字的销售对象数据
     * @param queryKey
     */
    public void setQueryKey(String queryKey){
        factory.setQueryUnitKey(queryKey);
        factory.getDataSourceLiveData().getValue().invalidate();
    }

    /**
     * 添加销售对象
     */
    public void addSandSalesTarget(List<SandSalesTargetBean> beans,List<String> delIds){
        loadState.postValue(LoadState.LOADING);
        repository.addSanSalesTarget(beans,delIds, new ResponseObserver<Object>() {
            @Override
            public void success(Object sandSalesTargetBatchBeans) {
                Object o = sandSalesTargetBatchBeans;
                loadState.postValue(LoadState.SUCCESS);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE);
                throwableLiveData.postValue(throwable);
            }
        });
    }


}
