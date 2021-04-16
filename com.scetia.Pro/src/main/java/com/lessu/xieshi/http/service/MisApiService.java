package com.lessu.xieshi.http.service;

import com.lessu.xieshi.module.mis.bean.EvaluationComparisonBean;
import com.lessu.xieshi.module.mis.bean.SealManageBean;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.module.mis.bean.CertificateBean;
import com.lessu.xieshi.module.mis.bean.MisAnnualLeaveData;
import com.lessu.xieshi.module.mis.bean.MisMemberSearchResultData;
import com.lessu.xieshi.module.mis.bean.NoticeBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * created by ljs
 * on 2020/11/26
 */
public interface MisApiService {
    /**
     * 获取会员信息数据
     *
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/GetMemberInfo")
    Observable<XSResultData<MisMemberSearchResultData>> search(@Query("param") String param);

    /**
     * 查看证书
     *
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/GetCertificateInfoByNum")
    Observable<XSResultData<CertificateBean>> searchCertificate(@Query("param") String param);

    /**
     * 获取通知信息数据
     *
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/GetMisTz")
    Observable<XSResultData<List<NoticeBean>>> getNotices(@Query("param") String param);

    /**
     * 获取年假信息数据
     *
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/NJQuery")
    Observable<XSResultData<MisAnnualLeaveData>> getMisAnnualLeave(@Query("param") String param);

    /**
     *用章管理申请列表默认加载
     */
    @GET("ServiceMis.asmx/YzQueryDefault")
    Observable<XSResultData<List<SealManageBean>>> getDefaultSealManageList(@Query("param") String param);

    /**
     * 根据条件查询用章管理申请列表
     * @param params
     * @return
     */
    @GET("ServiceMis.asmx/YzQuery")
    Observable<XSResultData<List<SealManageBean>>> getSealManageListByQuery(@Query("param") String params);

    /**
     * 获取用章类型
     * @return
     */
    @GET("ServiceMis.asmx/GetYzType")
    Observable<XSResultData<List<SealManageBean.SealType>>> getSealTypeList(@Query("param") String params);

    /**
     * 批准事项
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/YzApprove")
    Observable<XSResultData<Object>> approveSealMatter(@Query("param") String param);

    /**
     * 获取评估证书打印申请列表
     * @param params
     * @return
     */
    @GET("ServiceMis.asmx/ZSQuery")
    Observable<XSResultData<EvaluationComparisonBean>> getEvaluationComparisonByQuery(@Query("param") String params);

    /**
     * 获取评估证书打印申请列表
     * @param params
     * @return
     */
    @POST("ServiceMis.asmx/ZSApprove")
    @FormUrlEncoded
    Observable<XSResultData<Object>> postApproveEvaluationComparison(@Field("param") String params);
}
