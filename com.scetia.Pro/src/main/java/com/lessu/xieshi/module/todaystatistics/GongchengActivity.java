package com.lessu.xieshi.module.todaystatistics;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;

import java.util.HashMap;

public class GongchengActivity extends XieShiSlidingMenuActivity {
    JsonArray list = new JsonArray();
    String projectName = "";
    String projectArea = "";
    String baogaobianhao="";
    String jianshedanwei="";
    String shigongdanwei="";
    String jianlidanwei="";
    String jiancedanwei="";
    private Bundle bundle;
    private int rangeindex;
    private String currentLocation;
    private ListPageWrapper<View> wrapper;
    private PullToRefreshListView listView;
    private BarButtonItem handleButtonItem;
    private String projectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongcheng);
        this.setTitle("工程检测");
        navigationBar.setBackgroundColor(0xFF3598DC);
        BarButtonItem backbutton = new BarButtonItem(this , R.drawable.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(backbutton);
        initView();
        initData();
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.project_check_list_view);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        String token = LSUtil.valueStatic("Token");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", token);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/AFNETMETHOD_STAKE_GetJCLCount"), params, new EasyAPI.ApiFastSuccessCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                Log.e("gongchengjiance",result.getAsString());
                JsonObject jsonObject = result.getAsJsonObject().get("Data").getAsJsonObject();
                System.out.println(jsonObject);
                list = jsonObject.get("ListContent").getAsJsonArray();
                if (list==null||list.isJsonNull()||list.size()==0){
                    LSAlert.showAlert(GongchengActivity.this, "无相关记录");
                    return;
                }
            }
        });
    }
}
