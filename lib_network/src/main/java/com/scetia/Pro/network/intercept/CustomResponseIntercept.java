package com.scetia.Pro.network.intercept;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.scetia.Pro.network.ConstantApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * created by ljs
 * on 2021/1/20
 */
public class CustomResponseIntercept implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //改造response
        String header = response.header("X-Pagination");
        if(request.url().toString().contains(ConstantApi.BUILD_SAND_BASE_URL)&&!TextUtils.isEmpty(header)){
            Gson gson = new Gson();
            JsonObject headerJson = gson.fromJson(header, JsonObject.class);
            int pageCount = headerJson.get("pageCount").getAsInt();
            ResponseBody body = response.body();
            if(body!=null) {
                String bodyStr =body.string();
                JsonObject jsonObject = gson.fromJson(bodyStr, JsonObject.class);
                jsonObject.addProperty("pageCount",pageCount);
                response = response.newBuilder().body(ResponseBody.create(MediaType.parse("application/json"),jsonObject.toString()))
                        .build();
            }
        }
        return response;
    }
}
