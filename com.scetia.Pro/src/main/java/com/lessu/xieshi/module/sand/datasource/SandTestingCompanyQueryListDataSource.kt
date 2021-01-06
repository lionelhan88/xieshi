package com.lessu.xieshi.module.sand.datasource

import com.lessu.xieshi.base.BasePageKedDataSource2
import com.lessu.data.LoadState
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle
import com.lessu.xieshi.http.ResponseObserver
import com.lessu.xieshi.module.sand.bean.TestingCompanyBean
import com.lessu.xieshi.module.sand.repository.SandTestingCompanyQueryListRepository

/**
 * created by ljs
 * on 2020/12/21
 */
class SandTestingCompanyQueryListDataSource(private val queryCounties:String, private val queryUnitKey:String,
                                            private val repository: SandTestingCompanyQueryListRepository) :
        BasePageKedDataSource2<TestingCompanyBean>() {
    override fun loadInitData(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TestingCompanyBean>) {
        loadState.postValue(LoadState.LOAD_INIT)
        repository.queryTestingCompanies(params.requestedLoadSize,0,queryCounties,queryUnitKey,
                object :ResponseObserver<List<TestingCompanyBean>>(){
                    override fun success(t: List<TestingCompanyBean>?) {
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

    override fun loadAfterData(params: LoadParams<Int>, callback: LoadCallback<Int, TestingCompanyBean>) {
        loadState.postValue(LoadState.LOADING)
        repository.queryTestingCompanies(params.requestedLoadSize,params.key,queryCounties,queryUnitKey,
                object :ResponseObserver<List<TestingCompanyBean>>(){
                    override fun success(t: List<TestingCompanyBean>?) {
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