package com.lessu.xieshi.module.training;

import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class ApiObserver<T> implements Observer<T> {
    public abstract void success(T t);
    public abstract void failure(String errorMsg);
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
       if(t instanceof TrainingResultData) {
           TrainingResultData trainingResultData = (TrainingResultData) t;
           if (trainingResultData.getReturnCode().equals("S0000")) {
               //返回成功
               success(t);
           } else {
               failure(trainingResultData.getReturnMessage());
           }
       }else if(t instanceof ResponseBody){
           success(t);
       }
    }

    @Override
    public void onError(Throwable e) {
        String msg= "未知错误";
        if(e instanceof UnknownHostException){
            msg = "网络链接失败，请检查网路！";
        }else{
            msg = e.getLocalizedMessage();
        }
        failure(msg);
    }

    @Override
    public void onComplete() {

    }
}
