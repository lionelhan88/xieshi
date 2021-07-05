package com.scetia.app_sand.repository;

import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.app_sand.bean.SandSalesTargetBean;
import com.scetia.app_sand.service.BuildSandApiService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * created by ljs
 * on 2020/12/21
 */
public class SandSalesQueryListRepository extends BaseRepository {

    public void querySales(int pageSize, int pageIndex, String queryUnitType, String queryUnitKey
    , ResponseObserver<List<SandSalesTargetBean>> calBack){
        requestApi( BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getSandSalesTargets(pageSize,pageIndex,queryUnitType,queryUnitKey, "SerialNo"),calBack);
    }

    public void addSanSalesTarget(List<SandSalesTargetBean> beans,
                                  List<String> delBeans,
                                  ResponseObserver<Object> callBack){
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            for(SandSalesTargetBean bean : beans){
                JSONObject obj = new JSONObject();
                obj.put("customerUnitMemberCode",bean.getSerialNo());
                array.put(obj);
            }
            jsonObject.put("toBeCreateCustomerUnits", array);
            JSONArray array1 = new JSONArray();
            for (String s:delBeans){
                array1.put(s);
            }
            jsonObject.put("toBeDeleteCustomerUnitIds", array1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());
            requestApi( BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                    .addSandSalesTarget(body),callBack);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
