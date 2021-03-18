package com.lessu.xieshi.module.foundationpile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.scetia.Pro.common.Util.SPUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.OnClick;

public class ProjectSearchActivity extends XieShiSlidingMenuActivity {
    JsonArray projectJson = new JsonArray();
    String token = "";
    String memberCode = "";
    String hour = "4";

    @Override
    protected int getLayoutId() {
        return R.layout.project_search_activity;
    }

    @Override
    protected void initView() {
        this.setTitle("工程查询");
    }

    @Override
    protected void initData() {
        getType();
    }

    private void getType() {
        token = SPUtil.getSPLSUtil("Token", "");
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceStake.asmx/GetMemberList"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {

            @Override
            public void onSuccessJson(JsonElement result) {
                JsonArray json = result.getAsJsonObject().get("Data").getAsJsonArray();
                projectJson = json;
            }

            @Override
            public String onFailed(ApiError error) {
                int i = 1;
                return null;
            }
        });
    }

    @OnClick(R.id.projectButton)
    protected void projectButtonDidClick() {
        ArrayList<String> itemArrayList = new ArrayList<String>();
        int typeJsonSize = projectJson.size();
        if (typeJsonSize <= 0) {
            LSAlert.showAlert(ProjectSearchActivity.this, "目前没有会员");
            return;
        }
        for (int i = 0; i < projectJson.size(); i++) {
            itemArrayList.add(projectJson.get(i).getAsJsonObject().get("MemberName").getAsString());
        }
        final String[] itemString = itemArrayList.toArray(new String[projectJson.size()]);
        new AlertDialog.Builder(this)
                .setTitle("会员选择")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(itemString, 0,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                TextView tv = findViewById(R.id.projectTextView);
                                tv.setText(itemString[which]);
                                memberCode = projectJson.get(which).getAsJsonObject().get("MemberCode").getAsString();
                                dialog.dismiss();

                            }
                        }
                )
                .setNegativeButton("取消", null)
                .show();
    }

    @OnClick(R.id.timeButton)
    protected void timeButtonDidClick() {
        final String[] timeAging = new String[24];
        for (int i=1;i<=24;i++){
            timeAging[i-1] = String.valueOf(i);
        }
        new AlertDialog.Builder(this)
                .setTitle("时效选择")
                .setIcon(R.drawable.zuijin)
                .setSingleChoiceItems(timeAging, 4,
                        (dialog, which) -> {
                            TextView tv = findViewById(R.id.timeTextView);
                            tv.setText(timeAging[which]);
                            hour = timeAging[which];
                            dialog.dismiss();
                        }
                )
                .setNegativeButton("取消", null)
                .show();
    }


    @OnClick(R.id.searchButton)
    protected void searchButtonDidClick() {
        Bundle bundle = new Bundle();
        bundle.putString("MemberCode", memberCode);
        bundle.putString("Hour", hour);
        setResult(RESULT_OK, this.getIntent().putExtras(bundle));
        finish();
    }
}
