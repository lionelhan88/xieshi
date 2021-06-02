package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.module.sand.repository.SMFlowDeclarationDetailRepository;
import com.lessu.xieshi.module.sand.repository.TestingCommissionRepository;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.DateUtil;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.lib_map.BaiduMapLifecycle;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.http.service.BuildSandApiService;
import com.lessu.xieshi.module.sand.adapter.TakePhotosAdapter;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.bean.SandSamplerBean;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.Pro.network.manage.XSRetrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * created by ljs
 * on 2020/12/30
 */
public class SandTestingCommissionDetailViewModel extends BaseViewModel {
    public static final int SUPPLIER_INIT = 2;
    public static final int TESTING_INIT = 1;
    private TestingCommissionRepository repository = new TestingCommissionRepository();
    private SMFlowDeclarationDetailRepository smRepository = new SMFlowDeclarationDetailRepository();
    private TestingCommissionBean testingCommissionBean = new TestingCommissionBean();
    private TestingCommissionBean oldTestingCommissionBean = new TestingCommissionBean();
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
    private MutableLiveData<String> ccNoText = new MutableLiveData<>();
    //取样地点数据列表
    private List<String> sampleLocationDatas;
    private SparseArray<String> sampleLocationSparse = new SparseArray<>();
    private TakePhotosAdapter takePhotosAdapter;
    //当前取样单位的类型，默认时供应商
    private MutableLiveData<Integer> sampleCompanyType = new MutableLiveData<>(SUPPLIER_INIT);
    private MutableLiveData<List<TestingCommissionBean.CCNoBean>> ccNoLiveData;
    private BaiduMapLifecycle baiduMapLifecycle;
    private List<String> uploadProcess = new ArrayList<>();

    public SandTestingCommissionDetailViewModel(@NonNull Application application, LifecycleOwner lifecycleOwner) {
        super(application);
        baiduMapLifecycle = new BaiduMapLifecycle(application);
        lifecycleOwner.getLifecycle().addObserver(baiduMapLifecycle);
        lifecycleOwner.getLifecycle().addObserver(this);
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
            String date = DateUtil.FORMAT_BAR_YMD(new Date());
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
            commissionDate = new MutableLiveData<>(DateUtil.FORMAT_BAR_YMD(new Date()));
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
     * 设置唯一性标识号
     *
     * @param ccNo
     */
    public void setCCNo(String ccNo) {
        testingCommissionBean.setCcNo(ccNo);
        ccNoText.setValue(ccNo);
    }

    public MutableLiveData<List<TestingCommissionBean.CCNoBean>> getCcNoLiveData() {
        if (ccNoLiveData == null) {
            ccNoLiveData = new MutableLiveData<>();
        }
        return ccNoLiveData;
    }

    public MutableLiveData<String> getCcNoText() {
        return ccNoText;
    }

    /**
     * 显示取样的图片
     *
     * @param sampling
     */
    private void showSamplingProcess(String sampling) {
        if (TextUtils.isEmpty(sampling)) {
            return;
        }
        if (!sampling.contains(";")) {
            uploadProcess.add(0, sampling);
        } else {
            String[] split = sampling.split(";");
            uploadProcess.addAll(0, Arrays.asList(split));
        }
        takePhotosAdapter.addData(0, uploadProcess);
    }

    /**
     * 删除取样的图片
     *
     * @param imageIndex
     */
    public void deleteSamplingProcess(int imageIndex) {
        String item = takePhotosAdapter.getItem(imageIndex);
        uploadProcess.remove(imageIndex);
        if (item.contains("http")) {
            removeIndexPool.add(Integer.valueOf(item.substring(item.lastIndexOf("_") + 1, item.lastIndexOf("_") + 2)));
        }
        setCommissionSamplingProcess();
    }

    /**
     * 设置取样过程
     */
    private void setCommissionSamplingProcess() {
        if (uploadProcess.size() == 0) {
            testingCommissionBean.setSamplingProcess("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String imagePath : uploadProcess) {
            sb.append(imagePath).append(";");
        }
        testingCommissionBean.setSamplingProcess(sb.substring(0, sb.length() - 1));
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
     * 保存当前的委托信息
     */
    public void saveTestingCommission() {
        RequestBody body;
        loadState.postValue(LoadState.LOADING.setMessage("正在提交..."));
        Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
        if (testingCommissionBean.getSamplingMethod() == 1) {
            testingCommissionBean.setSamplingProcess("");
            String jsonStr = gson.toJson(testingCommissionBean);
            //检测单位
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonStr);
                jsonObject.put("appointmentSamplingTime", testingCommissionBean.getAppointmentSamplingTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        } else {
            //供应商;
            String jsonStr = gson.toJson(testingCommissionBean);
            body = RequestBody.create(MediaType.parse("application/json"), jsonStr);
        }

        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .addTestingCommission(body)
                .compose(BuildSandRetrofit.<BuildSandResultData<TestingCommissionBean>, TestingCommissionBean>applyTransformer())
                .observeOn(Schedulers.io())
                .flatMap((Function<TestingCommissionBean, ObservableSource<XSResultData<Integer>>>) bean -> {
                    testingCommissionBean.setId(bean.getId());
                    JsonObject param = new JsonObject();
                    param.addProperty("UnitId", Constants.User.GET_BOUND_UNIT_ID());
                    param.addProperty("UserName", SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, ""));
                    String ccNo = testingCommissionBean.getCcNo();
                    param.addProperty("TWNo", ccNo.substring(0, ccNo.lastIndexOf(";")));
                    return XSRetrofit.getInstance().getService(BuildSandApiService.class).setTTMState(param.toString());
                })
                .compose(XSRetrofit.<XSResultData<Integer>, Integer>applyTransformer())
                .subscribe(new ResponseObserver<Integer>() {
                    @Override
                    public void success(Integer integer) {
                        cloneOld(testingCommissionBean);
                        loadState.setValue(LoadState.SUCCESS.setCode(204).setMessage("提交成功"));
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }

    /**
     * 更新检测委托
     */
    public void updateTestingCommission() {
        RequestBody body;
        loadState.postValue(LoadState.LOADING.setMessage("正在提交..."));
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
                .compose(BuildSandRetrofit.applyTransformer())
                .observeOn(Schedulers.io())
                .flatMap((Function<Object, ObservableSource<XSResultData<Integer>>>) bean -> {
                    JsonObject param = new JsonObject();
                    param.addProperty("UnitId", Constants.User.GET_BOUND_UNIT_ID());
                    param.addProperty("UserName", SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, ""));
                    String ccNo = testingCommissionBean.getCcNo();
                    param.addProperty("TWNo", ccNo.substring(0, ccNo.lastIndexOf(";")));
                    return XSRetrofit.getInstance().getService(BuildSandApiService.class).setTTMState(param.toString());
                })
                .compose(XSRetrofit.<XSResultData<Integer>, Integer>applyTransformer())
                .subscribe(new ResponseObserver<Integer>() {
                    @Override
                    public void success(Integer integer) {
                        cloneOld(testingCommissionBean);
                        loadState.setValue(LoadState.SUCCESS.setCode(204).setMessage("提交成功"));
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }

    /**
     * 获取委托记录的详细信息
     */
    public void loadSingleInfo(String singleId) {
        loadState.postValue(LoadState.LOADING.setMessage("正在加载数据..."));
        repository.getSingleInfo(singleId, new ResponseObserver<TestingCommissionBean>() {
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
                //取样过程
                showSamplingProcess(testingCommissionBean.getSamplingProcess());
                //唯一性标识号
                ccNoText.setValue(testingCommissionBean.getCcNo());
                String appointmentSamplingTime = testingCommissionBean.getAppointmentSamplingTime();
                if (appointmentSamplingTime.contains("T")) {
                    appointmentSamplingTime = appointmentSamplingTime.replace("T", " ");
                }
                setOrderSampleDate(appointmentSamplingTime);
                cloneOld(testingCommissionBean);
                loadState.postValue(LoadState.SUCCESS.setCode(200));
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }

    /**
     * 获取取样地点资源
     *
     * @param sampleLocation 取样地点的id号 如果是-1则只获取全部的资源不获取指定id的对应名称
     */
    public void loadSampleLocations(int sampleLocation) {
        repository.getSampleLocation(new ResponseObserver<List<TestingCommissionBean.SampleLocation>>() {
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
        repository.getSamplers(memberCode, new ResponseObserver<List<SandSamplerBean>>() {
            @Override
            public void success(List<SandSamplerBean> sandSamplerBeans) {
                samplerBeans.clear();
                samplerBeans.addAll(sandSamplerBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }

    /**
     * 获取唯一性标识
     */
    public void getCCNoSource(String key) {
        loadState.setValue(LoadState.LOADING);
        repository.getCCNos(key, new ResponseObserver<List<TestingCommissionBean.CCNoBean>>() {
            @Override
            public void success(List<TestingCommissionBean.CCNoBean> ccNoBeans) {
                ccNoLiveData.setValue(ccNoBeans);
                loadState.setValue(LoadState.SUCCESS.setCode(200));
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }

    /**
     * 根据流向申请记录获取详情
     *
     * @param flowInfoId 流向记录的id
     */
    public void getCommissionInfo(String flowInfoId) {
        smRepository.getFlowDeclarationInfo(flowInfoId, new ResponseObserver<FlowDeclarationBean>() {
            @Override
            public void success(FlowDeclarationBean bean) {
                bean.setId(flowInfoId);
                setCurCommission(bean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
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
                .getAddedTestingCompanies(0, 500, "detectionAgencyCounties")
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

    private List<Integer> removeIndexPool = new ArrayList<>();

    /**
     * 上传取样过程
     *
     * @param id        当前这一组图片的id
     * @param photoPath 图片的本地路径
     */
    public void uploadSamplingProcess(String id, String photoPath) {
        loadState.setValue(LoadState.LOADING.setMessage("正在上传..."));
        int imageIndex;
        if (removeIndexPool.size() > 0) {
            imageIndex = removeIndexPool.get(0);
            removeIndexPool.remove(0);
        } else {
            imageIndex = takePhotosAdapter.getData().size();
        }
        repository.uploadSamplingProcess(id, imageIndex, photoPath, new ResponseObserver<String>() {
            @Override
            public void success(String uploadImagePath) {
                loadState.setValue(LoadState.SUCCESS);
                uploadProcess.add(0, uploadImagePath);
                setCommissionSamplingProcess();
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }
    /**
     * 是否编辑过信息，提示用户是否需要保存
     * @return
     */
    public boolean isEdit(){
        return testingCommissionBean.equals(oldTestingCommissionBean);
    }

    private void cloneOld(TestingCommissionBean testingCommissionBean){
        String s = GsonUtil.toJsonStr(testingCommissionBean);
        oldTestingCommissionBean = GsonUtil.JsonToObject(s,TestingCommissionBean.class);
    }
}
