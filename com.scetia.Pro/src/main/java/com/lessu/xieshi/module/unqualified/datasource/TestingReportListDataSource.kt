package com.lessu.xieshi.module.unqualified.datasource

import com.google.gson.Gson
import com.scetia.Pro.baseapp.datasource.BasePageKedDataSource
import com.scetia.Pro.baseapp.uitls.LoadState
import com.scetia.Pro.network.conversion.ResponseObserver
import com.scetia.Pro.network.bean.XSResultData
import com.scetia.Pro.network.manage.XSRetrofit
import com.lessu.xieshi.http.service.CommonApiService
import com.lessu.xieshi.module.unqualified.bean.TestingReportData
import com.scetia.Pro.network.bean.ExceptionHandle
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