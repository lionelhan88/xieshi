package com.lessu.xieshi.module.unqualified.datasource

import com.google.gson.Gson
import com.lessu.xieshi.base.BasePageKedDataSource
import com.lessu.data.LoadState
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle
import com.lessu.xieshi.http.ResponseObserver
import com.lessu.xieshi.http.XSResultData
import com.lessu.xieshi.http.XSRetrofit
import com.lessu.xieshi.http.api.CommonApiService
import com.lessu.xieshi.module.unqualified.bean.TestingReportData
import org.json.JSONObject
import kotlin.collections.HashMap

/**
 * created by ljs
 * on 2020/12/15
 */
class TestingReportListDataSource(private val hashMap: HashMap<String,Any>) : BasePageKedDataSource<TestingReportData.TestingReportBean>() {

    override fun loadInitData(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TestingReportData.TestingReportBean>) {
        loadState.postValue(LoadState.LOAD_INIT)
        val jsonObj = JSONObject(Gson().toJson(hashMap))
        jsonObj.put("CurrentPageNo", 1)
        jsonObj.put("PageSize", params.requestedLoadSize)
        XSRetrofit.getInstance().getService(CommonApiService::class.java)
                .getUnqualifiedTestingReportData(jsonObj.toString())
                .compose(XSRetrofit.applyTransformer<XSResultData<TestingReportData>, TestingReportData>())
                .subscribe(object : ResponseObserver<TestingReportData>() {
                    override fun success(t: TestingReportData?) {
                        if(t?.listContent==null){
                            loadState.postValue(LoadState.EMPTY)
                            return
                        }
                        loadState.postValue(LoadState.SUCCESS)
                        totalPage = t.pageCount
                        callback.onResult(t.listContent, null, 2)
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

    override fun loadAfterData(params: LoadParams<Int>, callback: LoadCallback<Int, TestingReportData.TestingReportBean>) {
        //加载下一页
        loadState.postValue(LoadState.LOADING)
        val jsonObj =  JSONObject(Gson().toJson(hashMap))
        jsonObj.put("CurrentPageNo", params.key)
        jsonObj.put("PageSize", params.requestedLoadSize)
        XSRetrofit.getInstance().getService(CommonApiService::class.java)
                .getUnqualifiedTestingReportData(jsonObj.toString())
                .compose(XSRetrofit.applyTransformer<XSResultData<TestingReportData>, TestingReportData>())
                .subscribe(object : ResponseObserver<TestingReportData>() {
                    override fun success(t: TestingReportData?) {
                        if(t?.listContent==null){
                            loadState.postValue(LoadState.NO_MORE)
                            return
                        }
                        loadState.postValue(LoadState.SUCCESS)
                        totalPage = t.pageCount
                        callback.onResult(t.listContent, params.key + 1)
                    }

                    override fun failure(throwable: ExceptionHandle.ResponseThrowable?) {
                        loadState.postValue(LoadState.FAILURE)
                        retry = { loadAfterData(params, callback) }
                    }
                })
    }
}