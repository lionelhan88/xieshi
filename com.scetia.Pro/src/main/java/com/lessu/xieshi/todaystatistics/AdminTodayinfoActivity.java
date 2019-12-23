package com.lessu.xieshi.todaystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lessu.xieshi.R;
import com.lessu.xieshi.XieShiSlidingMenuActivity;
import com.lessu.xieshi.unqualified.UnqualifiedSearchActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_todayinfo);
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
