package com.lessu.xieshi.http.api;

import com.lessu.xieshi.module.training.bean.CourseScore;
import com.lessu.xieshi.module.training.bean.PushToDx;
import com.lessu.xieshi.module.training.TrainingResultData;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TraningApiService {
    /**
     *获取guid
     * @param body
     * @return
     */
    @POST("thirdparty/jzjc/appInterfaceApi/updateUserCourse")
    public Observable<TrainingResultData<PushToDx>> updateUserCourse(@Body RequestBody body);

    /**
     * 获取学习数据
     * 表单形式提交
     * @return
     */
    @POST("thirdparty/jzjc/appInterfaceApi/getUserCourseScore")
    @FormUrlEncoded
    public Observable<TrainingResultData<List<CourseScore>>> getCourseScores(@Field("userId")String userId,
                                                                             @Field("projectCode")String projectCode,
                                                                             @Field("planNo")String planNo,
                                                                             @Field("timestamp")String timestamp,
                                                                             @Field("sign")String sign);

}
