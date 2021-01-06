package com.lessu.xieshi.module.unqualified.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.data.LoadState;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.http.XSRetrofit;
import com.lessu.xieshi.http.api.CommonApiService;
import com.lessu.xieshi.module.unqualified.bean.ReportConclusionBean;

import java.util.HashMap;
import java.util.List;

/**
 * created by ljs
 * on 2020/12/17
 */
public class TestingReportConclusionViewModel extends BaseViewModel {
    private MutableLiveData<List<ReportConclusionBean>> reportConclusionBeansLiveData = new MutableLiveData<>();
    public TestingReportConclusionViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<ReportConclusionBean>> getReportConclusionBeansLiveData() {
        return reportConclusionBeansLiveData;
    }

    /**
     * 加载数据
     * @param params
     */
    public void loadData(HashMap<String,Object> params){
        loadState.postValue(LoadState.LOADING);
        XSRetrofit.getInstance().getService(CommonApiService.class)
                .getReportConclusion(GsonUtil.mapToJsonStr(params))
                .compose(XSRetrofit.<XSResultData<List<ReportConclusionBean>>, List<ReportConclusionBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<ReportConclusionBean>>() {
                    @Override
                    public void success(List<ReportConclusionBean> reportConclusionBeans) {
                        loadState.postValue(LoadState.SUCCESS);
                        reportConclusionBeansLiveData.postValue(reportConclusionBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }
}
