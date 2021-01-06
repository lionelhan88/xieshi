package com.lessu.xieshi.http.api;

import com.lessu.xieshi.bean.ReportContentBean;
import com.lessu.xieshi.bean.TodayStatisticsBean;
import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.module.unqualified.bean.ConstructionData;
import com.lessu.xieshi.module.unqualified.bean.ReportConclusionBean;
import com.lessu.xieshi.module.unqualified.bean.TestingReportData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by ljs
 * on 2020/12/7
 */
public interface CommonApiService {
    /**
     * 今日统计
     *
     * @param param
     * @return
     */
    @GET("ServiceTS.asmx/DetectionTodayStatis")
    Observable<XSResultData<TodayStatisticsBean>> getDetectionTodayStatistics(@Query("param") String param);

    /**
     * 不合格信息检测报告
     *
     * @param param
     * @return
     */
    @GET("ServiceUQ.asmx/ReportList")
    Observable<XSResultData<TestingReportData>> getUnqualifiedTestingReportData(@Query("param") String param);

    /**
     * 不合格信息工程列表
     *
     * @param param
     * @return
     */
    @GET("ServiceUQ.asmx/ReportList")
    Observable<XSResultData<ConstructionData>> getUnqualifiedConstructionData(@Query("param") String param);

    /**
     * 不合格信息检测报告内容
     */
    @GET("ServiceUQ.asmx/ProjectConsign")
    Observable<XSResultData<ReportContentBean>> getUnqualifiedTestingReportContent(@Query("param") String param);

    /**
     * 不合格信息报告结论
     */
    @GET("ServiceUQ.asmx/SampleList")
    Observable<XSResultData<List<ReportConclusionBean>>> getReportConclusion(@Query("param") String param);
}
