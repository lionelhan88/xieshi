package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.EasyGson;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

public class HydetailActivity extends NavigationActivity {
    private BarButtonItem handleButtonItem;
    private TextView tv_mishy_huiyuanhao;
    private TextView tv_mishy_name;
    private TextView tv_mishy_xingzhi;
    private TextView tv_mishy_zhuangtai;
    private TextView tv_mishy_ruhuidate;
    private TextView tv_mishy_bianhao;
    private TextView tv_mishy_daoqidate;
    private TextView tv_mishy_fuzename;
    private TextView tv_mishy_fuzephone;
    private TextView tv_mishy_danweiphone;
    private TextView tv_mishy_dizhi;
    private String fzname;
    private String fzphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydetail);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("会员信息");
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
        tv_mishy_huiyuanhao = (TextView) findViewById(R.id.tv_mishy_huiyuanhao);
        tv_mishy_name = (TextView) findViewById(R.id.tv_mishy_name);
        tv_mishy_xingzhi = (TextView) findViewById(R.id.tv_mishy_xingzhi);
        tv_mishy_zhuangtai = (TextView) findViewById(R.id.tv_mishy_zhuangtai);
        tv_mishy_ruhuidate = (TextView) findViewById(R.id.tv_mishy_ruhuidate);
        tv_mishy_bianhao = (TextView) findViewById(R.id.tv_mishy_bianhao);
        tv_mishy_daoqidate = (TextView) findViewById(R.id.tv_mishy_daoqidate);
        tv_mishy_fuzename = (TextView) findViewById(R.id.tv_mishy_fuzename);
        tv_mishy_fuzephone = (TextView) findViewById(R.id.tv_mishy_fuzephone);
        tv_mishy_danweiphone = (TextView) findViewById(R.id.tv_mishy_danweiphone);
        tv_mishy_dizhi = (TextView) findViewById(R.id.tv_mishy_dizhi);
    }

    private void initData() {
        Intent getintent=getIntent();
        String hyString = getintent.getExtras().getString("HyString");
        System.out.println(hyString);
        String hynum = EasyGson.jsonFromString(hyString).getAsJsonObject().get("MemberId").getAsString();
        String hyname = EasyGson.jsonFromString(hyString).getAsJsonObject().get("MemberName").getAsString();
        String xingzhi = EasyGson.jsonFromString(hyString).getAsJsonObject().get("MemberType").getAsString();
        String zhuangtai = EasyGson.jsonFromString(hyString).getAsJsonObject().get("MemberStatus").getAsString();
        String ruhuidate = EasyGson.jsonFromString(hyString).getAsJsonObject().get("JoinDate").getAsString();
        String bianhao = EasyGson.jsonFromString(hyString).getAsJsonObject().get("CertificateId").getAsString();
        String daoqidate = EasyGson.jsonFromString(hyString).getAsJsonObject().get("CertificateExpirationDate").getAsString();
        String fuzeren = EasyGson.jsonFromString(hyString).getAsJsonObject().get("Fzr").getAsString();
        if(fuzeren.contains("/")){
            String[] split = fuzeren.split("/");
            fzname = split[0];
            if(split.length>=3&&!"".equals(split[2])) {
                fzphone = split[2];
            }
        }
        String danweiphone = EasyGson.jsonFromString(hyString).getAsJsonObject().get("PhoneNumber").getAsString();
        String dizhi = EasyGson.jsonFromString(hyString).getAsJsonObject().get("ContactAddress").getAsString();

        tv_mishy_huiyuanhao.setText(hynum);
        tv_mishy_name.setText(hyname);
        tv_mishy_xingzhi.setText(xingzhi);
        tv_mishy_zhuangtai.setText(zhuangtai);
        tv_mishy_ruhuidate.setText(ruhuidate);
        tv_mishy_bianhao.setText(bianhao);
        tv_mishy_daoqidate.setText(daoqidate);
        tv_mishy_fuzename.setText(fzname);
        tv_mishy_fuzephone.setText(fzphone);
        tv_mishy_danweiphone.setText(danweiphone);
        tv_mishy_dizhi.setText(dizhi);

    }
}
