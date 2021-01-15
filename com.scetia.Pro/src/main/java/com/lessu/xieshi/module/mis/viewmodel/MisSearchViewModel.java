package com.lessu.xieshi.module.mis.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.lessu.xieshi.base.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.mis.bean.MisMemberSearchResultData;
import com.lessu.xieshi.module.mis.datasource.MisMemberSearchDataFactory;
import com.lessu.xieshi.module.mis.datasource.MisMemberSearchDataSource;

/**
 * created by ljs
 * on 2020/11/26
 */
public class MisSearchViewModel extends BaseViewModel {
    private MisMemberSearchDataFactory factory = new MisMemberSearchDataFactory();
    private LiveData<LoadState> loadState = Transformations.switchMap(factory.getDataSourceLiveData(),
            new Function<MisMemberSearchDataSource, LiveData<LoadState>>() {
                @Override
                public LiveData<LoadState> apply(MisMemberSearchDataSource input) {
                    return input.getLoadState();
                }
            });

    //初始化paging的配置
    private PagedList.Config config = new PagedList.Config.Builder()
            //首页加载的数据条目
            .setInitialLoadSizeHint(20)
            //每页加载的数据条目
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build();
    //返回后的数据
    private LiveData<PagedList<MisMemberSearchResultData.ListContentBean>> pagedListLiveData
            = new LivePagedListBuilder(factory,config).build();

    public MisSearchViewModel(@NonNull Application application) {
        super(application);
    }

    //当前加载的状态
    public LiveData<LoadState> getSearchLoadState() {
        return loadState;
    }
    public LiveData<PagedList<MisMemberSearchResultData.ListContentBean>> getPagedListLiveData() {
        return pagedListLiveData;
    }


    /**
     *输入查询的关键字
     * @param queryKey
     */
    public void setQueryKey(String queryKey) {
        factory.setQuery(queryKey);
        MisMemberSearchDataSource value = factory.getDataSourceLiveData().getValue();
        value.invalidate();
    }

    /**
     * 加载失败，重新加载
     */
    public void retry(){
        kotlin.jvm.functions.Function0<Object> retry = factory.getDataSourceLiveData().getValue().getRetry();
        if(retry!=null)
        retry.invoke();
    }
    /**
     * 刷新数据
     */
    public void reFresh(){
        factory.getDataSourceLiveData().getValue().invalidate();
    }
}
