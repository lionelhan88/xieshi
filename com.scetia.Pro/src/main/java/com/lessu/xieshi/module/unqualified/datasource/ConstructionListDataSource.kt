package com.lessu.xieshi.module.unqualified.datasource

import com.google.gson.Gson
import com.lessu.xieshi.base.BasePageKedDataSource
import com.lessu.data.LoadState
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle
import com.lessu.xieshi.http.ResponseObserver
import com.lessu.xieshi.http.XSResultData
import com.lessu.xieshi.http.XSRetrofit
import com.lessu.xieshi.http.api.CommonApiService
import com.lessu.xieshi.module.unqualified.bean.ConstructionData
import org.json.JSONObject
import kotlin.collections.HashMap

/**
 * created by ljs
 * on 2020/12/15
 */
class ConstructionListDataSource(private val hashMap: HashMap<String,Any>) : BasePageKedDataSource<ConstructionData.ConstructionBean>() {
    override fun loadInitData(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ConstructionData.ConstructionBean>) {
        loadState.postValue(LoadState.LOAD_INIT)
        val jsonObj = JSONObject(Gson().toJson(hashMap))
        jsonObj.put("CurrentPageNo", 1)
        jsonObj.put("PageSize", params.requestedLoadSize)
        //TODO:请求工程数据列表
        XSRetrofit.getInstance().getService(CommonApiService::class.java)
                .getUnqualifiedConstructionData(jsonObj.toString())
                .compose(XSRetrofit.applyTransformer<XSResultData<ConstructionData>,ConstructionData>())
                .subscribe(object : ResponseObserver<ConstructionData>(){
                    override fun success(t: ConstructionData?) {
                        if(t?.listContent==null){
                            loadState.postValue(LoadState.EMPTY)
                            return
                        }
                        loadState.postValue(LoadState.SUCCESS)
                        totalPage = t.pageCount
                        callback.onResult(t.listContent,null,2)
                    }

                    override fun failure(throwable: ExceptionHandle.ResponseThrowable?) {
                        if (throwable?.code == 2000) {
                            loadState.postValue(LoadState.EMPTY)
                        } else {
                            loadState.postValue(LoadState.FAILURE)
                            retry = { loadInitData(params, callback) }
                        }
                    }
                })
    }

    override fun loadAfterData(params: LoadParams<Int>, callback: LoadCallback<Int, ConstructionData.ConstructionBean>) {
        loadState.postValue(LoadState.LOADING)
        val jsonObj =  JSONObject(Gson().toJson(hashMap))
        jsonObj.put("CurrentPageNo", params.key)
        jsonObj.put("PageSize", params.requestedLoadSize)
        //TODO:请求下一页工程数据列表
        XSRetrofit.getInstance().getService(CommonApiService::class.java)
                .getUnqualifiedConstructionData(jsonObj.toString())
                .compose(XSRetrofit.applyTransformer<XSResultData<ConstructionData>,ConstructionData>())
                .subscribe(object : ResponseObserver<ConstructionData>(){
                    override fun success(t: ConstructionData?) {
                        if(t?.listContent==null){
                            loadState.postValue(LoadState.NO_MORE)
                            return
                        }
                        loadState.postValue(LoadState.SUCCESS)
                        totalPage = t.pageCount
                        callback.onResult(t.listContent,params.key+1)

                    }

                    override fun failure(throwable: ExceptionHandle.ResponseThrowable?) {
                        loadState.postValue(LoadState.FAILURE)
                        retry = { loadAfterData(params, callback) }
                    }
                })
    }
}