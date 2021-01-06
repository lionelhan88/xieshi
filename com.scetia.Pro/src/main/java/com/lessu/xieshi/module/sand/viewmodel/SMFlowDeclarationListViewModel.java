package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.data.LoadState;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.BuildSandRetrofit;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.module.sand.adapter.SMFlowDeclarationListAdapter;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/24
 */
public class SMFlowDeclarationListViewModel extends BaseViewModel {
    private static final int DEFAULT_PAGE_INDEX = 0;
    private int currentPageIndex = DEFAULT_PAGE_INDEX;
    private MutableLiveData<LoadState> loadDataState;
    private MutableLiveData<List<FlowDeclarationBean>> flowDeclarationLiveData;
    public SMFlowDeclarationListViewModel(@NonNull Application application) {
        super(application);
    }
    private SMFlowDeclarationListAdapter listAdapter;
    public void setPageIndex(int pageIndex){
        currentPageIndex = pageIndex;
    }

    public MutableLiveData<LoadState> getLoadDataState() {
        if(loadDataState==null){
            loadDataState = new MutableLiveData<>();
        }
        return loadDataState;
    }

    public MutableLiveData<List<FlowDeclarationBean>> getFlowDeclarationLiveData() {
        if(flowDeclarationLiveData==null){
            flowDeclarationLiveData= new MutableLiveData<>();
        }
        return flowDeclarationLiveData;
    }

    public SMFlowDeclarationListAdapter getListAdapter() {
        if(listAdapter==null){
            listAdapter = new SMFlowDeclarationListAdapter();
        }
        return listAdapter;
    }

    /**
     * 刷新数据
     */
    public void refresh(){
        currentPageIndex = DEFAULT_PAGE_INDEX;
        loadData(false);
    }

    /**
     * 加载数据
     */
    public void loadData(boolean isInit){
        if(currentPageIndex==DEFAULT_PAGE_INDEX&&isInit&&getFlowDeclarationLiveData().getValue()==null){
            //第一页初始化
            loadDataState.postValue(LoadState.LOAD_INIT);
        }
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getSMFlowDeclarations(20,currentPageIndex,"createDatetime")
                .compose(BuildSandRetrofit.<BuildSandResultData<List<FlowDeclarationBean>>, List<FlowDeclarationBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<FlowDeclarationBean>>() {
                    @Override
                    public void success(List<FlowDeclarationBean> flowDeclarationBeans) {
                        if(flowDeclarationBeans.size()==0){
                            loadDataState.postValue(currentPageIndex==DEFAULT_PAGE_INDEX?LoadState.EMPTY :LoadState.NO_MORE);
                        }else{
                            loadDataState.postValue(currentPageIndex==DEFAULT_PAGE_INDEX?LoadState.LOAD_INIT_SUCCESS:LoadState.SUCCESS);
                            currentPageIndex++;
                        }
                        flowDeclarationLiveData.postValue(flowDeclarationBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadDataState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }

    /**
     * 删除指定的流转申报记录
     */
    public void delFlowDeclaration(String flowId,int position){
        loadState.postValue(LoadState.LOADING);
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .delSMFlowDeclaration(flowId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<ResponseBody>>() {
                    @Override
                    public void success(Response<ResponseBody> responseBodyResponse) {
                        if(responseBodyResponse.code()==204) {
                            loadState.postValue(LoadState.SUCCESS);
                            listAdapter.remove(position);
                        }else{
                            loadState.postValue(LoadState.FAILURE);
                            throwableLiveData.postValue(new ExceptionHandle.ResponseThrowable(ExceptionHandle.LOCAL_ERROR,"删除失败！"));
                        }
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });

    }
}
