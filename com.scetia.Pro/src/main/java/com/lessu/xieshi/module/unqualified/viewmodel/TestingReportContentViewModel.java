package com.lessu.xieshi.module.unqualified.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.data.LoadState;
import com.lessu.xieshi.bean.ReportContentBean;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.http.XSRetrofit;
import com.lessu.xieshi.http.api.CommonApiService;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/12/16
 */
public class TestingReportContentViewModel extends BaseViewModel {
    private MutableLiveData<ReportContentBean> reportContentLiveData = new MutableLiveData<>();
    public TestingReportContentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ReportContentBean> getReportContentLiveData() {
        return reportContentLiveData;
    }

    /**
     * 加载数据
     * @param params 传入参数
     */
    public void loadData(HashMap<String,Object> params){
        loadState.postValue(LoadState.LOADING);
        String param = GsonUtil.mapToJsonStr(params);
        XSRetrofit.getInstance()
                .getService(CommonApiService.class)
                .getUnqualifiedTestingReportContent(param)
                .compose(XSRetrofit.<XSResultData<ReportContentBean>, ReportContentBean>applyTransformer())
                .subscribe(new ResponseObserver<ReportContentBean>() {
                    @Override
                    public void success(ReportContentBean reportContentBean) {
                        loadState.postValue(LoadState.SUCCESS);
                        reportContentLiveData.postValue(reportContentBean);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }
}
