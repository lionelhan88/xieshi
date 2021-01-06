package com.lessu.xieshi.base;

import com.lessu.foundation.LSUtil;
import com.lessu.xieshi.BuildConfig;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.http.XSConverterJson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
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
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response;
                        if(request.url().toString().contains("http://api.scetia.com/")){
                            //建设用砂管理的服务
                            Request.Builder newBuilder = request.newBuilder().
                                    addHeader("Authorization", "Bearer " + LSUtil.valueStatic(Common.JWT_KEY));
                            response = chain.proceed(newBuilder.build());
                        }else{
                            response = chain.proceed(request);
                        }
                        if(BuildConfig.DEBUG) {
                            LogUtil.showLogD(request.url().toString());
                            LogUtil.showLogD(getResponseInfo(response));
                        }
                        return response;
                    }
                }).build();


        if(type==0){
            String service = Common.serviceMap.get(LSUtil.valueStatic("service"));
            retrofit = new Retrofit.Builder().baseUrl(baseUrl+service+"/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(XSConverterJson.create())
                    .build();
        }else if(type==1){
            retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

    }
    public static <E,T> ObservableTransformer<E,T> applyTransformer(){
       return new ObservableTransformer<E, T>() {
            @Override
            public ObservableSource<T> apply(Observable<E> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(e -> {
                            if (e instanceof XSResultData) {
                                XSResultData data = (XSResultData) e;
                                //协实服务返回的数据,判断是否有成功的数返回
                                if (data.isSuccess()) {
                                    //TODO:将数据继续返回给 前台
                                    return (T) data.getData();
                                } else {
                                    //数据返回失败
                                    //TODO:直接抛出异常，返回失败数据
                                    throw new ExceptionHandle.ResultException(((XSResultData) e).getCode(), ((XSResultData) e).getMessage());
                                }
                            }else if (e instanceof BuildSandResultData){
                                // 用砂管理的数据
                                BuildSandResultData<T> resultData = (BuildSandResultData) e;
                                if(resultData.getStatusCode()==200||resultData.getStatusCode()==201){
                                    return (T) resultData.getResult();
                                } else if(resultData.getStatusCode()==500){
                                    //添加数据失败
                                    //TODO:直接抛出异常，返回失败数据
                                    throw new ExceptionHandle.ResultException(((BuildSandResultData) e).getStatusCode(),"保存数据失败!");
                                } else{
                                    //数据返回失败
                                    //TODO:直接抛出异常，返回失败数据
                                    throw new ExceptionHandle.ResultException(((BuildSandResultData) e).getStatusCode(), ((BuildSandResultData) e).getMessage());
                                }
                            }
                            //暂时没有其他类型的数据，直接返回null
                            return (T)e;
                        });
            }
        };
    }

    /**
     * 打印返回的数据
     * @param response
     * @return
     */
    private String getResponseInfo(Response response){
        String responseInfo = "====";
        if(response==null){
            return responseInfo;
        }
        ResponseBody body = response.body();
        long contentLength =  body.contentLength();
        BufferedSource source = body.source();
        try {
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = Charset.forName("utf-8");
            if(contentLength!=0){
                responseInfo = buffer.clone().readString(charset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseInfo;
    }
}
