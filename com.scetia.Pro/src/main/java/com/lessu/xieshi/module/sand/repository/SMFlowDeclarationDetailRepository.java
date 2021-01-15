package com.lessu.xieshi.module.sand.repository;

import com.google.gson.Gson;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.http.api.SourceApiService;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.bean.SandItemParameterBean;
import com.lessu.xieshi.module.sand.bean.SandSampleBean;
import com.lessu.xieshi.module.sand.bean.SandSpecBean;
import com.scetia.Pro.network.manage.BuildSandRetrofit;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * created by ljs
 * on 2020/12/25
 */
public class SMFlowDeclarationDetailRepository {
    /**
     * 获取流向申报的具体数据
     */
    public void getFlowDeclarationInfo(String flowId, ResponseObserver<FlowDeclarationBean> callBack) {
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getSMFlowDeclarationInfo(flowId)
                .compose(BuildSandRetrofit.<BuildSandResultData<FlowDeclarationBean>, FlowDeclarationBean>applyTransformer())
                .subscribe(callBack);
    }

    public void getSandSamples(ResponseObserver<List<SandSampleBean>> callBack) {
        BuildSandRetrofit.getInstance().getService(SourceApiService.class).getItemSamples()
                .compose(BuildSandRetrofit.<BuildSandResultData<List<SandSampleBean>>, List<SandSampleBean>>applyTransformer())
                .subscribe(callBack);
    }

    public void getSpecAndParameters(String sampleId, ResponseObserver<List<? extends Object>> callBack) {
        //建设用砂检测参数需要先选择建设用砂名称，根据sampleId来获取数据
        Observable<BuildSandResultData<List<SandItemParameterBean>>> itemParameters =
                BuildSandRetrofit.getInstance().getService(SourceApiService.class).getItemParameters(sampleId)
                        .subscribeOn(Schedulers.io());
        //建设用砂规格
        Observable<BuildSandResultData<List<SandSpecBean>>> itemSpecs =
                BuildSandRetrofit.getInstance().getService(SourceApiService.class).getItemSpecs(sampleId)
                        .subscribeOn(Schedulers.io());
        Observable.merge(itemParameters, itemSpecs)
                .compose(BuildSandRetrofit.<BuildSandResultData<? extends List<?>>, List<Object>>applyTransformer())
                .subscribe(callBack);
    }

    public void saveFlowDeclaration(FlowDeclarationBean flowDeclarationBean, ResponseObserver<FlowDeclarationBean> callBack) {
        Observable<BuildSandResultData<FlowDeclarationBean>> request;
        Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(flowDeclarationBean));
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .addSMFlowDeclaration(body)
                .compose(BuildSandRetrofit.<BuildSandResultData<FlowDeclarationBean>, FlowDeclarationBean>applyTransformer())
                .subscribe(callBack);
    }

    public void updateFlowDeclaration(FlowDeclarationBean flowDeclarationBean, ResponseObserver<Response<ResponseBody>> callBack){
        Observable<BuildSandResultData<FlowDeclarationBean>> request;
        Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(flowDeclarationBean));
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .putSMFlowDeclarationInfo(flowDeclarationBean.getId(),body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }
}
