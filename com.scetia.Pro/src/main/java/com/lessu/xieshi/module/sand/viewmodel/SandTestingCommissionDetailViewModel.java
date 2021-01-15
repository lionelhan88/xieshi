package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.lessu.xieshi.module.sand.bean.TestingCompanyBean;
import com.lessu.xieshi.module.sand.bean.TestingQueryResultBean;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.DateUtil;
import com.lessu.xieshi.base.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadMoreState;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.http.api.SourceApiService;
import com.lessu.xieshi.lifcycle.BaiduMapLifecycle;
import com.lessu.xieshi.module.sand.adapter.TakePhotosAdapter;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.bean.SandSamplerBean;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.scetia.Pro.network.manage.BuildSandRetrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/30
 */
public class SandTestingCommissionDetailViewModel extends BaseViewModel {
    public static final int SUPPLIER_INIT = 2;
    public static final int TESTING_INIT = 1;
    private TestingCommissionBean testingCommissionBean = new TestingCommissionBean();
    private MutableLiveData<LoadState> loadDataState = new MutableLiveData<>();
    private MutableLiveData<LoadState> postDataState = new MutableLiveData<>();

    //保存当前的委托单位（流向申报）
    private MutableLiveData<FlowDeclarationBean> curCommission;
    //保存当前选择的检测单位
    private MutableLiveData<AddedTestingCompanyBean> testingCompany;
    //当前选择的取样地点
    private MutableLiveData<Integer> curSampleLocation;
    //当前选择的取样人员
    private MutableLiveData<SandSamplerBean> curSampler;
    //取样人数据列表
    private List<SandSamplerBean> samplerBeans = new ArrayList<>();
    //预约取样时间
    private MutableLiveData<String> orderSampleDate;
    //取样日期
    private MutableLiveData<String> sampleDate;
    //委托人
    private MutableLiveData<String> commissionUser;
    //委托日期
    private MutableLiveData<String> commissionDate;
    //唯一性标识号
    private MutableLiveData<String> ccNoLiveData = new MutableLiveData<>();
    //取样地点数据列表
    private List<String> sampleLocationDatas;
    private SparseArray<String> sampleLocationSparse = new SparseArray<>();
    private TakePhotosAdapter takePhotosAdapter;
    //当前取样单位的类型，默认时供应商
    private MutableLiveData<Integer> sampleCompanyType = new MutableLiveData<>(SUPPLIER_INIT);
    private BaiduMapLifecycle baiduMapLifecycle;

    public SandTestingCommissionDetailViewModel(@NonNull Application application, LifecycleOwner lifecycleOwner) {
        super(application);
        baiduMapLifecycle = new BaiduMapLifecycle(application);
        lifecycleOwner.getLifecycle().addObserver(baiduMapLifecycle);
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    public void setTestingCommissionBean(TestingCommissionBean testingCommissionBean) {
        this.testingCommissionBean = testingCommissionBean;
    }

    public TestingCommissionBean getTestingCommissionBean() {
        return testingCommissionBean;
    }

    /**
     * 设置当前选中的委托单位
     *
     * @param flowDeclarationBean
     */
    public void setCurCommission(FlowDeclarationBean flowDeclarationBean) {
        testingCommissionBean.setFlowInfoId(flowDeclarationBean.getId());
        curCommission.setValue(flowDeclarationBean);
    }

    public MutableLiveData<FlowDeclarationBean> getCurCommission() {
        if (curCommission == null) {
            curCommission = new MutableLiveData<>(new FlowDeclarationBean());
        }
        return curCommission;
    }

    public List<SandSamplerBean> getSamplerBeans() {
        return samplerBeans;
    }

    /**
     * 设置当前选中的检测单位
     *
     * @param bean
     */
    public void setTestingCompany(AddedTestingCompanyBean bean) {
        testingCommissionBean.setDetectionAgencyMemberCode(bean.getDetectionAgencyMemberCode());
        testingCompany.setValue(bean);
    }

    public MutableLiveData<AddedTestingCompanyBean> getTestingCompany() {
        if (testingCompany == null) {
            testingCompany = new MutableLiveData<>(new AddedTestingCompanyBean());
        }
        return testingCompany;
    }

    public TakePhotosAdapter getTakePhotosAdapter() {
        if (takePhotosAdapter == null) {
            takePhotosAdapter = new TakePhotosAdapter();
        }
        return takePhotosAdapter;
    }

    /**
     * 设置当前选中的取样地点
     *
     * @param sampleLocationIndex
     */
    public void setSampleLocation(int sampleLocationIndex) {
        testingCommissionBean.setSamplingLocation(sampleLocationIndex);
        curSampleLocation.setValue(sampleLocationIndex);
    }

    public MutableLiveData<Integer> getCurSampleLocation() {
        if (curSampleLocation == null) {
            curSampleLocation = new MutableLiveData<>(0);
        }
        return curSampleLocation;
    }

    /**
     * 根据取样地点的id获取取样地点的名称
     *
     * @return
     */
    public String getSampleLocationSparse(int sampleLocationIndex) {
        return sampleLocationSparse.get(sampleLocationIndex);
    }

    /**
     * 设置当前的取样人员
     *
     * @param bean
     */
    public void setCurSampler(SandSamplerBean bean) {
        testingCommissionBean.setSampler(bean.getSamplerName() == null ? "" : bean.getSamplerName());
        testingCommissionBean.setSamplerCerNo(bean.getSamplerCerNo() == null ? "" : bean.getSamplerCerNo());
        curSampler.setValue(bean);
    }

    public MutableLiveData<SandSamplerBean> getCurSampler() {
        if (curSampler == null) {
            curSampler = new MutableLiveData<>(new SandSamplerBean());
        }
        return curSampler;
    }

    /**
     * 设置当前的取样日期
     *
     * @param date
     */
    public void setSampleDate(String date) {
        testingCommissionBean.setSamplingTime(date);
        sampleDate.setValue(date);
    }

    public MutableLiveData<String> getSampleDate() {
        if (sampleDate == null) {
            String date = DateUtil.getDate(new Date());
            sampleDate = new MutableLiveData<>(date);
            testingCommissionBean.setSamplingTime(date);
        }
        return sampleDate;
    }

    public void setOrderSampleDate(String date) {
        testingCommissionBean.setAppointmentSamplingTime(date);
        orderSampleDate.setValue(date);
    }

    public MutableLiveData<String> getOrderSampleDate() {
        if (orderSampleDate == null) {
            orderSampleDate = new MutableLiveData<>("");
        }
        return orderSampleDate;
    }

    /**
     * 委托人
     *
     * @param userName
     */
    public void setCommissionUser(String userName) {
        testingCommissionBean.setPrincipal(userName);
        commissionUser.setValue(userName);
    }

    public MutableLiveData<String> getCommissionUser() {
        if (commissionUser == null) {
            commissionUser = new MutableLiveData<>("");
        }
        return commissionUser;
    }

    /**
     * 设置委托日期
     *
     * @param date
     */
    public void setCommissionDate(String date) {
        testingCommissionBean.setCommissionDate(date);
        commissionDate.setValue(date);
    }

    public MutableLiveData<String> getCommissionDate() {
        if (commissionDate == null) {
            commissionDate = new MutableLiveData<>(DateUtil.getDate(new Date()));
        }
        return commissionDate;
    }

    /**
     * 获取取样地点数据列表
     *
     * @return
     */
    public List<String> getSampleLocationDatas() {
        return sampleLocationDatas;
    }

    /**
     * 用户选择的取样单位
     *
     * @param sampleCompany
     */
    public void setWhichSampleCompany(int sampleCompany) {
        testingCommissionBean.setSamplingMethod(sampleCompany);
        this.sampleCompanyType.setValue(sampleCompany);
    }

    public MutableLiveData<Integer> getSampleCompanyType() {
        return sampleCompanyType;
    }

    /**
     * 设置取样过程
     *
     * @param process
     */
    public void setSamplingProcess(String process) {
        testingCommissionBean.setSamplingProcess(process);
    }

    /**
     * 设置唯一性标识号
     *
     * @param ccNo
     */
    public void setCCNo(String ccNo) {
        testingCommissionBean.setCcNo(ccNo);
    }

    public MutableLiveData<String> getCcNoLiveData() {
        return ccNoLiveData;
    }

    public MutableLiveData<LoadState> getLoadDataState() {
        return loadDataState;
    }

    public MutableLiveData<LoadState> getPostDataState() {
        return postDataState;
    }

    @Override
    public void onCreate() {
        baiduMapLifecycle.setBdLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                double x = bdLocation.getLatitude();
                double y = bdLocation.getLongitude();
                LatLng myLocation = new LatLng(x, y);
                baiduMapLifecycle.stopLocation();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }


    @Override
    public void onStop() {
        baiduMapLifecycle.stopLocation();
    }

    /**
     * 获取取样地点资源
     *
     * @param sampleLocation 取样地点的id号 如果是-1则只获取全部的资源不获取指定id的对应名称
     */
    public void loadSampleLocations(int sampleLocation) {
        BuildSandRetrofit.getInstance().getService(SourceApiService.class)
                .getSampleLocation()
                .compose(BuildSandRetrofit.<BuildSandResultData<List<TestingCommissionBean.SampleLocation>>, List<TestingCommissionBean.SampleLocation>>applyTransformer())
                .subscribe(new ResponseObserver<List<TestingCommissionBean.SampleLocation>>() {
                    @Override
                    public void success(List<TestingCommissionBean.SampleLocation> sampleLocations) {
                        if (sampleLocationDatas == null) {
                            sampleLocationDatas = new ArrayList<>();
                        } else {
                            sampleLocationDatas.clear();
                        }
                        for (int i = 0; i < sampleLocations.size(); i++) {
                            sampleLocationDatas.add(sampleLocations.get(i).getSamplingLocationName());
                            sampleLocationSparse.put(i + 1, sampleLocations.get(i).getSamplingLocationName());
                        }
                        //获取指定id对应的取样地点名称
                        if (sampleLocationSparse.size() > 0 && sampleLocation > 0) {
                            setSampleLocation(sampleLocation);
                        }
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {

                    }
                });
    }

    /**
     * 获取取样人资源，
     * 如果取样单位是“供应商”则直接获取，memberCode=null
     * 如果取样单位是“检测单位”则需要根据选择的检测单位会员编号获取取样人员 memberCode!=null
     */
    public void getSamplers(String memberCode) {
        SourceApiService service = BuildSandRetrofit.getInstance().getService(SourceApiService.class);
        Observable<BuildSandResultData<List<SandSamplerBean>>> supplierSampler;
        if (memberCode == null) {
            supplierSampler = service.getSupplierSampler();
        } else {
            supplierSampler = service.getTestingSampler(memberCode);
        }
        supplierSampler.compose(BuildSandRetrofit.<BuildSandResultData<List<SandSamplerBean>>, List<SandSamplerBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<SandSamplerBean>>() {
                    @Override
                    public void success(List<SandSamplerBean> sandSamplerBeans) {
                        samplerBeans.clear();
                        samplerBeans.addAll(sandSamplerBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        throwableLiveData.postValue(throwable);
                    }
                });
    }

    /**
     * 保存当前的委托信息
     */
    public void saveTestingCommission() {
        RequestBody body;
        postDataState.postValue(LoadState.LOADING);
        Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonStr = gson.toJson(testingCommissionBean);
        if (testingCommissionBean.getSamplingMethod() == 1) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonStr);
                jsonObject.put("appointmentSamplingTime", testingCommissionBean.getAppointmentSamplingTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        } else {
            body = RequestBody.create(MediaType.parse("application/json"), jsonStr);
        }

        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .addTestingCommission(body)
                .compose(BuildSandRetrofit.<BuildSandResultData<TestingCommissionBean>, TestingCommissionBean>applyTransformer())
                .subscribe(new ResponseObserver<TestingCommissionBean>() {
                    @Override
                    public void success(TestingCommissionBean bean) {
                        postDataState.postValue(LoadState.SUCCESS);
                        testingCommissionBean.setId(bean.getId());
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        postDataState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }

    /**
     * 获取委托记录的详细信息
     */
    public void loadSingleInfo(String singleId) {
        loadDataState.postValue(LoadState.LOADING);
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getTestingCommissionInfo(singleId)
                .compose(BuildSandRetrofit.<BuildSandResultData<TestingCommissionBean>, TestingCommissionBean>applyTransformer())
                .subscribe(new ResponseObserver<TestingCommissionBean>() {
                    @Override
                    public void success(TestingCommissionBean bean) {
                        //设置前一个页面传来的ID和flowInfoId
                        bean.setFlowInfoId(testingCommissionBean.getFlowInfoId());
                        bean.setId(testingCommissionBean.getId());
                        testingCommissionBean = bean;
                        //根据检测单位编号获取检测单位名称
                        getTestingCompanies(bean.getDetectionAgencyMemberCode());
                        //根据详情的流向记录id获取委托单位
                        getCommissionInfo(bean.getFlowInfoId());
                        //取样地点
                        loadSampleLocations(testingCommissionBean.getSamplingLocation());
                        //取样人员
                        SandSamplerBean sandSamplerBean = new SandSamplerBean();
                        sandSamplerBean.setSamplerCerNo(testingCommissionBean.getSamplerCerNo());
                        sandSamplerBean.setSamplerName(testingCommissionBean.getSampler());
                        setCurSampler(sandSamplerBean);

                        //取样日期
                        String samplingTime = testingCommissionBean.getSamplingTime();
                        if (samplingTime.contains("T")) {
                            samplingTime = samplingTime.substring(0, samplingTime.lastIndexOf("T"));
                        }
                        sampleDate.setValue(samplingTime);

                        //委托人
                        commissionUser.setValue(testingCommissionBean.getPrincipal());

                        //唯一性标识号
                        ccNoLiveData.setValue(testingCommissionBean.getCcNo());
                        String appointmentSamplingTime = testingCommissionBean.getAppointmentSamplingTime();
                        if (appointmentSamplingTime.contains("T")) {
                            appointmentSamplingTime = appointmentSamplingTime.replace("T", " ");
                        }
                        setOrderSampleDate(appointmentSamplingTime);
                        loadDataState.postValue(LoadState.SUCCESS);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadDataState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }

    /**
     * 根据流向申请记录获取详情
     *
     * @param flowInfoId 流向记录的id
     */
    public void getCommissionInfo(String flowInfoId) {
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getSMFlowDeclarationInfo(flowInfoId)
                .compose(BuildSandRetrofit.<BuildSandResultData<FlowDeclarationBean>, FlowDeclarationBean>applyTransformer())
                .subscribe(new ResponseObserver<FlowDeclarationBean>() {
                    @Override
                    public void success(FlowDeclarationBean bean) {
                        bean.setId(flowInfoId);
                        setCurCommission(bean);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        throwableLiveData.postValue(throwable);
                    }
                });
    }

    /**
     * 根据检测单位编号获取检测单位名称
     *
     * @param memberCode 检测单位编号
     */
    private void getTestingCompanies(String memberCode) {
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getAddedTestingCompanies(500, 0, "detectionAgencyCounties")
                .compose(BuildSandRetrofit.<BuildSandResultData<List<AddedTestingCompanyBean>>, List<AddedTestingCompanyBean>>applyTransformer())
                .map(addedTestingCompanyBeans -> {
                    HashMap<String, AddedTestingCompanyBean> map = new HashMap<>();
                    for (AddedTestingCompanyBean bean : addedTestingCompanyBeans) {
                        map.put(bean.getDetectionAgencyMemberCode(), bean);
                    }
                    return map;
                }).subscribe(new ResponseObserver<HashMap<String, AddedTestingCompanyBean>>() {
            @Override
            public void success(HashMap<String, AddedTestingCompanyBean> map) {
                /*因为没有接口直接获取，返回的详情数据中不包含检测单位名称，
                  故先要获取检测单位资源转为map集合,再根据memberCode 从map集合中取出bean */
                AddedTestingCompanyBean addedTestingCompanyBean = map.get(memberCode);
                setTestingCompany(addedTestingCompanyBean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {

            }
        });
    }

    /**
     * 更新检测委托
     */
    public void updateTestingCommission() {
        RequestBody body;
        postDataState.postValue(LoadState.LOADING);
        Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonStr = gson.toJson(testingCommissionBean);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            jsonObject.put("appointmentSamplingTime", testingCommissionBean.getAppointmentSamplingTime());
            jsonObject.remove("flowInfoId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .putTestingCommissionInfo(testingCommissionBean.getId(), body)
                .compose(BuildSandRetrofit.<Response<Object>, Object>applyTransformer())
                .subscribe(new ResponseObserver<Object>() {
                    @Override
                    public void success(Object responseBody) {
                        postDataState.setValue(LoadState.SUCCESS);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        postDataState.setValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }
}
