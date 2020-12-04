package com.lessu.xieshi.module.mis.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.lessu.xieshi.module.mis.bean.MisHySearchResultData;

/**
 * created by ljs
 * on 2020/11/27
 */
public class MisMemberSearchDataFactory extends DataSource.Factory<Integer, MisHySearchResultData.ListContentBean> {
    //保存dataSource对象，并将对象传递到viewModel，对象创建可能会有延迟，所以使用liveData存储，等待对象创建成功，通知数据变化
    private MutableLiveData<MisMemberSearchDataSource> dataSourceLiveData = new MutableLiveData<>();

    public MutableLiveData<MisMemberSearchDataSource> getDataSourceLiveData() {
        return dataSourceLiveData;
    }
    private String query;

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public DataSource<Integer, MisHySearchResultData.ListContentBean> create() {
        MisMemberSearchDataSource dataSource = new MisMemberSearchDataSource(query);
        dataSourceLiveData.postValue(dataSource);
        return dataSource;
    }
}
