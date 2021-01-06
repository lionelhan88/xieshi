package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.data.LoadState;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.BuildSandRetrofit;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.module.sand.bean.TestingQueryResultBean;

import java.util.List;

/**
 * created by ljs
 * on 2021/1/4
 */
public class TestingInfoQueryListViewModel extends BaseViewModel {
    private static final int DEFAULT_PAGE_INDEX = 0;
    private int currentPage = DEFAULT_PAGE_INDEX;
    private MutableLiveData<List<TestingQueryResultBean>> resultQueryLiveData = new MutableLiveData<>();
    public TestingInfoQueryListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<TestingQueryResultBean>> getResultQueryLiveData() {
        return resultQueryLiveData;
    }

    public void loadInitData() {
        currentPage = DEFAULT_PAGE_INDEX;
        loadState.postValue(LoadState.LOAD_INIT);
        loadData();
    }

    public void refreshLoad(){
        currentPage = DEFAULT_PAGE_INDEX;
        loadData();
    }

    public void loadMoreData(){
        loadData();
    }
    private void loadData() {
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getTestingInfoResultData("consignCreateDate desc", currentPage, 20, "")
                .compose(BuildSandRetrofit.<BuildSandResultData<List<TestingQueryResultBean>>, List<TestingQueryResultBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<TestingQueryResultBean>>() {
                    @Override
                    public void success(List<TestingQueryResultBean> testingQueryResultBeans) {
                        if (testingQueryResultBeans.size() == 0) {
                            loadState.postValue(currentPage == DEFAULT_PAGE_INDEX ? LoadState.EMPTY : LoadState.NO_MORE);
                        } else {
                            loadState.postValue(currentPage == DEFAULT_PAGE_INDEX ? LoadState.LOAD_INIT_SUCCESS : LoadState.SUCCESS);
                            currentPage++;
                        }
                        resultQueryLiveData.postValue(testingQueryResultBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }
}
