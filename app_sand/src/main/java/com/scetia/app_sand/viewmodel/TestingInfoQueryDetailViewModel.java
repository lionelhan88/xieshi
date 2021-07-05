package com.scetia.app_sand.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.app_sand.bean.SandParameterResultBean;
import com.scetia.app_sand.bean.TestingQueryResultBean;
import com.scetia.app_sand.service.BuildSandApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * created by ljs
 * on 2021/1/13
 */
public class TestingInfoQueryDetailViewModel extends BaseViewModel {
    public TestingInfoQueryDetailViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<TestingQueryResultBean> queryResultBean = new MutableLiveData<>();
    private MutableLiveData<List<SandParameterResultBean>> sandParameterResult;

    public MutableLiveData<TestingQueryResultBean> getQueryResultBean() {
        return queryResultBean;
    }

    public MutableLiveData<List<SandParameterResultBean>> getSandParameterResult() {
        if (sandParameterResult == null) {
            sandParameterResult = new MutableLiveData<>();
        }
        return sandParameterResult;
    }

    /**
     * 获取详情数据
     *
     * @param flowConsignInfoId 列表传来的id
     */
    public void loadTestingInfo(String flowConsignInfoId) {
        loadState.setValue(LoadState.LOADING);
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getTestingInfoResultDetail(flowConsignInfoId)
                .compose(BuildSandRetrofit.<BuildSandResultData<TestingQueryResultBean>, TestingQueryResultBean>applyTransformer())
                .subscribeOn(Schedulers.io())
                .map(new Function<TestingQueryResultBean, TestingQueryResultBean>() {
                    @Override
                    public TestingQueryResultBean apply(TestingQueryResultBean testingQueryResultBean) throws Exception {
                        List<SandParameterResultBean> resultBeans =new ArrayList<>();
                        //得到检测参数的名称
                        String[] parameters = testingQueryResultBean.getParameterNames().split(";");
                       // String[] parameterDetectValuesArray = new String[0];
                        String[] parameterResultsArray = new String[0];

                        //参数检测值
                     /*   String parameterDetectValues = testingQueryResultBean.getParameterDetectValue();
                        if (!TextUtils.isEmpty(parameterDetectValues)) {
                            parameterDetectValuesArray = parameterDetectValues.split(";");
                        }*/

                        //参数检测结果
                        String parameterResults = testingQueryResultBean.getParameterDetectResult();
                        if (!TextUtils.isEmpty(parameterResults)) {
                            parameterResultsArray = parameterResults.split(";");
                        }

                        for (int i = 0; i < parameters.length; i++) {
                            SandParameterResultBean bean = new SandParameterResultBean();
                            bean.setParameterName(parameters[i]);
                        /*    if (parameterDetectValuesArray.length > i) {
                                bean.setParameterValue(parameterDetectValuesArray[i]);
                            }*/
                            if (parameterResultsArray.length > i) {
                                bean.setParameterResult(parameterResultsArray[i]);
                            }
                            resultBeans.add(bean);
                        }
                        sandParameterResult.postValue(resultBeans);
                        return testingQueryResultBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<TestingQueryResultBean>() {
                    @Override
                    public void success(TestingQueryResultBean testingQueryResultBean) {
                        loadState.setValue(LoadState.SUCCESS);
                        queryResultBean.setValue(testingQueryResultBean);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }
}
