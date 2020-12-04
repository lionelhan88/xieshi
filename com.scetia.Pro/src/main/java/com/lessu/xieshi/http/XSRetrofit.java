package com.lessu.xieshi.http;

import com.lessu.xieshi.base.BaseRetrofitManage;

/**
 * created by ljs
 * on 2020/11/25
 */
public class XSRetrofit extends BaseRetrofitManage {
    private static XSRetrofit instance;
    private XSRetrofit(){
        super("http://",0);
    }

    public static XSRetrofit getInstance(){
        if(instance==null){
            synchronized (XSRetrofit.class){
                if(instance==null){
                    instance = new XSRetrofit();
                }
            }
        }
        return instance;
    }

    public <T> T getService(Class<T> service){
        return retrofit.create(service);
    }

    public void changeBaseUrl(String newBaseUrl){
        retrofit=retrofit.newBuilder().baseUrl(newBaseUrl).build();
    }
}
