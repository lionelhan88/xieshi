package com.scetia.app_sand.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.scetia.app_sand.bean.SandSalesTargetBean;
import com.scetia.app_sand.repository.SandSalesQueryListRepository;


/**
 * created by ljs
 * on 2020/12/21
 */
public class SandSalesQueryListDataFactory extends DataSource.Factory<Integer, SandSalesTargetBean> {
    private MutableLiveData<SandSalesQueryListDataSource> dataSourceLiveData = new MutableLiveData<>();
    private String queryUnitType="预拌混凝土";
    private String queryUnitKey="";
    private SandSalesQueryListRepository repository;

    public SandSalesQueryListDataFactory(SandSalesQueryListRepository repository) {
        this.repository = repository;
    }

    public void setQueryUnitType(String queryUnitType) {
        this.queryUnitType = queryUnitType;
    }

    public void setQueryUnitKey(String queryUnitKey) {
        this.queryUnitKey = queryUnitKey;
    }

    public MutableLiveData<SandSalesQueryListDataSource> getDataSourceLiveData() {
        return dataSourceLiveData;
    }

    @Override
    public DataSource<Integer, SandSalesTargetBean> create() {
        SandSalesQueryListDataSource dataSource = new SandSalesQueryListDataSource(queryUnitType,queryUnitKey,repository);
        dataSourceLiveData.postValue(dataSource);
        return dataSource;
    }
}
