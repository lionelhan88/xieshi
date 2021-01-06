package com.lessu.xieshi.module.sand.repository;

import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.BuildSandRetrofit;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/23
 */
public class TestingCompanyListRepository {

    public void getAddedTestingCompanies(int pageIndex, ResponseObserver<List<AddedTestingCompanyBean>> callBack){
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getAddedTestingCompanies(20,pageIndex,"detectionAgencyCounties")
                .compose(BuildSandRetrofit.<BuildSandResultData<List<AddedTestingCompanyBean>>, List<AddedTestingCompanyBean>>applyTransformer())
                .subscribe(callBack);
    }

    public void delAddTestingCompanies(AddedTestingCompanyBean bean,ResponseObserver<Response<ResponseBody>> callBack){
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .delAddedTestingCompany(bean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }
}
