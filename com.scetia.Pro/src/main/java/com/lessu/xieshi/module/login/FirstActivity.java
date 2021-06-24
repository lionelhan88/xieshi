package com.lessu.xieshi.module.login;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.BuildConfig;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.base.IndexActivity;
import com.lessu.xieshi.module.construction.ConstructionListActivity;
import com.lessu.xieshi.module.dataauditing.DataAuditingActivity;
import com.lessu.xieshi.module.dataexamine.DataExamineActivity;
import com.lessu.xieshi.module.foundationpile.ProjectListActivity;
import com.lessu.xieshi.module.meet.activity.MeetingListActivity;
import com.lessu.xieshi.module.sand.SandHomeActivity;
import com.lessu.xieshi.module.scan.BluetoothActivity;
import com.lessu.xieshi.module.scan.PrintDataActivity;
import com.lessu.xieshi.module.scan.SampleIdentificationActivity;
import com.lessu.xieshi.module.todaystatistics.SiteSearchListMapActivity;
import com.lessu.xieshi.module.todaystatistics.TodayStatisticsActivity;
import com.lessu.xieshi.module.training.TrainingActivity;
import com.lessu.xieshi.module.unqualified.UnqualifiedSearchActivity;
import com.lessu.xieshi.module.weather.WeatherDetailActivity;
import com.lessu.xieshi.set.SettingActivity;
import com.lessu.xieshi.uploadpicture.UploadPictureActivity;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.GlideUtil;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.common.photo.XXPhotoUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class FirstActivity extends IndexActivity {
    @BindView(R.id.iv_tianqi)
    ImageView ivTianqi;
    @BindView(R.id.tv_tianqi)
    TextView tvTianqi;
    @BindView(R.id.first_tv_tmp)
    TextView firstTvTmp;
    @BindView(R.id.first_set)
    ImageView firstSet;
    @BindView(R.id.ll_tianqi)
    RelativeLayout llTianqi;
    @BindView(R.id.iv_touxiang)
    ImageView ivTouxiang;
    @BindView(R.id.tv_yonghuming)
    TextView tvYonghuming;
    @BindView(R.id.iv_seccion1)
    ImageView ivSeccion1;
    @BindView(R.id.tv_seccion1)
    TextView tvSeccion1;
    @BindView(R.id.ll_seccion1)
    LinearLayout llSeccion1;
    @BindView(R.id.iv_seccion2)
    ImageView ivSeccion2;
    @BindView(R.id.tv_seccion2)
    TextView tvSeccion2;
    @BindView(R.id.ll_seccion2)
    LinearLayout llSeccion2;
    @BindView(R.id.iv_seccion3)
    ImageView ivSeccion3;
    @BindView(R.id.tv_seccion3)
    TextView tvSeccion3;
    @BindView(R.id.ll_seccion3)
    LinearLayout llSeccion3;
    @BindView(R.id.iv_seccion4)
    ImageView ivSeccion4;
    @BindView(R.id.tv_seccion4)
    TextView tvSeccion4;
    @BindView(R.id.ll_seccion4)
    LinearLayout llSeccion4;
    @BindView(R.id.iv_seccion5)
    ImageView ivSeccion5;
    @BindView(R.id.tv_seccion5)
    TextView tvSeccion5;
    @BindView(R.id.ll_seccion5)
    LinearLayout llSeccion5;
    @BindView(R.id.iv_seccion6)
    ImageView ivSeccion6;
    @BindView(R.id.tv_seccion6)
    TextView tvSeccion6;
    @BindView(R.id.ll_seccion6)
    LinearLayout llSeccion6;
    private static final String permission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_first;
    }

    @Override
    protected View getTitleBar() {
        return llTianqi;
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this).titleBarMarginTop(llTianqi)
                .navigationBarColor(R.color.light_gray)
                .navigationBarDarkIcon(true).init();
    }

    /**
     * 监听数据变化，更新UI界面
     */
    @Override
    protected void observerData() {
        super.observerData();
        mViewModel.getHourBeanData().observe(this, dataBean -> {
            String cityName = dataBean.getCityName();
            String wthr = dataBean.getWthr();
            tvTianqi.setText(cityName + " > " + wthr);
            firstTvTmp.setText(dataBean.getTemp() + "℃");
        });
    }

    @Override
    protected void initView() {
        super.initView();
        navigationBar.setVisibility(View.GONE);
        //该功能还未开放！！！！！！
        llSeccion6.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        //加载头像
        GlideUtil.showImageViewNoCacheCircle(this, R.drawable.touxiang,
                SPUtil.getSPConfig(Constants.User.PIC_NAME, ""), ivTouxiang);
        String userName = SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, "");
        tvYonghuming.setText(userName);
    }

    @Override
    protected void initExternalMenu(String power) {
        if (power.equals("1")) {
            //供应商账号
            llSeccion1.setVisibility(View.VISIBLE);
            ivSeccion1.setImageResource(R.drawable.home_meeting_bg);
            tvSeccion1.setText("用砂管理");
            llSeccion1.setOnClickListener(v -> {
                startOtherActivity(SandHomeActivity.class);
            });
            llSeccion2.setVisibility(View.INVISIBLE);
            llSeccion3.setVisibility(View.INVISIBLE);
            llSeccion4.setVisibility(View.INVISIBLE);
            llSeccion5.setVisibility(View.INVISIBLE);
            llSeccion6.setVisibility(View.INVISIBLE);
            //界面初始化完成，开启自动登录
            SPUtil.setSPConfig(SPUtil.AUTO_LOGIN_KEY, true);
            return;
        }

        final String userPower = power.substring(0, 14);
        char userPower2 = power.charAt(15);
    /*    if (BuildConfig.DEBUG) {
            //gly 1319 质监站人员
            llSeccion1.setVisibility(View.VISIBLE);
            llSeccion2.setVisibility(View.VISIBLE);
            llSeccion3.setVisibility(View.VISIBLE);
            llSeccion4.setVisibility(View.VISIBLE);
            llSeccion5.setVisibility(View.INVISIBLE);
            llSeccion6.setVisibility(View.INVISIBLE);

            ivSeccion1.setImageResource(R.drawable.jinritongji1);
            tvSeccion1.setText("工地查询");
            ivSeccion2.setImageResource(R.drawable.xinxichaxun);
            tvSeccion2.setText("不合格信息查询");
            ivSeccion3.setImageResource(R.drawable.gongchengchaxun1);
            tvSeccion3.setText("基桩静载");
            ivSeccion4.setImageResource(R.drawable.yangpinshibie);
            tvSeccion4.setText("样品识别");
            llSeccion1.setOnClickListener(view -> {
                Intent intent = new Intent(FirstActivity.this, SiteSearchListMapActivity.class);
                startActivity(intent);
            });
            llSeccion2.setOnClickListener(view -> {
                Intent intent = new Intent(FirstActivity.this, UnqualifiedSearchActivity.class);
                startActivity(intent);
            });
            llSeccion3.setOnClickListener(view -> {
                Intent intent = new Intent(FirstActivity.this, ProjectListActivity.class);
                startActivity(intent);
            });
            llSeccion4.setOnClickListener(view -> {
                Intent intent = new Intent();
                if (!SPUtil.getSPConfig("deviceaddress", "").equals("")) {
                    intent.setClass(FirstActivity.this, SampleIdentificationActivity.class);
                } else {
                    AppApplication.isGLY = false;
                    intent.setClass(FirstActivity.this, BluetoothActivity.class);
                }
                startActivity(intent);
            });
        return;
    }*/
        if (userPower.equals("00010010100000")) {//j20623 279162 见证人
            llSeccion1.setVisibility(View.VISIBLE);
            llSeccion2.setVisibility(View.VISIBLE);
            llSeccion3.setVisibility(View.VISIBLE);
            llSeccion4.setVisibility(View.INVISIBLE);
            llSeccion5.setVisibility(View.INVISIBLE);
            llSeccion6.setVisibility(View.INVISIBLE);
            ivSeccion1.setImageResource(R.drawable.yangpinchaxun);
            tvSeccion1.setText("样品查询");
            ivSeccion2.setImageResource(R.drawable.tupianshangchuan);
            tvSeccion2.setText("现场图片上传");
            ivSeccion3.setImageResource(R.drawable.yangpinshibie);
            tvSeccion3.setText("样品识别");
            llSeccion1.setOnClickListener(view -> startOtherActivity(ConstructionListActivity.class));
            llSeccion2.setOnClickListener(view -> startOtherActivity(UploadPictureActivity.class));
            llSeccion3.setOnClickListener(view -> {
                if (!SPUtil.getSPConfig(SPUtil.BLUETOOTH_DEVICE, "").equals("")) {
                    startOtherActivity(SampleIdentificationActivity.class);
                } else {
                    AppApplication.isGLY = false;
                    startOtherActivity(BluetoothActivity.class);
                }
            });
        }

        //Q13503 123456 取样人
        if (userPower.equals("00010000100000")) {
            llSeccion1.setVisibility(View.VISIBLE);
            llSeccion2.setVisibility(View.VISIBLE);
            llSeccion3.setVisibility(View.INVISIBLE);
            llSeccion4.setVisibility(View.INVISIBLE);
            llSeccion5.setVisibility(View.INVISIBLE);
            llSeccion6.setVisibility(View.INVISIBLE);
            ivSeccion1.setImageResource(R.drawable.yangpinchaxun);
            tvSeccion1.setText("样品查询");
            ivSeccion2.setImageResource(R.drawable.yangpinshibie);
            tvSeccion2.setText("样品识别");
            llSeccion1.setOnClickListener(view -> startOtherActivity(ConstructionListActivity.class));

            llSeccion2.setOnClickListener(view -> {
                if (!SPUtil.getSPConfig("deviceaddress", "").equals("")) {
                    startOtherActivity(SampleIdentificationActivity.class);
                } else {
                    AppApplication.isGLY = false;
                    startOtherActivity(BluetoothActivity.class);
                }
            });
        }

        //gly 1319 质监站人员
        if (userPower.equals("10001101100000")) {
            llSeccion1.setVisibility(View.VISIBLE);
            llSeccion2.setVisibility(View.VISIBLE);
            llSeccion3.setVisibility(View.VISIBLE);
            llSeccion4.setVisibility(View.VISIBLE);
            llSeccion5.setVisibility(View.INVISIBLE);
            llSeccion6.setVisibility(View.INVISIBLE);
            ivSeccion1.setImageResource(R.drawable.jinritongji1);
            tvSeccion1.setText("工地查询");
            ivSeccion2.setImageResource(R.drawable.xinxichaxun);
            tvSeccion2.setText("不合格信息查询");
            ivSeccion3.setImageResource(R.drawable.gongchengchaxun1);
            tvSeccion3.setText("基桩静载");
            ivSeccion4.setImageResource(R.drawable.yangpinshibie);
            tvSeccion4.setText("样品识别");
            llSeccion1.setOnClickListener(view -> startOtherActivity(SiteSearchListMapActivity.class));
            llSeccion2.setOnClickListener(view -> startOtherActivity(UnqualifiedSearchActivity.class));
            llSeccion3.setOnClickListener(view -> startOtherActivity(ProjectListActivity.class));
            llSeccion4.setOnClickListener(view -> {
                if (!SPUtil.getSPConfig("deviceaddress", "").equals("")) {
                    startOtherActivity(SampleIdentificationActivity.class);
                } else {
                    AppApplication.isGLY = false;
                    startOtherActivity(BluetoothActivity.class);
                }
            });
        }

        //t9990001 1 检测人员
        if (userPower.equals("01101000000000")) {
            //如果登录的账号中有"Meet"开头的，才显示会议菜单按钮，其他隐藏
            if (SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, "").toUpperCase().startsWith("MEET")) {
                llSeccion1.setVisibility(View.VISIBLE);
                llSeccion2.setVisibility(View.INVISIBLE);
                llSeccion3.setVisibility(View.INVISIBLE);
                llSeccion4.setVisibility(View.INVISIBLE);
                llSeccion5.setVisibility(View.INVISIBLE);
                llSeccion6.setVisibility(View.INVISIBLE);
                ivSeccion1.setImageResource(R.drawable.home_meeting_bg);
                tvSeccion1.setText("会议安排");
                llSeccion1.setOnClickListener(view -> {
                    Intent intentMeeting = new Intent(FirstActivity.this, MeetingListActivity.class);
                    intentMeeting.putExtra("type_user", 1);
                    startActivity(intentMeeting);
                });
            } else {
                llSeccion1.setVisibility(View.VISIBLE);
                llSeccion2.setVisibility(View.VISIBLE);
                llSeccion3.setVisibility(View.VISIBLE);
                llSeccion4.setVisibility(View.VISIBLE);
                llSeccion5.setVisibility(View.VISIBLE);
                //暂时定位 用砂管理
                llSeccion6.setVisibility(View.INVISIBLE);
                ivSeccion1.setImageResource(R.drawable.shujushenhe1);
                tvSeccion1.setText("记录审核");
                llSeccion1.setOnClickListener(view -> startOtherActivity(DataAuditingActivity.class));
            }
            ivSeccion2.setImageResource(R.drawable.baogaopizhun);
            tvSeccion2.setText("报告批准");
            ivSeccion3.setImageResource(R.drawable.jinritongji1);
            tvSeccion3.setText("统计信息");
            ivSeccion4.setImageResource(R.drawable.shouchijisaomiao);
            tvSeccion4.setText("手持机扫描");
            if (userPower2 == '1') {
                ivSeccion5.setImageResource(R.drawable.zaixianjiaoyu);
            }
            tvSeccion5.setText("在线培训");
            ivSeccion6.setImageResource(R.drawable.home_meeting_bg);
            tvSeccion6.setText("用砂管理");

            llSeccion2.setOnClickListener(view -> startOtherActivity( DataExamineActivity.class));
            llSeccion3.setOnClickListener(view -> {
                startOtherActivity(TodayStatisticsActivity.class);
            });
            llSeccion4.setOnClickListener(view -> {
                if (!SPUtil.getSPConfig("deviceaddress", "").equals("")) {
                    startOtherActivity(PrintDataActivity.class);
                } else {
                    AppApplication.isGLY = true;
                    startOtherActivity(BluetoothActivity.class);
                }
            });
            llSeccion5.setOnClickListener(view -> {
                Intent intentXITONG = new Intent(FirstActivity.this, TrainingActivity.class);
                startActivity(intentXITONG);

            });
            llSeccion6.setOnClickListener(view -> startOtherActivity(SandHomeActivity.class));
        }
    }

    /**
     * 打开天气页面
     */
    @PermissionNeed(value = permission, requestCode = 1)
    private void openWeatherPage() {
        startOtherActivity(WeatherDetailActivity.class);
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
                            PermissionSettingPage.start(FirstActivity.this, true);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    }

    @OnClick({R.id.iv_tianqi, R.id.first_set, R.id.iv_touxiang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tianqi:
                openWeatherPage();
                break;
            case R.id.first_set:
                //跳转到设置页面
                startOtherActivity(SettingActivity.class);
                break;
            case R.id.iv_touxiang:
                XXPhotoUtil.with(this).setPhotoName("header").setListener((photoPath, photoUri) -> {
                    GlideUtil.showImageViewNoCacheCircle(FirstActivity.this, R.drawable.touxiang, photoPath, ivTouxiang);
                    //保存头像目录
                    SPUtil.setSPConfig(Constants.User.PIC_NAME, photoPath);
                }).start();
                break;
        }
    }
}
