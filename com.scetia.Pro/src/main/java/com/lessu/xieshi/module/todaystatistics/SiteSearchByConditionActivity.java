package com.lessu.xieshi.module.todaystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SiteSearchByConditionActivity extends NavigationActivity {

    @BindView(R.id.statistics_search_site_name)
    EditText statisticsSearchSiteName;
    @BindView(R.id.statistics_search_site_area)
    TextView statisticsSearchSiteArea;
    @BindView(R.id.statistics_search_report_no)
    EditText statisticsSearchReportNo;
    @BindView(R.id.statistics_search_build_name)
    EditText statisticsSearchBuildName;
    @BindView(R.id.statistics_search_construction_name)
    EditText statisticsSearchConstructionName;
    @BindView(R.id.statistics_search_supervision_name)
    EditText statisticsSearchSupervisionName;
    @BindView(R.id.statistics_search_testing_name)
    EditText statisticsSearchTestingName;
    @BindView(R.id.cb_statistics_site_all)
    CheckBox cbStatisticsSiteAll;
    @BindView(R.id.btn_statistics_search_confirm)
    Button btnStatisticsSearchConfirm;
    private String projectName = "";
    private String projectArea = "";
    private String baogaobianhao = "";
    private String jianshedanwei = "";
    private String shigongdanwei = "";
    private String jianlidanwei = "";
    private String jiancedanwei = "";
    private int xianshistage = -1;
    private int rangeIndex;
    private String currentLocation;

    @Override
    protected int getLayoutId() {
        return R.layout.admin_today_statistics_search_activity;
    }

    @Override
    protected void initView() {
        this.setTitle("工地搜索");
        xianshistage = 1;
        cbStatisticsSiteAll.setOnCheckedChangeListener((compoundButton, b) -> {
            xianshistage = b?2:1;
        });
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        projectName = bundle.getString("ProjectName");
        projectArea = bundle.getString("ProjectArea");
        baogaobianhao = bundle.getString("baogaobianhao");
        jianshedanwei = bundle.getString("jianshedanwei");
        shigongdanwei = bundle.getString("shigongdanwei");
        jianlidanwei = bundle.getString("jianlidanwei");
        jiancedanwei = bundle.getString("jiancedanwei");
        rangeIndex = bundle.getInt("range");
        currentLocation = bundle.getString("CurrentLocation");
        statisticsSearchSiteName.setText(projectName);
        statisticsSearchSiteArea.setText(projectArea);
        statisticsSearchReportNo.setText(baogaobianhao);
        statisticsSearchBuildName.setText(jianshedanwei);
        statisticsSearchConstructionName.setText(shigongdanwei);
        statisticsSearchSupervisionName.setText(jianlidanwei);
        statisticsSearchTestingName.setText(jiancedanwei);
    }

    /**
     * 工地所属区县点击事件
     */
    @OnClick(R.id.statistics_search_site_area)
    protected void projectAreaButtonDidClick() {
        final String[] itemString = {"金山区", "崇明县", "浦东新区", "南汇区", "闵行区", "卢湾区", "徐汇区", "奉贤区", "宝山区", "杨浦区", "普陀区", "黄浦区", "青浦区", "松江区", "外省市", "闸北区", "长宁区", "嘉定区", "静安区", "虹口区"};
        LSAlert.showDialogSingleChoice(this, "工程区县", android.R.drawable.ic_dialog_info, itemString, "取消", new LSAlert.SelectItemCallback() {
            @Override
            public void selectItem(int position) {
                statisticsSearchSiteArea.setText(itemString[position]);
            }
        });
    }

    /**
     * 确认按钮点击事件
     */
    @OnClick(R.id.btn_statistics_search_confirm)
    public void btnStatisticsSearchConfirmClick(){
        Bundle bundle = new Bundle();
        projectName = statisticsSearchSiteName.getText().toString();
        bundle.putString("ProjectName", projectName);
        projectArea = statisticsSearchSiteArea.getText().toString();
        bundle.putString("ProjectArea", projectArea);
        baogaobianhao = statisticsSearchReportNo.getText().toString();
        bundle.putString("baogaobianhao", baogaobianhao);
        jianshedanwei = statisticsSearchBuildName.getText().toString();
        bundle.putString("jianshedanwei", jianshedanwei);
        shigongdanwei = statisticsSearchConstructionName.getText().toString();
        bundle.putString("shigongdanwei", shigongdanwei);
        jianlidanwei = statisticsSearchSupervisionName.getText().toString();
        bundle.putString("jianlidanwei", jianlidanwei);
        jiancedanwei = statisticsSearchTestingName.getText().toString();
        bundle.putString("jiancedanwei", jiancedanwei);
        bundle.putBoolean("sousuo", true);
        if (xianshistage == 1) {
            setResult(RESULT_OK, this.getIntent().putExtras(bundle));
            finish();
        } else if (xianshistage == 2) {
            bundle.putBoolean("isxianshall", true);
            bundle.putInt("DistanceRange", rangeIndex);
            bundle.putString("CurrentLocation", currentLocation);
            Intent intent = new Intent(this, TodayStatisticsDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            ToastUtil.showShort("请选择显示的内容");
        }
    }
}
