package com.lessu.xieshi.meet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.config;
import com.lessu.xieshi.meet.bean.MeetingBean;
import com.lessu.xieshi.meet.event.MeetingScanResult;
import com.lessu.xieshi.meet.event.SendMeetingDetailToList;
import com.lessu.xieshi.meet.event.SendMeetingListToDetail;
import com.lessu.xieshi.mis.activitys.Content;
import com.lessu.xieshi.scan.ScanActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MeetingDetailActivity extends NavigationActivity {
    @BindView(R.id.meeting_detail_name)
    TextView meetingDetailName;
    @BindView(R.id.meeting_detail_create_user)
    TextView meetingDetailCreateUser;
    @BindView(R.id.meeting_detail_phone)
    TextView meetingDetailPhone;
    @BindView(R.id.meeting_detail_start_date)
    TextView meetingDetailStartDate;
    @BindView(R.id.meeting_detail_end_date)
    TextView meetingDetailEndDate;
    @BindView(R.id.meeting_detail_address)
    TextView meetingDetailAddress;
    @BindView(R.id.bt_meeting_is_confirm)
    TextView btMeetingIsConfirm;
    @BindView(R.id.meeting_detail_content)
    TextView meetingDetailContent;
    @BindView(R.id.meeting_user_is_signed)
    TextView meetingUserIsSigned;
    @BindView(R.id.ll_meeting_confirm)
    LinearLayout llMeetingConfirm;
    @BindView(R.id.ll_meeting_signed)
    LinearLayout llMeetingSigned;
    private BarButtonItem handleButtonItem2;
    private MeetingBean meetingBean;
    private CustomDialog customDialog;
    private MeetingBean.MeetingUserBean curMeetingUserBean = new MeetingBean.MeetingUserBean();
    private String curUserId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        navigationBar.setBackgroundColor(0xFF3598DC);
        setTitle("会议详情");
        handleButtonItem2 = new BarButtonItem(this, R.drawable.icon_scan_white);
        navigationBar.addRightBarItem(handleButtonItem2);
        handleButtonItem2.setOnClickMethod(this, "scanSign");
        initView();
       // initData();
    }

    private void initView() {

    }
    private void initData(){
        meetingDetailName.setText(meetingBean.getMeetingName());
        meetingDetailCreateUser.setText(meetingBean.getCreatePerson());
        meetingDetailPhone.setText(meetingBean.getCreatePersonPhone());
        meetingDetailStartDate.setText(meetingBean.getMeetingStartTime());
        meetingDetailEndDate.setText(meetingBean.getMeetingEndTime());
        meetingDetailAddress.setText(meetingBean.getPlaceAddress()+meetingBean.getMeetingPlace());
        meetingDetailContent.setText(meetingBean.getMeetingDetail());
        if(curMeetingUserBean.getUserId()==null||curMeetingUserBean.getUserId().equals("")){
            //不是参会人员
            llMeetingConfirm.setVisibility(View.GONE);
            llMeetingSigned.setVisibility(View.GONE);
            return;
        }
        if(curMeetingUserBean.getCheckStatus()!=null&&curMeetingUserBean.getCheckStatus().equals("1")){
            //已经签到过了
            meetingUserIsSigned.setText("已签到");
            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.danlan));
        }else{
            meetingUserIsSigned.setText("未签到");
            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.orange1));
        }
        if(curMeetingUserBean.getConfirmNotify()!=null&&curMeetingUserBean.getConfirmNotify().equals("1")){
            btMeetingIsConfirm.setText("已确认");
            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
        }else{
            btMeetingIsConfirm.setText("请确认会议通知");
            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_orange_stroke_bg);
        }

    }

    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveMeetingBean(SendMeetingListToDetail event) {
        EventBus.getDefault().removeStickyEvent(event);
        meetingBean = event.getMeetingBean();
        //签到信息
        curUserId = Shref.getString(this, Common.USERID,"");
        //curUserId = Shref.testUserId;
        //此处要循环比较,如果当前登录的用户也是参与人员，则也显示签到信息
        Observable.fromArray(meetingBean.getListUserContent())
                .map(new Function<List<MeetingBean.MeetingUserBean>, MeetingBean.MeetingUserBean>() {
                    MeetingBean.MeetingUserBean bean = new MeetingBean.MeetingUserBean();

                    @SuppressLint("CheckResult")
                    @Override
                    public MeetingBean.MeetingUserBean apply(List<MeetingBean.MeetingUserBean> meetingUserBeans) throws Exception {
                        for (MeetingBean.MeetingUserBean meetingUserBean : meetingBean.getListUserContent()) {
                            if (meetingUserBean.getUserId().equals(curUserId)) {
                                bean = meetingUserBean;
                                break;
                            }
                        }
                        return bean;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MeetingBean.MeetingUserBean>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void accept(MeetingBean.MeetingUserBean meetingUserBean) throws Exception {
                        curMeetingUserBean = meetingUserBean;
                        initData();
                    }
                });
    }

    /**
     * 扫码签到
     * @param scanResult 扫码返回的meetingid
     * @param userId 当前用户的 userid
     */
    private void requestScanResult(String scanResult,String userId,String signImage){
        if(userId==null||userId.equals("")){
            LSAlert.showAlert(this,"不是参会人员，无法签到！");
            return;
        }
        HashMap<String,Object> params = new HashMap<>();
        params.put("Token", Content.gettoken());
        params.put("s1", scanResult.toUpperCase());
        params.put("s2", userId.toUpperCase());
        params.put("s3",signImage);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.post("/ServiceMis.asmx/ScanCode"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        int data = result.getAsJsonObject().get("Data").getAsInt();
                        if(data==1){
                            //签到成功改变状态
                            meetingUserIsSigned.setText("已签到");
                            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.danlan));
                            if(customDialog!=null){
                                customDialog.dismiss();
                            }
                            //发送通知列表页面刷新
                            EventBus.getDefault().post(new SendMeetingDetailToList(true));
                            //提交成功
                            LSAlert.showAlert(MeetingDetailActivity.this, "签到成功");
                        }else if(data==2){
                            LSAlert.showAlert(MeetingDetailActivity.this, "提示", "已经签过了哦"
                                    , "确定", false, new LSAlert.AlertCallback() {
                                        @Override
                                        public void onConfirm() {
                                            if(customDialog!=null){
                                                customDialog.dismiss();
                                            }
                                        }
                                    });
                        }else{
                            LSAlert.showAlert(MeetingDetailActivity.this,"签到失败");
                        }
                    }
                });
    }
    /**
     * 请求确认会议通知
     * @param meetingID  会议id
     * @param userID     用户id
     */
    private void requestMeetingConfirm(String meetingID,String userID){
        HashMap<String,Object> params = new HashMap<>();
        params.put("Token", Content.gettoken());
        params.put("s1", meetingID);
        params.put("s2", userID);
        EasyAPI.apiConnectionAsync(MeetingDetailActivity.this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/ConfirmNotify"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        int resultCode = result.getAsJsonObject().get("Data").getAsInt();
                        if(resultCode==1){
                            //确认成功
                            curMeetingUserBean.setCheckStatus("1");
                            btMeetingIsConfirm.setText("已确认");
                            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
                            //发送通知列表页面刷新
                            EventBus.getDefault().post(new SendMeetingDetailToList(true));
                        }else{
                            //确认失败
                            LSAlert.showAlert(MeetingDetailActivity.this,"确认失败！");
                        }
                    }
                });
    }
    /**
     * 点击开启扫码签到
     */
    public void scanSign() {
        Intent scanIntent = new Intent(this,ScanActivity.class);
        scanIntent.putExtra(config.SCAN_TYPE,config.SCAN_MEETING_SIGNED);
        startActivityForResult(scanIntent,0x11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x11){
            if(resultCode==RESULT_OK){
                final String result =data.getStringExtra("scanResult");
                if(result==null){
                    return;
                }
                if(meetingBean.getMeetingNeedSign().equals("1")){
                    //需要手写签名
                    customDialog = CustomDialog.newInstance(curMeetingUserBean.getUnitMemberCode(),
                            curMeetingUserBean.getMemberName(),curMeetingUserBean.getUserFullName());
                    customDialog.show(getSupportFragmentManager(),"dialog");
                    customDialog.setCustomDialogInterface(new CustomDialog.CustomDialogInterface() {

                        @Override
                        public void clickOkButton(String base64Str) {
                            requestScanResult(result,curMeetingUserBean.getUserId(),base64Str);
                        }

                        @Override
                        public void clickCancelButton() {

                        }

                        @Override
                        public void clickResetSign() {

                        }
                    });
                }else{
                    requestScanResult(result,curUserId,"");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.bt_meeting_is_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_meeting_is_confirm:
                //确认会议通知
                if(curMeetingUserBean.getConfirmNotify()!=null&&!curMeetingUserBean.getConfirmNotify().equals("1")){
                    requestMeetingConfirm(meetingBean.getMeetingId(),curUserId);
                }
                break;
        }
    }
}