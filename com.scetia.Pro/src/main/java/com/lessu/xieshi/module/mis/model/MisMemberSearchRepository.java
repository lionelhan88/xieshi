package com.lessu.xieshi.module.mis.model;

import com.google.gson.JsonObject;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.http.XSRetrofit;
import com.lessu.xieshi.module.mis.api.MisApiService;
import com.lessu.xieshi.module.mis.bean.MisHySearchResultData;

/**
 * created by ljs
 * on 2020/11/26
 */
public class MisMemberSearchRepository {
   // private static final int PAGE_SIZE = 5;
    public void search(String token, String queryKey, int currentPage,int pageSize, ResponseObserver<MisHySearchResultData> callBack){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Token",token);
        jsonObject.addProperty("key",queryKey);
        jsonObject.addProperty("CurrentPageNo",currentPage);
        jsonObject.addProperty("PageSize",pageSize);
        XSRetrofit.getInstance().getService(MisApiService.class)
                .search(jsonObject.toString())
                .compose(XSRetrofit.<XSResultData<MisHySearchResultData>,MisHySearchResultData>applyTransformer())
                .subscribe(callBack);

    }
}
