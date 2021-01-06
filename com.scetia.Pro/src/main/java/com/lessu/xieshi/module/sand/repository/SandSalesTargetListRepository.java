package com.lessu.xieshi.module.sand.repository;

import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.BuildSandRetrofit;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * created by ljs
 * on 2020/12/22
 */
public class SandSalesTargetListRepository {

    public void loadAddedSanSalesTarget(int pageIndex, ResponseObserver<List<AddedSandSalesTargetBean>> callBack){
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getAddedSandSalesTargets(20,pageIndex,"customerUnitMemberCode")
                .compose(BuildSandRetrofit.<BuildSandResultData<List<AddedSandSalesTargetBean>>, List<AddedSandSalesTargetBean>>applyTransformer())
                .subscribe(callBack);

    }

    public void delAddedSanSalesTarget(AddedSandSalesTargetBean bean,ResponseObserver<retrofit2.Response<ResponseBody>> callBack){
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .delAddedSanSalesTarget(bean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }
}
