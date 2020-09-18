package com.lessu.xieshi.meet;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.lessu.foundation.ValidateHelper;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.meet.event.OtherConfirmEvent;
import com.lessu.xieshi.meet.event.SendMeetingDetailToList;
import com.lessu.xieshi.mis.activitys.Content;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/9/17
 */
public class OtherConfirmActivity extends NavigationActivity {
    @BindView(R.id.other_confirm_full_name)
    EditText otherConfirmFullName;
    @BindView(R.id.other_confirm_phone)
    EditText otherConfirmPhone;
    @BindView(R.id.other_confirm_bt_ok)
    Button otherConfirmBtOk;
    private String meetingId;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_confirm_layout);
        ButterKnife.bind(this);
        navigationBar.setTitle("会议确认");
        navigationBar.setBackgroundColor(0xFF3598DC);
        meetingId = getIntent().getStringExtra("meeting_id");
        userId = getIntent().getStringExtra("user_id");
    }

    /**
     * 请求确认会议通知
     *
     * @param meetingID 会议id
     * @param userID    用户id
     */
    private void requestMeetingConfirm(String meetingID, String userID, final String userName, final String phone) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.gettoken());
        params.put("s1", meetingID);
        params.put("s2", userID);
        params.put("s3", userName);
        params.put("s4", phone);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceMis.asmx/ConfirmNotify"),
                params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        int resultCode = result.getAsJsonObject().get("Data").getAsInt();
                        if (resultCode == 1) {
                            //确认成功,更改确认状态
                            EventBus.getDefault().post(new OtherConfirmEvent(userName,phone));
                            finish();
                        } else {
                            //确认失败
                            LSAlert.showAlert(OtherConfirmActivity.this, "确认失败！");
                        }
                    }
                });
    }
    @OnClick(R.id.other_confirm_bt_ok)
    public void onViewClicked() {
        String userName = otherConfirmFullName.getText().toString();
        if(TextUtils.isEmpty(userName)){
            ToastUtil.showShort("请输入姓名！");
            return;
        }
        String phone = otherConfirmPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showShort("请输入手机号！");
            return;
        }
        if(!ValidateHelper.validatePhone(phone)){
            ToastUtil.showShort("请输入正确的手机号！");
            return;
        }
        requestMeetingConfirm(meetingId,userId,userName,phone);
    }
}
