package com.scetia.Pro.network.manage;


import com.scetia.Pro.network.ConstantApi;
import com.scetia.Pro.network.base.BaseRetrofitManage;

/**
 * created by ljs
 * on 2020/12/21
 * 建设用砂
 */
public class BuildSandRetrofit extends BaseRetrofitManage {
    private static BuildSandRetrofit instance;
    public BuildSandRetrofit() {
        super(ConstantApi.BUILD_SAND_BASE_URL,1);
    }

    public static BuildSandRetrofit getInstance(){
        if(instance==null){
            synchronized (BuildSandRetrofit.class){
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
