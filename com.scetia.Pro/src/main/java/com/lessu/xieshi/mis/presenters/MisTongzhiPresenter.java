package com.lessu.xieshi.mis.presenters;

import android.app.Activity;

import com.google.gson.JsonElement;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.mis.bean.Tongzhibean;
import com.lessu.xieshi.mis.contracts.IMistongzhiContract;

import java.util.HashMap;

/**
 * Created by fhm on 2017/10/12.
 */

public class MisTongzhiPresenter implements IMistongzhiContract.Presenter {
    private IMistongzhiContract.View view;
    private Activity activity;

    public MisTongzhiPresenter(IMistongzhiContract.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Override
    public void getTongzhi(String token) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", token);
        System.out.println(params);
        EasyAPI.apiConnectionAsync(activity, true, false, ApiMethodDescription.get("/ServiceMis.asmx/GetMisTz"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                System.out.println(result);
                Tongzhibean tongzhibean = GsonUtil.JsonToObject(result.toString(), Tongzhibean.class);
                view.getTongzhiCall(true,tongzhibean);
            }

            @Override
            public String onFailed(ApiError error) {
                view.getTongzhiCall(false,null);
                return null;
            }
        });
    }
}
