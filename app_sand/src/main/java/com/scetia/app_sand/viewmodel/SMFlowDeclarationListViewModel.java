package com.scetia.app_sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.app_sand.adapter.SMFlowDeclarationListAdapter;
import com.scetia.app_sand.bean.FlowDeclarationBean;
import com.scetia.app_sand.service.BuildSandApiService;

import java.util.HashMap;
import java.util.List;

/**
 * created by ljs
 * on 2020/12/24
 */
public class SMFlowDeclarationListViewModel extends BaseViewModel {
    private static final int DEFAULT_PAGE_INDEX = 0;
    private int currentPageIndex = DEFAULT_PAGE_INDEX;
    private static final String DEFAULT_QUERY_KEY = "未委托";
    private MutableLiveData<List<FlowDeclarationBean>> flowDeclarationLiveData;
    public SMFlowDeclarationListViewModel(@NonNull Application application) {
        super(application);
    }
    private SMFlowDeclarationListAdapter listAdapter;
    private String queryKey =DEFAULT_QUERY_KEY;

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
     * 按条件查询
     * @param queryKey
     */
    public void setQueryKey(String queryKey){
        this.queryKey = queryKey;
        currentPageIndex = DEFAULT_PAGE_INDEX;
        loadData(queryKey,true);
    }
    /**
     * 刷新数据
     */
    public void refresh(){
        currentPageIndex = DEFAULT_PAGE_INDEX;
        loadData(queryKey,false);
    }
    public void loadMoreData(){
        loadData(queryKey,false);
    }


    /**
     * 加载数据
     */
    public void loadData(String queryKey,boolean isInit){
        if(currentPageIndex==DEFAULT_PAGE_INDEX&&isInit){
            //第一页初始化
            loadState.setValue(LoadState.LOAD_INIT);
        }
        HashMap<String,Object> params = new HashMap<>();
        if(queryKey!=null){
            params.put("flowInfoStatus",queryKey);
        }
        params.put("pageSize",20);
        params.put("pageIndex",currentPageIndex);
        params.put("orderBy","createDatetime desc");

        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getSMFlowDeclarations(params)
                .compose(BuildSandRetrofit.<BuildSandResultData<List<FlowDeclarationBean>>, List<FlowDeclarationBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<FlowDeclarationBean>>() {
                    @Override
                    public void success(List<FlowDeclarationBean> flowDeclarationBeans) {
                        if(flowDeclarationBeans.size()==0){
                            loadState.setValue(currentPageIndex==DEFAULT_PAGE_INDEX?LoadState.EMPTY :LoadState.NO_MORE);
                        }else{
                            loadState.setValue(currentPageIndex==DEFAULT_PAGE_INDEX?LoadState.LOAD_INIT_SUCCESS:LoadState.SUCCESS);
                            currentPageIndex++;
                        }
                        flowDeclarationLiveData.postValue(flowDeclarationBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }

    /**
     * 删除指定的流转申报记录
     */
    public void delFlowDeclaration(String flowId,int position){
        loadState.postValue(LoadState.LOADING.setMessage("正在删除..."));
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .delSMFlowDeclaration(flowId)
                .compose(BuildSandRetrofit.applyTransformer())
                .subscribe(new ResponseObserver<Object>() {
                    @Override
                    public void success(Object o) {
                        loadState.postValue(LoadState.SUCCESS);
                        listAdapter.remove(position);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE.setMessage("删除失败！"));
                    }
                });

    }
}
