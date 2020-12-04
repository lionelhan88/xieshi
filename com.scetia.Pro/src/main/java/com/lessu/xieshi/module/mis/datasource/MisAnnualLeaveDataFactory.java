package com.lessu.xieshi.module.mis.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.lessu.xieshi.module.mis.bean.MisAnnualLeaveData;

import java.util.Calendar;

/**
 * created by ljs
 * on 2020/12/2
 */
public class MisAnnualLeaveDataFactory extends DataSource.Factory<Integer, MisAnnualLeaveData.AnnualLeaveBean> {
    private MutableLiveData<MisAnnualLeaveDataSource> annualLeaveDataSource = new MutableLiveData<>();

    public MutableLiveData<MisAnnualLeaveDataSource> getAnnualLeaveDataSource() {
        return annualLeaveDataSource;
    }
    Calendar calendar = Calendar.getInstance();  //获取当前时间，作为图标的名字
    private String year= String.valueOf(calendar.get(Calendar.YEAR));
    private String state="";

    public void setYear(String year) {
        this.year = year;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public DataSource<Integer, MisAnnualLeaveData.AnnualLeaveBean> create() {
        MisAnnualLeaveDataSource dataSource = new MisAnnualLeaveDataSource(year,state);
        annualLeaveDataSource.postValue(dataSource);
        return dataSource;
    }
}
