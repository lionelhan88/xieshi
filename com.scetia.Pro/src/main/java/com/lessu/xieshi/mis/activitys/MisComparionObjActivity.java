package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.xieshi.R;
import com.lessu.xieshi.mis.bean.ComparisonObjBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class MisComparionObjActivity extends NavigationActivity  {
    private ListPageWrapper wrapper;
    private PullToRefreshListView misComparisonObjList;
    private BarButtonItem handleButtonItem;
    private String token;
    private List<ComparisonObjBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_comparion_obj);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("参加比对单位");
        initView();
        initData();
    }

    private void initView() {
        handleButtonItem = new BarButtonItem(this , R.drawable.back );
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        misComparisonObjList = (PullToRefreshListView) findViewById(R.id.mis_com_obj_list_view);
    }
    private void initData(){
        Intent intent = getIntent();
        String planNum = intent.getStringExtra("Id");
        token = Content.gettoken();
        if(planNum!=null) {
            queryObjData(planNum);
        }
    }

    /**
     * 从服务器获取数据
     */
    private void queryObjData(final String planNum) {
        wrapper = new ListPageWrapper<View>(MisComparionObjActivity.this){

            @Override
            protected ApiMethodDescription onPageGetApiMethodDescription() {
                return ApiMethodDescription.get("/ServiceMis.asmx/BDMemberQuery");
            }

            @Override
            protected void onPageToInit(final PageController pageController) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("Token", token);
                params.put("s3",planNum);
                pageController.setApiParams(params);
                pageController.pageName = "s2";
                pageController.stepName = "s1";
                pageController.step = 11;
                pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){
                    @Override
                    public PageInfo adapter(JsonElement input) {
                        PageInfo pageInfo = new PageInfo();
                        pageInfo.isSuccess = true;
                        System.out.println(input);
                        JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
                        JsonArray inputJsonArray = inputJson.get("ListContent").getAsJsonArray();
                        int size = inputJsonArray.size();
                        List<JsonElement> list = new ArrayList<JsonElement>();
                        for (int i=0;i<size;i++){
                            list.add(inputJsonArray.get(i));
                        }
                        pageInfo.listData = list;
                        pageInfo.totalPage = pageController.getCurrentPage()+1;
                        return pageInfo;
                    }
                });
            }

            @Override
            protected View onPageCreateCell(int position) {
                LinearLayout listCell = (LinearLayout) View.inflate(MisComparionObjActivity.this, R.layout.mis_comparison_obj_item, null);
                return listCell;
            }

            @Override
            protected void onPageCellSetData(final int position, View cell, final Object data) {
                //会员号
                TextView comparisonItemVip = (TextView) cell.findViewById(R.id.mis_comparison_item_vip);
                //单位名称
                TextView comparisonItemName = (TextView) cell.findViewById(R.id.mis_comparison_item_name);
                //计划编号
                TextView comparisonItemNum = (TextView) cell.findViewById(R.id.mis_comparison_item_num);
                EasyUI.setTextViewText(comparisonItemName, (JsonObject)data, "s3", "暂无");
                EasyUI.setTextViewText(comparisonItemNum, (JsonObject)data, "s1", "暂无");
                EasyUI.setTextViewText(comparisonItemVip, (JsonObject)data, "s2", "暂无");
            }
        };
        wrapper.wrap(misComparisonObjList);
        ButterKnife.inject(this);
    }

}
