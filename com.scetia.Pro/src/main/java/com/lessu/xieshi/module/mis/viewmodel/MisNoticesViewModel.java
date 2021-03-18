package com.lessu.xieshi.module.mis.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.http.service.MisApiService;
import com.lessu.xieshi.module.mis.bean.NoticeBean;
import com.scetia.Pro.network.manage.XSRetrofit;

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
        params.put("Token",  Constants.User.GET_TOKEN());
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
                        loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });

    }

}
