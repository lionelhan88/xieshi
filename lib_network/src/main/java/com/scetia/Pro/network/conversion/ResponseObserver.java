package com.scetia.Pro.network.conversion;



import com.scetia.Pro.network.bean.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * created by ljs
 * on 2020/11/25
 * 处理服务返回的完整数据，我们只需要有效的数据，其他
 */
public abstract class ResponseObserver<T> implements Observer<T> {
    private Disposable disposable;
    public abstract void success(T t);
    public abstract void failure(ExceptionHandle.ResponseThrowable throwable);
    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
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

    public Disposable getDisposable() {
        return disposable;
    }
}
