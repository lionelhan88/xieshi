package com.lessu.xieshi.module.meet.fragment;

import android.content.Intent;
import androidx.annotation.NonNull;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.xieshi.module.meet.CustomDialog;
import com.lessu.xieshi.module.meet.activity.MisMeetingActivity;
import com.lessu.xieshi.utils.DeviceUtil;
import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.meet.activity.ScalePictureActivity;
import com.lessu.xieshi.utils.ToastUtil;
import com.scetia.Pro.common.Util.Constants;
import com.lessu.xieshi.module.meet.bean.MeetingBean;
import com.lessu.xieshi.module.meet.event.MeetingUserBeanToMeetingActivity;
import com.lessu.xieshi.module.meet.event.MisMeetingFragmentToMis;
import com.lessu.xieshi.module.meet.event.SendMeetingDetailToList;
import com.scetia.Pro.baseapp.fragment.LazyFragment;
import com.scetia.Pro.common.Util.DateUtil;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.common.Util.GlideUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.lessu.xieshi.module.meet.activity.MeetingDetailActivity.MEETING_DETAIL_IMG;

public class MisMeetingDetailFragment extends LazyFragment {

    @BindView(R.id.meeting_detail_address_online)
    LinearLayout meetingDetailAddressOnline;
    @BindView(R.id.meeting_detail_online_sign)
    LinearLayout meetDetailOnlineSign;
    @BindView(R.id.meeting_tip)
    TextView meetingTip;
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
    @BindView(R.id.fragment_meeting_detail_refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.meeting_detail_photo)
    ImageView meetingDetailPhoto;
    @BindView(R.id.meeting_detail_content_img)
    ImageView meetingDetailContentImg;
    private MeetingBean meetingBean;
    private MeetingBean.MeetingUserBean curMeetingUserBean = new MeetingBean.MeetingUserBean();
    private String curUserId = "";
    private CustomDialog customDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meeting_detail;
    }

    @Override
    protected void initView() {
        //签到信息
        curUserId = SPUtil.getSPConfig(Constants.User.USER_ID, "");
        EventBus.getDefault().register(this);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMeetingList( Constants.User.GET_TOKEN(), getArguments().getString("meetingID"),
                        new ResultResponse() {
                            @Override
                            public void getResult(boolean success, JsonElement result, String errorMsg) {
                                smartRefreshLayout.finishRefresh();
                                if (result == null) {
                                    return;
                                }
                                if (success) {
                                    JsonArray data = result.getAsJsonObject().get("Data").getAsJsonArray();
                                    List<MeetingBean> meetingBeans = GsonUtil.JsonToList(data.toString(), MeetingBean.class);
                                    if (meetingBeans.size() == 0) {
                                        return;
                                    }
                                    EventBus.getDefault().post(new MisMeetingFragmentToMis(meetingBeans.get(0)));
                                    meetingBean = meetingBeans.get(0);
                                    //比对获取当前用户是否是参与人员
                                    for (MeetingBean.MeetingUserBean meetingUserBean : meetingBeans.get(0).getListUserContent()) {
                                        if (meetingUserBean.getUserId().equals(curUserId)) {
                                            curMeetingUserBean = meetingUserBean;
                                            break;
                                        }
                                    }
                                    //将当前页面发送到activity页面，判断是否显示扫码
                                    EventBus.getDefault().post(new MeetingUserBeanToMeetingActivity(curMeetingUserBean));
                                }
                                initNowData();
                            }
                        });
            }
        });
    }

    @Override
    protected void initData() {
        smartRefreshLayout.autoRefresh();
        btMeetingIsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认会议通知
                if (curMeetingUserBean.getConfirmNotify() != null && !curMeetingUserBean.getConfirmNotify().equals("1")) {
                    requestMeetingConfirm(meetingBean.getMeetingId(), curUserId);
                }
            }
        });
        meetingDetailPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scaleIntent = new Intent(getActivity(), ScalePictureActivity.class);
                scaleIntent.putExtra("detail_photo",meetingBean.getMeetingDetailPhoto());
                startActivity(scaleIntent);
                requireActivity().overridePendingTransition(R.anim.acitvity_zoom_open, 0);
            }
        });
        meetingDetailContentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scaleIntent = new Intent(getActivity(), ScalePictureActivity.class);
                scaleIntent.putExtra("detail_photo", MEETING_DETAIL_IMG);
                startActivity(scaleIntent);
                requireActivity().overridePendingTransition(R.anim.acitvity_zoom_open, 0);
            }
        });

        meetingDetailAddressOnline.setOnClickListener(v->{
            DeviceUtil.startSysUri(requireActivity(),meetingBean.getPlaceAddress());
        });

        meetDetailOnlineSign.setOnClickListener(v->{
            if (curMeetingUserBean.getCheckStatus().equals("1")) {
                ToastUtil.showSignedSuccess(requireActivity(), curMeetingUserBean.getUnitMemberCode(),
                        curMeetingUserBean.getMemberName(),
                        curMeetingUserBean.getUserFullName());
                return;
            }
            double count =
                    DateUtil.getGapHour(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm"),meetingBean.getMeetingStartTime());
            if(count<=1){
                openSigned(meetingBean.getMeetingId());
            }else{
                ToastUtil.showShort("会议开始前1小时才可以签到！");
            }
        });

    }

    /**
     * 开启签到
     */
    private void openSigned(String meetingId){
        //如果已经签过到了，就提示已签到，不再签到
        if (curMeetingUserBean.getCheckStatus().equals("1")) {
            ToastUtil.showSignedSuccess(requireActivity(), curMeetingUserBean.getUnitMemberCode(),
                    curMeetingUserBean.getMemberName(),
                    curMeetingUserBean.getUserFullName());
            return;
        }
        if (meetingBean.getMeetingNeedSign().equals("1")) {
            //需要手写签名
            customDialog = CustomDialog.newInstance(curMeetingUserBean.getUnitMemberCode(),
                    curMeetingUserBean.getMemberName(), curMeetingUserBean.getUserFullName());
            customDialog.show(getChildFragmentManager(), "dialog");
            customDialog.setCustomDialogInterface(new CustomDialog.CustomDialogInterface() {

                @Override
                public void clickOkButton(String base64Str) {
                    if (base64Str.equals("")) {
                        ToastUtil.showShort("请手写姓名！");
                        return;
                    }
                    requestScanResult(meetingId, curMeetingUserBean.getUserId(), base64Str);
                }

            });
        } else {
            //不需要手写签名，直接提交
            requestScanResult(meetingId, curMeetingUserBean.getUserId(), "");
        }
    }


    /**
     * 扫码签到
     * @param scanResult 扫码返回的meetingid
     * @param userId 当前用户的 userid
     */
    private void requestScanResult(String scanResult,String userId,String signImage){
        if(userId==null||userId.equals("")){
            LSAlert.showAlert(requireActivity(),"不是参会人员，无法签到！");
            return;
        }
        HashMap<String,Object> params = new HashMap<>();
        params.put("Token",  Constants.User.GET_TOKEN());
        params.put("s1", scanResult.toUpperCase());
        params.put("s2", userId.toUpperCase());
        params.put("s3",signImage);
        EasyAPI.apiConnectionAsync(requireActivity(), true, false, ApiMethodDescription.post("/ServiceMis.asmx/ScanCode"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        int data = result.getAsJsonObject().get("Data").getAsInt();
                        if(data==1){
                            if(customDialog!=null){
                                customDialog.dismiss();
                            }
                            //发送通知列表页面刷新
                            EventBus.getDefault().post(new SendMeetingDetailToList(true));
                            //提交成功
                            LSAlert.showAlert(requireActivity(), "签到成功");
                            //签到成功后，显示进入在线会议按钮
                            meetingDetailAddressOnline.setVisibility(View.VISIBLE);
                            //签到成功以后，不能再次点击签到按钮
                            meetDetailOnlineSign.setEnabled(false);
                        }else if(data==2){
                            if(customDialog!=null){
                                customDialog.dismiss();
                            }
                            LSAlert.showAlert(requireActivity(),"已经签过了,不能再签到了");

                        }else{
                            LSAlert.showAlert(requireActivity(),"签到失败");
                        }
                    }
                });
    }

    @Override
    protected void stopData() {
        smartRefreshLayout.finishRefresh();
        if(getApiConnection()!=null) {
            getApiConnection().cancel(true);
        }
    }

    @Override
    protected void initImmersionBar() {

    }

    private void initNowData() {
        //页面被销毁了，就不再显示了
        if (meetingDetailName == null || meetingBean.getMeetingId() == null) {
            return;
        }
        meetingDetailName.setText(meetingBean.getMeetingName());
        meetingDetailCreateUser.setText(meetingBean.getCreatePerson());
        meetingDetailPhone.setText(meetingBean.getCreatePersonPhone());
        meetingDetailStartDate.setText(meetingBean.getMeetingStartTime());
        meetingDetailEndDate.setText(meetingBean.getMeetingEndTime());
        if(meetingBean.getPlaceAddress().startsWith("http:")||meetingBean.getPlaceAddress().startsWith("https:")){
            meetingDetailAddress.setText("腾讯会议");
        }else{
            meetingDetailAddress.setText(meetingBean.getPlaceAddress() + meetingBean.getMeetingPlace());
        }
        meetingDetailContent.setText(meetingBean.getMeetingDetail());
        //加载图片之前清除缓存
     /*   ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().displayImage(MEETING_DETAIL_IMG,meetingDetailContentImg,
                ImageloaderUtil.MeetingImageOptions());*/
        Glide.with(this).load(MEETING_DETAIL_IMG).
                override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(meetingDetailContentImg);
        String photoUrl = meetingBean.getMeetingDetailPhoto();
        if (photoUrl == null || photoUrl.equals("")) {
            //隐藏图片显示
            meetingDetailPhoto.setVisibility(View.GONE);
        } else {
            meetingDetailPhoto.setVisibility(View.VISIBLE);
            GlideUtil.showImageViewNoCache(requireActivity(),photoUrl,meetingDetailPhoto);
        }
        if (curMeetingUserBean.getUserId() == null || curMeetingUserBean.getUserId().equals("")) {
            //不是参会人员,隐藏会议通知确认和签到状态
            llMeetingConfirm.setVisibility(View.GONE);
            llMeetingSigned.setVisibility(View.GONE);
            meetDetailOnlineSign.setVisibility(View.GONE);
            meetingTip.setVisibility(View.GONE);
            return;
        }
        if (curMeetingUserBean.getCheckStatus() != null && curMeetingUserBean.getCheckStatus().equals("1")) {
            //已经签到过了
            meetingUserIsSigned.setText("已签到");
            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.blue_normal2));
            meetDetailOnlineSign.setEnabled(false);
            meetingDetailAddressOnline.setVisibility(View.VISIBLE);
        } else {
            meetingUserIsSigned.setText("未签到");
            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.orange1));
        }
        if (curMeetingUserBean.getConfirmNotify() != null && curMeetingUserBean.getConfirmNotify().equals("1")) {
            btMeetingIsConfirm.setText("已确认");
            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
        } else {
            btMeetingIsConfirm.setText("参会确认");
            btMeetingIsConfirm.setBackgroundResource(R.drawable.orange_round_bg);
        }
    }

    /**
     * 请求确认会议通知
     *
     * @param meetingID 会议id
     * @param userID    用户id
     */
    private void requestMeetingConfirm(String meetingID, String userID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token",  Constants.User.GET_TOKEN());
        params.put("s1", meetingID.toUpperCase());
        params.put("s2", userID.toUpperCase());
        EasyAPI.apiConnectionAsync(getActivity(), true, false, ApiMethodDescription.get("/ServiceMis.asmx/ConfirmNotify"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        int resultCode = result.getAsJsonObject().get("Data").getAsInt();
                        if (resultCode == 1) {
                            //确认成功
                            curMeetingUserBean.setConfirmNotify("1");
                            btMeetingIsConfirm.setText("已确认");
                            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
                            //发送通知列表页面刷新
                            EventBus.getDefault().post(new SendMeetingDetailToList(true));
                        } else {
                            //确认失败
                            LSAlert.showAlert(getActivity(), "确认失败！");
                        }
                    }
                });
    }

    /**
     * 扫码签到以后，刷新当前状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMeetingDetailRefresh(SendMeetingDetailToList event) {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}