package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class MisHysearchActivity extends NavigationActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private BarButtonItem handleButtonItem;
    private PullToRefreshListView lv_hysearch;
    ListPageWrapper wrapper;
    private EditText et_hy_search;
    private TextView tv_hy_search;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_huiyuanchaxun);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("会员信息查询");
        handleButtonItem = new BarButtonItem(this , R.drawable.back );
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        initView();
        initData();
    }

    private void initView() {
        et_hy_search = (EditText) findViewById(R.id.et_hy_search);
        tv_hy_search = (TextView) findViewById(R.id.tv_hy_search);
        lv_hysearch = (PullToRefreshListView) findViewById(R.id.lv_hysearch);
        tv_hy_search.setOnClickListener(this);
    }

    private void initData() {
        key = et_hy_search.getText().toString().trim();
        queryHY(key);
    }

    private void queryHY(final String key) {
        wrapper = new ListPageWrapper<View>(MisHysearchActivity.this) {
            @Override
            protected ApiMethodDescription onPageGetApiMethodDescription() {
                // TODO Auto-generated method stub
                return ApiMethodDescription.get("/ServiceMis.asmx/GetMemberInfo");
            }
            @Override
            protected void onPageToInit(final PageController pageController) {
                // TODO Auto-generated method stub
                //这里要改1
                String token = Content.gettoken();
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("Token", token);
                params.put("key", key);
                pageController.setApiParams(params);
                pageController.pageName = "CurrentPageNo";
                pageController.stepName = "PageSize";
                pageController.step = 11;
                pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){
                    @Override
                    public PageInfo adapter(JsonElement input) {
                        // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                LinearLayout listCell = (LinearLayout) View.inflate(MisHysearchActivity.this, R.layout.mis_hy_item, null);
                return listCell;
            }
            @Override
            protected void onPageCellSetData(int position, View cell,
                                             Object data) {
                // TODO Auto-generated method stub
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_mis_hynum), (JsonObject)data, "MemberId", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_mis_hyname), (JsonObject)data, "MemberName", "暂无");
            }
        };
        wrapper.wrap(lv_hysearch);
        lv_hysearch.setOnItemClickListener(MisHysearchActivity.this);
       // ButterKnife.bind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
        Intent intent = new Intent(MisHysearchActivity.this, HydetailActivity.class);
        List list = wrapper.getPageController().getList();
        String jsonString = list.get(position-1).toString();
        System.out.println("lalala............"+jsonString);
       // String projectId = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("ProjectId").getAsString();
        Bundle bundle = new Bundle();
        bundle.putString("HyString", jsonString);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        key = et_hy_search.getText().toString().trim();
        queryHY(key);
    }
}
