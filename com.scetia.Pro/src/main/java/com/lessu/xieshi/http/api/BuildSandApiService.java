package com.lessu.xieshi.http.api;

import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.bean.TestingQueryResultBean;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;
import com.lessu.xieshi.module.sand.bean.SandSupplierBean;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.lessu.xieshi.module.sand.bean.TestingCompanyBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * created by ljs
 * on 2020/12/21
 * 用砂管理相关服务的api
 */
public interface BuildSandApiService {
    //供应商列表
    @GET("gravel.app/unitProductionUnit")
    Observable<BuildSandResultData<List<SandSupplierBean>>> getSuppliers();

    //已添加的销售对象列表
    @GET("gravel.app/customerUnit")
    Observable<BuildSandResultData<List<AddedSandSalesTargetBean>>> getAddedSandSalesTargets(
            @Query("pageSize") int pageSize,
            @Query("pageIndex") int pageIndex,
            @Query("orderBy") String orderBy);

    //查询销售对象列表
    @GET("gravel.app/salestarget")
    Observable<BuildSandResultData<List<SandSalesTargetBean>>> getSandSalesTargets(
            @Query("pageSize") int pageSize,
            @Query("pageIndex") int pageIndex,
            @Query("unitType") String unitType,
            @Query("unitName") String unitName,
            @Query("orderBy") String orderBy);


    //添加销售对象
    @POST("gravel.app/customerUnit/s")
    Observable<BuildSandResultData<Object>>
    addSandSalesTarget(@Body RequestBody requestBody);

    //删除单一的销售对象
    @DELETE("gravel.app/customerUnit/{id}")
    Observable<Response<ResponseBody>> delAddedSanSalesTarget(@Path("id") String id);

    //获取所有的检测单位
    @GET("gravel.app/detectionunit")
    Observable<BuildSandResultData<List<TestingCompanyBean>>> getTestingCompanies(
            @Query("pageSize") int pageSize,
            @Query("pageIndex") int pageIndex,
            @Query("unitName") String unitName,
            @Query("counties") String counties,
            @Query("orderBy") String orderBy);

    //添加检测单位
    @POST("gravel.app/DetectionAgency/s")
    Observable<BuildSandResultData<Object>>
    addTestingCompanies(@Body RequestBody requestBody);

    //获取已经添加过的检测单位
    @GET("gravel.app/detectionAgency")
    Observable<BuildSandResultData<List<AddedTestingCompanyBean>>> getAddedTestingCompanies(
            @Query("pageSize") int pageSize,
            @Query("pageIndex") int pageIndex,
            @Query("orderBy") String orderBy);

    //删除已经添加的检测单位
    @DELETE("gravel.app/detectionAgency/{id}")
    Observable<Response<ResponseBody>> delAddedTestingCompany(@Path("id") String id);


    //流向申报记录列表
    @GET("gravel.app/flowinfo")
    Observable<BuildSandResultData<List<FlowDeclarationBean>>> getSMFlowDeclarations(
            @Query("pageSize") int pageSize,
            @Query("pageIndex") int pageIndex,
            @Query("orderBy") String orderBy);

    //获取流向申报的详情
    @GET("gravel.app/flowinfo/{flowId}")
    Observable<BuildSandResultData<FlowDeclarationBean>> getSMFlowDeclarationInfo(@Path("flowId") String flowId);

    //添加流向申报记录
    @POST("gravel.app/flowInfo")
    Observable<BuildSandResultData<FlowDeclarationBean>> addSMFlowDeclaration(@Body RequestBody bean);

    //更新流向申报记录
    @PUT("gravel.app/flowinfo/{flowId}")
    Observable<Response<ResponseBody>> putSMFlowDeclarationInfo(@Path("flowId") String flowId,
                                                                @Body RequestBody bean);

    //删除流向申报记录
    @DELETE("gravel.app/flowInfo/{flowId}")
    Observable<Response<ResponseBody>> delSMFlowDeclaration(@Path("flowId") String flowId);


    //获取检测委托的列表
    @GET("gravel.app/consigninfo")
    Observable<BuildSandResultData<List<TestingCommissionBean>>> getTestingCommissions(
            @Query("pageSize") int pageSize,
            @Query("pageIndex") int pageIndex,
            @Query("orderBy") String orderBy);

    //新增检测委托
    @POST("gravel.app/consignInfo")
    Observable<BuildSandResultData<TestingCommissionBean>> addTestingCommission(@Body RequestBody body);

    //删除检测委托
    @DELETE("gravel.app/consigninfo{consignInfoId}")
    Observable<Response<ResponseBody>> delTestingCommission(@Path("consignInfoId") String id);

    //获取检测结果数据列表
    @GET("gravel.infoquery/flowconsigninfo")
    Observable<BuildSandResultData<List<TestingQueryResultBean>>> getTestingInfoResultData(
            @Query("orderBy") String orderBy,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("sampleStatus") String status);

    @GET("gravel.infoquery/flowconsigninfo/{flowConsignInfoId}")
    Observable<BuildSandResultData<TestingQueryResultBean>> getTestingInfoResultDetail(@Path("flowConsignInfoId") String flowConsignInfoId);

}
