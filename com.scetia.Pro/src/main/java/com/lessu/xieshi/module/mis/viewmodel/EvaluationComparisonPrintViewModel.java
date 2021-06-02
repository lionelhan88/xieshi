package com.lessu.xieshi.module.mis.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.http.service.MisApiService;
import com.lessu.xieshi.module.mis.bean.EvaluationComparisonBean;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.HashMap;
import java.util.List;

/**
 * created by Lollipop
 * on 2021/4/15
 */
public class EvaluationComparisonPrintViewModel extends BaseViewModel {
    private MutableLiveData<List<EvaluationComparisonBean.EvaluationComparisonItem>> evaluationComparisonLiveData;
    private MutableLiveData<String> approveState = new MutableLiveData<>();
    public EvaluationComparisonPrintViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<EvaluationComparisonBean.EvaluationComparisonItem>> getEvaluationComparisonLiveData() {
        if(evaluationComparisonLiveData==null){
            evaluationComparisonLiveData = new MutableLiveData<>();
        }
        return evaluationComparisonLiveData;
    }

    public MutableLiveData<String> getApproveState() {
        return approveState;
    }

    public void loadData(boolean showLoading, HashMap<String,Object> params){
        if(showLoading){
            loadState.setValue(LoadState.LOADING);
        }
        params.put(Constants.User.XS_TOKEN,Constants.User.GET_TOKEN());
        XSRetrofit.getInstance().getService(MisApiService.class)
                .getEvaluationComparisonByQuery(GsonUtil.mapToJsonStr(params))
                .compose(XSRetrofit.<XSResultData<EvaluationComparisonBean>, EvaluationComparisonBean>applyTransformer())
                .subscribe(new ResponseObserver<EvaluationComparisonBean>() {
                    @Override
                    public void success(EvaluationComparisonBean evaluationComparisonBeans) {
                        loadState.setValue(LoadState.SUCCESS);
                        approveState.setValue(evaluationComparisonBeans.getPGApproveBtn());
                        evaluationComparisonLiveData.setValue(evaluationComparisonBeans.getListContent());
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }

}
