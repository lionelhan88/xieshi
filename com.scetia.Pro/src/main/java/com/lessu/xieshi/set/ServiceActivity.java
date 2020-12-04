package com.lessu.xieshi.set;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiBase;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.http.XSRetrofit;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends NavigationActivity {
    String serviceText = "";
    @BindView(R.id.telecomIcon)
    ImageView telecomIcon;
    @BindView(R.id.telecomButton)
    LinearLayout telecomButton;
    @BindView(R.id.unicomIcon)
    ImageView unicomIcon;
    @BindView(R.id.unicomButton)
    LinearLayout unicomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_activity);
        ButterKnife.bind(this);
        setTitle("选择服务器");

        BarButtonItem completeButtonItem = new BarButtonItem(this, "完成");
        completeButtonItem.setOnClickMethod(this, "completeButtonDidClick");
        navigationBar.setRightBarItem(completeButtonItem);

        BarButtonItem nullButtonItem = new BarButtonItem(this, "");
        navigationBar.setLeftBarItem(nullButtonItem);
        if(LSUtil.valueStatic("service").equals("telecom")){
            //电信
            telecomIcon.setImageResource(R.drawable.icon_service_selected);
        }else{
            //联通
            unicomIcon.setImageResource(R.drawable.icon_service_selected);
        }
    }

    /**
     * “完成”点击事件
     */
    public void completeButtonDidClick() {
        if(serviceText.contains("h")){
            LSUtil.setValueStatic("service", "unicom");
        }else{
            LSUtil.setValueStatic("service", "telecom");
        }
        ApiBase.sharedInstance().apiUrl = serviceText;
        XSRetrofit.getInstance().changeBaseUrl("http://" + serviceText + "/");
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.telecomButton, R.id.unicomButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.telecomButton:
                serviceText = "www.scetia.com/scetia.app.ws";
                telecomIcon.setImageResource(R.drawable.icon_service_selected);
                unicomIcon.setImageResource(R.drawable.icon_service_unselected);
                break;
            case R.id.unicomButton:
                serviceText = "www.schetia.com/scetia.app.ws";
                unicomIcon.setImageResource(R.drawable.icon_service_selected);
                telecomIcon.setImageResource(R.drawable.icon_service_unselected);
                break;
        }
    }
}
