package com.scetia.Pro.network.base;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scetia.Pro.common.Util.Common;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;
import com.scetia.Pro.network.BuildConfig;
import com.scetia.Pro.network.conversion.XSConverterJson;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.intercept.SandRequestIntercept;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.Pro.network.response.CommonResultData;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * created by ljs
 * on 2020/11/25
 */
public abstract class BaseRetrofitManage {

    protected Retrofit retrofit;
    public BaseRetrofitManage(){

    }

    public BaseRetrofitManage(String baseUrl,int type){
        switch (type){
            case 0:
                String service = Common.getService(SPUtil.getSPLSUtil("service",""));
                retrofit = new Retrofit.Builder().baseUrl(baseUrl+service+"/")
                        .client(getOkHttpClient())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(XSConverterJson.create())
                        .build();
                break;
            case 1:
                retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                        .client(getOkHttpClient())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                break;
        }
    }

    /**
     * 获取OkHttpClient对象
     * @return
     */
    private OkHttpClient getOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .addInterceptor(new SandRequestIntercept());

        //只有调试模式才会打印返回的日志信息
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d("TAG_SCETIA",message));
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        return builder.build();
    }

    protected static <E,T> Function<E,T> getApiErrorHandler(){
        return new Function<E, T>() {
            @Override
            public T apply(E e) throws Exception {
                if (e instanceof XSResultData) {
                    //协实服务的返回数据
                    CommonResultData<T> commonResultData = new CommonResultData<>((XSResultData<T>) e);
                    return commonResultData.handleData();
                }else if (e instanceof BuildSandResultData){
                    // 用砂管理的数据
                    CommonResultData<T> commonResultData = new CommonResultData<>((BuildSandResultData<T>) e);
                    return commonResultData.handleData();
                } else if(e instanceof Response){
                    //注：后台有些服务没有返回body体，只返回的response原始数据，故获取Response判断 code码
                    Response response = (Response) e;
                    if(response.code()==204){
                        //操作成功
                        return (T) Integer.valueOf(response.code());
                    }else{
                        ResponseBody body = response.errorBody();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(body.string(), JsonObject.class);
                        JsonElement jsonElement = jsonObject.getAsJsonObject("responseException").get("exceptionMessage");
                        throw new ExceptionHandle.ResultException(response.code(), jsonElement.toString());
                    }
                }
                return (T)e;
            }
        };
    }

    public static <E,T> ObservableTransformer<E, T> applyTransformer(){
       return new ObservableTransformer<E, T>() {
            @Override
            public ObservableSource<T> apply(Observable<E> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(getApiErrorHandler());
            }
        };
    }

}
