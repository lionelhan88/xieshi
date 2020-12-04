package com.lessu.xieshi.module.sand.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * created by ljs
 * on 2020/11/9
 */
public class TestingCompanyManageViewModel extends ViewModel {
    /**
     * 当前页面是否时查询状态，并且保存当前状态
     */
    private MutableLiveData<Boolean> isSearchData = new MutableLiveData<>(false);


    public LiveData<Boolean> getIsSearchData() {
        return isSearchData;
    }



    /**
     * 设置当前状态
     * @param search
     */
    public void setIsSearchData(boolean search) {
        isSearchData.postValue(search);
    }

}
