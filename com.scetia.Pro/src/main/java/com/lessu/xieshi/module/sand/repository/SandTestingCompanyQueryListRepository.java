package com.lessu.xieshi.module.sand.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.lessu.xieshi.module.sand.bean.TestingCompanyBean;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * created by ljs
 * on 2020/12/23
 */
public class SandTestingCompanyQueryListRepository {

    public void queryTestingCompanies(int pageSize,int pageIndex, String queryCounties, String queryKey,
                                      ResponseObserver<List<TestingCompanyBean>> callBack) {
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getTestingCompanies(pageSize,pageIndex,queryKey,queryCounties,"memberCode")
                .compose(BuildSandRetrofit.<BuildSandResultData<List<TestingCompanyBean>>, List<TestingCompanyBean>>applyTransformer())
                .subscribe(callBack);
    }

    public void addTestingCompanies(List<TestingCompanyBean> beans,
                                    List<String> delBeans,
                                    ResponseObserver<Object> callBack){
        JSONObject jsonObject = new JSONObject();
        List<AddedTestingCompanyBean> addedTestingCompanyBeans = new ArrayList<>();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            for(TestingCompanyBean bean : beans){
                AddedTestingCompanyBean addedTestingCompanyBean = new AddedTestingCompanyBean();
                addedTestingCompanyBean.setDetectionAgencyMemberCode(bean.getMemberCode());
                addedTestingCompanyBean.setDetectionAgencyContactPerson(bean.getContactPerson());
                addedTestingCompanyBean.setDetectionAgencyContactPersonPhoneNo(bean.getContactPersonPhoneNo());
                addedTestingCompanyBean.setDetectionAgencyCounties(bean.getCounties());
                addedTestingCompanyBean.setDetectionAgencyUnitAddress(bean.getUnitAddress());
                addedTestingCompanyBean.setDetectionAgencyUnitName(bean.getUnitName());
                addedTestingCompanyBeans.add(addedTestingCompanyBean);
            }
            JSONArray jsonArray = new JSONArray(gson.toJson(addedTestingCompanyBeans));
            jsonObject.put("toBeCreateDetectionAgencys", jsonArray);

            JSONArray array1 = new JSONArray();
            for (String s:delBeans){
                array1.put(s);
            }
            jsonObject.put("toBeDeleteDetectionAgencyIds", array1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());
            BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                    .addTestingCompanies(body)
                    .compose(BuildSandRetrofit.<BuildSandResultData<Object>, Object>applyTransformer())
                    .subscribe(callBack);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
