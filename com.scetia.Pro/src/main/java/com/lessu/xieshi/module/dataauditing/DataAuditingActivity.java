package com.lessu.xieshi.module.dataauditing;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.google.gson.EasyGson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.xieshi.utils.ToastUtil;
import com.lessu.foundation.RegKit;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiBase;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.http.HttpUrlConnect;
import com.lessu.xieshi.view.ListViewCell;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.scetia.Pro.common.Util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataAuditingActivity extends XieShiSlidingMenuActivity implements OnItemClickListener {
    String itemId = "";
    String doneFlag = "";
    String kindId = "";
    String typeTitle = "";
    String projectTitle = "";
    String flagTitle = "";
    String token =  Constants.User.GET_TOKEN();
    int auditedCounter = 0;
    int loseauditedCounter = 0;
    private ListPageWrapper wrapper;
    ArrayList<String> arrayList = new ArrayList<String>();
    private String uri;
    private ListViewCell listCell;


    @Override
    protected int getLayoutId() {
        return R.layout.data_auditing_activity;
    }

    @Override
    protected void initView() {
        setTitle("记录审核");
        BarButtonItem searchButtonitem = new BarButtonItem(this, R.drawable.icon_navigation_search);
        searchButtonitem.setOnClickMethod(this, "searchButtonDidClick");
        navigationBar.setRightBarItem(searchButtonitem);
        BarButtonItem menuButtonitem = new BarButtonItem(this, R.drawable.icon_navigation_menu);
        menuButtonitem.setOnClickMethod(this, "menuButtonDidClick");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //跳转到搜索页面
        if (itemId == null || itemId.isEmpty()) {
            getDefaultData();
        } else {
            getData();
        }
        ButterKnife.bind(this);
    }

    private void getData() {
        if (doneFlag.equals("1")) {
            findViewById(R.id.handleView).setVisibility(View.GONE);
        } else {
            findViewById(R.id.handleView).setVisibility(View.VISIBLE);
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", token);
        params.put("ItemId", itemId);
        params.put("PageSize", 10);
        params.put("CurrentPageNo", 1);
        PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listView);
        wrapper = new ListPageWrapper<View>(DataAuditingActivity.this) {

            @Override
            protected ApiMethodDescription onPageGetApiMethodDescription() {
                uri = "/ServiceUST.asmx/Get_ExamRecordList";
                if (doneFlag.equals("1")) {
                    uri = "/ServiceUST.asmx/Get_AppExamedList";
                }
                return ApiMethodDescription.get(uri);
            }

            @Override
            protected void onPageToInit(final PageController pageController) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("Token", token);
                params.put("ItemId", itemId);
                pageController.setApiParams(params);
                pageController.pageName = "CurrentPageNo";
                pageController.stepName = "PageSize";
                pageController.setPageinfoAdapter(new PageInfoAdapterInterface() {

                    @Override
                    public PageInfo adapter(JsonElement input) {
                        // TODO Auto-generated method stub
                        PageInfo pageInfo = new PageInfo();
                        JsonObject inputJson = input.getAsJsonObject();
                        boolean resIsSuccess = inputJson.get("Success").getAsBoolean();
                        //返回数据没有内容，提示用户
                        if (!resIsSuccess) {
                            String message = inputJson.get("Message").getAsString();
                            ToastUtil.showShort(message);
                            return pageInfo;
                        }
                        pageInfo.isSuccess = true;
                        JsonArray inputJsonArray = inputJson.get("Data").getAsJsonArray();
                        int size = inputJsonArray.size();
                        List<JsonElement> list = new ArrayList<JsonElement>();
                        for (int i = 0; i < size; i++) {
                            list.add(inputJsonArray.get(i));
                        }

                        pageInfo.listData = list;

                        pageInfo.totalPage = pageController.getCurrentPage() + 1;
                        return pageInfo;
                    }

                });
            }

            @Override
            protected View onPageCreateCell(int position) {
                // TODO Auto-generated method stub
                int itemViewID = R.layout.data_auditing_item;
                if (doneFlag.equals("1")) {
                    itemViewID = R.layout.data_audited_item;
                }
                listCell = (ListViewCell) View.inflate(DataAuditingActivity.this, itemViewID, null);
                listCell.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        ListViewCell cell = (ListViewCell) arg0;
                        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                            int i = (int) arg1.getX();
                            String positionflag = String.valueOf(cell.getPosition());
                            if (!doneFlag.equals("1")) {
                                if (i >= 950) {
                                    if (arrayList.contains(positionflag)) {
                                        arrayList.remove(positionflag);
                                        ((ImageView) cell.findViewById(R.id.selectIcon)).setImageResource(R.drawable.icon_unchosen);
                                    } else {
                                        arrayList.add(positionflag);
                                        ((ImageView) cell.findViewById(R.id.selectIcon)).setImageResource(R.drawable.icon_chosen);
                                    }
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });

                return listCell;

            }

            @Override
            protected void onPageCellSetData(int position, View cell,
                                             Object data) {
                // TODO Auto-generated method stub
                ListViewCell cellview = (ListViewCell) cell;
                cellview.setPosition(position);
                if (doneFlag.equals("1")) {
                    EasyUI.setTextViewText(cellview.findViewById(R.id.sampleIdTextView), (JsonObject) data, "Sample_ID", "暂无");
                    EasyUI.setTextViewText(cellview.findViewById(R.id.conSignIdTextView), (JsonObject) data, "ConSign_ID", "暂无");
                    EasyUI.setTextViewText(cellview.findViewById(R.id.examTimeTextView), (JsonObject) data, "Exam_Time", "暂无");
                    EasyUI.setTextViewText(cellview.findViewById(R.id.tv_shouchijishenhe), (JsonObject) data, "Report_Audit_Time", "暂无");
                } else {
                    if (!arrayList.contains(position)) {
                        ((ImageView) cellview.findViewById(R.id.selectIcon)).setImageResource(R.drawable.icon_unchosen);
                    } else {
                        ((ImageView) cellview.findViewById(R.id.selectIcon)).setImageResource(R.drawable.icon_chosen);
                    }
                    EasyUI.setTextViewText(cellview.findViewById(R.id.sampleIdTextView), (JsonObject) data, "Sample_ID", "暂无");
                    EasyUI.setTextViewText(cellview.findViewById(R.id.reportIdTextView), (JsonObject) data, "ConSign_ID", "暂无");
                    EasyUI.setTextViewText(cellview.findViewById(R.id.reportTimeTextView), (JsonObject) data, "Exam_Time", "暂无");
                }
            }
        };

        wrapper.wrap(listView);

        listView.setOnItemClickListener(this);
    }

    private void getDefaultData() {
        //获取分类
        String Type = "1";//数据审核分类为1
        HashMap<String, Object> paramsType = new HashMap<String, Object>();
        paramsType.put("Token", token);
        paramsType.put("Type", Type);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/Get_ItemKind"), paramsType, new EasyAPI.ApiFastSuccessFailedCallBack() {

            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                JsonArray json = result.getAsJsonObject().get("Data").getAsJsonArray();
                if (json.size() > 0) {
                    kindId = json.get(0).getAsJsonObject().get("KindId").getAsString();
                    HashMap<String, Object> paramsItem = new HashMap<String, Object>();
                    String Type = "1";//数据审核分类为1
                    paramsItem.put("Token", token);
                    paramsItem.put("Type", Type);
                    paramsItem.put("KindId", kindId);
                    EasyAPI.apiConnectionAsync(DataAuditingActivity.this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/Get_ItemItem"), paramsItem, new EasyAPI.ApiFastSuccessFailedCallBack() {

                        @Override
                        public void onSuccessJson(JsonElement result) {
                            // TODO Auto-generated method stub
                            JsonArray json = result.getAsJsonObject().get("Data").getAsJsonArray();
                            if (json.size() > 0) {
                                itemId = json.get(0).getAsJsonObject().get("ItemId").getAsString();
                                getData();
                            }
                        }

                        @Override
                        public String onFailed(ApiError error) {
                            // TODO Auto-generated method stub
                            return null;
                        }
                    });
                } else {
                    LSAlert.showAlert(DataAuditingActivity.this, "当前用户没有审核权限或当前不存在审核记录");
                }
            }

            @Override
            public String onFailed(ApiError error) {
                // TODO Auto-generated method stub
                int i = 1;
                return null;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
        List list = wrapper.getPageController().getList();
        String jsonString = list.get(position - 1).toString();
        String consignId = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("ConSign_ID").getAsString();
        String sampleId = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("Sample_ID").getAsString();
//		if (doneFlag.equals("1")){
        Intent intent = new Intent(DataAuditingActivity.this, AuditingDetailActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("ConsignId", consignId);
        bundle.putString("SampleId", sampleId);
        intent.putExtras(bundle);
        startActivity(intent);
//		}
    }

    public void searchButtonDidClick() {
        Intent intent = new Intent(DataAuditingActivity.this, DataSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("TypeTitle", typeTitle);
        bundle.putString("ProjectTitle", projectTitle);
        bundle.putString("FlagTitle", flagTitle);
        bundle.putString("ItemId", itemId);
        bundle.putString("doneFlag", doneFlag);
        bundle.putString("KindId", kindId);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.auditingAllButton)
    protected void auditingAllButtonDidClick() {
        if (wrapper != null && wrapper.getPageController().getList().size() > 0) {
            auditedCounter = 0;
            loseauditedCounter = 0;
            LSAlert.showProgressHud(DataAuditingActivity.this, "审核中");
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... arg0) {
                    List listTemp = wrapper.getPageController().getList();
                    for (int i = 0; i < listTemp.size(); i++) {
                        String jsonString = listTemp.get(i).toString();
                        JsonObject json = EasyGson.jsonFromString(jsonString).getAsJsonObject();
                        operationReuest(json);
                    }
                    return null;
                }

                protected void onPostExecute(Void result) {
                    String alertMessage = String.valueOf(auditedCounter) + "个数据审核完毕！" + String.valueOf(loseauditedCounter) + "个数据审核失败！";
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(DataAuditingActivity.this, alertMessage);
                    arrayList.clear();
                    getData();
                    //wrapper.refreshNoMerge();
                    wrapper.shuaxin();
                }

                ;

            }.execute();
        } else {
            LSAlert.showAlert(this, "没有需要审核的条目！");
        }
    }

    private void operationReuest(JsonObject json) {
        String ConSign_ID = json.get("ConSign_ID").getAsString();
        String Sample_ID = json.get("Sample_ID").getAsString();
        String Exam_Time = json.get("Exam_Time").getAsString();
        HashMap<String, Object> handlParams = new HashMap<String, Object>();
        handlParams.put("Token", token);
        handlParams.put("ItemId", itemId);
        handlParams.put("ConSign_ID", ConSign_ID);
        handlParams.put("Sample_ID", Sample_ID);
        handlParams.put("Exam_Time", Exam_Time);
        String url = "http://" + ApiBase.sharedInstance().apiUrl + "/ServiceUST.asmx/Set_ExamList";
        HttpUrlConnect httpConnect = new HttpUrlConnect(url, handlParams);
        if (httpConnect.startConnection()) {
            String result = httpConnect.getResultString();
            String jsonString = RegKit.match(result, ">(\\{.+\\})</", 1);
            JsonObject jsonResult = EasyGson.jsonFromString(jsonString).getAsJsonObject();
            if (jsonResult.get("Success").getAsString().equalsIgnoreCase("true")) {
                auditedCounter = auditedCounter + 1;
            } else {
                loseauditedCounter = loseauditedCounter + 1;
            }
        } else {
            loseauditedCounter = loseauditedCounter + 1;
        }
    }

    @OnClick(R.id.auditingChoosenButton)
    protected void auditingChoosenButtonDidClick() {
        if (arrayList.size() > 0) {
            auditedCounter = 0;
            loseauditedCounter = 0;
            LSAlert.showProgressHud(DataAuditingActivity.this, "审核中");
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... arg0) {
                    List listTemp = wrapper.getPageController().getList();
                    for (int i = 0; i < arrayList.size(); i++) {
                        int position = Integer.parseInt(arrayList.get(i));
                        String jsonString = listTemp.get(position).toString();
                        JsonObject json = EasyGson.jsonFromString(jsonString).getAsJsonObject();
                        operationReuest(json);
                    }
                    return null;
                }

                protected void onPostExecute(Void result) {
                    String alertMessage = String.valueOf(auditedCounter) + "个数据审核完毕！" + String.valueOf(loseauditedCounter) + "个数据审核失败！";
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(DataAuditingActivity.this, alertMessage);
                    arrayList.clear();
                    getData();
                    //wrapper.refreshNoMerge();
                    wrapper.shuaxin();
                }

                ;

            }.execute();
        } else {
            LSAlert.showAlert(this, "没有需要审核的条目！");
        }

    }

    public void menuButtonDidClick() {
        menu.toggle();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg2 != null) {
            Bundle bundle = arg2.getExtras();
            kindId = bundle.getString("KindId");
            itemId = bundle.getString("ItemId");
            doneFlag = bundle.getString("doneFlag");
            typeTitle = bundle.getString("TypeTitle");
            projectTitle = bundle.getString("ProjectTitle");
            flagTitle = bundle.getString("FlagTitle");
        }
    }
}
