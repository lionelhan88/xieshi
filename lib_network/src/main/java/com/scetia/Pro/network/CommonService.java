package com.scetia.Pro.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * created by Lollipop
 * on 2021/3/3
 */
public interface CommonService {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
