package com.lessu.xieshi.module.mis.model;


import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.http.service.MisApiService;
import com.lessu.xieshi.module.mis.bean.MisAnnualLeaveData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/12/2
 */
public class MisAnnualLeaveRepository {

    public void getAnnualLeaveData(String token, String year, String state, int currentPageNo, int pageSize, ResponseObserver<MisAnnualLeaveData> callBack){
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("year", year);
        params.put("status", state);
        params.put("CurrentPageNo", currentPageNo);
        params.put("PageSize", pageSize);
        XSRetrofit.getInstance().getService(MisApiService.class)
                .getMisAnnualLeave(GsonUtil.toJsonStr(params))
                .compose(XSRetrofit.<XSResultData<MisAnnualLeaveData>, MisAnnualLeaveData>applyTransformer())
                .subscribe(callBack);
    }
}
