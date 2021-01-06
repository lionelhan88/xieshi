package com.lessu.xieshi.module.sand.datasource

import com.lessu.xieshi.base.BasePageKedDataSource2
import com.lessu.data.LoadState
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle
import com.lessu.xieshi.http.ResponseObserver
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean
import com.lessu.xieshi.module.sand.repository.SandSalesQueryListRepository

/**
 * created by ljs
 * on 2020/12/21
 */
class SandSalesQueryListDataSource(private val queryUnitType:String,private val queryUnitKey:String,
                                   private val sandSalesRepository: SandSalesQueryListRepository) : BasePageKedDataSource2<SandSalesTargetBean>() {
    override fun loadInitData(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, SandSalesTargetBean>) {
        loadState.postValue(LoadState.LOAD_INIT)
         sandSalesRepository.querySales(params.requestedLoadSize,0,queryUnitType,queryUnitKey,
                object :ResponseObserver<List<SandSalesTargetBean>>(){
                    override fun success(t: List<SandSalesTargetBean>?) {
                        if(t?.size==0){
                            loadState.postValue(LoadState.EMPTY)
                        }else{
                            loadState.postValue(LoadState.SUCCESS)
                            callback.onResult(t!!,null,1)
                        }

                    }

                    override fun failure(throwable: ExceptionHandle.ResponseThrowable?) {
                        loadState.postValue(LoadState.FAILURE)
                        retry = {loadInitData(params,callback)}
                    }
                })
    }

    override fun loadAfterData(params: LoadParams<Int>, callback: LoadCallback<Int, SandSalesTargetBean>) {
        loadState.postValue(LoadState.LOADING)
        sandSalesRepository.querySales(params.requestedLoadSize,params.key,queryUnitType,queryUnitKey,
                object :ResponseObserver<List<SandSalesTargetBean>>(){
                    override fun success(t: List<SandSalesTargetBean>?) {
                        if(t?.size==0){
                            loadState.postValue(LoadState.NO_MORE)
                        }else{
                            loadState.postValue(LoadState.SUCCESS)
                            callback.onResult(t!!,params.key+1)
                        }

                    }

                    override fun failure(throwable: ExceptionHandle.ResponseThrowable?) {
                        loadState.postValue(LoadState.FAILURE)
                        retry = {loadAfterData(params,callback)}
                    }
                })
    }
}