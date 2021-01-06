package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.data.LoadState;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;
import com.lessu.xieshi.module.sand.repository.SandSalesTargetListRepository;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/21
 */
public class SandSalesTargetListViewModel extends BaseViewModel {
    private static final int DEFAULT_PAGE_INDEX = 0;
    private int currentPageIndex = DEFAULT_PAGE_INDEX;
    private SandSalesTargetListRepository repository = new SandSalesTargetListRepository();

    private MutableLiveData<LoadState> loadDataState = new MutableLiveData<>();

    private MutableLiveData<List<AddedSandSalesTargetBean>> addedSanSalesTargetLiveData = new MutableLiveData<>();



    private MutableLiveData<Integer> itemPosition = new MutableLiveData<>();

    public SandSalesTargetListViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<List<AddedSandSalesTargetBean>> getAddedSanSalesTargetLiveData() {
        return addedSanSalesTargetLiveData;
    }

    public MutableLiveData<LoadState> getLoadDataState() {
        return loadDataState;
    }

    public MutableLiveData<Integer> getItemPosition() {
        return itemPosition;
    }

    public void setPageIndex(int pageIndex) {
        currentPageIndex = pageIndex;
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
        repository.loadAddedSanSalesTarget(currentPageIndex, new ResponseObserver<List<AddedSandSalesTargetBean>>() {
            @Override
            public void success(List<AddedSandSalesTargetBean> addedSandSalesTargetBeans) {
                if (addedSandSalesTargetBeans.size() == 0) {
                    loadDataState.postValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.EMPTY : LoadState.NO_MORE);
                } else {
                    loadDataState.postValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.LOAD_INIT_SUCCESS : LoadState.SUCCESS);
                    currentPageIndex++;
                }
                addedSanSalesTargetLiveData.postValue(addedSandSalesTargetBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadDataState.postValue(LoadState.FAILURE);
                throwableLiveData.postValue(throwable);
            }
        });
    }


    /**
     * 删除已经添加的销售对象
     */
    public void delAddedSanSalesTarget(AddedSandSalesTargetBean bean, int position) {
        loadState.postValue(LoadState.LOADING);
        repository.delAddedSanSalesTarget(bean, new ResponseObserver<Response<ResponseBody>>() {
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
