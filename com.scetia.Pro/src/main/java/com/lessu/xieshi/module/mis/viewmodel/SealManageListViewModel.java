package com.lessu.xieshi.module.mis.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.http.service.MisApiService;
import com.lessu.xieshi.module.mis.bean.SealManageBean;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * created by Lollipop
 * on 2021/3/2
 */
public class SealManageListViewModel extends BaseViewModel {
    private MutableLiveData<List<SealManageBean>> sealManageList;
    private List<SealManageBean.SealType> sealTypes = new ArrayList<>();

    public SealManageListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<SealManageBean>> getSealManageList() {
        if (sealManageList == null) {
            sealManageList = new MutableLiveData<>();
        }
        return sealManageList;
    }

    public List<SealManageBean.SealType> getSealTypes() {
        return sealTypes;
    }

    /**
     * 加载默认用章事项列表
     */
    public void getSealManagerListByQuery(HashMap<String,Object> param,boolean isShowLoading) {
        if(isShowLoading) {
            loadState.setValue(LoadState.LOADING);
        }
        Observable<XSResultData<List<SealManageBean>>> sealManageListObserver;
        if(param.size()==1){
            sealManageListObserver = XSRetrofit.getInstance().getService(MisApiService.class)
                    .getDefaultSealManageList(GsonUtil.mapToJsonStr(param));
        }else{
            sealManageListObserver= XSRetrofit.getInstance().getService(MisApiService.class)
                    .getSealManageListByQuery(GsonUtil.mapToJsonStr(param));
        }
        sealManageListObserver.compose(XSRetrofit.<XSResultData<List<SealManageBean>>, List<SealManageBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<SealManageBean>>() {
                    @Override
                    public void success(List<SealManageBean> sealManageBeans) {
                        loadState.setValue(LoadState.SUCCESS);
                        sealManageList.setValue(sealManageBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }

    /**
     * 获取数据列表
     */
    public void getSealManagerListByQuery(HashMap<String, Object> param) {
        loadState.setValue(LoadState.LOADING);
        XSRetrofit.getInstance().getService(MisApiService.class)
                .getSealManageListByQuery(GsonUtil.mapToJsonStr(param))
                .compose(XSRetrofit.<XSResultData<List<SealManageBean>>, List<SealManageBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<SealManageBean>>() {
                    @Override
                    public void success(List<SealManageBean> sealManageBeans) {
                        loadState.setValue(LoadState.SUCCESS);
                        sealManageList.setValue(sealManageBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
                    }
                });
    }

    /**
     * 获取事项类型的数据列表
     */
    public void getSealTypeList() {
        XSRetrofit.getInstance().getService(MisApiService.class)
                .getSealTypeList("")
                .compose(XSRetrofit.<XSResultData<List<SealManageBean.SealType>>, List<SealManageBean.SealType>>applyTransformer())
                .subscribe(new ResponseObserver<List<SealManageBean.SealType>>() {
                    @Override
                    public void success(List<SealManageBean.SealType> types) {
                        sealTypes.clear();
                        sealTypes.add(new SealManageBean.SealType("","全部"));
                        sealTypes.addAll(types);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {

                    }
                });

    }

}
