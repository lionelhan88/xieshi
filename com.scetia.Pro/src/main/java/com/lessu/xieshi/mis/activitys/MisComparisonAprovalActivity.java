package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.EasyGson;
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
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.MyToast;
import com.lessu.xieshi.mis.bean.ComparisonPlan;
import com.lessu.xieshi.mis.bean.MisComaprisonResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class MisComparisonAprovalActivity extends NavigationActivity implements  View.OnClickListener,
        AdapterView.OnItemClickListener{
    private BarButtonItem handleButtonItem;
    private ListPageWrapper wrapper;
    private PullToRefreshListView misComparisonApprovalList;
    private String token;
    private String BDApproveBtn;//是否显示审批按钮 0不显示 1显示
    private Button misComApprovaled;
    private CompoundButton checkedcb;
    private boolean isfirst=true;
    private int checkedposition=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_comparison_aproval);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("比对审批");
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private  void initView(){
        handleButtonItem = new BarButtonItem(this , R.drawable.back );
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        misComparisonApprovalList = (PullToRefreshListView) findViewById(R.id.mis_comparison_list_view);
        misComparisonApprovalList.setOnClickListener(this);
        misComApprovaled = (Button) findViewById(R.id.mis_com_approvaled);
        misComApprovaled.setOnClickListener(this);
    }

    /**
     * 初始化数据即第一页数据
     */
    private void  initData(){
        token = Content.gettoken();
        queryApprovalData();
    }
    /**
     * 获取比对计划数据
     */
    private void queryApprovalData(){
        isfirst=true;
        checkedposition=-1;
        wrapper = new ListPageWrapper<View>(MisComparisonAprovalActivity.this){

            @Override
            protected ApiMethodDescription onPageGetApiMethodDescription() {
                return ApiMethodDescription.get("/ServiceMis.asmx/BDQuery");
            }

            @Override
            protected void onPageToInit(final PageController pageController) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("Token", token);
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
                        BDApproveBtn = inputJson.get("BDApproveBtn").toString();
                        System.out.println("BDApproveBtn............"+ BDApproveBtn);
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
                LinearLayout listCell = (LinearLayout) View.inflate(MisComparisonAprovalActivity.this, R.layout.mis_comparison_approval_item, null);
                return listCell;
            }

            @Override
            protected void onPageCellSetData(final int position, View cell, final Object data) {
                //是否选中
                CheckBox cbComparisonApproval = (CheckBox) cell.findViewById(R.id.cb_comparison_approval);
                //计划时间
                TextView comApprovalDate = (TextView) cell.findViewById(R.id.mis_com_approval_date);
                //计划编号
                TextView comApprovalNum = (TextView) cell.findViewById(R.id.mis_com_approval_num);
                //计划项目名称
                TextView comApprovalName = (TextView) cell.findViewById(R.id.mis_com_approval_name);
                TextView comIsApproval  = (TextView) cell.findViewById(R.id.mis_com_is_approval);
                if(BDApproveBtn.equals("0")){
                    //如果为0则不显示审批按钮
                    misComApprovaled.setVisibility(View.GONE);
                    cbComparisonApproval.setVisibility(View.GONE);
                }else{
                    misComApprovaled.setVisibility(View.VISIBLE);
                    cbComparisonApproval.setVisibility(View.VISIBLE);
                    cbComparisonApproval.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
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
                EasyUI.setTextViewText(comApprovalDate, (JsonObject)data, "s3", "暂无");
                EasyUI.setTextViewText(comApprovalNum, (JsonObject)data, "s1", "暂无");
                EasyUI.setTextViewText(comApprovalName, (JsonObject)data, "s2", "暂无");
                EasyUI.setTextViewText(comIsApproval, (JsonObject)data, "s4", "暂无");
                if(comIsApproval.getText().toString().equals("待批准")){
                    comIsApproval.setTextColor(Color.parseColor("#268BEB"));
                    cbComparisonApproval.setVisibility(View.VISIBLE);
                }else{
                    comIsApproval.setTextColor(Color.parseColor("#73BF47"));
                    cbComparisonApproval.setVisibility(View.GONE);
                }
            }
        };
        wrapper.wrap(misComparisonApprovalList);
        misComparisonApprovalList.setOnItemClickListener(this);
        ButterKnife.inject(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mis_com_approvaled:
                if(checkedposition==-1){
                    MyToast.showShort("请选择审批的条目");
                }else{
                    List list = wrapper.getPageController().getList();
                    String jsonString = list.get(checkedposition).toString();
                    String Id = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("s1").getAsString();
                    System.out.println("批准。。。。。............" + Id);
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("Token", token);
                    params.put("s1", Id);
                    System.out.println(params);
                    EasyAPI.apiConnectionAsync(MisComparisonAprovalActivity.this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/BDApprove "), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                        @Override
                        public void onSuccessJson(JsonElement result) {
                            System.out.println(result);
                            MisComaprisonResponse mispingubean = GsonUtil.JsonToObject(result.toString(), MisComaprisonResponse.class);
                            boolean success = mispingubean.isSuccess();
                            if(success){
                                 MyToast.showShort("批准成功");
                                queryApprovalData();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        System.out.println("点击了。。。。。。。。。。");
        Intent intent = new Intent(MisComparisonAprovalActivity.this, MisComparionObjActivity.class);
        List list = wrapper.getPageController().getList();
        String jsonString = list.get(position-1).toString();
        System.out.println("lalala............"+jsonString);
        ComparisonPlan planBean = GsonUtil.JsonToObject(jsonString, ComparisonPlan.class);
        String id1 = planBean.getComparisonNum();
        intent.putExtra("Id",id1);
        startActivity(intent);
    }
}
