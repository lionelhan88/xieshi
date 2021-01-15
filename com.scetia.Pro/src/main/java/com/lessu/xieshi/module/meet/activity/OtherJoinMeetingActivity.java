package com.lessu.xieshi.module.meet.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.foundation.ValidateHelper;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.module.meet.MyAutoCompleteView;
import com.lessu.xieshi.view.SignView;
import com.scetia.Pro.common.Util.Common;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.meet.adapter.OtherMeetingUserListAdapter;
import com.lessu.xieshi.module.meet.bean.OtherMeetingBean;
import com.lessu.xieshi.module.meet.event.SendMeetingDetailToList;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.scetia.Pro.common.Util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.mapapi.BMapManager.getContext;

public class OtherJoinMeetingActivity extends NavigationActivity {

    @BindView(R.id.other_meeting_join_name)
    MyAutoCompleteView otherMeetingJoinName;
    @BindView(R.id.other_meeting_join_user_hand_sign)
    SignView otherMeetingJoinUserHandSign;
    @BindView(R.id.bt_other_meeting_join_out)
    TextView btOtherMeetingJoinOut;
    @BindView(R.id.bt_bt_other_meeting_join_confirm)
    TextView btBtOtherMeetingJoinConfirm;
    @BindView(R.id.bt_bt_other_meeting_join_reset)
    TextView btBtOtherMeetingJoinReset;
    @BindView(R.id.other_meeting_join_name_error)
    TextView otherMeetingJoinNameError;
    @BindView(R.id.other_join_phone_label)
    TextView otherJoinPhoneLabel;
    @BindView(R.id.other_join_phone)
    EditText otherJoinPhone;
    private OtherMeetingUserListAdapter adapter;
    private OtherMeetingBean otherMeetingBean;
    private String meetingId;
    private String hyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_join_meeting);
        ButterKnife.bind(this);
        navigationBar.setTitle("代替参会确认");
        navigationBar.setBackgroundColor(0xFF3598DC);

        otherMeetingJoinName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                otherMeetingBean = adapter.getBeans().get(position);
            }
        });
        //当用户点击输入框时如果列表未展示，则显示列表
        otherMeetingJoinName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!otherMeetingJoinName.isPopupShowing()) {
                    otherMeetingJoinName.showDropDown();
                }
                return false;
            }
        });
        meetingId = getIntent().getStringExtra("meeting_id");
        hyId = getIntent().getStringExtra("hy_id");
        getCurrentUnitPerson(meetingId, hyId);
    }

    @OnClick({R.id.bt_other_meeting_join_out, R.id.bt_bt_other_meeting_join_confirm, R.id.bt_bt_other_meeting_join_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_other_meeting_join_out:
                finish();
                break;
            case R.id.bt_bt_other_meeting_join_confirm:
                if (TextUtils.isEmpty(otherMeetingJoinName.getText().toString())) {
                    ToastUtil.showShort("请选择本单位参会人！");
                    return;
                }
                if (otherMeetingBean == null) {
                    ToastUtil.showShort("请选择列表中的人员！");
                    return;
                }
                //如果被指定的参会人没有账号
                if (otherMeetingBean.getUserId().equals("1")) {
                    String phone = otherJoinPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtil.showShort("请填写手机号！");
                        return;
                    }
                    if (!ValidateHelper.validatePhone(phone)) {
                        ToastUtil.showShort("输入的手机号有误！");
                        return;
                    }
                    otherMeetingBean.setUserTel(phone);
                }
                //提交 他人参会请求
                if (!otherMeetingJoinUserHandSign.isHandSign()) {
                    ToastUtil.showShort("请手写您的姓名！");
                    return;
                }
                setSubstituteUser(meetingId, SPUtil.getSPConfig(Common.USERID, ""), otherMeetingBean, hyId,
                        otherMeetingJoinUserHandSign.saveBase64Str());
                break;
            case R.id.bt_bt_other_meeting_join_reset:
                //重写签名
                otherMeetingJoinUserHandSign.clear();
                break;
        }
    }

    /**
     * 获取参会单位的所有人员
     *
     * @param meetingId
     * @param hyId
     */
    private void getCurrentUnitPerson(String meetingId, String hyId) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.getToken());
        //会议id
        params.put("s1", meetingId);
        //会员号
        params.put("s2", hyId);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/GetCurrentUnitPerson"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if (isSuccess) {
                            JsonArray data = result.getAsJsonObject().get("Data").getAsJsonArray();
                            List<OtherMeetingBean> meetingBeans = GsonUtil.JsonToList(data.toString(), OtherMeetingBean.class);
                            if (meetingBeans == null) {
                                meetingBeans = new ArrayList<>();
                            }
                            initAdapter(meetingBeans);
                        } else {
                            String message = result.getAsJsonObject().get("Message").getAsString();
                            LSAlert.showAlert(OtherJoinMeetingActivity.this, message);
                        }
                    }
                });
    }

    /**
     * 初始化adapter
     *
     * @param meetingBeans
     */
    private void initAdapter(List<OtherMeetingBean> meetingBeans) {
        adapter = new OtherMeetingUserListAdapter(OtherJoinMeetingActivity.this, meetingBeans);
        otherMeetingJoinName.setAdapter(adapter);
        adapter.setSearchResultListener(new OtherMeetingUserListAdapter.SearchResultListener() {
            @Override
            public void searchResult(List<OtherMeetingBean> searchResultData) {
                otherMeetingBean = null;
                if (searchResultData.size() == 0) {
                    otherMeetingBean = new OtherMeetingBean();
                    otherMeetingJoinNameError.setText("列表中无此人，请在下方填写手机号");
                    otherMeetingJoinNameError.setVisibility(View.VISIBLE);
                    //如果用户要指定一个没有账号的人，就需要指定这个人的userid为1
                    otherMeetingBean.setUserName(otherMeetingJoinName.getText().toString());
                    otherMeetingBean.setUserId("1");
                    otherJoinPhoneLabel.setVisibility(View.VISIBLE);
                    otherJoinPhone.setVisibility(View.VISIBLE);
                } else {
                    otherMeetingJoinNameError.setVisibility(View.GONE);
                    otherJoinPhoneLabel.setVisibility(View.GONE);
                    otherJoinPhone.setVisibility(View.GONE);
                }
            }
        });
        adapter.setTouchConvertViewListener(new OtherMeetingUserListAdapter.TouchConvertViewListener() {
            @Override
            public void onTouchConvertView(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当用户滑动下拉列表时，隐藏键盘
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(otherMeetingJoinName.getWindowToken(), 0);
                }
            }
        });
    }

    /**
     * 上传代替参会信息
     */
    private void setSubstituteUser(String meetingId, String userId, final OtherMeetingBean otherMeetingBean, String hyId,
                                   String signImage) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.getToken());
        //会议id
        params.put("s1", meetingId);
        //当前用户id
        params.put("s2", userId);
        //指定参会人id
        params.put("s3", otherMeetingBean.getUserId());
        //姓名
        params.put("s4", otherMeetingBean.getUserName());
        //参会人手机号
        params.put("s5", otherMeetingBean.getUserTel());
        //会员号
        params.put("s6", hyId);
        //签名图片
        params.put("s7", signImage);

        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.post("/ServiceMis.asmx/SetSubstituteUser"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if (isSuccess) {
                            int data = result.getAsJsonObject().get("Data").getAsInt();
                            if (data == 1) {
                                //提交成功
                                LSAlert.showAlert(OtherJoinMeetingActivity.this, "提示", "提交成功", "确定",
                                        false, new LSAlert.AlertCallback() {
                                            @Override
                                            public void onConfirm() {
                                                finish();
                                                EventBus.getDefault().post(new SendMeetingDetailToList(true));
                                                EventBus.getDefault().post(otherMeetingBean);
                                            }
                                        });
                            }
                        } else {
                            String message = result.getAsJsonObject().get("Message").getAsString();
                            LSAlert.showAlert(OtherJoinMeetingActivity.this, message);
                        }
                    }
                });

    }
}