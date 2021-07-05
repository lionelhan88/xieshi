package com.scetia.app_sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.app_sand.adapter.SandTestingCompanyListAdapter;
import com.scetia.app_sand.bean.AddedTestingCompanyBean;
import com.scetia.app_sand.repository.TestingCompanyListRepository;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/23
 */
public class SandTestingCompanyListViewModel extends BaseViewModel {
    private static final int DEFAULT_PAGE_INDEX = 0;
    private int currentPageIndex = DEFAULT_PAGE_INDEX;
    private MutableLiveData<List<AddedTestingCompanyBean>> addedSanSalesTargetLiveData = new MutableLiveData<>();

    private TestingCompanyListRepository repository = new TestingCompanyListRepository();

    private MutableLiveData<Integer> itemPosition = new MutableLiveData<>();
    private SandTestingCompanyListAdapter listAdapter;

    public SandTestingCompanyListViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<List<AddedTestingCompanyBean>> getAddedSanSalesTargetLiveData() {
        return addedSanSalesTargetLiveData;
    }

    public MutableLiveData<Integer> getItemPosition() {
        return itemPosition;
    }


    public SandTestingCompanyListAdapter getListAdapter() {
        if (listAdapter == null) {
            listAdapter = new SandTestingCompanyListAdapter();
        }
        return listAdapter;
    }

    /**
     * 初始化加载数据
     */
    public void loadDataInit(){
        currentPageIndex = DEFAULT_PAGE_INDEX;
        //第一页初始化
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
        repository.getAddedTestingCompanies(currentPageIndex, new ResponseObserver<List<AddedTestingCompanyBean>>() {
            @Override
            public void success(List<AddedTestingCompanyBean> addedTestingCompanyBeans) {
                if (addedTestingCompanyBeans.size() == 0) {
                    loadState.postValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.EMPTY : LoadState.NO_MORE);
                } else {
                    loadState.postValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.LOAD_INIT_SUCCESS : LoadState.SUCCESS);
                    currentPageIndex++;
                }
                addedSanSalesTargetLiveData.postValue(addedTestingCompanyBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }

    /**
     * 删除添加的检测单位
     * @param bean
     * @param position
     */
    public void delAddedTestingCompany(AddedTestingCompanyBean bean, int position) {
        loadState.postValue(LoadState.LOADING.setMessage("正在删除..."));
        repository.delAddTestingCompanies(bean, new ResponseObserver<Object>() {
            @Override
            public void success(Object o) {
                loadState.postValue(LoadState.SUCCESS);
                itemPosition.postValue(position);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }
}
