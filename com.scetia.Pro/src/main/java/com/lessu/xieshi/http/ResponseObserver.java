package com.lessu.xieshi.http;

import com.lessu.xieshi.http.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * created by ljs
 * on 2020/11/25
 * 处理服务返回的完整数据，我们只需要有效的数据，其他
 */
public abstract class ResponseObserver<T> implements Observer<T> {
    public abstract void success(T t);
    public abstract void failure(ExceptionHandle.ResponseThrowable throwable);
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        success(t);
    }

    @Override
    public void onError(Throwable e) {
        ExceptionHandle.ResponseThrowable responseThrowable = ExceptionHandle.handleException(e);
        failure(responseThrowable);
    }

    @Override
    public void onComplete() {

    }
}
