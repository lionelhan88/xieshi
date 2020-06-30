package com.lessu.xieshi.training;

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
       if(t instanceof BaseResponse) {
           BaseResponse baseResponse = (BaseResponse) t;
           if (baseResponse.getReturnCode().equals("S0000")) {
               //返回成功
               success(t);
           } else {
               failure(baseResponse.getReturnMessage());
           }
       }else if(t instanceof ResponseBody){
           success(t);
       }
    }

    @Override
    public void onError(Throwable e) {
        failure(e.getLocalizedMessage());
    }

    @Override
    public void onComplete() {

    }
}
