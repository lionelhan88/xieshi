package com.scetia.app_sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.scetia.app_sand.bean.TestingCompanyBean;
import com.scetia.app_sand.datasource.SandTestingCompanyQueryListDataFactory;
import com.scetia.app_sand.datasource.SandTestingCompanyQueryListDataSource;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.app_sand.repository.SandTestingCompanyQueryListRepository;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/23
 */
public class TestingCompanyQueryListViewModel extends BaseViewModel {
    private SandTestingCompanyQueryListRepository repository = new SandTestingCompanyQueryListRepository();
    private SandTestingCompanyQueryListDataFactory factory = new SandTestingCompanyQueryListDataFactory(repository);
    private LiveData<LoadState> pageState = Transformations.switchMap(factory.getDataSourceLiveData(),
            input -> input.getLoadState());

    private PagedList.Config config = new PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build();
    private LiveData<PagedList<TestingCompanyBean>> pagedListLiveData =
            new LivePagedListBuilder<>(factory,config).build();

    public TestingCompanyQueryListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoadState> getPageState() {
        return pageState;
    }

    public LiveData<PagedList<TestingCompanyBean>> getPagedListLiveData() {
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

    public void setQueryCounties(String queryCounties){
        factory.setQueryCounties(queryCounties);
        SandTestingCompanyQueryListDataSource value = factory.getDataSourceLiveData().getValue();
        if(value!=null) {
            value.invalidate();
        }
    }

    public void setQueryKey(String queryKey){
        factory.setQueryUnitKey(queryKey);
        factory.getDataSourceLiveData().getValue().invalidate();
    }

    public void addTestingCompanies(List<TestingCompanyBean> beans, List<String> delIds) {
        loadState.postValue(LoadState.LOADING);
        repository.addTestingCompanies(beans, delIds, new ResponseObserver<Object>() {
            @Override
            public void success(Object sandSalesTargetBatchBeans) {
                Object o = sandSalesTargetBatchBeans;
                loadState.postValue(LoadState.SUCCESS);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }
}
