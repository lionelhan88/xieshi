package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.data.LoadState;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.module.sand.adapter.SandTestingCompanyListAdapter;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.lessu.xieshi.module.sand.repository.TestingCompanyListRepository;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/23
 */
public class SandTestingCompanyListViewModel extends BaseViewModel {
    private static final int DEFAULT_PAGE_INDEX = 0;
    private int currentPageIndex = DEFAULT_PAGE_INDEX;
    private MutableLiveData<LoadState> loadDataState = new MutableLiveData<>();
    private MutableLiveData<List<AddedTestingCompanyBean>> addedSanSalesTargetLiveData = new MutableLiveData<>();

    private TestingCompanyListRepository repository = new TestingCompanyListRepository();

    private MutableLiveData<Integer> itemPosition = new MutableLiveData<>();
    private SandTestingCompanyListAdapter listAdapter;

    public SandTestingCompanyListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<LoadState> getLoadDataState() {
        return loadDataState;
    }

    public MutableLiveData<List<AddedTestingCompanyBean>> getAddedSanSalesTargetLiveData() {
        return addedSanSalesTargetLiveData;
    }

    public MutableLiveData<Integer> getItemPosition() {
        return itemPosition;
    }

    public void setPageIndex(int pageIndex) {
        currentPageIndex = pageIndex;
    }

    public SandTestingCompanyListAdapter getListAdapter() {
        if (listAdapter == null) {
            listAdapter = new SandTestingCompanyListAdapter();
        }
        return listAdapter;
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        currentPageIndex = DEFAULT_PAGE_INDEX;
        loadData(false);
    }

    public void loadData(boolean isInit) {
        if (currentPageIndex == DEFAULT_PAGE_INDEX && isInit) {
            //第一页初始化
            loadDataState.postValue(LoadState.LOAD_INIT);
        }
        repository.getAddedTestingCompanies(currentPageIndex, new ResponseObserver<List<AddedTestingCompanyBean>>() {
            @Override
            public void success(List<AddedTestingCompanyBean> addedTestingCompanyBeans) {
                if (addedTestingCompanyBeans.size() == 0) {
                    loadDataState.postValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.EMPTY : LoadState.NO_MORE);
                } else {
                    loadDataState.postValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.LOAD_INIT_SUCCESS : LoadState.SUCCESS);
                    currentPageIndex++;
                }
                addedSanSalesTargetLiveData.postValue(addedTestingCompanyBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadDataState.postValue(LoadState.FAILURE);
                throwableLiveData.postValue(throwable);
            }
        });
    }

    public void delAddedTestingCompany(AddedTestingCompanyBean bean, int position) {
        loadState.postValue(LoadState.LOADING);
        repository.delAddTestingCompanies(bean, new ResponseObserver<Response<ResponseBody>>() {
            @Override
            public void success(Response<ResponseBody> responseBodyResponse) {
                if (responseBodyResponse.code() == 204) {
                    loadState.postValue(LoadState.SUCCESS);
                    itemPosition.postValue(position);
                } else {
                    loadState.postValue(LoadState.FAILURE);
                    throwableLiveData.postValue(new ExceptionHandle.ResponseThrowable(ExceptionHandle.LOCAL_ERROR, "删除失败！"));
                }
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE);
                throwableLiveData.postValue(throwable);
            }
        });
    }
}
