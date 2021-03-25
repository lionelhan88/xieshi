package com.lessu.xieshi.module.todaystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.bean.ProjectQueryTestingBean;
import com.scetia.Pro.common.Util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class TodayStatisticsDetailActivity extends NavigationActivity {
    JsonArray list = new JsonArray();
    private String projectName = "";
    private String projectArea = "";
    private String reportNumber = "";
    private String jianshedanwei = "";
    private String shigongdanwei = "";
    private String jianlidanwei = "";
    private String jiancedanwei = "";
    @BindView(R.id.project_testing_low_unqualified)
    TextView projectTestingLowUnqualified;
    @BindView(R.id.project_testing_high_unqualified)
    TextView projectTestingHighUnqualified;
    @BindView(R.id.project_testing_jizhuang_unqualified)
    TextView projectTestingJizhuangUnqualified;
    @BindView(R.id.project_testing_huitan_unqualified)
    TextView projectTestingHuitanUnqualified;
    @BindView(R.id.project_testing_zonghe_unqualified)
    TextView projectTestingZongheUnqualified;
    @BindView(R.id.project_testing_zuanxin_unqualified)
    TextView projectTestingZuanxinUnqualified;
    @BindView(R.id.project_testing_layout)
    LinearLayout projectTestingLayout;
    @BindView(R.id.listView)
    PullToRefreshListView listView;
    private Bundle bundle;
    private int rangeIndex;
    private String currentLocation;

    @Override
    protected int getLayoutId() {
        return R.layout.construction_detail_activity;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        //type为1为查询工程检测，为0则为查询材料检测
        bundle = getIntent().getExtras();
        if (bundle.getBoolean("isxianshall")) {
            connectAll();
        } else if (intent.getIntExtra("buttonId", 0) == 1) {
            this.setTitle("工程检测");
            listView.setVisibility(View.GONE);
            projectTestingLayout.setVisibility(View.VISIBLE);
            getProjectQueryList();
        } else {
            this.setTitle("材料检测");
            listView.setVisibility(View.VISIBLE);
            projectTestingLayout.setVisibility(View.GONE);
            listView.setAdapter(adapter);
            connectList();
        }
    }

    private void connectAll() {
        //2019-05-27　修改，勾选“显示所有工地统计信息”时，如果有工地名称，不能有距离参数和坐标参数
        ListPageWrapper<View> wrapper = new ListPageWrapper<View>(this) {
            @Override
            protected ApiMethodDescription onPageGetApiMethodDescription() {
                return ApiMethodDescription.get("/ServiceTS.asmx/ManageUnitTodayStatisProjectDetectInfo");
            }

            @Override
            protected void onPageToInit(final PageController pageController) {
                //这里要改1
                String token = Constants.User.GET_TOKEN();
                HashMap<String, Object> params = new HashMap<>();
                projectArea = bundle.getString("ProjectArea");
                projectName = bundle.getString("ProjectName");
                reportNumber = bundle.getString("baogaobianhao");
                jianshedanwei = bundle.getString("jianshedanwei");
                shigongdanwei = bundle.getString("shigongdanwei");
                jianlidanwei = bundle.getString("jianlidanwei");
                jiancedanwei = bundle.getString("jiancedanwei");
                rangeIndex = bundle.getInt("DistanceRange");
                currentLocation = bundle.getString("CurrentLocation");

                params.put("Token", token);
                params.put("Type", 1);
                params.put("ProjectName", projectName);
                params.put("ProjectArea", projectArea);
                params.put("BuildingReportNumber", reportNumber);
                params.put("BuildUnitName", shigongdanwei);
                params.put("ConstructUnitName", jianshedanwei);
                params.put("SuperviseUnitName", jianlidanwei);
                params.put("DetectionUnitName", jiancedanwei);
                //2019-05-27　修改，勾选“显示所有工地统计信息”时，如果有工地名称，不能有距离参数和坐标参数
                if (TextUtils.isEmpty(projectName)) {
                    params.put("CurrentLocation", currentLocation);
                    params.put("DistanceRange", rangeIndex);
                }
                pageController.setApiParams(params);
                pageController.pageName = "CurrentPageNo";
                pageController.stepName = "PageSize";
                pageController.step = 9;
                pageController.setPageinfoAdapter(new PageInfoAdapterInterface() {
                    @Override
                    public PageInfo adapter(JsonElement input) {
                        PageInfo pageInfo = new PageInfo();
                        pageInfo.isSuccess = true;
                        System.out.println(input);
                        JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
                        JsonArray inputJsonArray = inputJson.get("ListContent").getAsJsonArray();
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
                LinearLayout listCell = (LinearLayout) View.inflate(TodayStatisticsDetailActivity.this, R.layout.construction_detail_item, null);
                return listCell;
            }

            @Override
            protected void onPageCellSetData(int position, View cell, Object data) {
                EasyUI.setTextViewText(cell.findViewById(R.id.ItemNameTextView), (JsonObject) data, "ItemName", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.QualifiedRateTextView), (JsonObject) data, "QualifiedRate", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.DetectedSampleCountTextView), (JsonObject) data, "DetectedSampleCount", "暂无");
                EasyUI.setTextViewText(cell.findViewById(R.id.UnqualifiedSampleCountTextView), (JsonObject) data, "UnqualifiedSampleCount", "暂无");
            }
        };
        wrapper.wrap(listView);
    }

    /**
     * 获取材料检测的所有数据
     */
    private void connectList() {
        String token = Constants.User.GET_TOKEN();
        HashMap<String, Object> params = new HashMap<>();
        String projectId = getIntent().getStringExtra(Constants.Site.KEY_PROJECT_ID);
        String type = "1";
        params.put("Token", token);
        params.put("Type", type);
        params.put("ProjectId", projectId);
        EasyAPI.apiConnectionAsync(this, true, false,
                ApiMethodDescription.get("/ServiceTS.asmx/ManageUnitTodayStatisInfoList"), params, result -> {
                    JsonObject jsonObject = result.getAsJsonObject().get("Data").getAsJsonObject();
                    System.out.println(jsonObject);
                    list = jsonObject.get("ListContent").getAsJsonArray();
                    if (list == null || list.isJsonNull() || list.size() == 0) {
                        LSAlert.showAlert(TodayStatisticsDetailActivity.this, "无相关记录");
                        return;
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    /**
     * 获取工程检测数据
     */
    private void getProjectQueryList() {
        String token = Constants.User.GET_TOKEN();
        HashMap<String, Object> params = new HashMap<>();
        String projectId = getIntent().getStringExtra(Constants.Site.KEY_PROJECT_ID);
        params.put("Token", token);
        params.put("ProjectId", projectId);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceStake.asmx/GetJCLCount"), params, result -> {
            boolean success = result.getAsJsonObject().get("Success").getAsBoolean();
            if (success) {
                //返回数据成功
                JsonObject data = result.getAsJsonObject().get("Data").getAsJsonObject();
                System.out.println(data);
                //如果返回的data为空数据，提示用户没有数据
                ProjectQueryTestingBean projectQueryTestingBean = new Gson().fromJson(data, ProjectQueryTestingBean.class);
                /**
                 * 这里得到的都是百分率，所以要通过计算得到不合格率
                 * 前三组按照(bhgstakeM1_int / stakeM1_int)*100
                 * 后三组按照(bhgconcreteM1_int/ concreteM1_int)
                 * 所得结果都是保留两位小数
                 *
                 */
                String stakem1Percent = getStakemPercent(projectQueryTestingBean.getBhgstakem1(), projectQueryTestingBean.getStakem1());
                String stakem2Percent = getStakemPercent(projectQueryTestingBean.getBhgstakem2(), projectQueryTestingBean.getStakem2());
                String stakem3Percent = getStakemPercent(projectQueryTestingBean.getBhgstakem3(), projectQueryTestingBean.getStakem3());
                String concreteM1Percent = getStakemPercent(projectQueryTestingBean.getBhgconcretem1(), projectQueryTestingBean.getConcretem1());
                String concreteM2Percent = getStakemPercent(projectQueryTestingBean.getBhgconcretem2(), projectQueryTestingBean.getConcretem2());
                String concreteM3Percent = getStakemPercent(projectQueryTestingBean.getBhgconcretem3(), projectQueryTestingBean.getConcretem3());
                projectTestingLowUnqualified.setText(stakem1Percent);
                projectTestingHighUnqualified.setText(stakem2Percent);
                projectTestingJizhuangUnqualified.setText(stakem3Percent);
                projectTestingHuitanUnqualified.setText(concreteM1Percent);
                projectTestingZongheUnqualified.setText(concreteM2Percent);
                projectTestingZuanxinUnqualified.setText(concreteM3Percent);

            } else {
                //返回不成功
                String message = result.getAsJsonObject().get("Message").getAsString();
                LSAlert.showAlert(TodayStatisticsDetailActivity.this, message);
            }

        });
    }

    /**
     * 计算得到保留两位小数的百分数
     *
     * @param s1
     * @param s2
     * @return
     */
    private String getStakemPercent(int s1, int s2) {
        float v = ((float) s1 / s2) * 100;
        String format = String.format("%.2f%%", v);
        return Float.isNaN(v) ? "" : format;
    }

    protected BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup parentViewGroup) {
            if (view == null) {
                view = View.inflate(TodayStatisticsDetailActivity.this, R.layout.construction_detail_item, null);
            }
            EasyUI.setTextViewText(view.findViewById(R.id.KindNameTextView), list.get(position), "KindName", "暂无");
            EasyUI.setTextViewText(view.findViewById(R.id.ItemNameTextView), list.get(position), "ItemName", "暂无");
            EasyUI.setTextViewText(view.findViewById(R.id.QualifiedRateTextView), list.get(position), "QualifiedRate", "暂无");
            EasyUI.setTextViewText(view.findViewById(R.id.DetectedSampleCountTextView), list.get(position), "DetectedSampleCount", "暂无");
            EasyUI.setTextViewText(view.findViewById(R.id.UnqualifiedSampleCountTextView), list.get(position), "UnqualifiedSampleCount", "暂无");
            return view;
        }


        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }


    };
}
