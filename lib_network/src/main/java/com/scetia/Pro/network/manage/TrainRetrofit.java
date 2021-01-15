package com.scetia.Pro.network.manage;

import com.scetia.Pro.network.ConstantApi;
import com.scetia.Pro.network.base.BaseRetrofitManage;

public class TrainRetrofit extends BaseRetrofitManage {

    private static TrainRetrofit instance;
    private TrainRetrofit(){
        super(ConstantApi.TRAINING_BASE_URL,1);
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

    public <T> T getService(Class<T> service){
        return retrofit.create(service);
    }

}
