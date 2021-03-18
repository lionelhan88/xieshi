package com.lessu.xieshi.module.mis.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.http.service.MisApiService;
import com.lessu.xieshi.module.mis.bean.CertificateBean;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.HashMap;

/**
 * Created by fhm on 2017/10/11.
 */

public class MisCertificateSearchViewModel extends BaseViewModel {
    private MutableLiveData<CertificateBean> certificateBeanData = new MutableLiveData<>();

    public MisCertificateSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<CertificateBean> getCertificateBeanData() {
        return certificateBeanData;
    }

    public void getCertificateSearch(String token, String key) {
        loadState.postValue(LoadState.LOADING);
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("key", key);
        XSRetrofit.getInstance().getService(MisApiService.class).searchCertificate(new Gson().toJson(params))
                .compose(XSRetrofit.<XSResultData<CertificateBean>, CertificateBean>applyTransformer())
                .subscribe(new ResponseObserver<CertificateBean>() {
                    @Override
                    public void success(CertificateBean bean) {
                        if(bean.getCertificateNumber()==null){
                            //输入的证书比编号不正确
                            loadState.postValue(LoadState.FAILURE.setMessage("请输入正确的证书编号！"));
                        }else{
                            certificateBeanData.postValue(bean);
                            loadState.postValue(LoadState.SUCCESS);
                        }
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }
}
