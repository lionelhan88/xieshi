package com.lessu.xieshi.mis.presenters;

import android.app.Activity;

import com.google.gson.JsonElement;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.mis.bean.Miszhengshu;
import com.lessu.xieshi.mis.contracts.IMisZssearchContract;

import java.util.HashMap;

/**
 * Created by fhm on 2017/10/11.
 */

public class MisZsPresenter implements IMisZssearchContract.Presenter {

    private IMisZssearchContract.View view;
    private Activity activity;
    public MisZsPresenter(Activity activity,IMisZssearchContract.View view) {
        this.activity=activity;
        this.view = view;
    }

    @Override
    public void Zssearch(String token, String key) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", token);
        params.put("key", key);
        System.out.println(params);
        EasyAPI.apiConnectionAsync(activity, true, false, ApiMethodDescription.get("/ServiceMis.asmx/GetCertificateInfoByNum"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                System.out.println(result);
                Miszhengshu miszhengshu = GsonUtil.JsonToObject(result.toString(), Miszhengshu.class);
                view.ZssearchCall(true,miszhengshu);
            }

            @Override
            public String onFailed(ApiError error) {
                view.ZssearchCall(false,null);
                return null;
            }
        });
    }
}
