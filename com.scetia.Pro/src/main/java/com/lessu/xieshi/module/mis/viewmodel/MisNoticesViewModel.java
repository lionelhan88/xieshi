package com.lessu.xieshi.module.mis.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.bean.LoadState;
import com.lessu.xieshi.http.ExceptionHandle;
import com.lessu.xieshi.http.XSRetrofit;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.lessu.xieshi.module.mis.api.MisApiService;
import com.lessu.xieshi.module.mis.bean.NoticeBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fhm on 2017/10/12.
 * 处理信息通知的业务逻辑类
 */
public class MisNoticesViewModel extends BaseViewModel {
    private MutableLiveData<List<NoticeBean>> noticeBeanData = new MutableLiveData<>();

    public MisNoticesViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<NoticeBean>> getNoticeBeanData() {
        return noticeBeanData;
    }

    public void getNotices(){
        loadState.postValue(LoadState.LOADING);
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.getToken());
        XSRetrofit.getInstance().getService(MisApiService.class)
                .getNotices(new Gson().toJson(params))
                .compose(XSRetrofit.<XSResultData<List<NoticeBean>>, List<NoticeBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<NoticeBean>>() {
                    @Override
                    public void success(List<NoticeBean> noticeBeans) {
                        noticeBeanData.postValue(noticeBeans);
                        loadState.postValue(LoadState.SUCCESS);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE);
                        throwableLiveData .postValue(throwable);
                    }
                });

    }

}
