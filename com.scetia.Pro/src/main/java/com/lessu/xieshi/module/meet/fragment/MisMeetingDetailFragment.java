package com.lessu.xieshi.module.meet.fragment;

import android.content.Intent;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.meet.activity.ScalePictureActivity;
import com.scetia.Pro.common.Util.Constants;
import com.lessu.xieshi.module.meet.bean.MeetingBean;
import com.lessu.xieshi.module.meet.event.MeetingUserBeanToMeetingActivity;
import com.lessu.xieshi.module.meet.event.MisMeetingFragmentToMis;
import com.lessu.xieshi.module.meet.event.SendMeetingDetailToList;
import com.scetia.Pro.baseapp.fragment.LazyFragment;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.common.Util.GlideUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.lessu.xieshi.module.meet.activity.MeetingDetailActivity.MEETING_DETAIL_IMG;

public class MisMeetingDetailFragment extends LazyFragment {

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
        meetingDetailAddress.setText(meetingBean.getPlaceAddress() + meetingBean.getMeetingPlace());
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
            return;
        }
        if (curMeetingUserBean.getCheckStatus() != null && curMeetingUserBean.getCheckStatus().equals("1")) {
            //已经签到过了
            meetingUserIsSigned.setText("已签到");
            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.blue_normal2));
        } else {
            meetingUserIsSigned.setText("未签到");
            meetingUserIsSigned.setTextColor(getResources().getColor(R.color.orange1));
        }
        if (curMeetingUserBean.getConfirmNotify() != null && curMeetingUserBean.getConfirmNotify().equals("1")) {
            btMeetingIsConfirm.setText("已确认");
            btMeetingIsConfirm.setBackgroundResource(R.drawable.text_blue_round_bg);
        } else {
            btMeetingIsConfirm.setText("请确认会议通知");
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