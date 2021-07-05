package com.scetia.app_sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.app_sand.bean.TestingQueryResultBean;
import com.scetia.app_sand.service.BuildSandApiService;

import java.util.HashMap;
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
    private String queryKey;

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
        loadInitData();
    }

    public void loadInitData() {
        currentPage = DEFAULT_PAGE_INDEX;
        loadState.postValue(LoadState.LOAD_INIT);
        loadData(queryKey);
    }

    public void refreshLoad(){
        currentPage = DEFAULT_PAGE_INDEX;
        loadData(queryKey);
    }

    public void loadMoreData(){
        loadData(queryKey);
    }
    private void loadData(String queryKey) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("orderBy","consignCreateDate desc");
        params.put("pageIndex",currentPage);
        params.put("pageSize",20);
        if(queryKey!=null){
            params.put("detectionAgencyMemberName",queryKey);
        }
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getTestingInfoResultData(params)
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
                        loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }
}
