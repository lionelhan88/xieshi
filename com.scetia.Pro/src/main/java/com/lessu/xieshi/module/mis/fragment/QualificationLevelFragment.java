package com.lessu.xieshi.module.mis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lessu.navigation.NavigationBar;
import com.lessu.xieshi.R;
import com.lessu.xieshi.http.service.MisApiService;
import com.lessu.xieshi.module.mis.bean.CertificateBean;
import com.lessu.xieshi.module.mis.bean.MemberQualificationLevel;
import com.lessu.xieshi.module.mis.bean.MisMemberSearchResultData;
import com.lessu.xieshi.utils.GsonUtil;
import com.scetia.Pro.baseapp.fragment.BaseFragment;
import com.scetia.Pro.baseapp.fragment.LazyFragment;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.XSRetrofit;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * created by Lollipop
 * on 2022/1/13
 * 资质等级
 */
public class QualificationLevelFragment extends BaseFragment {
    @BindView(R.id.member_ql_category)
    TextView memberQlCategory;
    @BindView(R.id.member_ql_building_materials)
    TextView memberQlBuildingMaterials;
    @BindView(R.id.member_ql_basement)
    TextView memberQlBasement;
    @BindView(R.id.member_ql_structure)
    TextView memberQlStructure;
    @BindView(R.id.member_ql_steel_structure)
    TextView memberQlSteelStructure;
    @BindView(R.id.member_ql_indoor_env)
    TextView memberQlIndoorEnv;
    @BindView(R.id.member_ql_curtain_energy)
    TextView memberQlCurtainEnergy;
    @BindView(R.id.member_ql_curtain_window)
    TextView memberQlCurtainWindow;
    @BindView(R.id.member_ql_ventilation)
    TextView memberQlVentilation;

    @BindView(R.id.member_ql_indoor_great)
    ImageView memberQlIndoorGreat;
    @BindView(R.id.member_ql_efficiency)
    ImageView memberQlEfficiency;
    @BindView(R.id.member_ql_deformation_quality)
    ImageView memberQlDeformationQuality;
    @BindView(R.id.member_ql_static_electricity)
    ImageView memberQlStaticElectricity;
    @BindView(R.id.member_ql_greening)
    ImageView memberQlGreening;

    @BindView(R.id.content_linear)
    ScrollView contentLinear;
    @BindView(R.id.no_qualification_level)
    TextView noQualificationLevel;
    @BindView(R.id.content_loading_layout)
    RelativeLayout loadingLayout;
    @Override
    protected boolean isNeedDispatchKeyBack() {
        return  false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qualification_level;
    }

    @Override
    protected void initView() {
        EventBusUtil.register(this);
    }

    @Override
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }

    /**
     * 初始化数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void loadData(GlobalEvent<MisMemberSearchResultData.ListContentBean> event) {
        if (event.getCode() == EventBusUtil.E) {
            Map<String, Object> param = new HashMap<>();
            param.put("s1", event.getData().getMemberId());
            request(GsonUtil.mapToJsonStr(param));
        }
    }

    private void request(String param){
        loadingLayout.setVisibility(View.VISIBLE);
        XSRetrofit.getInstance().getService(MisApiService.class)
                .getMemberNl(param)
                .compose(XSRetrofit.<XSResultData<List<MemberQualificationLevel>>, List<MemberQualificationLevel>>applyTransformer())
                .subscribe(new ResponseObserver<List<MemberQualificationLevel>>() {
                               @Override
                               public void success(List<MemberQualificationLevel> memberQualificationLevels) {
                                   loadingLayout.setVisibility(View.GONE);
                                   if(memberQualificationLevels.size()==0){
                                       //暂无等级
                                       noQualificationLevel.setVisibility(View.VISIBLE);
                                       contentLinear.setVisibility(View.GONE);
                                       return;
                                   }
                                   noQualificationLevel.setVisibility(View.GONE);
                                   contentLinear.setVisibility(View.VISIBLE);
                                   MemberQualificationLevel memberQualificationLevel = memberQualificationLevels.get(0);
                                   setBeanInCom(memberQualificationLevel);
                               }

                               @Override
                               public void failure(ExceptionHandle.ResponseThrowable throwable) {
                                    loadingLayout.setVisibility(View.GONE);
                                   noQualificationLevel.setVisibility(View.VISIBLE);
                                   noQualificationLevel.setText(throwable.message);
                               }
                           }
                );
    }

    private void setBeanInCom( MemberQualificationLevel memberQualificationLevel){
        memberQlCategory.setText(memberQualificationLevel.getLevelType());
        memberQlBuildingMaterials.setText(memberQualificationLevel.getBuildingMatcrial());
        memberQlBasement.setText(memberQualificationLevel.getBaiscFoundation());
        memberQlStructure.setText(memberQualificationLevel.getMajorStructure());
        memberQlSteelStructure.setText(memberQualificationLevel.getSteelStructure());
        memberQlIndoorEnv.setText(memberQualificationLevel.getIndoorClimate());
        memberQlCurtainEnergy.setText(memberQualificationLevel.getEnergySavingOfBuilding());
        memberQlCurtainWindow.setText(memberQualificationLevel.getDoorWindowCurtainWall());
        memberQlVentilation.setText(memberQualificationLevel.getVentilatedAirCondition());
        if(memberQualificationLevel.getS1()){
            memberQlIndoorGreat.setImageResource(R.drawable.xuanzhongmis);
        }
        if(memberQualificationLevel.getS1()){
            memberQlIndoorGreat.setImageResource(R.drawable.xuanzhongmis);
        }
        if(memberQualificationLevel.getS2()){
            memberQlEfficiency.setImageResource(R.drawable.xuanzhongmis);
        }
        if(memberQualificationLevel.getS3()){
            memberQlDeformationQuality.setImageResource(R.drawable.xuanzhongmis);
        }
        if(memberQualificationLevel.getS4()){
            memberQlStaticElectricity.setImageResource(R.drawable.xuanzhongmis);
        }
        if(memberQualificationLevel.getS5()){
            memberQlGreening.setImageResource(R.drawable.xuanzhongmis);
        }
    }

    @Override
    protected NavigationBar createTopBarView() {
        return null;
    }
}
