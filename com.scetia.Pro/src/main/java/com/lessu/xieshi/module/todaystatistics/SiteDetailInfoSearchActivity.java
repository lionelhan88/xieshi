package com.lessu.xieshi.module.todaystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.lessu.xieshi.bean.TodayInfoProjectBean;
import com.lessu.xieshi.module.unqualified.UnqualifiedSearchActivity;
import com.scetia.Pro.common.Util.Constants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SiteDetailInfoSearchActivity extends XieShiSlidingMenuActivity {
    private String projectId;
    //工地名称
    private String projectName;
    //工地地址
    private String projectArea;
    @BindView(R.id.today_info_project_name)
    TextView tvProjectName;
    @BindView(R.id.today_info_project_nature)
    TextView tvProjectNature;
    @BindView(R.id.today_info_project_address)
    TextView tvProjectAddress;
    @BindView(R.id.today_info_project_construct)
    TextView tvProjectConstruct;
    @BindView(R.id.today_info_project_build)
    TextView tvProjectBuild;
    @BindView(R.id.today_info_project_supervior)
    TextView tvProjectSupervisor;
    @BindView(R.id.today_info_project_detection)
    TextView tvProjectDetection;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin_todayinfo;
    }

    @Override
    protected void initView() {
        this.setTitle("信息查询");
    }

    @Override
    protected void initData() {
        Intent dataFromSitSearchMap = getIntent();
        projectId = dataFromSitSearchMap.getStringExtra(Constants.Site.KEY_PROJECT_ID);
        projectName = dataFromSitSearchMap.getStringExtra(Constants.Site.KEY_PROJECT_NAME);
        projectArea = dataFromSitSearchMap.getStringExtra(Constants.Site.KEY_PROJECT_AREA);
        getProjectInfo(projectId);
    }

    /**
     * 请求获取工程的详细信息
     *
     * @param projectId
     */
    private void getProjectInfo(String projectId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Type", 1);
        params.put("ProjectId", projectId);
        params.put("Token", Constants.User.GET_TOKEN());
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/GetProjectInfo")
                , params, result -> {
                    boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                    if (isSuccess) {
                        //成功获取到数据
                        //获取数据成功
                        JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
                        TodayInfoProjectBean todayInfoProjectBean = new Gson().fromJson(json, TodayInfoProjectBean.class);
                        tvProjectName.setText(todayInfoProjectBean.getProjectName());
                        tvProjectNature.setText(todayInfoProjectBean.getProjectNature());
                        tvProjectAddress.setText(todayInfoProjectBean.getProjectAddress());
                        tvProjectBuild.setText(todayInfoProjectBean.getBuildUnitName());
                        tvProjectConstruct.setText(todayInfoProjectBean.getConstructUnitName());
                        tvProjectSupervisor.setText(todayInfoProjectBean.getSuperviorUnitName());
                        tvProjectDetection.setText(todayInfoProjectBean.getDetectionUnitNames());

                    } else {
                        LSAlert.showAlert(SiteDetailInfoSearchActivity.this, "获取数据失败！");
                    }
                });
    }

   @OnClick({R.id.ll_material_testing,R.id.ll_project_testing,R.id.ll_on_site_info,R.id.ll_unqualified_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_project_testing:
                Intent intentGC = new Intent(SiteDetailInfoSearchActivity.this, TodayStatisticsDetailActivity.class);
                intentGC.putExtra(Constants.Site.KEY_PROJECT_ID, projectId);
                intentGC.putExtra("buttonId", Constants.Site.SITE_INFO_SEARCH_BY_PROJECT);
                startActivity(intentGC);
                break;
            case R.id.ll_material_testing:
                Intent intent = new Intent(SiteDetailInfoSearchActivity.this, TodayStatisticsDetailActivity.class);
                intent.putExtra(Constants.Site.KEY_PROJECT_ID, projectId);
                intent.putExtra("buttonId", Constants.Site.SITE_INFO_SEARCH_BY_MATERIAL);
                startActivity(intent);
                break;
            case R.id.ll_on_site_info:
                Intent intentc = new Intent(SiteDetailInfoSearchActivity.this, SiteSceneBlockActivity.class);
                intentc.putExtra(Constants.Site.KEY_PROJECT_ID, projectId);
                startActivity(intentc);
                break;
            case R.id.ll_unqualified_info:
                Intent intentd = new Intent(SiteDetailInfoSearchActivity.this, UnqualifiedSearchActivity.class);
                intentd.putExtra("projectId", projectId);
                intentd.putExtra("projectName", projectName);
                intentd.putExtra("projectArea", projectArea);
                startActivity(intentd);
                break;


        }
    }
}
