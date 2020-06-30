package com.lessu.xieshi.http;

import com.lessu.xieshi.bean.CourseScore;
import com.lessu.xieshi.bean.PushToDx;
import com.lessu.xieshi.training.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {
    /**
     *获取guid
     * @param body
     * @return
     */
    @POST("thirdparty/jzjc/appInterfaceApi/updateUserCourse")
    public Observable<BaseResponse<PushToDx>> updateUserCourse(@Body RequestBody body);

    /**
     * 获取学习数据
     * 表单形式提交
     * @return
     */
    @POST("thirdparty/jzjc/appInterfaceApi/getUserCourseScore")
    @FormUrlEncoded
    public Observable<BaseResponse<List<CourseScore>>> getCourseScores(@Field("userId")String userId,
                                                                      @Field("projectCode")String projectCode,
                                                                      @Field("planNo")String planNo,
                                                                      @Field("timestamp")String timestamp,
                                                                      @Field("sign")String sign);

}
