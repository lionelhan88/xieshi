package com.lessu.xieshi.module.mis.model;

import com.google.gson.JsonObject;
import com.lessu.xieshi.http.api.MisApiService;
import com.lessu.xieshi.module.mis.bean.MisMemberSearchResultData;
import com.scetia.Pro.network.base.BaseRetrofitManage;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.manage.XSRetrofit;

/**
 * created by ljs
 * on 2020/11/26
 */
public class MisMemberSearchRepository {
   // private static final int PAGE_SIZE = 5;
    public void search(String token, String queryKey, int currentPage,int pageSize, ResponseObserver<MisMemberSearchResultData> callBack){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Token",token);
        jsonObject.addProperty("key",queryKey);
        jsonObject.addProperty("CurrentPageNo",currentPage);
        jsonObject.addProperty("PageSize",pageSize);
        XSRetrofit.getInstance().getService(MisApiService.class)
                .search(jsonObject.toString())
                .compose(BaseRetrofitManage.<XSResultData<MisMemberSearchResultData>, MisMemberSearchResultData>applyTransformer())
                .subscribe(callBack);

    }
}
