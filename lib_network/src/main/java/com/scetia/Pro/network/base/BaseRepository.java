package com.scetia.Pro.network.base;

import com.scetia.Pro.network.conversion.ResponseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * created by ljs
 * on 2021/2/3
 */
public class BaseRepository {
    //装载所有的订阅
    private List<ResponseObserver<?>> responseObservers = new ArrayList<>();

    protected <E,T> void requestApi(Observable<E> observable, ResponseObserver<T> responseObserver){
        responseObservers.add(responseObserver);
        observable.compose(BaseRetrofitManage.<E,T>applyTransformer()).subscribe(responseObserver);
    }

    /**
     * 清除所有未完成的网络请求
     */
    public void cancelAllRequest(){
        for (ResponseObserver<?> responseObserver:responseObservers){
            Disposable disposable = responseObserver.getDisposable();
            if(disposable!=null&&!disposable.isDisposed()){
                disposable.dispose();
            }
        }
        responseObservers.clear();
    }
}
