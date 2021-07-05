package com.scetia.app_sand.repository;

import com.google.gson.JsonObject;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.photo.ImageUtil;
import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.BuildSandRetrofit;
import com.scetia.Pro.network.manage.XSRetrofit;
import com.scetia.app_sand.bean.SandSamplerBean;
import com.scetia.app_sand.bean.TestingCommissionBean;
import com.scetia.app_sand.service.BuildSandApiService;
import com.scetia.app_sand.service.SourceApiService;

import java.util.List;

import io.reactivex.Observable;

/**
 * created by ljs
 * on 2020/12/29
 */
public class TestingCommissionRepository extends BaseRepository {
    /**
     * 获取检测委托列表
     *
     * @param pageIndex 当前页数
     * @param callBack  请求回调接口
     */
    public void getTestingCommissions(int pageIndex, ResponseObserver<List<TestingCommissionBean>> callBack) {
        requestApi(BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getTestingCommissions(20, pageIndex, "commissionDate desc"), callBack);
    }

    /**
     * 删除检测委托项
     *
     * @param id
     * @param callBack
     */
    public void delTestingCommission(String id, ResponseObserver<Object> callBack) {
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .delTestingCommission(id)
                .compose(BuildSandRetrofit.applyTransformer())
                .subscribe(callBack);
    }

    /**
     * 获取检测委托的详情
     *
     * @param singleId
     * @param callBack
     */
    public void getSingleInfo(String singleId, ResponseObserver<TestingCommissionBean> callBack) {
        requestApi(BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getTestingCommissionInfo(singleId), callBack);
    }

    /**
     * 获取取样地点资源
     *
     * @param callBack
     */
    public void getSampleLocation(ResponseObserver<List<TestingCommissionBean.SampleLocation>> callBack) {
        requestApi(BuildSandRetrofit.getInstance().getService(SourceApiService.class)
                .getSampleLocation(), callBack);
    }

    /**
     * 获取取样人信息
     *
     * @param memberCode
     * @param callBack
     */
    public void getSamplers(String memberCode, ResponseObserver<List<SandSamplerBean>> callBack) {
        SourceApiService service = BuildSandRetrofit.getInstance().getService(SourceApiService.class);
        Observable<BuildSandResultData<List<SandSamplerBean>>> supplierSampler;
        if (memberCode == null) {
            supplierSampler = service.getSupplierSampler();
        } else {
            supplierSampler = service.getTestingSampler(memberCode);
        }
        requestApi(supplierSampler.compose(BuildSandRetrofit.<BuildSandResultData<List<SandSamplerBean>>,
                List<SandSamplerBean>>applyTransformer()), callBack);
    }

    /**
     * 获取唯一性标识
     * @param key 模糊查询关键字
     * @param callBack 请求回调接口
     */
    public void getCCNos(String key,ResponseObserver<List<TestingCommissionBean.CCNoBean>> callBack) {
        JsonObject param = new JsonObject();
        param.addProperty("UnitId", Constants.User.GET_BOUND_UNIT_ID());
        param.addProperty("key",key);
        requestApi(XSRetrofit.getInstance().getService(SourceApiService.class)
                .getCCNos(param.toString()), callBack);
    }

    public void uploadSamplingProcess(String id, int imageIndex, String photoPath, ResponseObserver<String> callBack) {
        JsonObject param = new JsonObject();
        param.addProperty("ID", id);
        param.addProperty("ImgIndex", imageIndex);
        param.addProperty("ImgByte", ImageUtil.imageToBase64ByPath(photoPath));
        requestApi(XSRetrofit.getInstance().getService(BuildSandApiService.class)
                .uploadSamplingProcess(param.toString()), callBack);
    }

}
