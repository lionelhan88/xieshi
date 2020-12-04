package com.lessu.xieshi.module.todaystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.lessu.xieshi.bean.TodayInfoProjectBean;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.lessu.xieshi.module.unqualified.UnqualifiedSearchActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminTodayinfoActivity extends XieShiSlidingMenuActivity implements View.OnClickListener {

    private LinearLayout ll_cailiaojiance;
    private LinearLayout ll_gongchengjiance;
    private LinearLayout ll_xianchanginfo;
    private LinearLayout ll_buhegeinfo;
    private String projectid;
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
    TextView tvProjectSupervior;
    @BindView(R.id.today_info_project_detection)
    TextView tvProjectDetection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_todayinfo);
        ButterKnife.bind(this);
        this.setTitle("信息查询");
        initView();
        initData();
        navigationBar.setBackgroundColor(0xFF3598DC);
    }

    private void initView() {
        ll_cailiaojiance = (LinearLayout) findViewById(R.id.ll_cailiaojiance);
        ll_gongchengjiance = (LinearLayout) findViewById(R.id.ll_gongchengjiance);
        ll_xianchanginfo = (LinearLayout) findViewById(R.id.ll_xianchanginfo);
        ll_buhegeinfo = (LinearLayout) findViewById(R.id.ll_buhegeinfo);
        ll_cailiaojiance.setOnClickListener(this);
        ll_gongchengjiance.setOnClickListener(this);
        ll_xianchanginfo.setOnClickListener(this);
        ll_buhegeinfo.setOnClickListener(this);
    }

    private void initData() {
        Intent getintent=getIntent();
        projectid = getintent.getStringExtra("projectid");
        projectName = getintent.getStringExtra("projectName");
        projectArea = getintent.getStringExtra("projectArea");
        getProjectInfo(projectid);
    }

    /**
     * 请求获取工程的详细信息
     * @param projectId
     */
    private void getProjectInfo(String projectId){
        HashMap<String,Object> params = new HashMap<>();
        params.put("Type",1);
        params.put("ProjectId",projectId);
        params.put("Token", Content.getToken());
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/GetProjectInfo")
                , params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if(isSuccess){
                            //成功获取到数据
                            //获取数据成功
                            JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
                            TodayInfoProjectBean todayInfoProjectBean = new Gson().fromJson(json, TodayInfoProjectBean.class);
                            tvProjectName.setText(todayInfoProjectBean.getProjectName());
                            tvProjectNature.setText(todayInfoProjectBean.getProjectNature());
                            tvProjectAddress.setText(todayInfoProjectBean.getProjectAddress());
                            tvProjectBuild.setText(todayInfoProjectBean.getBuildUnitName());
                            tvProjectConstruct.setText(todayInfoProjectBean.getConstructUnitName());
                            tvProjectSupervior.setText(todayInfoProjectBean.getSuperviorUnitName());
                            tvProjectDetection.setText(todayInfoProjectBean.getDetectionUnitNames());

                        }else{
                            LSAlert.showAlert(AdminTodayinfoActivity.this,"获取数据失败！");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ll_gongchengjiance:
                Intent intentGC = new Intent(AdminTodayinfoActivity.this, TodayStatisticsDetailActivity.class);
                Bundle bundleGC = new Bundle();
                bundleGC.putString("ProjectId", projectid);
                intentGC.putExtras(bundleGC);
                intentGC.putExtra("buttonId",1);
                startActivity(intentGC);
                break;
            case R.id.ll_cailiaojiance:
                Intent intent = new Intent(AdminTodayinfoActivity.this, TodayStatisticsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ProjectId", projectid);
                intent.putExtras(bundle);
                intent.putExtra("buttonId",0);
                startActivity(intent);
                break;
            case R.id.ll_xianchanginfo:
                Intent intentc=new Intent(AdminTodayinfoActivity.this,XianchangActivity.class);
                intentc.putExtra("projectid",projectid);
                startActivity(intentc);
                break;
            case R.id.ll_buhegeinfo:
                Intent intentd=new Intent(AdminTodayinfoActivity.this, UnqualifiedSearchActivity.class);
                intentd.putExtra("projectId",projectid);
                intentd.putExtra("projectName",projectName);
                intentd.putExtra("projectArea",projectArea);
                startActivity(intentd);
                break;



        }
    }
}
