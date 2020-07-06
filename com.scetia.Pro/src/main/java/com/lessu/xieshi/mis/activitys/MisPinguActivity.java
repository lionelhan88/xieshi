package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.lessu.xieshi.mis.bean.Mispingubean;
import com.lessu.xieshi.mis.bean.Pgitembean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class MisPinguActivity extends NavigationActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private BarButtonItem handleButtonItem;
    private EditText et_pg_search;
    private TextView tv_pg_search;
    private PullToRefreshListView lv_pgsearch;
    private Button bt_pg;
    private String huiyuanhao;
    ListPageWrapper wrapper;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pingu);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("评估信息查询");
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
        et_pg_search = (EditText) findViewById(R.id.et_pg_search);
        tv_pg_search = (TextView) findViewById(R.id.tv_pg_search);
        lv_pgsearch = (PullToRefreshListView) findViewById(R.id.lv_pgsearch);
        bt_pg = (Button) findViewById(R.id.bt_pg);
        tv_pg_search.setOnClickListener(this);
        bt_pg.setOnClickListener(this);
    }

    private void initData() {
        //这里要改3
        token = Content.gettoken();
        huiyuanhao = et_pg_search.getText().toString().trim();
        queryPG(huiyuanhao);
    }
    private CompoundButton checkedcb;
    private boolean isfirst=true;
    private int checkedposition=-1;
    private void queryPG(final String huiyuanhao) {
        isfirst=true;
        checkedposition=-1;
        wrapper = new ListPageWrapper<View>(MisPinguActivity.this) {

            private String pgApproveBtn;

            @Override
            protected ApiMethodDescription onPageGetApiMethodDescription() {
                // TODO Auto-generated method stub
                return ApiMethodDescription.get("/ServiceMis.asmx/PGQuery");
            }
            @Override
            protected void onPageToInit(final PageController pageController) {
                // TODO Auto-generated method stub

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("Token", token);
                params.put("s3", huiyuanhao);
                pageController.setApiParams(params);
                pageController.pageName = "s2";
                pageController.stepName = "s1";
                pageController.step = 11;
                pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){
                    @Override
                    public PageInfo adapter(JsonElement input) {
                        // TODO Auto-generated method stub
                        PageInfo pageInfo = new PageInfo();
                        pageInfo.isSuccess = true;
                        System.out.println(input);
                        JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
                        pgApproveBtn = inputJson.get("PGApproveBtn").toString();
                        System.out.println("pgApproveBtn............"+ pgApproveBtn);
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
                LinearLayout listCell = (LinearLayout) View.inflate(MisPinguActivity.this, R.layout.mis_pg_item, null);
                return listCell;
            }
            @Override
            protected void onPageCellSetData(final int position, View cell,
                                             Object data) {
                // TODO Auto-generated method stub
                //ImageView iv_xuanzhong = (ImageView) cell.findViewById(R.id.iv_xuanzhong);
                CheckBox cb_xuanzhong = (CheckBox) cell.findViewById(R.id.cb_xuanzhong);
                TextView tv_pg3 = (TextView) cell.findViewById(R.id.tv_pg3);
                TextView tv_pg4 = (TextView) cell.findViewById(R.id.tv_pg4);
                System.out.println("pgApproveBtn...00000000000sssssss"+pgApproveBtn);
                if(pgApproveBtn.equals("\"0\"")){
                    System.out.println("gone..........");
                    cb_xuanzhong.setVisibility(View.GONE);
                    bt_pg.setVisibility(View.GONE);
                    tv_pg3.setText("状态: ");
                    tv_pg4.setText("编号: ");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_pg_danwei), (JsonObject)data, "MemberName", "暂无");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_pg_leixin), (JsonObject)data, "ApplicationType", "暂无");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_pg_zhuangtai), (JsonObject)data, "Status", "暂无");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_pg_num), (JsonObject)data, "InnerId", "暂无");
                }else if(pgApproveBtn.equals("\"1\"")){
                    System.out.println("VISIBLE..........");
                    cb_xuanzhong.setVisibility(View.VISIBLE);
                    cb_xuanzhong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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


                    bt_pg.setVisibility(View.VISIBLE);
                    tv_pg3.setText("审核人: ");
                    tv_pg4.setText("审核日: ");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_pg_danwei), (JsonObject)data, "MemberName", "暂无");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_pg_leixin), (JsonObject)data, "ApplicationType", "暂无");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_pg_zhuangtai), (JsonObject)data, "CheckerName", "暂无");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_pg_num), (JsonObject)data, "CheckDate", "暂无");
                }
            }
        };
        wrapper.wrap(lv_pgsearch);
        lv_pgsearch.setOnItemClickListener(MisPinguActivity.this);
        ButterKnife.bind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
        System.out.println("点击了。。。。。。。。。。");
        Intent intent = new Intent(MisPinguActivity.this, PgdetailActivity.class);
        List list = wrapper.getPageController().getList();
        String jsonString = list.get(position-1).toString();
        System.out.println("lalala............"+jsonString);
        Pgitembean pgdetailbean = GsonUtil.JsonToObject(jsonString, Pgitembean.class);
        String id1 = pgdetailbean.getId();
        // String projectId = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("ProjectId").getAsString();
        Bundle bundle = new Bundle();
        bundle.putString("Id", id1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_pg_search:
                huiyuanhao = et_pg_search.getText().toString().trim();
                queryPG(huiyuanhao);
                break;
            case R.id.bt_pg:
                if(checkedposition==-1){
                    MyToast.showShort("请选择批准的条目");
                }else {
                    List list = wrapper.getPageController().getList();
                    String jsonString = list.get(checkedposition).toString();
                    String Id = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("Id").getAsString();
                    System.out.println("批准。。。。。............" + Id);

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("Token", token);
                    params.put("s1", Id);
                    System.out.println(params);
                    EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/PGApprove "), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                        @Override
                        public void onSuccessJson(JsonElement result) {
                            // TODO Auto-generated method stub
                            System.out.println(result);
                            Mispingubean mispingubean = GsonUtil.JsonToObject(result.toString(), Mispingubean.class);
                            boolean success = mispingubean.isSuccess();
                            if(success){
                                MyToast.showShort("批准成功");
                                queryPG(huiyuanhao);
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
