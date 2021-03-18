package com.lessu.xieshi.http.service;

import com.scetia.Pro.network.bean.BuildSandResultData;
import com.lessu.xieshi.module.sand.bean.SandItemParameterBean;
import com.lessu.xieshi.module.sand.bean.SandSampleBean;
import com.lessu.xieshi.module.sand.bean.SandSpecBean;
import com.lessu.xieshi.module.sand.bean.SandSamplerBean;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.scetia.Pro.network.bean.XSResultData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * created by ljs
 * on 2020/12/24
 */
public interface SourceApiService {
    //建设用砂参数列表
    @GET("gravel.app/itemSource/parameter/{sampleId}")
    Observable<BuildSandResultData<List<SandItemParameterBean>>> getItemParameters(@Path("sampleId") String sampleId);

    //建设用砂样品列表
    @GET("gravel.app/itemSource/sample")
    Observable<BuildSandResultData<List<SandSampleBean>>> getItemSamples();

    //建设用砂规格
    @GET("gravel.app/itemSource/spec/{sampleId}")
    Observable<BuildSandResultData<List<SandSpecBean>>> getItemSpecs(@Path("sampleId") String sampleId);

    //获取委托取样地点
    @GET("gravel.app/samplingLocation")
    Observable<BuildSandResultData<List<TestingCommissionBean.SampleLocation>>> getSampleLocation();

    //供应商取样人员
    @GET("gravel.app/samplerCer/supplier")
    Observable<BuildSandResultData<List<SandSamplerBean>>> getSupplierSampler();

    //检测单位取样人员
    @GET("gravel.app/samplerCer/detection")
    Observable<BuildSandResultData<List<SandSamplerBean>>> getTestingSampler(@Query("memberCode") String memberCode);

    //唯一性标识资源
    @GET("/scetia.app.ws/ServiceShashi.asmx/GetTMList")
    Observable<XSResultData<List<TestingCommissionBean.CCNoBean>>> getCCNos(@Query("param") String param);
}
