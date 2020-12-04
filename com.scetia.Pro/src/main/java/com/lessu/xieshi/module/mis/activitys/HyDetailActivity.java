package com.lessu.xieshi.module.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.gson.EasyGson;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.bean.MisHySearchResultData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HyDetailActivity extends NavigationActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydetail);
        navigationBar.setBackgroundColor(ContextCompat.getColor(this,R.color.top_bar_background));
        this.setTitle("会员信息");
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        tv_mishy_huiyuanhao = findViewById(R.id.tv_mishy_huiyuanhao);
        tv_mishy_name = findViewById(R.id.tv_mishy_name);
        tv_mishy_xingzhi = findViewById(R.id.tv_mishy_xingzhi);
        tv_mishy_zhuangtai = findViewById(R.id.tv_mishy_zhuangtai);
        tv_mishy_ruhuidate = findViewById(R.id.tv_mishy_ruhuidate);
        tv_mishy_bianhao = findViewById(R.id.tv_mishy_bianhao);
        tv_mishy_daoqidate = findViewById(R.id.tv_mishy_daoqidate);
        tv_mishy_fuzename = findViewById(R.id.tv_mishy_fuzename);
        tv_mishy_fuzephone = findViewById(R.id.tv_mishy_fuzephone);
        tv_mishy_danweiphone = findViewById(R.id.tv_mishy_danweiphone);
        tv_mishy_dizhi = findViewById(R.id.tv_mishy_dizhi);
    }

    /**
     * 初始化数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void initData(MisHySearchResultData.ListContentBean contentBean) {
        EventBus.getDefault().removeStickyEvent(contentBean);
        tv_mishy_huiyuanhao.setText(contentBean.getMemberId());
        tv_mishy_name.setText(contentBean.getMemberName());
        tv_mishy_xingzhi.setText(contentBean.getMemberType());
        tv_mishy_zhuangtai.setText(contentBean.getMemberStatus());
        tv_mishy_ruhuidate.setText(contentBean.getJoinDate());
        tv_mishy_bianhao.setText(contentBean.getCertificateId());
        tv_mishy_daoqidate.setText(contentBean.getCertificateExpirationDate());
        String fzr = contentBean.getFzr();
        String fzname="";
        String fzphone="";
        if(fzr.contains("/")){
            String[] split = fzr.split("/");
            fzname = split[0];
            if(split.length>=3&&!"".equals(split[2])) {
                fzphone = split[2];
            }
        }
        tv_mishy_fuzename.setText(fzname);
        tv_mishy_fuzephone.setText(fzphone);
        tv_mishy_danweiphone.setText(contentBean.getPhoneNumber());
        tv_mishy_dizhi.setText(contentBean.getContactAddress());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
