package com.lessu.xieshi.module.todaystatistics;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;

import butterknife.OnClick;

public class SiteSearchByConditionActivity extends NavigationActivity implements View.OnClickListener {
    String projectName = "";
    String projectArea = "";
    String baogaobianhao = "";
    String jianshedanwei = "";
    String shigongdanwei = "";
    String jianlidanwei = "";
    String jiancedanwei = "";
    private int xianshistage = -1;
    private int rangeindex;
    private String currentLocation;
    private CheckBox cb_xainshi;

    @Override
    protected int getLayoutId() {
        return R.layout.admin_today_statistics_search_activity;
    }

    @Override
    protected void initView() {
        this.setTitle("工地搜索");
        Bundle bundle = getIntent().getExtras();
        projectName = bundle.getString("ProjectName");
        projectArea = bundle.getString("ProjectArea");
        baogaobianhao = bundle.getString("baogaobianhao");
        jianshedanwei = bundle.getString("jianshedanwei");
        shigongdanwei = bundle.getString("shigongdanwei");
        jianlidanwei = bundle.getString("jianlidanwei");
        jiancedanwei = bundle.getString("jiancedanwei");
        rangeindex = bundle.getInt("range");
        currentLocation = bundle.getString("CurrentLocation");
        TextView tv1 = findViewById(R.id.projectNameTextView);
        tv1.setText(projectName);
        Button bt_qveren = findViewById(R.id.bt_qveren);
        bt_qveren.setOnClickListener(this);

        TextView tv2 = findViewById(R.id.projectAreaTextView);
        tv2.setText(projectArea);
        TextView tv3 = findViewById(R.id.baogaobianhao);
        tv3.setText(baogaobianhao);
        TextView tv4 = findViewById(R.id.jianshedanwei);
        tv4.setText(jianshedanwei);
        TextView tv5 = findViewById(R.id.shigongdanwei);
        tv5.setText(shigongdanwei);
        TextView tv6 = findViewById(R.id.jianlidanwei);
        tv6.setText(jianlidanwei);
        TextView tv7 = findViewById(R.id.jiancedanwei);
        tv7.setText(jiancedanwei);

        cb_xainshi = findViewById(R.id.cb_xainshi);
        xianshistage = 1;
        cb_xainshi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    xianshistage = 2;
                } else {
                    xianshistage = 1;
                }
            }
        });
    }

    @OnClick(R.id.ProjectAreaButton)
    protected void ProjectAreaButtonDidClick() {
        final String[] itemString = {"金山区", "崇明县", "浦东新区", "南汇区", "闵行区", "卢湾区", "徐汇区", "奉贤区", "宝山区", "杨浦区", "普陀区", "黄浦区", "青浦区", "松江区", "外省市", "闸北区", "长宁区", "嘉定区", "静安区", "虹口区"};
        final String[] valueString = itemString;
        new AlertDialog.Builder(this)
                .setTitle("工程区县")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(itemString, 0,
                        (dialog, which) -> {
                            TextView tv = findViewById(R.id.projectAreaTextView);
                            tv.setText(itemString[which]);
                            projectArea = valueString[which];
                            dialog.dismiss();
                        }
                )
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onClick(View view) {

        Bundle bundle = new Bundle();

        TextView tv1 = findViewById(R.id.projectNameTextView);
        projectName = tv1.getText().toString();
        bundle.putString("ProjectName", projectName);
        TextView tv2 = findViewById(R.id.projectAreaTextView);
        projectArea = tv2.getText().toString();
        bundle.putString("ProjectArea", projectArea);
        TextView tv3 = findViewById(R.id.baogaobianhao);
        baogaobianhao = tv3.getText().toString();
        bundle.putString("baogaobianhao", baogaobianhao);
        TextView tv4 = findViewById(R.id.jianshedanwei);
        jianshedanwei = tv4.getText().toString();
        bundle.putString("jianshedanwei", jianshedanwei);
        TextView tv5 = findViewById(R.id.shigongdanwei);
        shigongdanwei = tv5.getText().toString();
        bundle.putString("shigongdanwei", shigongdanwei);
        TextView tv6 = findViewById(R.id.jianlidanwei);
        jianlidanwei = tv6.getText().toString();
        bundle.putString("jianlidanwei", jianlidanwei);
        TextView tv7 = findViewById(R.id.jiancedanwei);
        jiancedanwei = tv7.getText().toString();
        bundle.putString("jiancedanwei", jiancedanwei);
        bundle.putBoolean("sousuo", true);
        if (xianshistage == 1) {
            setResult(RESULT_OK, this.getIntent().putExtras(bundle));
            finish();
        } else if (xianshistage == 2) {
            bundle.putBoolean("isxianshall", true);
            bundle.putInt("DistanceRange", rangeindex);
            System.out.println("发送。。。。。。。。。" + rangeindex);
            bundle.putString("CurrentLocation", currentLocation);
            Intent intent = new Intent(SiteSearchByConditionActivity.this, TodayStatisticsDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            ToastUtil.showShort("请选择显示的内容");
        }
    }
}
