package com.lessu.xieshi.module.mis.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.lessu.xieshi.module.mis.bean.MisAnnualLeaveData
import com.lessu.xieshi.module.mis.model.MisAnnualLeaveRepository
import com.scetia.Pro.baseapp.uitls.LoadState
import com.scetia.Pro.common.Util.Constants
import com.scetia.Pro.network.bean.ExceptionHandle
import com.scetia.Pro.network.conversion.ResponseObserver

/**
 * created by ljs
 * on 2020/11/30
 */
class MisAnnualLeaveDataSource(private val year: String, private val state: String) : PageKeyedDataSource<Int, MisAnnualLeaveData.AnnualLeaveBean>() {
    private val repository: MisAnnualLeaveRepository by lazy {
        MisAnnualLeaveRepository()
    }
    val loadState = MutableLiveData<LoadState>()
    val annualLeaveData = MutableLiveData<MisAnnualLeaveData>()
    private var totalPage = 0;
    var retry:(()->Any)?=null
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MisAnnualLeaveData.AnnualLeaveBean>) {
        retry = null
        loadState.postValue(LoadState.LOAD_INIT)
        repository.getAnnualLeaveData(Constants.User.GET_TOKEN(), year, state, 1, params.requestedLoadSize, object : ResponseObserver<MisAnnualLeaveData>() {
            override fun success(t: MisAnnualLeaveData?) {
                //初始化数据返回成功
                loadState.postValue(LoadState.SUCCESS)
                totalPage = t?.pageCount!!
                annualLeaveData.postValue(t)
                callback.onResult(t.listContent,null,2)
            }

            override fun failure(throwable: ExceptionHandle.ResponseThrowable?) {
                if (throwable?.code == 2000) {
                    //没有相关数据
                    loadState.postValue(LoadState.EMPTY)
                } else {
                    loadState.postValue(LoadState.FAILURE)
                    //加载失败，记录当前状态
                    retry = { loadInitial(params, callback) }
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MisAnnualLeaveData.AnnualLeaveBean>) {
        if(params.key>totalPage){
            //没有更多数据了
            loadState.postValue(LoadState.NO_MORE)
            return
        }
        retry = null
        //加载下一页
        loadState.postValue(LoadState.LOADING)
        repository.getAnnualLeaveData(Constants.User.GET_TOKEN(), year, state, params.key, params.requestedLoadSize, object : ResponseObserver<MisAnnualLeaveData>() {
            override fun success(t: MisAnnualLeaveData?) {
                loadState.postValue(LoadState.SUCCESS)
                annualLeaveData.postValue(t)
                callback.onResult(t?.listContent!!,params.key+1)
            }

            override fun failure(throwable: ExceptionHandle.ResponseThrowable?) {
                loadState.postValue(LoadState.FAILURE)
                retry = {loadAfter(params,callback)}
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MisAnnualLeaveData.AnnualLeaveBean>) {
    }
}