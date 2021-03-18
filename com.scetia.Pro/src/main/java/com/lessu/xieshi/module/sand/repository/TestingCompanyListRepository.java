package com.lessu.xieshi.module.sand.repository;

import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.http.service.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.scetia.Pro.network.manage.BuildSandRetrofit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/23
 */
public class TestingCompanyListRepository extends BaseRepository {

    public void getAddedTestingCompanies(int pageIndex, ResponseObserver<List<AddedTestingCompanyBean>> callBack) {
        requestApi(BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getAddedTestingCompanies(pageIndex, 20, "detectionAgencyCounties"), callBack);
    }

    public void delAddTestingCompanies(AddedTestingCompanyBean bean, ResponseObserver<Object> callBack) {
        requestApi(BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .delAddedTestingCompany(bean.getId()), callBack);
    }
}
