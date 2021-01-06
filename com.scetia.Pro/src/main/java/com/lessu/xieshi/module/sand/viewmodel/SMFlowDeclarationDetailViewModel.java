package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.lessu.data.LoadState;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.xieshi.bean.LoadMoreState;
import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.BuildSandRetrofit;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.http.api.SourceApiService;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.module.sand.adapter.TestingParametersListAdapter;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.bean.SandItemParameterBean;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;
import com.lessu.xieshi.module.sand.bean.SandSampleBean;
import com.lessu.xieshi.module.sand.bean.SandSpecBean;
import com.lessu.xieshi.module.sand.bean.SandSupplierBean;
import com.lessu.xieshi.module.sand.repository.SMFlowDeclarationDetailRepository;

import org.intellij.lang.annotations.Flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/24
 */
public class SMFlowDeclarationDetailViewModel extends BaseViewModel {
    private SMFlowDeclarationDetailRepository repository  = new SMFlowDeclarationDetailRepository();
    //保存当前获取的建设用砂样品列表数据
    private MutableLiveData<List<SandSampleBean>> sanSampleLiveData = new MutableLiveData<>();
    //保存当前获取的建设用砂样品检测参数列表
    private MutableLiveData<List<SandItemParameterBean>> sandItemParameterLiveData = new MutableLiveData<>();
    //保存当前获取的建设用砂规格列表
    private MutableLiveData<List<SandSpecBean>> sandSpecLiveData = new MutableLiveData<>();
    //保存当前选中的样品规格
    private MutableLiveData<SandSpecBean> curSandSpecBean = new MutableLiveData<>(new SandSpecBean("",""));
    private MutableLiveData<String> curParameterNames = new MutableLiveData<>("");
    //样品规格的map
    private Map<String,SandSpecBean> sandSpecBeanMap;
    //样品检测参数map
    private Map<String, SandItemParameterBean> sandParameterMap;

    private TestingParametersListAdapter listAdapter;

    //当前操作加载状态
    private LoadMoreState moreStateBean = new LoadMoreState();

    private FlowDeclarationBean flowDeclarationBean;

    private MutableLiveData<FlowDeclarationBean> flowDeclarationLiveData = new MutableLiveData<>();

    public SMFlowDeclarationDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void setFlowDeclarationBean(FlowDeclarationBean flowDeclarationBean) {
        this.flowDeclarationBean = flowDeclarationBean;
    }
    public TestingParametersListAdapter getListAdapter() {
        if(listAdapter==null){
            listAdapter = new TestingParametersListAdapter();
        }
        return listAdapter;
    }
    public FlowDeclarationBean getFlowDeclarationBean() {
        if (flowDeclarationBean == null) {
            flowDeclarationBean = new FlowDeclarationBean();
        }
        return flowDeclarationBean;
    }

    public MutableLiveData<List<SandSampleBean>> getSanSampleLiveData() {
        return sanSampleLiveData;
    }

    public MutableLiveData<List<SandItemParameterBean>> getSandItemParameterLiveData() {
        return sandItemParameterLiveData;
    }

    public MutableLiveData<List<SandSpecBean>> getSandSpecLiveData() {
        return sandSpecLiveData;
    }


    public MutableLiveData<SandSpecBean> getCurSandSpecBean() {
        return curSandSpecBean;
    }

    public MutableLiveData<FlowDeclarationBean> getFlowDeclarationLiveData() {
        return flowDeclarationLiveData;
    }

    public MutableLiveData<String> getCurParameterNames() {
        return curParameterNames;
    }

    public Map<String, SandItemParameterBean> getSandParameterMap() {
        return sandParameterMap;
    }

    //获取建设用砂流向申报的详细信息
    public void loadInitFlowDeclaration(String flowId){
        moreStateBean.loadState = LoadState.LOADING;
        moreStateBean.loadType = 0;
        loadMoreState.postValue(moreStateBean);
        repository.getFlowDeclarationInfo(flowId, new ResponseObserver<FlowDeclarationBean>() {
            @Override
            public void success(FlowDeclarationBean bean) {
                flowDeclarationBean.setPutOnRecordsPassport(bean.getPutOnRecordsPassport());
                flowDeclarationBean.setProductionUnitName(bean.getProductionUnitName());
                flowDeclarationBean.setTerminalName(bean.getTerminalName());
                flowDeclarationBean.setSpecID(bean.getSpecID());
                flowDeclarationBean.setSampleID(bean.getSampleID());
                flowDeclarationBean.setParameterIDs(bean.getParameterIDs());
                flowDeclarationBean.setCustomerUnitMemberCode(bean.getCustomerUnitMemberCode());
                flowDeclarationBean.setSalesVolumePost(bean.getSalesVolume());
                flowDeclarationLiveData.setValue(flowDeclarationBean);
                loadSandItemParameters(flowDeclarationBean.getSampleID());
                moreStateBean.loadState = LoadState.SUCCESS;
                loadMoreState.postValue(moreStateBean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                moreStateBean.loadState = LoadState.FAILURE;
                loadMoreState.postValue(moreStateBean);
                throwableLiveData.postValue(throwable);
            }
        });
    }
    /**
     * 加载建设用砂样品数据
     */
    public void loadSandItemSamples() {
        if (sanSampleLiveData.getValue() != null) {
            return;
        }
        moreStateBean.loadState = LoadState.LOADING;
        moreStateBean.loadType = 0;
        loadMoreState.postValue(moreStateBean);
        //建设用砂样品名称
        repository.getSandSamples(new ResponseObserver<List<SandSampleBean>>() {
            @Override
            public void success(List<SandSampleBean> sandSampleBeans) {
                moreStateBean.loadState = LoadState.SUCCESS;
                loadMoreState.postValue(moreStateBean);
                sanSampleLiveData.postValue(sandSampleBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                moreStateBean.loadState = LoadState.FAILURE;
                loadMoreState.postValue(moreStateBean);
                throwableLiveData.postValue(throwable);
            }
        });
    }

    /**
     * 获取样品检测参数列表和样品规格列表
     *
     * @param sampleId 样品名称id
     */
    public void loadSandItemParameters(String sampleId) {
        repository.getSpecAndParameters(sampleId, new ResponseObserver<List<? extends Object>>() {
            @Override
            public void success(List<?> responseList) {
                if (responseList.size() > 0) {
                    String name = responseList.get(0).getClass().getName();
                    if (SandItemParameterBean.class.getName().equals(name)) {
                        List<SandItemParameterBean> list = (List<SandItemParameterBean>) responseList;
                        sandItemParameterLiveData.setValue(list);
                        handleParameter(list);
                    } else if (SandSpecBean.class.getName().equals(name)) {
                        List<SandSpecBean> list = (List<SandSpecBean>) responseList;
                        sandSpecLiveData.postValue(list);
                        handleSpec(list);
                    }
                }
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                throwableLiveData.postValue(throwable);
            }
        });
    }

    /**
     * 保存流向申报
     */
    public void saveFlowDeclaration() {
        moreStateBean.loadState = LoadState.LOADING;
        moreStateBean.loadType = 1;
        loadMoreState.postValue(moreStateBean);
        if(flowDeclarationBean.getId()==null){
            repository.saveFlowDeclaration(flowDeclarationBean, new ResponseObserver<FlowDeclarationBean>() {
                @Override
                public void success(FlowDeclarationBean bean) {
                    //保存流向申报成功，赋值返回的id
                    flowDeclarationBean.setId(bean.getId());
                    //提交成功
                    moreStateBean.loadState = LoadState.SUCCESS;
                    loadMoreState.postValue(moreStateBean);
                }

                @Override
                public void failure(ExceptionHandle.ResponseThrowable throwable) {
                    moreStateBean.loadState = LoadState.FAILURE;
                    loadMoreState.postValue(moreStateBean);
                    //提交失败，错误信息展示到前台给用户
                    throwableLiveData.postValue(throwable);
                }
            });
        }else{
           repository.updateFlowDeclaration(flowDeclarationBean, new ResponseObserver<Response<ResponseBody>>() {
               @Override
               public void success(Response<ResponseBody> responseBodyResponse) {
                   if(responseBodyResponse.code()==204){
                       //提交成功
                       moreStateBean.loadState = LoadState.SUCCESS;
                   }else{
                       moreStateBean.loadState = LoadState.FAILURE;
                   }
                   loadMoreState.postValue(moreStateBean);

               }

               @Override
               public void failure(ExceptionHandle.ResponseThrowable throwable) {
                   moreStateBean.loadState = LoadState.FAILURE;
                   loadMoreState.postValue(moreStateBean);
                   //提交失败，错误信息展示到前台给用户
                   throwableLiveData.postValue(throwable);
               }
           });
        }
    }

    /**
     * 编辑状态的话，解析parametersId，获取检测参数名称显示
     * @param list 检测参数集合
     */
    private void handleParameter(List<SandItemParameterBean> list){
        //解析parameterIds,转换未对象集合
        sandParameterMap = SandItemParameterBean.parseToMap(list);
        StringBuilder sb = new StringBuilder();
        if(flowDeclarationBean.getParameterIDs()!=null&&!flowDeclarationBean.getParameterIDs().equals("")){
            for (String s:flowDeclarationBean.getParameterIDs().split(";")){
                String parameterName = sandParameterMap.get(s).getParameterName();
                sb.append(parameterName).append(";");
            }
            curParameterNames.setValue(sb.toString());
        }
    }

    /**
     * 编辑状态的话，解析specId，获取检测规格名称
     * @param list 检测规格集合
     */
    private void handleSpec(List<SandSpecBean> list){
        sandSpecBeanMap = SandSpecBean.parseToMap(list);
        if(flowDeclarationBean.getId()!=null){
            //当前未编辑状态，进入页面需要显示当前的样品规格
            SandSpecBean bean = sandSpecBeanMap.get(flowDeclarationBean.getSpecID());
            if(bean==null){
                bean = new SandSpecBean("","");
            }
            curSandSpecBean.postValue(bean);
        }
    }
}
