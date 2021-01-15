package com.scetia.Pro.network.response;

/**
 * created by ljs
 * on 2021/1/11
 */
public  class CommonResultData<T> implements IResultData<T> {
    private IResultData<T> iResultData;

    public CommonResultData(IResultData<T> iResultData) {
        this.iResultData = iResultData;
    }

    @Override
    public T handleData() {
        return iResultData.handleData();
    }
}
