package com.lessu.xieshi.login;

import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.login.bean.LoginUserBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by ljs
 * on 2020/11/25
 */
public interface LoginApiService {

    @GET("ServiceUST.asmx/UserLogin")
    public Observable<XSResultData<LoginUserBean>> login(@Query("param") String param);
}
