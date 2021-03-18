package com.lessu.xieshi.module.sand.repository;

import com.lessu.xieshi.http.service.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.SandSupplierBean;
import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;

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
