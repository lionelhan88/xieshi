package com.scetia.app_sand.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.scetia.app_sand.bean.TestingCompanyBean;
import com.scetia.app_sand.repository.SandTestingCompanyQueryListRepository;


/**
 * created by ljs
 * on 2020/12/21
 */
public class SandTestingCompanyQueryListDataFactory extends DataSource.Factory<Integer, TestingCompanyBean> {
    private MutableLiveData<SandTestingCompanyQueryListDataSource> dataSourceLiveData = new MutableLiveData<>();
    private String queryCounties="";
    private String queryUnitKey="";
    private SandTestingCompanyQueryListRepository repository;

    public SandTestingCompanyQueryListDataFactory(SandTestingCompanyQueryListRepository repository) {
        this.repository = repository;
    }

    public void setQueryCounties(String queryCounties) {
        this.queryCounties = queryCounties;
    }

    public void setQueryUnitKey(String queryUnitKey) {
        this.queryUnitKey = queryUnitKey;
    }

    public MutableLiveData<SandTestingCompanyQueryListDataSource> getDataSourceLiveData() {
        return dataSourceLiveData;
    }

    @Override
    public DataSource<Integer, TestingCompanyBean> create() {
        SandTestingCompanyQueryListDataSource dataSource = new SandTestingCompanyQueryListDataSource(queryCounties,queryUnitKey,repository);
        dataSourceLiveData.postValue(dataSource);
        return dataSource;
    }
}
