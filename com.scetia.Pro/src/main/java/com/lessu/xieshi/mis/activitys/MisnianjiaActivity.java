package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.GsonValidate;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

import static com.lessu.xieshi.R.id.bt_njone_sq;
import static com.lessu.xieshi.R.id.bt_njtwo_sp;
import static com.lessu.xieshi.R.id.bt_njtwo_sq;

public class MisnianjiaActivity extends NavigationActivity implements View.OnClickListener {

    public static final String SP_YEAR= "sp_year";
    private BarButtonItem handleButtonItem;
    private Spinner sp_nj_nianfen;
    private Spinner sp_nj_zt;
    private String[] nianfenitem;
    private String[] ztitem;
    private Button bt_nj_search;
    private String year;
    private String status;

    ListPageWrapper wrapper;
    private PullToRefreshListView lv_nianjia;
    private String njApproveBtn;
    private LinearLayout ll_nj_one;
    private LinearLayout ll_nj_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misnianjia);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("年假管理");
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
        sp_nj_nianfen = (Spinner) findViewById(R.id.sp_nj_nianfen);
        sp_nj_zt = (Spinner) findViewById(R.id.sp_nj_zt);
        bt_nj_search = (Button) findViewById(R.id.bt_nj_search);
        lv_nianjia = (PullToRefreshListView) findViewById(R.id.lv_nianjia);
        ll_nj_one = (LinearLayout) findViewById(R.id.ll_nj_one);
        ll_nj_two = (LinearLayout) findViewById(R.id.ll_nj_two);
        Button bt_njone_sq = (Button) findViewById(R.id.bt_njone_sq);
        Button bt_njtwo_sp = (Button) findViewById(R.id.bt_njtwo_sp);
        Button bt_njtwo_sq = (Button) findViewById(R.id.bt_njtwo_sq);
        bt_njone_sq.setOnClickListener(this);
        bt_njtwo_sp.setOnClickListener(this);
        bt_njtwo_sq.setOnClickListener(this);


        bt_nj_search.setOnClickListener(this);

    }

    private void initData() {
        Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
        int i = calendar.get(Calendar.YEAR);
        nianfenitem = new String[]{String.valueOf(i), String.valueOf(i+1)};
        ArrayAdapter<String> nianfenadapter=new ArrayAdapter<String>(this,R.layout.spinner_custom_style, nianfenitem);
        sp_nj_nianfen.setAdapter(nianfenadapter);
        ztitem = new String[]{"全部","申请中","已批准"};
        ArrayAdapter<String> ztadapter=new ArrayAdapter<String>(this,R.layout.spinner_custom_style, ztitem);
        sp_nj_zt.setAdapter(ztadapter);
        year = nianfenitem[0];
        sp_nj_nianfen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = nianfenitem[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sp_nj_zt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    status ="";
                }else if(i==1){
                    status ="0";
                }else if(i==2){
                    status ="1";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        queryNJ(year,"");

    }

    private void queryNJ(final String currentYear, final String status) {
        wrapper = new ListPageWrapper<View>(MisnianjiaActivity.this) {
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
                pageController.step = 5;
                pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){
                    @Override
                    public PageInfo adapter(JsonElement input) {
                        // TODO Auto-generated method stub
                        PageInfo pageInfo = new PageInfo();
                        pageInfo.isSuccess = true;
                        System.out.println(input);
                        JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
                        njApproveBtn = inputJson.get("NJApproveBtn").toString();
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
                LinearLayout listCell = (LinearLayout) View.inflate(MisnianjiaActivity.this, R.layout.mis_nj_item, null);
                return listCell;
            }
            @Override
            protected void onPageCellSetData(int position, View cell,
                                             Object data) {
                // TODO Auto-generated method stub
                LinearLayout ll_disanhang = (LinearLayout) cell.findViewById(R.id.ll_disanhang);
                TextView tv_nj_status = (TextView) cell.findViewById(R.id.tv_nj_status);
                String Status = GsonValidate.getStringByKeyPath((JsonObject) data, "Status", "暂无");
                if(Status.equals("已批准")){
                    ll_disanhang.setVisibility(View.VISIBLE);
                    tv_nj_status.setText("已批准");
                    tv_nj_status.setTextColor(0xff73bf47);
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_nj_pztime), (JsonObject)data, "DepartTime", "暂无");
                    EasyUI.setTextViewText(cell.findViewById(R.id.tv_nj_bmpz), (JsonObject)data, "DepartApprove", "暂无");

                }else if(Status.equals("申请中")){
                    ll_disanhang.setVisibility(View.GONE);
                    tv_nj_status.setText("待审批");
                    tv_nj_status.setTextColor(0xff268beb);
                }
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_nj_name), (JsonObject)data, "xm", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_nj_qingjia), (JsonObject)data, "LeaveTime", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_nj_reason), (JsonObject)data, "LeaveReason", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.tv_nj_shenqing), (JsonObject)data, "ApplyTime", "暂无");
                //EasyUI.setTextViewText(cell.findViewById(R.id.tv_nj_status), (JsonObject)data, "Status", "暂无");


                if(njApproveBtn.equals("\"0\"")){
                    System.out.println("是不是00000000000000000000000000000000000000");
                    ll_nj_one.setVisibility(View.VISIBLE);
                    ll_nj_two.setVisibility(View.INVISIBLE);
                }else if(njApproveBtn.equals("\"1\"")){
                    ll_nj_one.setVisibility(View.INVISIBLE);
                    ll_nj_two.setVisibility(View.VISIBLE);
                }
            }
        };
        wrapper.wrap(lv_nianjia);
      //  ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_nj_search:
                queryNJ(year,status);
                break;
            case bt_njone_sq:
                startActivity(new Intent(MisnianjiaActivity.this,MisnjsqActivity.class));
                break;
            case bt_njtwo_sp:
                Intent intentSP = new Intent(this,MisnianjiaspActivity.class);
                intentSP.putExtra(SP_YEAR,year);
                startActivity(intentSP);
                break;
            case bt_njtwo_sq:
                startActivity(new Intent(MisnianjiaActivity.this,MisnjsqActivity.class));
                break;

        }
    }
}
