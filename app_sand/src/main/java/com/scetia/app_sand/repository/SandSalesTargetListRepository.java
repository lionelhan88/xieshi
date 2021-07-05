package com.scetia.app_sand.repository;


import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.app_sand.bean.AddedSandSalesTargetBean;
import com.scetia.app_sand.service.BuildSandApiService;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/22
 */
public class SandSalesTargetListRepository extends BaseRepository {

    public void loadAddedSanSalesTarget(int pageIndex, ResponseObserver<List<AddedSandSalesTargetBean>> callBack){
        requestApi(BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getAddedSandSalesTargets(20,pageIndex,"customerUnitMemberCode"),callBack);
    }

    public void delAddedSanSalesTarget(AddedSandSalesTargetBean bean,ResponseObserver<Object> callBack){
        requestApi(BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .delAddedSanSalesTarget(bean.getId()),callBack);
    }
}
