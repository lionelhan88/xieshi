package com.scetia.Pro.baseapp.basepage

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.scetia.Pro.baseapp.uitls.LoadState

/**
 * created by ljs
 * on 2020/12/15
 */
abstract class BasePageKedDataSource<T> : PageKeyedDataSource<Int, T>() {
    val loadState = MutableLiveData<LoadState>()
    protected var totalPage = 0;
    var retry:(()->Any)?=null
    abstract fun loadInitData(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>)
    abstract fun loadAfterData(params: LoadParams<Int>, callback: LoadCallback<Int, T>)
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        //初始化加载
        retry = null
        loadInitData(params,callback)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        retry = null
        if(params.key>totalPage){
            //没有更多数据了
            loadState.postValue(LoadState.NO_MORE)
            return
        }
        loadAfterData(params,callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
    }
}