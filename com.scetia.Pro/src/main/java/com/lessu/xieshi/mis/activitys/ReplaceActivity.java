package com.lessu.xieshi.mis.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.foundation.ValidateHelper;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.SignView;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.MyToast;
import com.lessu.xieshi.meet.adapter.MeetingCompanyListAdapter;
import com.lessu.xieshi.meet.bean.MeetingBean;
import com.lessu.xieshi.meet.bean.MeetingCompanyBean;
import com.lessu.xieshi.meet.event.ReplaceSignAddEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplaceActivity extends NavigationActivity {

    @BindView(R.id.replace_sign_hy_code)
    TextView replaceSignHyCode;
    @BindView(R.id.replace_sign_user_name)
    EditText replaceSignUserName;
    @BindView(R.id.replace_sign_hand_sign_view)
    SignView replaceSignHandSignView;
    @BindView(R.id.bt_replace_confirm)
    TextView btReplaceConfirm;
    @BindView(R.id.bt_replace_reset)
    TextView btReplaceReset;
    @BindView(R.id.replace_sign_company_name)
    TextView replaceSignCompanyName;
    @BindView(R.id.replace_sign_user_phone)
    EditText replaceSignUserPhone;
    @BindView(R.id.replace_sign_hand_image)
    ImageView replaceSignHandImage;
    @BindView(R.id.handle_sign_title)
    TextView handleSignTitle;
    private MeetingCompanyListAdapter listAdapter;
    private List<MeetingCompanyBean> meetingCompanyBeans;
    private String meetingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relpace);
        ButterKnife.bind(this);
        navigationBar.setBackgroundColor(0xFF3598DC);
        setTitle("代签");
        initView();
        meetingID = getIntent().getStringExtra("meetingID");
        if (meetingID != null) {
            getCompanyList(meetingID);
        }
        EventBus.getDefault().register(this);
    }

    private void initView() {
        meetingCompanyBeans = new ArrayList<>();
        listAdapter = new MeetingCompanyListAdapter(this, meetingCompanyBeans);
        replaceSignCompanyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LSAlert.showDialog(ReplaceActivity.this, "选择单位", listAdapter, "取消", new LSAlert.SelectItemCallback() {
                    @Override
                    public void selectItem(int position) {
                        MeetingCompanyBean item = (MeetingCompanyBean) listAdapter.getItem(position);
                        replaceSignCompanyName.setText(item.getMemberName());
                        replaceSignHyCode.setText(item.getMemberCode());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    /**
     * 重置控件状态
     */
    private void clearViewState(){
        replaceSignHyCode.setText("");
        replaceSignCompanyName.setText("");
        replaceSignUserName.setText("");
        replaceSignUserPhone.setText("");
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveMeetingUserBean(ReplaceSignAddEvent event) {
        //如果是false才是列表页面传来的值，需要显示明细
        EventBus.getDefault().removeStickyEvent(event);
        if (event.isRefresh()) {
            return;
        }
        MeetingBean.MeetingUserBean meetingUserBean = event.getMeetingUserBean();
        meetingID = event.getMeetingID();
        replaceSignHyCode.setText(meetingUserBean.getUnitMemberCode());
        replaceSignCompanyName.setText(meetingUserBean.getMemberName());
        replaceSignUserName.setText(meetingUserBean.getUserFullName());
        replaceSignUserPhone.setText(meetingUserBean.getTel());
        //查看明细不可以修改
        replaceSignCompanyName.setClickable(false);
        replaceSignUserName.setEnabled(false);
        replaceSignUserPhone.setEnabled(false);
        btReplaceConfirm.setVisibility(View.GONE);
        btReplaceReset.setVisibility(View.GONE);
        handleSignTitle.setText("手写签名");
        //手写签名图片变为只显示，不能手写了
        replaceSignHandSignView.setVisibility(View.GONE);
        replaceSignHandImage.setVisibility(View.VISIBLE);

        getHandSignImage(meetingID, meetingUserBean.getUnitMemberCode(), meetingUserBean.getUserFullName());

    }

    @OnClick({R.id.bt_replace_confirm, R.id.bt_replace_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_replace_confirm:
                //确认提交
                String hyCode = replaceSignHyCode.getText().toString();
                String userName = replaceSignUserName.getText().toString();
                String userPhone = replaceSignUserPhone.getText().toString();
                if (TextUtils.isEmpty(hyCode)) {
                    MyToast.showShort("请选择会员单位！");
                    return;
                }
                if (TextUtils.isEmpty(userName)) {
                    MyToast.showShort("请输入姓名！");
                    return;
                }
                if (!ValidateHelper.validatePhone(userPhone)) {
                    MyToast.showShort("请输入正确的手机号！");
                    return;
                }
                if(!replaceSignHandSignView.isHandSign()){
                    //请手写姓名
                    MyToast.showShort("请手写姓名！");
                    return;
                }
                requestSign(meetingID, hyCode, userName, userPhone, replaceSignHandSignView.saveBase64Str());
                break;
            case R.id.bt_replace_reset:
                //重写
                replaceSignHandSignView.clear();
                break;
        }
    }

    /**
     * 获取代签的明细记录
     *
     * @param meetingID
     * @param hyCode
     */
    private void getHandSignImage(String meetingID, String hyCode, String userName) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.gettoken());
        params.put("s1", meetingID);
        params.put("s2", hyCode);
        params.put("s4", userName);
        EasyAPI.apiConnectionAsync(ReplaceActivity.this, true, false,
                ApiMethodDescription.get("/ServiceMis.asmx/GetSubstituteSignPhoto"), params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if (isSuccess) {
                            //成功获取到签名图片
                            String data = result.getAsJsonObject().get("Data").getAsString();
                            byte[] decode = Base64.decode(data, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                            replaceSignHandImage.setImageBitmap(bitmap);
                        } else {
                            String message = result.getAsJsonObject().get("Message").getAsString();
                            LSAlert.showAlert(ReplaceActivity.this, message);
                        }
                    }
                });
    }

    /**
     * 获取参会单位列表
     *
     * @param meetingID
     */
    private void getCompanyList(String meetingID) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.gettoken());
        params.put("s1", meetingID);
        EasyAPI.apiConnectionAsync(ReplaceActivity.this, true, false,
                ApiMethodDescription.get("/ServiceMis.asmx/GetMemberList"), params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if (isSuccess) {
                            //成功获取数据
                            JsonArray data = result.getAsJsonObject().get("Data").getAsJsonArray();
                            List<MeetingCompanyBean> beans = GsonUtil.JsonToList(data.toString(), MeetingCompanyBean.class);
                            meetingCompanyBeans.addAll(beans);
                            listAdapter.notifyDataSetChanged();
                        } else {
                            String message = result.getAsJsonObject().get("Message").getAsString();
                            LSAlert.showAlert(ReplaceActivity.this, message);
                        }
                    }
                });
    }

    /**
     * 提交代签请求
     */
    private void requestSign(String meetingID, String hyCode, String userName, String userPhone
            , String signImage) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.gettoken());
        params.put("s1", meetingID);
        params.put("s2", hyCode);
        params.put("s3", signImage);
        params.put("s4", userName);
        params.put("s5", userPhone);
        EasyAPI.apiConnectionAsync(ReplaceActivity.this, true, false,
                ApiMethodDescription.post("/ServiceMis.asmx/SubstituteSign"), params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if (isSuccess) {
                            //成功获取数据
                            int data = result.getAsJsonObject().get("Data").getAsInt();
                            /**
                             * 1签到成功    0签到失败   2已经签到了
                             */
                            switch (data) {
                                case 1:
                                    LSAlert.showAlert(ReplaceActivity.this, "签到成功");
                                    clearViewState();
                                    EventBus.getDefault().post(new ReplaceSignAddEvent(true));
                                    break;
                                case 2:
                                    LSAlert.showAlert(ReplaceActivity.this, "已经签过了，不能再签到了哦");
                                    break;
                                case 0:
                                    LSAlert.showAlert(ReplaceActivity.this, "签到失败");
                                    break;
                            }
                        } else {
                            String message = result.getAsJsonObject().get("Message").getAsString();
                            LSAlert.showAlert(ReplaceActivity.this, message);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}