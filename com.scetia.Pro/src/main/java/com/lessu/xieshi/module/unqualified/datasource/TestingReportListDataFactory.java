package com.lessu.xieshi.module.unqualified.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.lessu.xieshi.module.unqualified.bean.TestingReportData;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/12/15
 */
public class TestingReportListDataFactory extends DataSource.Factory<Integer, TestingReportData.TestingReportBean> {
    private HashMap<String,Object> params;
    private MutableLiveData<TestingReportListDataSource> dataSourceLiveData = new MutableLiveData<>();

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public MutableLiveData<TestingReportListDataSource> getDataSourceLiveData() {
        return dataSourceLiveData;
    }

    @Override
    public DataSource<Integer, TestingReportData.TestingReportBean> create() {
        TestingReportListDataSource dataSource = new TestingReportListDataSource(params);
        dataSourceLiveData.postValue(dataSource);
        return dataSource;
    }
}
