package com.scetia.app_sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.app_sand.adapter.TestingParametersListAdapter;
import com.scetia.app_sand.bean.FlowDeclarationBean;
import com.scetia.app_sand.bean.SandItemParameterBean;
import com.scetia.app_sand.bean.SandSampleBean;
import com.scetia.app_sand.bean.SandSpecBean;
import com.scetia.app_sand.repository.SMFlowDeclarationDetailRepository;
import com.scetia.app_sand.util.GsonUtil;

import java.util.List;
import java.util.Map;

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

    private FlowDeclarationBean flowDeclarationBean;
    private FlowDeclarationBean oldFlowDeclarationBean;

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
            cloneOld(flowDeclarationBean);
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
        loadState.setValue(LoadState.LOADING);
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
                cloneOld(flowDeclarationBean);
                flowDeclarationLiveData.setValue(flowDeclarationBean);
                loadSandItemParameters(flowDeclarationBean.getSampleID());
                loadState.setValue(LoadState.SUCCESS.setCode(200).setMessage("加载数据成功"));
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
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
        loadState.setValue(LoadState.LOADING);
        //建设用砂样品名称
        repository.getSandSamples(new ResponseObserver<List<SandSampleBean>>() {
            @Override
            public void success(List<SandSampleBean> sandSampleBeans) {
                loadState.setValue(LoadState.SUCCESS.setCode(200).setMessage("加载数据成功"));
                sanSampleLiveData.postValue(sandSampleBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
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
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }

    /**
     * 保存流向申报
     */
    public void saveFlowDeclaration() {
        loadState.setValue(LoadState.LOADING.setMessage("正在提交..."));
        if(flowDeclarationBean.getId()==null){
            repository.saveFlowDeclaration(flowDeclarationBean, new ResponseObserver<FlowDeclarationBean>() {
                @Override
                public void success(FlowDeclarationBean bean) {
                    //保存流向申报成功，赋值返回的id
                    flowDeclarationBean.setId(bean.getId());
                    //提交成功后，也要赋值给旧对象，用来比较是否有信息更改
                    cloneOld(flowDeclarationBean);
                    //提交成功
                    loadState.setValue(LoadState.SUCCESS.setCode(204).setMessage("提交成功"));
                }

                @Override
                public void failure(ExceptionHandle.ResponseThrowable throwable) {
                  loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
                }
            });
        }else{
           repository.updateFlowDeclaration(flowDeclarationBean, new ResponseObserver<Object>() {
               @Override
               public void success(Object o) {
                   cloneOld(flowDeclarationBean);
                   loadState.setValue(LoadState.SUCCESS.setCode(204).setMessage("提交成功"));
               }

               @Override
               public void failure(ExceptionHandle.ResponseThrowable throwable) {
                   loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
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

    /**
     * 是否编辑过信息，提示用户是否需要保存
     * @return
     */
    public boolean isEdit(){
        return flowDeclarationBean.equals(oldFlowDeclarationBean);
    }

    private void cloneOld(FlowDeclarationBean flowDeclarationBean){
        String s = GsonUtil.toJsonStr(flowDeclarationBean);
        oldFlowDeclarationBean = GsonUtil.JsonToObject(s,FlowDeclarationBean.class);
    }
}
