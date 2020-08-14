package com.lessu.xieshi.training;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.EasyGson;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.MyToast;
import com.lessu.xieshi.Utils.PermissionUtils;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.bean.PaidItem;
import com.lessu.xieshi.bean.Project;
import com.lessu.xieshi.bean.PushToDx;
import com.lessu.xieshi.bean.TrainingUserInfo;
import com.lessu.xieshi.http.RetrofitManager;
import com.lessu.xieshi.mis.activitys.Content;
import com.lessu.xieshi.scan.ScanActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TrainingActivity extends NavigationActivity implements View.OnClickListener {
    private TextView userName,sgz,huiyuanhao,huiyuanName;
    private TextView trainingCustomName;
    private TextView tvDay;
    private TextView tvMonthYear;
    private TagFlowLayout flowLayout;
    private TagAdapter<PaidItem> tagAdapter;
    private ArrayList<PaidItem> tags = new ArrayList<>();
    private ArrayList<Project> projects;
    private LinearLayout trainingScanLogin;
    private LinearLayout trainingLearnsOnline;
    private LinearLayout trainingTeachData;
    private TrainingUserInfo curTrainingUserInfo;
    private PushToDx pushToDx;
    private TextView trainLearningTitle;
    private ImageView trainScanLogin,trainLearnOnline,trainTeach;
    private Map<String,String> paidItemMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        setTitle("在线培训");
        navigationBar.setBackgroundColor(0xFF3598DC);
        initView();
        initData();
    }
    private void initView(){
        userName =  findViewById(R.id.training_user_name);
        sgz =  findViewById(R.id.tv_sgz);
        huiyuanhao =  findViewById(R.id.training_huiyuanhao);
        huiyuanName =  findViewById(R.id.tv_huiyuan_name);
        trainingCustomName =  findViewById(R.id.training_Customer_Name);
        tvDay =  findViewById(R.id.tv_day_date);
        tvMonthYear =  findViewById(R.id.tv_month_year);
        trainingScanLogin =  findViewById(R.id.training_scan_login);
        trainingLearnsOnline =  findViewById(R.id.training_learns_online);
        trainingTeachData =  findViewById(R.id.training_teach_data);
        trainLearningTitle = findViewById(R.id.training_learns_title);
        trainScanLogin = findViewById(R.id.iv_train_scan_login);
        trainLearnOnline = findViewById(R.id.iv_train_learn_online);
        trainTeach = findViewById(R.id.iv_train_teach);
        trainingScanLogin.setOnClickListener(this);
        trainingLearnsOnline.setOnClickListener(this);
        trainingTeachData.setOnClickListener(this);
        flowLayout =  findViewById(R.id.flowLayout);
        //课程标签适配器
        tagAdapter = new TagAdapter<PaidItem>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, PaidItem paidItem) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                int betweenDay =-1;
                try {
                    Date parse = sdf.parse(paidItem.getEndDate());
                    if(parse!=null) {
                        betweenDay = Common.betweenDate(new Date(), parse);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.learns_tag_item_layout,flowLayout,false);
                TextView v = ll.findViewById(R.id.learns_tag_item_name);
                TextView over = ll.findViewById(R.id.learns_tag_item_over);
                //如果当前日期距离课程结束日期小于等于7天，显示红色,否则显示正常颜色
                if(betweenDay>=0&&betweenDay<=7){
                    ll.setBackgroundResource(R.drawable.tag_click_background_red);
                    v.setText(paidItem.getProjectName()+"(截止时间："+paidItem.getEndDate()+")");
                    return ll;
                }
                ll.setBackgroundResource(R.drawable.tag_click_background);
                v.setTextColor(getResources().getColor(android.R.color.white));
                v.setText(paidItem.getProjectName()+"(截止时间："+paidItem.getEndDate()+")");
                return ll;
            }
        };
        flowLayout.setAdapter(tagAdapter);
    }
    /**
     * 初始化数据
     */
    private void initData(){
        String mm = Shref.getString(this, Common.MEMBERINFOSTR,"");
        if(!mm.equals("")){
            String[] mms = mm.split("\\|");
            huiyuanhao.setText(mms[0]);
            huiyuanName.setText(mms[1]);
        }
        getLearInfo();
    }

    /**
     * 获取用户数据
     */
    private void getLearInfo(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] dates = sdf.format(new Date()).split("-");
        tvDay.setText(dates[2]);
        tvMonthYear.setText(dates[1]+"，"+dates[0]);
        final HashMap<String,Object> params = new HashMap<>();
        params.put("token", Content.gettoken());
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.post("/ServiceEAT.asmx/GetPaidContinuedEducationInfo")
                , params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        //获取数据成功
                        boolean isSuccess = result.getAsJsonObject().get("success").getAsBoolean();
                        if(isSuccess) {
                            JsonObject json = result.getAsJsonObject().get("data").getAsJsonObject();
                            Gson gson = new Gson();
                            TrainingUserInfo trainingUserInfo = gson.fromJson(json, TrainingUserInfo.class);
                            pushToDx = trainingUserInfo.getPushToDx();
                            curTrainingUserInfo= trainingUserInfo;
                            userName.setText(trainingUserInfo.getFullName());
                            sgz.setText(trainingUserInfo.getCertificateNo());
                            trainingCustomName.setText(trainingUserInfo.getPlanName());
                            projects = (ArrayList<Project>) trainingUserInfo.getPushToDx().getProjects();
                            List<PaidItem> paidItemNames = trainingUserInfo.getPaidItemNames();
                            if(paidItemNames.size()<=0){
                                //没有课程信息
                                trainingScanLogin.setEnabled(false);
                                trainingLearnsOnline.setEnabled(false);
                                trainingTeachData.setEnabled(false);
                                trainScanLogin.setImageResource(R.drawable.icon_training_scan_disabled);
                                trainLearnOnline.setImageResource(R.drawable.icon_training_learn_disabled);
                                trainTeach.setImageResource(R.drawable.icon_training_teach_disabled);
                                trainLearningTitle.setText("您目前没有任何继续教育报名及付费记录");
                                return;
                            }
                            tags.addAll(trainingUserInfo.getPaidItemNames());
                            tagAdapter.notifyDataChanged();
                            for (PaidItem item:trainingUserInfo.getPaidItemNames()){
                                paidItemMap.put(item.getProjectCode(),item.getProjectName());
                            }
                        }else{
                            LSAlert.showAlert(TrainingActivity.this,"获取数据失败！");
                        }
                    }

                    @Override
                    public String onFailed(ApiError error) {
                        //获取数据失败
                        return error.errorMeesage;
                    }
                });
    }

    /**
     * 打开扫码页面
     */
    private void openScan(){
        EventBus.getDefault().postSticky(new ScanEvent(curTrainingUserInfo));
        Intent scanIntent = new Intent(TrainingActivity.this, ScanActivity.class);
        startActivity(scanIntent);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.training_scan_login:
                if(curTrainingUserInfo==null){
                    LSAlert.showAlert(this,"未获取到用户信息，请尝试退出页面重新进入！");
                    return;
                }
                PermissionUtils.requestPermission(this, new PermissionUtils.permissionResult() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        openScan();
                    }
                },Manifest.permission.CAMERA);
                break;
            case R.id.training_learns_online:
                //点击了在线课程
                if(pushToDx!=null) {
                    EventBus.getDefault().postSticky(pushToDx);
                    Intent learOnlineIntent = new Intent(TrainingActivity.this, OnlineLearnActivity.class);
                    startActivity(learOnlineIntent);
                }
                break;
            case R.id.training_teach_data:
                //点击了授课情况
                if(pushToDx!=null) {
                    EventBus.getDefault().postSticky(new LearnDataEvent(pushToDx,paidItemMap,tags));
                    Intent learOnlineIntent = new Intent(TrainingActivity.this, LearnDataActivity.class);
                    startActivity(learOnlineIntent);
                }
                break;
            default:
                break;
        }
    }
}