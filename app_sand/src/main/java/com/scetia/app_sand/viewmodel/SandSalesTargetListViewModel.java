package com.scetia.app_sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.app_sand.bean.AddedSandSalesTargetBean;
import com.scetia.app_sand.repository.SandSalesTargetListRepository;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/21
 */
public class SandSalesTargetListViewModel extends BaseViewModel {
    private static final int DEFAULT_PAGE_INDEX = 0;
    private int currentPageIndex = DEFAULT_PAGE_INDEX;
    private SandSalesTargetListRepository repository = new SandSalesTargetListRepository();

    private MutableLiveData<LoadState> deleteDataState = new MutableLiveData<>();

    private MutableLiveData<List<AddedSandSalesTargetBean>> addedSanSalesTargetLiveData = new MutableLiveData<>();



    private MutableLiveData<Integer> itemPosition = new MutableLiveData<>();

    public SandSalesTargetListViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<List<AddedSandSalesTargetBean>> getAddedSanSalesTargetLiveData() {
        return addedSanSalesTargetLiveData;
    }

    public MutableLiveData<LoadState> getDeleteDataState() {
        return deleteDataState;
    }


    public MutableLiveData<Integer> getItemPosition() {
        return itemPosition;
    }

    /**
     * 初始化加载数据
     */
    public void loadInitData(){
        currentPageIndex = DEFAULT_PAGE_INDEX;
        loadState.postValue(LoadState.LOAD_INIT);
        loadData();
    }
    /**
     * 刷新数据
     */
    public void refresh() {
        currentPageIndex = DEFAULT_PAGE_INDEX;
        loadData();
    }

    /**
     * 加载更多数据
     */
    public void loadMoreData(){
        loadData();
    }

    private void loadData() {
        repository.loadAddedSanSalesTarget(currentPageIndex, new ResponseObserver<List<AddedSandSalesTargetBean>>() {
            @Override
            public void success(List<AddedSandSalesTargetBean> addedSandSalesTargetBeans) {
                if (addedSandSalesTargetBeans.size() == 0) {
                    loadState.postValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.EMPTY : LoadState.NO_MORE);
                } else {
                    loadState.postValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.LOAD_INIT_SUCCESS : LoadState.SUCCESS);
                    currentPageIndex++;
                }
                addedSanSalesTargetLiveData.postValue(addedSandSalesTargetBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }

    /**
     * 删除已经添加的销售对象
     */
    public void delAddedSanSalesTarget(AddedSandSalesTargetBean bean, int position) {
        deleteDataState.postValue(LoadState.LOADING);
        repository.delAddedSanSalesTarget(bean, new ResponseObserver<Object>() {
            @Override
            public void success(Object o) {
                //删除成功
                deleteDataState.setValue(LoadState.SUCCESS);
                itemPosition.postValue(position);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                //删除失败
                deleteDataState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }
}
