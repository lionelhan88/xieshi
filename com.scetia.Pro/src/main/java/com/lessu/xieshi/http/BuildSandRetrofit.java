package com.lessu.xieshi.http;

import com.lessu.xieshi.base.BaseRetrofitManage;

/**
 * created by ljs
 * on 2020/12/21
 * 建设用砂
 */
public class BuildSandRetrofit extends BaseRetrofitManage {
    private static BuildSandRetrofit instance;
    private static final String BASE_URL = "http://api.scetia.com/";
    public BuildSandRetrofit() {
        super(BASE_URL,1);
    }

    public static BuildSandRetrofit getInstance(){
        if(instance==null){
            synchronized (TrainRetrofit.class){
                if(instance==null){
                    instance = new BuildSandRetrofit();
                }
            }
        }
        return instance;
    }

    public <T> T getService(Class<T> service){
        return retrofit.create(service);
    }
}
