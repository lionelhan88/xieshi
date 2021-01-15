package com.lessu.xieshi.module.mis.activitys;

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
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Common;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.mis.bean.Misnjbean;
import com.scetia.Pro.common.Util.SPUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class MisAnnualLeaveApprovalActivity extends NavigationActivity implements View.OnClickListener {
    private ListPageWrapper wrapper;
    private PullToRefreshListView lv_nianjiasp;
    private String token;
    private String year;
    private CompoundButton checkedcb;
    private boolean isFirst =true;
    private int checkedPosition =-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misnianjiasp);
        this.setTitle("年假审批");
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
        year = intent.getStringExtra(MisAnnualLeaveManageActivity.SP_YEAR);
        token = Content.getToken();
        queryNJ(year,"0");
    }

    private void queryNJ(final String currentYear, final String status) {
        wrapper = new ListPageWrapper<View>(MisAnnualLeaveApprovalActivity.this) {
            @Override
            protected ApiMethodDescription onPageGetApiMethodDescription() {
                // TODO Auto-generated method stub
                return ApiMethodDescription.get("/ServiceMis.asmx/NJQuery");
            }
            @Override
            protected void onPageToInit(final PageController pageController) {
                // TODO Auto-generated method stub
                //这里要改1
                String token = Content.getToken();
                HashMap<String, Object> params = new HashMap<>();
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
                        //测试
                        JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
                        //njApproveBtn = inputJson.get("NJApproveBtn").toString();
                        JsonArray inputJsonArray = inputJson.get("ListContent").getAsJsonArray();
                        int size = inputJsonArray.size();
                        List<JsonElement> list = new ArrayList<>();
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
                LinearLayout listCell = (LinearLayout) View.inflate(MisAnnualLeaveApprovalActivity.this, R.layout.mis_njsp_item, null);
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
                        if(b&&!isFirst) {
                            checkedcb.setChecked(false);
                            checkedcb = compoundButton;
                            checkedPosition = position;
                            isFirst =false;
                        }else if(b && isFirst){
                            checkedcb = compoundButton;
                            checkedPosition =position;
                            isFirst =false;
                        }else if(!b){
                            checkedPosition =-1;
                            isFirst =true;
                        }
                    }
                });
            }
        };
        wrapper.wrap(lv_nianjiasp);
        ButterKnife.bind(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_njsp_pizhun:
                if(checkedPosition ==-1){
                    ToastUtil.showShort("请选择批准的条目");
                }else {
                    List list = wrapper.getPageController().getList();
                    String jsonString = list.get(checkedPosition).toString();
                    String Id = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("mId").getAsString();

                    String name = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("xm").getAsString();
                    if(name.equals(SPUtil.getSPConfig(Common.USERNAME,null))){
                        ToastUtil.showShort("不能批准自己的");
                        return;
                    }

                    System.out.println("批准。。。。。............" + Id);
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("Token", token);
                    params.put("s1", Id);
                    System.out.println(params);
                    EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/NJApprove "), params,
                            new EasyAPI.ApiFastSuccessFailedCallBack() {
                        @Override
                        public void onSuccessJson(JsonElement result) {
                            System.out.println(result);
                            Misnjbean misnjbean = GsonUtil.JsonToObject(result.toString(), Misnjbean.class);
                            boolean success = misnjbean.isSuccess();
                            if(success){
                                ToastUtil.showShort("批准成功");
                                queryNJ(year+"",0+"");
                            }else{
                                ToastUtil.showShort("批准未成功");
                            }
                        }
                        @Override
                        public String onFailed(ApiError error) {
                            ToastUtil.showShort("批准未成功");
                            return null;
                        }
                    });
                }
                break;
        }
    }
}
