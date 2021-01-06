package com.lessu.xieshi.module.sand.repository;

import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.BuildSandRetrofit;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/29
 */
public class TestingCommissionRepository {

    public void getTestingCommissions(int pageIndex, ResponseObserver<List<TestingCommissionBean>> callBack){
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getTestingCommissions(20,pageIndex,"commissionDate desc")
                .compose(BuildSandRetrofit.<BuildSandResultData<List<TestingCommissionBean>>, List<TestingCommissionBean>>applyTransformer())
                .subscribe(callBack);
    }

    public void delTestingCommission(String id, ResponseObserver<Response<ResponseBody>> callBack){
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .delTestingCommission(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }
}
