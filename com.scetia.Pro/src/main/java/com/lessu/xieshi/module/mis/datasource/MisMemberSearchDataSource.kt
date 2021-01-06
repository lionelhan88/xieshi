package com.lessu.xieshi.module.mis.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.lessu.xieshi.http.ResponseObserver
import com.lessu.data.LoadState
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle.ResponseThrowable
import com.lessu.xieshi.module.mis.activitys.Content
import com.lessu.xieshi.module.mis.bean.MisMemberSearchResultData
import com.lessu.xieshi.module.mis.model.MisMemberSearchRepository

/**
 * created by ljs
 * on 2020/11/30
 */
class MisMemberSearchDataSource(private val queryKey: String?) : PageKeyedDataSource<Int, MisMemberSearchResultData.ListContentBean>() {
    //记录当前加载的数据加
    // 载的状态，并把状态传递到dataSourceFactory
    private val loadState = MutableLiveData<LoadState>()
    private val model = MisMemberSearchRepository()
    private var totalPage = 0
    var retry: (() -> Any)? = null
    fun getLoadState(): MutableLiveData<LoadState>? {
        return loadState
    }

    override fun loadInitial(params: LoadInitialParams<Int?>, callback: LoadInitialCallback<Int?, MisMemberSearchResultData.ListContentBean?>) {
        loadState.postValue(LoadState.LOAD_INIT)
        retry = null
        //初始化数据加载
        model.search(Content.getToken(), queryKey, 1, params.requestedLoadSize, object : ResponseObserver<MisMemberSearchResultData?>() {
            override fun success(misMemberSearchResultData: MisMemberSearchResultData?) {
                loadState.postValue(LoadState.SUCCESS)
                val list = misMemberSearchResultData?.listContent!!
                totalPage = misMemberSearchResultData.pageCount
                callback.onResult(list, null, 2)
            }

            override fun failure(throwable: ResponseThrowable) {
                if (throwable.code == 2000) {
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

    override fun loadBefore(params: LoadParams<Int?>, callback: LoadCallback<Int?, MisMemberSearchResultData.ListContentBean?>) {
        //暂时不用实现，我们不需要加载前一页
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int?, MisMemberSearchResultData.ListContentBean?>) {
        //加载后一页
        //如果当前要加载的页数大于总页数，说明数据已经没有了，通知UI更新“没有更多数据了”
        if (params.key > totalPage) {
            loadState.postValue(LoadState.NO_MORE)
            return;
        }
        loadState.postValue(LoadState.LOADING)
        retry = null
        model.search(Content.getToken(), queryKey, params.key, params.requestedLoadSize, object : ResponseObserver<MisMemberSearchResultData?>() {
            override fun success(t: MisMemberSearchResultData?) {
                loadState.postValue(LoadState.SUCCESS)
                val list = t?.listContent!!
                //加载下一页
                callback.onResult(list, params.key + 1)
            }

            override fun failure(throwable: ResponseThrowable) {
                loadState.postValue(LoadState.FAILURE)
                retry = { loadAfter(params, callback) }
            }
        })
    }
}