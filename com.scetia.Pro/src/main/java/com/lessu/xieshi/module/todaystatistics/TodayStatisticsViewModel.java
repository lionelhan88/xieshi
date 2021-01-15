package com.lessu.xieshi.module.todaystatistics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.xieshi.bean.TodayStatisticsBean;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.http.api.CommonApiService;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.scetia.Pro.network.manage.XSRetrofit;

/**
 * created by ljs
 * on 2020/12/7
 */
public class TodayStatisticsViewModel extends BaseViewModel {
    public TodayStatisticsViewModel(@NonNull Application application) {
        super(application);
    }
    private MutableLiveData<TodayStatisticsBean> todayStatisticsLiveData = new MutableLiveData<>();

    public MutableLiveData<TodayStatisticsBean> getTodayStatisticsLiveData() {
        return todayStatisticsLiveData;
    }

    public void loadData(String type, String date){
        JsonObject param = new JsonObject();
        param.addProperty("Token", Content.getToken());
        param.addProperty("Type", type);
        param.addProperty("SummDate",date);
        loadState.postValue(LoadState.LOADING);
        XSRetrofit.getInstance().getService(CommonApiService.class)
                .getDetectionTodayStatistics(param.toString())
                .compose(XSRetrofit.<XSResultData<TodayStatisticsBean>, TodayStatisticsBean>applyTransformer())
                .subscribe(new ResponseObserver<TodayStatisticsBean>() {
                    @Override
                    public void success(TodayStatisticsBean todayStatisticsBean) {
                        TodayStatisticsBean.JsonContentBean jsonContent = todayStatisticsBean.getJsonContent();
                        if(jsonContent.getItemList().size()==0){
                            loadState.postValue(LoadState.FAILURE);
                            throwableLiveData.postValue(new ExceptionHandle.ResponseThrowable(3000,""));
                            return;
                        }
                        todayStatisticsLiveData.postValue(todayStatisticsBean);
                        loadState.postValue(LoadState.SUCCESS);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }
}
