package com.lessu.xieshi.module.mis.api;

import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.module.mis.bean.CertificateBean;
import com.lessu.xieshi.module.mis.bean.MisAnnualLeaveData;
import com.lessu.xieshi.module.mis.bean.MisHySearchResultData;
import com.lessu.xieshi.module.mis.bean.NoticeBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by ljs
 * on 2020/11/26
 */
public interface MisApiService {
    /**
     * 获取会员信息数据
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/GetMemberInfo")
    public Observable<XSResultData<MisHySearchResultData>> search(@Query("param") String param);

    /**
     * 查看证书
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/GetCertificateInfoByNum")
    public Observable<XSResultData<CertificateBean>> searchCertificate(@Query("param") String param);

    /**
     * 获取通知信息数据
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/GetMisTz")
    public Observable<XSResultData<List<NoticeBean>>> getNotices(@Query("param") String param);

    /**
     * 获取年假信息数据
     * @param param
     * @return
     */
    @GET("ServiceMis.asmx/NJQuery")
    public Observable<XSResultData<MisAnnualLeaveData>> getMisAnnualLeave(@Query("param") String param);
}
