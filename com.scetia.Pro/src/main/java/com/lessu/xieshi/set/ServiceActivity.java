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
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.network.manage.XSRetrofit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends NavigationActivity {
    private String serviceText;
    @BindView(R.id.telecomIcon)
    ImageView telecomIcon;
    @BindView(R.id.telecomButton)
    LinearLayout telecomButton;
    @BindView(R.id.unicomIcon)
    ImageView unicomIcon;
    @BindView(R.id.unicomButton)
    LinearLayout unicomButton;

    @Override
    protected int getLayoutId() {
        return R.layout.service_activity;
    }

    @Override
    protected void initView() {
        setTitle("选择服务器");
        BarButtonItem completeButtonItem = new BarButtonItem(this, "完成");
        completeButtonItem.setOnClickMethod(this, "completeButtonDidClick");
        navigationBar.setRightBarItem(completeButtonItem);

        BarButtonItem nullButtonItem = new BarButtonItem(this, "");
        navigationBar.setLeftBarItem(nullButtonItem);
        serviceText = SPUtil.GET_SERVICE_API();
        if (serviceText.equals(ConstantApi.XS_TELECOM_BASE_URL)) {
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
        SPUtil.setSPLSUtil(Constants.Setting.SERVICE,serviceText);
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
