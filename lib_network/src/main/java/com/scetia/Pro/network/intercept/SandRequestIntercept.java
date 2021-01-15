package com.scetia.Pro.network.intercept;

import com.scetia.Pro.common.Util.Common;
import com.scetia.Pro.common.Util.SPUtil;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by ljs
 * on 2021/1/11
 */
public class SandRequestIntercept implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        if(request.url().toString().contains("http://api.scetia.com/")){
            //建设用砂管理的服务,需要添加统一的 Authorization
            Request.Builder newBuilder = request.newBuilder().
                    addHeader("Authorization", "Bearer " + SPUtil.getSPLSUtil(Common.JWT_KEY,""));
            response = chain.proceed(newBuilder.build());
        }else{
            response = chain.proceed(request);
        }
        return response;
    }
}
