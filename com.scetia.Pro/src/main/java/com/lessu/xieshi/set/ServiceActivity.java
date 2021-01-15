package com.lessu.xieshi.set;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiBase;
import com.lessu.xieshi.R;
import com.scetia.Pro.network.ConstantApi;
import com.scetia.Pro.common.Util.Common;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.network.manage.XSRetrofit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends NavigationActivity {
    private String serviceText = "";
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
        if (SPUtil.getSPLSUtil("service", "").equals(Common.TELECOM_SERVICE)) {
            //电信
            telecomIcon.setImageResource(R.drawable.icon_service_selected);
        } else {
            //联通
            unicomIcon.setImageResource(R.drawable.icon_service_selected);
        }
        serviceText = Common.getService(SPUtil.getSPLSUtil("service", ""));

    }

    /**
     * “完成”点击事件
     */
    public void completeButtonDidClick() {
        if (serviceText.contains("h")) {
            SPUtil.setSPLSUtil("service", "unicom");
        } else {
            SPUtil.setSPLSUtil("service", "telecom");
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
                serviceText = ConstantApi.XS_TELECOM_BASE_URL;
                telecomIcon.setImageResource(R.drawable.icon_service_selected);
                unicomIcon.setImageResource(R.drawable.icon_service_unselected);
                break;
            case R.id.unicomButton:
                serviceText = ConstantApi.XS_UNICOM_BASE_URL;
                unicomIcon.setImageResource(R.drawable.icon_service_selected);
                telecomIcon.setImageResource(R.drawable.icon_service_unselected);
                break;
        }
    }
}
