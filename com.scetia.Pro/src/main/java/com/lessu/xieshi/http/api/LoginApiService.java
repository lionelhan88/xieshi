package com.lessu.xieshi.http.api;

import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.lessu.xieshi.module.login.bean.ValidateCodeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by ljs
 * on 2020/11/25
 */
public interface LoginApiService {

    //登陆请求
    @GET("ServiceUST.asmx/UserLogin")
     Observable<XSResultData<LoginUserBean>> login(@Query("param") String param);

    //获取验证码
    @GET("ServiceUST.asmx/User_BindStart")
     Observable<XSResultData<ValidateCodeBean>> getVerificationCode(@Query("param") String param);

     //验证手机
    @GET("ServiceUST.asmx/User_BindEnd")
    Observable<XSResultData<LoginUserBean>> validateVerificationCode(@Query("param") String param);

}
