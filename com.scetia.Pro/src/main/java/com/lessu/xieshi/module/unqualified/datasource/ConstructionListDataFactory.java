package com.lessu.xieshi.module.unqualified.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.lessu.xieshi.module.unqualified.bean.ConstructionData;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/12/15
 */
public class ConstructionListDataFactory extends DataSource.Factory<Integer, ConstructionData.ConstructionBean> {
    private HashMap<String,Object> params;
    private MutableLiveData<ConstructionListDataSource> dataSourceLiveData = new MutableLiveData<>();

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public MutableLiveData<ConstructionListDataSource> getDataSourceLiveData() {
        return dataSourceLiveData;
    }
    @Override
    public DataSource<Integer, ConstructionData.ConstructionBean> create() {
        ConstructionListDataSource dataSource = new ConstructionListDataSource(params);
        dataSourceLiveData.postValue(dataSource);
        return dataSource;
    }
}
