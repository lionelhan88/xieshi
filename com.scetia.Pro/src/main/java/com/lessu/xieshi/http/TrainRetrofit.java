package com.lessu.xieshi.http;

import com.lessu.xieshi.base.BaseRetrofitManage;
import com.lessu.xieshi.http.api.TraningApiService;

public class TrainRetrofit extends BaseRetrofitManage {
    public static final String BASE_URL="https://bgtj.o-learn.cn/";
    private static TrainRetrofit instance;
    private TrainRetrofit(){
        super(BASE_URL,1);
    }
    public static TrainRetrofit getInstance(){
        if(instance==null){
            synchronized (TrainRetrofit.class){
                if(instance==null){
                    instance = new TrainRetrofit();
                }
            }
        }
        return instance;
    }
    public TraningApiService getService(){
        return retrofit.create(TraningApiService.class);
    }
}
