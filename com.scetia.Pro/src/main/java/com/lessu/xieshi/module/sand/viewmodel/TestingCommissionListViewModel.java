package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.http.service.BuildSandApiService;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.module.sand.adapter.TestingCommissionListAdapter;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.lessu.xieshi.module.sand.repository.TestingCommissionRepository;
import com.scetia.Pro.network.manage.BuildSandRetrofit;

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
    //删除项的下标索引
    private MutableLiveData<Integer> delPosition = new MutableLiveData<>();
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

    public MutableLiveData<List<TestingCommissionBean>> getTestingCommissionLiveData() {
        return testingCommissionLiveData;
    }

    public MutableLiveData<Integer> getDelPosition() {
        return delPosition;
    }

    public void refreshOnLoad() {
        currentPageIndex = DEFAULT_PAGE_INDEX;
        loadData(false);
    }

    /**
     * 加载数据列表
     *
     * @param isShowLoading 是否展示加载提示框
     */
    public void loadData(boolean isShowLoading) {
        if (isShowLoading && currentPageIndex == DEFAULT_PAGE_INDEX) {
            //第一次进入页面需要加载提示框
            loadState.setValue(LoadState.LOAD_INIT);
        }
        repository.getTestingCommissions(currentPageIndex, new ResponseObserver<List<TestingCommissionBean>>() {
            @Override
            public void success(List<TestingCommissionBean> testingCommissionBeans) {
                if (testingCommissionBeans.size() == 0) {
                    //如果当前时第一页，没有数据时提示初始化无数据否则提示没有更多数据
                    loadState.setValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.EMPTY : LoadState.NO_MORE);
                } else {
                    loadState.setValue(currentPageIndex == DEFAULT_PAGE_INDEX ? LoadState.LOAD_INIT_SUCCESS : LoadState.SUCCESS);
                    //准备加载下一页
                    currentPageIndex++;
                }
                testingCommissionLiveData.setValue(testingCommissionBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }

    /**
     * 删除选中的委托记录
     *
     * @param commissionId
     */
    public void delCommission(String commissionId, int position) {
        loadState.setValue(LoadState.LOADING.setMessage("正在删除..."));
        repository.delTestingCommission(commissionId, new ResponseObserver<Object>() {
            @Override
            public void success(Object o) {
                //删除成功
                loadState.postValue(LoadState.SUCCESS);
                delPosition.setValue(position);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }


}
