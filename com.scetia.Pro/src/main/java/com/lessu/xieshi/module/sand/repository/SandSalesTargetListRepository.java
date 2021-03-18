package com.lessu.xieshi.module.sand.repository;

import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.http.service.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;
import com.scetia.Pro.network.manage.BuildSandRetrofit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

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
