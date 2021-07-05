package com.scetia.app_sand.repository;
import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.app_sand.bean.AddedTestingCompanyBean;
import com.scetia.app_sand.service.BuildSandApiService;

import java.util.List;

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
