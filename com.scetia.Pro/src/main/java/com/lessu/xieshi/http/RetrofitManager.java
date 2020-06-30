package com.lessu.xieshi.http;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    public static final String BASE_URL="https://bgtj.o-learn.cn/";
    private static RetrofitManager instance;
    private API service;
    private RetrofitManager(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service=retrofit.create(API.class);
    }
    public static RetrofitManager getInstance(){
        if(instance==null){
            synchronized (RetrofitManager.class){
                if(instance==null){
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }
    public API getService(){
        return service;
    }
}
