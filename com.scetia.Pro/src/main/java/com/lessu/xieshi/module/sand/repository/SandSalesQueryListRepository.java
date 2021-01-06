package com.lessu.xieshi.module.sand.repository;

import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.BuildSandRetrofit;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * created by ljs
 * on 2020/12/21
 */
public class SandSalesQueryListRepository {

    public void querySales(int pageSize, int pageIndex, String queryUnitType, String queryUnitKey
    , ResponseObserver<List<SandSalesTargetBean>> calBack){
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getSandSalesTargets(pageSize,pageIndex,queryUnitType,queryUnitKey, "SerialNo")
                .compose(BuildSandRetrofit.<BuildSandResultData<List<SandSalesTargetBean>>,List<SandSalesTargetBean>>applyTransformer())
                .subscribe(calBack);
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
            BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                    .addSandSalesTarget(body)
                    .compose(BuildSandRetrofit.<BuildSandResultData<Object>,
                            Object>applyTransformer())
                    .subscribe(callBack);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
