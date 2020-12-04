package com.lessu.xieshi.module.meet.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonElement;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.GlideUtil;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.meet.CustomDialog;
import com.lessu.xieshi.module.meet.bean.MeetingBean;
import com.lessu.xieshi.module.meet.bean.OtherMeetingBean;
import com.lessu.xieshi.module.meet.event.OtherConfirmEvent;
import com.lessu.xieshi.module.meet.event.SendMeetingDetailToList;
import com.lessu.xieshi.module.meet.event.SendMeetingListToDetail;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.lessu.xieshi.module.scan.ScanActivity;

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
    public static final String MEETING_DETAIL_IMG="http://www.scetia.com/Scetia_Meet_Gonggao_2020-09-18.jpg";
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
    @BindView(R.id.meeting_detail_photo)
    ImageView meetingDetailPhoto;
    @BindView(R.id.meeting_detail_join_user_full_name)
    TextView meetingDetailJoinUserFullName;
    @BindView(R.id.meeting_detail_join_user_phone)
    TextView meetingDetailJoinUserPhone;
    @BindView(R.id.meeting_detail_join_hy_code)
    TextView meetingDetailJoinHyCode;
    @BindView(R.id.meeting_detail_content_img)
    ImageView meetingDetailContentImg;
    private BarButtonItem handleButtonItem2;
    private MeetingBean meetingBean;
    private CustomDialog customDialog;
    private MeetingBean.MeetingUserBean curMeetingUserBean = new MeetingBean.MeetingUserBean();
    private String curUserId = "";

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
    }

    @SuppressLint("CheckResult")
    private void initView() {
        //签到信息
        curUserId = Shref.getString(this, Common.USERID, "");
        //此处要循环比较,如果当前登录的用户也是参与人员，则也显示签到信息
        if (meetingBean == null) {
            finish();
            return;
        }
        Observable.fromArray(meetingBean.getListUserContent())
                .map(new Function<List<MeetingBean.MeetingUserBean>, MeetingBean.MeetingUserBean>() {
                    MeetingBean.MeetingUserBean bean = new MeetingBean.MeetingUserBean();

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
                    @Override
                    public void accept(MeetingBean.MeetingUserBean meetingUserBean) throws Exception {
                        curMeetingUserBean = meetingUserBean;
                        initData();
                    }
                });
    }

    /**
     * 初始化设置数据
     */
    private void initData() {
        meetingDetailName.setText(meetingBean.getMeetingName());
        meetingDetailCreateUser.setText(meetingBean.getCreatePerson());
        meetingDetailPhone.setText(meetingBean.getCreatePersonPhone());
        meetingDetailStartDate.setText(meetingBean.getMeetingStartTime());
        meetingDetailEndDate.setText(meetingBean.getMeetingEndTime());
        meetingDetailAddress.setText(meetingBean.getPlaceAddress() + meetingBean.getMeetingPlace());
        meetingDetailContent.setText(meetingBean.getMeetingDetail());
        //加载图片之前清除缓存

        /*ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().displayImage(MEETING_DETAIL_IMG,meetingDetailContentImg,
                ImageloaderUtil.MeetingImageOptions());*/
        Glide.with(this).load(MEETING_DETAIL_IMG).override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                .into(meetingDetailContentImg);
        String photoUrl = meetingBean.getMeetingDetailPhoto();
        if (photoUrl == null || photoUrl.equals("")) {
            //隐藏图片显示
            meetingDetailPhoto.setVisibility(View.GONE);
        } else {
            meetingDetailPhoto.setVisibility(View.VISIBLE);
            GlideUtil.showImageViewNoCache(this,photoUrl,meetingDetailPhoto);
           /* ImageLoader.getInstance().displayImage(photoUrl, meetingDetailPhoto,
                    ImageloaderUtil.MeetingImageOptions());*/
        }

        if (curMeetingUserBean.getUserId() == null || curMeetingUserBean.getUserId().equals("")) {
            //不是参会人员
            llMeetingConfirm.setVisibility(View.GONE);
            llMeetingSigned.setVisibility(View.GONE);
            return;
        }

        if (curMeetingUserBean.getCheckStatus() != null && curMeetingUserBean.getCheckStatus().equals("1")) {
            //已经签到过了
            meetingUserIsSigned.setText("已签到");
            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.danlan));
        } else {
            meetingUserIsSigned.setText("未签到");
            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.orange1));
        }
        if (curMeetingUserBean.getConfirmNotify().equals("1")) {
            btMeetingIsConfirm.setText("已确认");
            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
        } else {
            btMeetingIsConfirm.setText("请确认会议通知");
            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_orange_stroke_bg);
        }
        meetingDetailJoinUserFullName.setText(curMeetingUserBean.getUserFullName());
        meetingDetailJoinUserPhone.setText(curMeetingUserBean.getTel());
        if(curMeetingUserBean.getUnitMemberCode()!=null) {
            meetingDetailJoinHyCode.setText(curMeetingUserBean.getUnitMemberCode());
        }
        /**
         * 2020-09-19 暂时去除此功能，直接显示是否确认通知了
         * 请注意如IsSelf=1，ConfirmNotify=0，SubstituteUser<>'' 说明当前账号是已经被选为替代账号了，
         * 不可再选择替代操作，直接只有一个按钮确认会议即可。如IsSelf=1，ConfirmNotify=1，SubstituteUser=''，
         * 说明当前用户已经确认本人参加了，显示已确认即可，如IsSelf=0，ConfirmNotify=1，SubstituteUser<>''
         * ,说明是已选他人参加，显示已确认他人替代参会，参会人（XXX）
         */
      /*  if(curMeetingUserBean.getIsSelf().equals("1")){
            if(curMeetingUserBean.getConfirmNotify().equals("1")) {
                btMeetingIsConfirm.setText("已确认");
                btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
            }else{
                btMeetingIsConfirm.setText("请确认会议通知");
                btMeetingIsConfirm.setBackgroundResource(R.drawable.text_orange_stroke_bg);
            }
        }else{
            if(curMeetingUserBean.getConfirmNotify().equals("1")&&!curMeetingUserBean.getSubstituteUser().equals("")) {
                //已经选择他人参加,同时不再显示签到状态
                btMeetingIsConfirm.setBackgroundResource(R.color.white);
                btMeetingIsConfirm.setTextColor(getResources().getColor(R.color.black));
                btMeetingIsConfirm.setText("已确认他人替代参会，参会人("+curMeetingUserBean.getSubstituteUser()+")");
                llMeetingSigned.setVisibility(View.GONE);
                handleButtonItem2.setVisibility(View.GONE);
            }
        }*/

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveMeetingBean(SendMeetingListToDetail event) {
        EventBus.getDefault().removeStickyEvent(event);
        meetingBean = event.getMeetingBean();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveOtherJoinMeeting(OtherMeetingBean otherMeetingBean) {
        //指派别人代替参会，右上角扫码隐藏，签到状态隐藏
        curMeetingUserBean.setIsSelf("0");
        curMeetingUserBean.setConfirmNotify("1");
        curMeetingUserBean.setSubstituteUser(otherMeetingBean.getUserName());
        //已经选择他人参加,同时不再显示签到状态
        btMeetingIsConfirm.setBackgroundResource(R.color.white);
        btMeetingIsConfirm.setTextColor(getResources().getColor(R.color.black));
        btMeetingIsConfirm.setText("已确认他人替代参会，参会人(" + curMeetingUserBean.getSubstituteUser() + ")");
        llMeetingSigned.setVisibility(View.GONE);
        handleButtonItem2.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveOtherConfirmMeeting(OtherConfirmEvent confirmEvent) {
        //他人用此账号点击了确认会议通知
        //确认成功,更改确认状态
        curMeetingUserBean.setConfirmNotify("1");
        btMeetingIsConfirm.setText("已确认");
        btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
        //更改参会人姓名
        curMeetingUserBean.setUserFullName(confirmEvent.getUserFullName());
        //更改参会人手机号
        curMeetingUserBean.setTel(confirmEvent.getUserPhone());
        meetingDetailJoinUserFullName.setText(confirmEvent.getUserFullName());
        meetingDetailJoinUserPhone.setText(confirmEvent.getUserPhone());
        //发送通知列表页面刷新
        EventBus.getDefault().post(new SendMeetingDetailToList(true));
    }

    /**
     * 扫码签到
     *
     * @param scanResult 扫码返回的meetingid
     * @param userId     当前用户的 userid
     */
    private void requestScanResult(String scanResult, String userId, String signImage) {
        if (userId == null || userId.equals("")) {
            LSAlert.showAlert(this, "不是参会人员，无法签到！");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.getToken());
        params.put("s1", scanResult.toUpperCase());
        params.put("s2", userId.toUpperCase());
        params.put("s3", signImage);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.post("/ServiceMis.asmx/ScanCode"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        int data = result.getAsJsonObject().get("Data").getAsInt();
                        if (data == 1) {
                            //签到成功改变状态
                            meetingUserIsSigned.setText("已签到");
                            curMeetingUserBean.setCheckStatus("1");
                            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.danlan));
                            if (customDialog != null) {
                                customDialog.dismiss();
                            }
                            //发送通知列表页面刷新
                            EventBus.getDefault().post(new SendMeetingDetailToList(true));
                            //提交成功
                            LSAlert.showAlert(MeetingDetailActivity.this, "签到成功");
                        } else if (data == 2) {
                            LSAlert.showAlert(MeetingDetailActivity.this, "提示", "已经签过了哦"
                                    , "确定", false, new LSAlert.AlertCallback() {
                                        @Override
                                        public void onConfirm() {
                                            if (customDialog != null) {
                                                customDialog.dismiss();
                                            }
                                        }
                                    });
                        } else {
                            LSAlert.showAlert(MeetingDetailActivity.this, "签到失败");
                        }
                    }
                });
    }

    /**
     * 请求确认会议通知
     *
     * @param meetingID 会议id
     * @param userID    用户id
     */
    private void requestMeetingConfirm(String meetingID, String userID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.getToken());
        params.put("s1", meetingID);
        params.put("s2", userID);
        EasyAPI.apiConnectionAsync(MeetingDetailActivity.this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/ConfirmNotify"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        int resultCode = result.getAsJsonObject().get("Data").getAsInt();
                        if (resultCode == 1) {
                            //确认成功,更改确认状态
                            curMeetingUserBean.setConfirmNotify("1");
                            btMeetingIsConfirm.setText("已确认");
                            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
                            //发送通知列表页面刷新
                            EventBus.getDefault().post(new SendMeetingDetailToList(true));
                        } else {
                            //确认失败
                            LSAlert.showAlert(MeetingDetailActivity.this, "确认失败！");
                        }
                    }
                });
    }

    /**
     * 点击开启扫码签到
     */
    public void scanSign() {
        Intent scanIntent = new Intent(this, ScanActivity.class);
        scanIntent.putExtra(Common.SCAN_TYPE, Common.SCAN_MEETING_SIGNED);
        startActivityForResult(scanIntent, 0x11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x11) {
            if (resultCode == RESULT_OK) {
                final String result = data.getStringExtra("scanResult");
                if (result == null) {
                    return;
                }
                //如果已经签过到了，就提示已签到，不再签到
                if (curMeetingUserBean.getCheckStatus().equals("1")) {
                    ToastUtil.showSignedSuccess(this, curMeetingUserBean.getUnitMemberCode(),
                            curMeetingUserBean.getMemberName(),
                            curMeetingUserBean.getUserFullName());
                    return;
                }
                if (meetingBean.getMeetingNeedSign().equals("1")) {
                    //需要手写签名
                    customDialog = CustomDialog.newInstance(curMeetingUserBean.getUnitMemberCode(),
                            curMeetingUserBean.getMemberName(), curMeetingUserBean.getUserFullName());
                    customDialog.show(getSupportFragmentManager(), "dialog");
                    customDialog.setCustomDialogInterface(new CustomDialog.CustomDialogInterface() {

                        @Override
                        public void clickOkButton(String base64Str) {
                            if (base64Str.equals("")) {
                                ToastUtil.showShort("请手写姓名！");
                                return;
                            }
                            requestScanResult(result, curMeetingUserBean.getUserId(), base64Str);
                        }

                    });

                } else {
                    //不需要手写签名，直接提交
                    requestScanResult(result, curMeetingUserBean.getUserId(), "");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.bt_meeting_is_confirm, R.id.meeting_detail_photo,R.id.meeting_detail_content_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_meeting_is_confirm:
                //2020-09-18 直接进入参会人和手机号页面，不再弹出是否选择本人或其他人
                if (curMeetingUserBean.getConfirmNotify().equals("0")) {
                    //指定他人参加
                    Intent intent = new Intent(MeetingDetailActivity.this, OtherConfirmActivity.class);
                    intent.putExtra("meeting_id", meetingBean.getMeetingId());
                    intent.putExtra("user_id", curUserId);
                    startActivity(intent);
                }
                /**
                 * 请注意如IsSelf=1，ConfirmNotify=0，SubstituteUser<>'' 说明当前账号是已经被选为替代账号了，
                 * 不可再选择替代操作，直接只有一个按钮确认会议即可。如IsSelf=1，ConfirmNotify=1，SubstituteUser=''，
                 * 说明当前用户已经确认本人参加了，显示已确认即可，如IsSelf=0，ConfirmNotify=1，SubstituteUser<>''
                 * ,说明是已选他人参加，显示已确认他人替代参会，参会人（XXX）
                 */
              /*  if(curMeetingUserBean.getIsSelf().equals("1")&&curMeetingUserBean.getConfirmNotify().equals("0")
                &&!curMeetingUserBean.getSubstituteUser().equals("")){
                    //当前账号为替代账号,直接确认会议
                    requestMeetingConfirm(meetingBean.getMeetingId(), curUserId);
                }else if (curMeetingUserBean.getIsSelf().equals("1") &&curMeetingUserBean.getConfirmNotify().equals("0")
                &&curMeetingUserBean.getSubstituteUser().equals("")) {
                    //需要有个提示框，要提示是本人参加还是指定人员
                    BottomMenuDialog bottomMenuDialog =BottomMenuDialog.newInstance();
                    bottomMenuDialog.show(getSupportFragmentManager(),"");
                    bottomMenuDialog.setCustomDialogInterface(new BottomMenuDialog.CustomDialogInterface() {
                        @Override
                        public void selfJoin() {
                            //确定是本人参加
                            requestMeetingConfirm(meetingBean.getMeetingId(), curUserId);
                        }

                        @Override
                        public void otherJoin() {
                            //指定他人参加
                            Intent intent = new Intent(MeetingDetailActivity.this,OtherJoinMeetingActivity.class);
                            intent.putExtra("meeting_id",meetingBean.getMeetingId());
                            intent.putExtra("hy_id",curMeetingUserBean.getUnitMemberCode());
                            startActivity(intent);
                        }
                    });
                }*/
                break;
            case R.id.meeting_detail_photo:
                Intent scaleIntent = new Intent(this, ScalePictureActivity.class);
                scaleIntent.putExtra("detail_photo", meetingBean.getMeetingDetailPhoto());
                startActivity(scaleIntent);
                this.overridePendingTransition(R.anim.acitvity_zoom_open, 0);
                break;
            case R.id.meeting_detail_content_img:
                scaleIntent = new Intent(this, ScalePictureActivity.class);
                scaleIntent.putExtra("detail_photo", MEETING_DETAIL_IMG);
                startActivity(scaleIntent);
                this.overridePendingTransition(R.anim.acitvity_zoom_open, 0);
                break;
        }
    }
}