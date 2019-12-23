package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.EasyGson;
import com.google.gson.GsonValidate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.MyToast;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.mis.bean.Misnjbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class MisnianjiaspActivity extends NavigationActivity implements View.OnClickListener {

    private BarButtonItem handleButtonItem;
    ListPageWrapper wrapper;
    private PullToRefreshListView lv_nianjiasp;
    private String token;
    private String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misnianjiasp);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("年假审批");
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

    private void  initView() {
        lv_nianjiasp = (PullToRefreshListView) findViewById(R.id.lv_nianjiasp);
        Button bt_njsp_pizhun = (Button) findViewById(R.id.bt_njsp_pizhun);
        bt_njsp_pizhun.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        year = intent.getStringExtra(MisnianjiaActivity.SP_YEAR);
        token = Content.gettoken();
       /* Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
        year = calendar.get(Calendar.YEAR);*/
        queryNJ(year+"",0+"");
    }

    private CompoundButton checkedcb;
    private boolean isfirst=true;
    private int checkedposition=-1;
    private void queryNJ(final String currentYear, final String status) {
        wrapper = new ListPageWrapper<View>(MisnianjiaspActivity.this) {
            @Override
            protected ApiMethodDescription onPageGetApiMethodDescription() {
                // TODO Auto-generated method stub
                return ApiMethodDescription.get("/ServiceMis.asmx/NJQuery");
            }
            @Override
            protected void onPageToInit(final PageController pageController) {
                // TODO Auto-generated method stub
                //这里要改1
                String token = Content.gettoken();
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("Token", token);
                params.put("year", currentYear);
                params.put("status", status );
                pageController.setApiParams(params);
                pageController.pageName = "CurrentPageNo";
                pageController.stepName = "PageSize";
                pageController.step = 6;
                pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){
                    @Override
                    public PageInfo adapter(JsonElement input) {
                        // TODO Auto-generated method stub
                        PageInfo pageInfo = new PageInfo();
                        pageInfo.isSuccess = true;
                        System.out.println(input);
                        JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
                        //njApproveBtn = inputJson.get("NJApproveBtn").toString();
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
                LinearLayout listCell = (LinearLayout) View.inflate(MisnianjiaspActivity.this, R.layout.mis_njsp_item, null);
                return listCell;
            }
            @Override
            protected void onPageCellSetData(final int position, View cell,
                                             Object data) {
                // TODO Auto-generated method stub
                CheckBox cb_njsp_xuanzhong = (CheckBox) cell.findViewById(R.id.cb_njsp_xuanzhong);
                TextView tv_nj_status = (TextView) cell.findViewById(R.id.tv_njsp_status);
                String Status = GsonValidate.getStringByKeyPath((JsonObject) data, "Status", "暂无");
                String name = GsonValidate.getStringByKeyPath((JsonObject) data, "xm", "暂无");
                tv_nj_status.setText("待审批");
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_njsp_name), (JsonObject)data, "xm", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_njsp_qingjia), (JsonObject)data, "LeaveTime", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_njsp_shenqing), (JsonObject)data, "ApplyTime", "暂无");
                //EasyUI.setTextViewText(cell.findViewById(R.id.tv_nj_status), (JsonObject)data, "Status", "暂无");
                cb_njsp_xuanzhong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        System.out.println("isfirst....."+isfirst);
                        System.out.println("compoundButton..."+compoundButton);
                        System.out.println("b..."+b);
                        if(b==true&&!isfirst) {
                            checkedcb.setChecked(false);
                            checkedcb = compoundButton;
                            checkedposition = position;
                            isfirst=false;
                        }else if(b==true&&isfirst){
                            checkedcb = compoundButton;
                            checkedposition=position;
                            isfirst=false;
                        }else if(b==false){
                            checkedposition=-1;
                            isfirst=true;
                        }
                    }
                });
            }
        };
        wrapper.wrap(lv_nianjiasp);
        ButterKnife.inject(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_njsp_pizhun:
                if(checkedposition==-1){
                    MyToast.showShort("请选择批准的条目");
                }else {
                    List list = wrapper.getPageController().getList();
                    String jsonString = list.get(checkedposition).toString();
                    String Id = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("mId").getAsString();

                    String name = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("xm").getAsString();
                    if(name.equals(Shref.getString(MisnianjiaspActivity.this, Common.USERNAME,null))){
                        MyToast.showShort("不能批准自己的");
                        return;
                    }

                    System.out.println("批准。。。。。............" + Id);
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("Token", token);
                    params.put("s1", Id);
                    System.out.println(params);
                    EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/NJApprove "), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                        @Override
                        public void onSuccessJson(JsonElement result) {
                            // TODO Auto-generated method stub
                            System.out.println(result);
                            Misnjbean misnjbean = GsonUtil.JsonToObject(result.toString(), Misnjbean.class);
                            boolean success = misnjbean.isSuccess();
                            if(success){
                                MyToast.showShort("批准成功");
                                queryNJ(year+"",0+"");
                            }else{
                                MyToast.showShort("批准未成功");
                            }
                        }
                        @Override
                        public String onFailed(ApiError error) {
                            MyToast.showShort("批准未成功");
                            return null;
                        }
                    });
                }
                break;
        }
    }
}
