package com.lessu.xieshi.http.service;

import com.lessu.xieshi.bean.ReportContentBean;
import com.lessu.xieshi.bean.TodayStatisticsBean;
import com.lessu.xieshi.module.foundationpile.FoundationPileBean;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.module.unqualified.bean.ConstructionData;
import com.lessu.xieshi.module.unqualified.bean.ReportConclusionBean;
import com.lessu.xieshi.module.unqualified.bean.TestingReportData;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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


    //基装静载

    /**
     * 获取基装静载数据列表
     * @param param
     * @return
     */
    @GET("ServiceStake.asmx/GetMapList")
    Observable<XSResultData<List<FoundationPileBean>>> getFoundationPile(@Query("param") String param);

}
