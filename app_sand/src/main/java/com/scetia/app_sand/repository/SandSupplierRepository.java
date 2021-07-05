package com.scetia.app_sand.repository;

import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.app_sand.bean.SandSupplierBean;
import com.scetia.app_sand.service.BuildSandApiService;

import java.util.List;

/**
 * created by ljs
 * on 2021/2/3
 */
public class SandSupplierRepository extends BaseRepository {
    public void getSupplierList(ResponseObserver<List<SandSupplierBean>> callBack) {
        requestApi(BuildSandRetrofit.getInstance().getService(BuildSandApiService.class).getSuppliers(), callBack);
    }
}
