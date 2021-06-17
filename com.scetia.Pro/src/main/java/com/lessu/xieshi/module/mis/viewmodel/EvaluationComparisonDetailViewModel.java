package com.lessu.xieshi.module.mis.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.http.service.MisApiService;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.HashMap;

/**
 * created by Lollipop
 * on 2021/4/15
 */
public class EvaluationComparisonDetailViewModel extends BaseViewModel {
    public EvaluationComparisonDetailViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 请求批准操作
     */
    public void requestApprove(HashMap<String,Object> params){
        params.put(Constants.User.XS_TOKEN,Constants.User.GET_TOKEN());
        loadState.setValue(LoadState.LOADING);
        XSRetrofit.getInstance().getService(MisApiService.class)
                .postApproveEvaluationComparison(GsonUtil.mapToJsonStr(params))
                .compose(XSRetrofit.applyTransformer())
                .subscribe(new ResponseObserver<Object>() {
                    @Override
                    public void success(Object o) {
                        loadState.setValue(LoadState.SUCCESS);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }
}
