package com.lessu.xieshi.module.unqualified.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.utils.GsonUtil;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.http.service.CommonApiService;
import com.lessu.xieshi.module.unqualified.bean.ReportConclusionBean;
import com.scetia.Pro.network.manage.XSRetrofit;

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
                        loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }
}
