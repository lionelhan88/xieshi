package com.lessu.xieshi.module.mis.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.data.LoadState;
import com.lessu.xieshi.module.mis.bean.MisAnnualLeaveData;
import com.lessu.xieshi.module.mis.datasource.MisAnnualLeaveDataFactory;
import com.lessu.xieshi.module.mis.datasource.MisAnnualLeaveDataSource;

/**
 * created by ljs
 * on 2020/12/2
 */
public class MisAnnualLeaveViewModel extends BaseViewModel {
    private MisAnnualLeaveDataFactory factory = new MisAnnualLeaveDataFactory();
    private LiveData<LoadState> pageLoadState = Transformations.switchMap(factory.getAnnualLeaveDataSource(),
            new Function<MisAnnualLeaveDataSource, LiveData<LoadState>>() {
        @Override
        public LiveData<LoadState> apply(MisAnnualLeaveDataSource input) {
            return input.getLoadState();
        }
    });
    //获取Approve字段，判断菜单权限
    private LiveData<MisAnnualLeaveData> annualLeaveData = Transformations.switchMap(factory.getAnnualLeaveDataSource(), new Function<MisAnnualLeaveDataSource, LiveData<MisAnnualLeaveData>>() {
        @Override
        public LiveData<MisAnnualLeaveData> apply(MisAnnualLeaveDataSource input) {
            return input.getAnnualLeaveData();
        }
    });
    //初始化配置
    private PagedList.Config config = new PagedList.Config.Builder()
            .setInitialLoadSizeHint(5)
            .setEnablePlaceholders(false)
            .setPageSize(5)
            .build();
    private LiveData<PagedList<MisAnnualLeaveData.AnnualLeaveBean>> pagedListLiveData =
            new LivePagedListBuilder<>(factory,config).build();

    public MisAnnualLeaveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoadState> getPageLoadState() {
        return pageLoadState;
    }

    public LiveData<MisAnnualLeaveData> getAnnualLeaveData() {
        return annualLeaveData;
    }

    //数据驱动，通知UI更新
    public LiveData<PagedList<MisAnnualLeaveData.AnnualLeaveBean>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    /**
     * 刷新列表
     */
    public void reRefresh() {
        MisAnnualLeaveDataSource value = factory.getAnnualLeaveDataSource().getValue();
        if (value != null) {
            value.invalidate();
        }
    }

    /**
     * 加载失败，用户可以诚信尝试
     */
    public void reTry(){
        MisAnnualLeaveDataSource value = factory.getAnnualLeaveDataSource().getValue();
        if(value!=null){
            value.getRetry().invoke();
        }
    }

    /**
     * 传入查询的年份
     * @param year
     */
    public void setQueryYear(String year){
        factory.setYear(year);
        factory.getAnnualLeaveDataSource().getValue().invalidate();
    }

    /**
     * 传入要查询的数据状态
     * @param state
     */
    public void setQueryState(String state){
        factory.setState(state);
        factory.getAnnualLeaveDataSource().getValue().invalidate();
    }

}
