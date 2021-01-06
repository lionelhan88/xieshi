package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.data.LoadState;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.module.sand.adapter.TestingCommissionListAdapter;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.lessu.xieshi.module.sand.repository.TestingCommissionRepository;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/29
 */
public class TestingCommissionListViewModel extends BaseViewModel {
    private static final int DEFAULT_PAGE_INDEX = 0;
    private int currentPageIndex = DEFAULT_PAGE_INDEX;

    private TestingCommissionListAdapter listAdapter;
    private TestingCommissionRepository repository = new TestingCommissionRepository();
    //加载数据列表时的状态
    private MutableLiveData<LoadState> loadDatState = new MutableLiveData<>();
    private MutableLiveData<List<TestingCommissionBean>> testingCommissionLiveData = new MutableLiveData<>();

    public TestingCommissionListViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 创建TestingCommissionListAdapter对象，并保存
     *
     * @return
     */
    public TestingCommissionListAdapter getListAdapter() {
        if (listAdapter == null) {
            listAdapter = new TestingCommissionListAdapter();
        }
        return listAdapter;
    }

    public MutableLiveData<LoadState> getLoadDatState() {
        return loadDatState;
    }

    public MutableLiveData<List<TestingCommissionBean>> getTestingCommissionLiveData() {
        return testingCommissionLiveData;
    }

    public void refreshOnLoad(){
        currentPageIndex=DEFAULT_PAGE_INDEX;
        loadData(false);
    }
    /**
     * 加载数据列表
     * @param isShowLoading 是否展示加载提示框
     */
    public void loadData(boolean isShowLoading) {
        if(isShowLoading&&currentPageIndex==DEFAULT_PAGE_INDEX){
            //第一次进入页面需要加载提示框
            loadDatState.setValue(LoadState.LOAD_INIT);
        }
        repository.getTestingCommissions(currentPageIndex, new ResponseObserver<List<TestingCommissionBean>>() {
            @Override
            public void success(List<TestingCommissionBean> testingCommissionBeans) {
                if(testingCommissionBeans.size()==0){
                    //如果当前时第一页，没有数据时提示初始化无数据否则提示没有更多数据
                    loadDatState.setValue(currentPageIndex==DEFAULT_PAGE_INDEX?LoadState.EMPTY :LoadState.NO_MORE);
                }else{
                    loadDatState.setValue(currentPageIndex==DEFAULT_PAGE_INDEX?LoadState.LOAD_INIT_SUCCESS:LoadState.SUCCESS);
                    //准备加载下一页
                    currentPageIndex++;
                }
                testingCommissionLiveData.setValue(testingCommissionBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadDatState.setValue(LoadState.FAILURE);
                throwableLiveData.postValue(throwable);
            }
        });
    }


}
