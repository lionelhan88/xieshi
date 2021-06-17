package com.lessu.xieshi.module.mis.activities;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.lessu.navigation.BarButtonItem;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.IndexActivity;
import com.lessu.xieshi.module.mis.bean.MisGuideBean;
import com.lessu.xieshi.module.scan.ScanActivity;
import com.lessu.xieshi.module.weather.WeatherDetailActivity;
import com.lessu.xieshi.set.SettingActivity;

import butterknife.BindView;

/**
 * 协会内部人员登录后进入的主菜单界面
 */
public class MisGuideActivity extends IndexActivity {
    @BindView(R.id.ll_addparent)
    LinearLayout llParentView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_misguideactivity;
    }

    @Override
    protected void initView() {
        setTitle("内部管理");
        navigationBar.setLeftBarItem(null);
        BarButtonItem weatherItem = new BarButtonItem(this, R.drawable.duoyund);
        BarButtonItem scanItem = new BarButtonItem(this, R.drawable.icon_scan_white);
        weatherItem.setOnClickMethod(this, "openWeatherPage");
        scanItem.setOnClickMethod(this, "openScan");
        navigationBar.addRightBarItem(scanItem);
        navigationBar.setLeftBarItem(weatherItem);
        addItemView(new MisGuideBean(R.drawable.shezhimis, "系统设置", SettingActivity.class));
        super.initView();
    }

    /**
     * 添加菜单选项
     *
     * @param misguidebean 菜单选项
     */
    @Override
    protected void addItemView(MisGuideBean misguidebean) {
        View view = View.inflate(this, R.layout.misaddguide_item, null);
        LinearLayout ll_additem = view.findViewById(R.id.ll_additem);
        ImageView iv_additem = view.findViewById(R.id.iv_additem);
        TextView tv_additem = view.findViewById(R.id.tv_additem);
        iv_additem.setImageResource(misguidebean.pic);
        tv_additem.setText(misguidebean.text);
        ll_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (misguidebean.clazz.getName().contains("MeetingListActivity")) {
                    Intent meetingIntent = new Intent(MisGuideActivity.this, misguidebean.clazz);
                    meetingIntent.putExtra("type_user", 0);
                    startActivity(meetingIntent);
                } else {
                    startOtherActivity(misguidebean.clazz);
                }
            }
        });
        llParentView.addView(view, 0);
    }

    @PermissionNeed(value = {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode = 1)
    public void openWeatherPage() {
        startOtherActivity(WeatherDetailActivity.class);
    }

    @PermissionNeed(value = {Manifest.permission.CAMERA}, requestCode = 0)
    public void openScan() {
        startActivity(new Intent((MisGuideActivity.this), ScanActivity.class));
    }

    /**
     * 如果用户永久拒绝了，就要打开
     */
    @PermissionDenied
    private void shouldOpenLocation(int requestCode) {
        if (requestCode == 1) {
            LSAlert.showDialog(this, "提示", "天气功能需要定位权限，请在系统设置中打开权限！", "去设置", "不设置",
                    new LSAlert.DialogCallback() {
                        @Override
                        public void onConfirm() {
                            PermissionSettingPage.start(MisGuideActivity.this, true);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }
}
