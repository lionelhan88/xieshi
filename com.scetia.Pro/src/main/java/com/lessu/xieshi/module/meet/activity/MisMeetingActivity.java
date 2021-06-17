package com.lessu.xieshi.module.meet.activity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Constants;
import com.lessu.xieshi.utils.ToastUtil;
import com.lessu.xieshi.module.meet.CustomDialog;
import com.lessu.xieshi.module.meet.bean.MeetingBean;
import com.lessu.xieshi.module.meet.event.MeetingUserBeanToMeetingActivity;
import com.lessu.xieshi.module.meet.event.MisMeetingFragmentToMis;
import com.lessu.xieshi.module.meet.event.SendMeetingDetailToList;
import com.lessu.xieshi.module.meet.fragment.CompanySignFragment;
import com.lessu.xieshi.module.meet.fragment.MisMeetingDetailFragment;
import com.lessu.xieshi.module.meet.fragment.PersonSignFragment;
import com.lessu.xieshi.module.meet.fragment.ReplaceSignFragment;
import com.lessu.xieshi.module.scan.ScanActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MisMeetingActivity extends NavigationActivity {

    @BindView(R.id.mis_meeting_name)
    TextView misMeetingName;
    @BindView(R.id.danwei_sign_number)
    TextView danweiSignNumberText;
    @BindView(R.id.person_sign_number)
    TextView personSignNumberText;
    @BindView(R.id.daiqian_sign_number)
    TextView daiqianSignNumberText;
    @BindView(R.id.total_sign_number)
    TextView totalSignNumberText;
    @BindView(R.id.mis_meeting_tab_layout)
    TabLayout misMeetingTabLayout;
    @BindView(R.id.mis_meeting_view_pager)
    ViewPager misMeetingViewPager;
    private List<Fragment> fragments;
    //2020-09-16暂时把 手动签到更名为 嘉宾签到，原嘉宾签到隐藏
    private String[] titles = {"会议概述","单位签到","嘉宾签到"};
    private  String meetingID;
    private  BarButtonItem handleButtonItem2;
    private MeetingBean curMeetingBean;
    private MeetingBean.MeetingUserBean curMeetingUserBean;
    private CustomDialog customDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mis_meeting;
    }

    private void initFragment(){
        meetingID = getIntent().getStringExtra("meetingID");
        Bundle bundle1 = new Bundle();
        bundle1.putString("meetingID",meetingID);
        fragments = new ArrayList<>();
        MisMeetingDetailFragment misMeetingDetailFragment = new MisMeetingDetailFragment();
        CompanySignFragment companySignFragment = new CompanySignFragment();

        PersonSignFragment personSignFragment = new PersonSignFragment();
        ReplaceSignFragment replaceSignFragment = new ReplaceSignFragment();

        misMeetingDetailFragment.setArguments(bundle1);
        companySignFragment.setArguments(bundle1);
        personSignFragment.setArguments(bundle1);
        replaceSignFragment.setArguments(bundle1);
        fragments.add(misMeetingDetailFragment);
        fragments.add(companySignFragment);
        //暂时隐藏嘉宾签到
        //fragments.add(personSignFragment);
        fragments.add(replaceSignFragment);
        misMeetingViewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void initView(){
        setTitle("会议详情");
        EventBus.getDefault().register(this);
        handleButtonItem2 = new BarButtonItem(this, R.drawable.icon_scan_white);
        navigationBar.addRightBarItem(handleButtonItem2);
        handleButtonItem2.setVisibility(View.GONE);
        handleButtonItem2.setOnClickMethod(this, "scanSign");
        BarButtonItem qCode = new BarButtonItem(this, R.drawable.icon_q_code);
        navigationBar.addRightBarItem(qCode);
        qCode.setOnClickMethod(this, "showSignQCode");
        initFragment();
        misMeetingViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }


            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        //将viewpager与tablayout绑定
        misMeetingTabLayout.setupWithViewPager(misMeetingViewPager);
    }

    private void initMeeting(MeetingBean meetingBean){
        int replaceSignedNumber = 0;
        int companySignedNumber = 0;
        int personSignedNumber = 0;
        misMeetingName.setText(meetingBean.getMeetingName());
        for (MeetingBean.MeetingUserBean meetingUserBean:meetingBean.getListUserContent()) {
            /*
             * 0为单位性质，1为人员性质, 这里各个性质的签到要汇总
             * SubstituteSign = 1有两种情况，一种是手动签到，一种是被指派参会，被指派参会人是属于单位签到
             * 这里需要判断 SubstituteSign=1但是SubstituteUser是空，说明不是被指派参会人,是手动签到的人
             */
            if(meetingUserBean.getSubstituteSign().equals("1")){
                if(meetingUserBean.getSubstituteUser().equals("")) {
                    replaceSignedNumber++;
                }else if(meetingUserBean.getCheckStatus().equals("1")){
                    //这里需要判断 SubstituteSign=1但是SubstituteUser不是空的时候，并且CheckStatus=1也是单位签到
                    companySignedNumber++;
                }
            }else if (meetingUserBean.getCheckStatus().equals("1")&&meetingUserBean.getType().equals("0")) {
                //单位已签到
                companySignedNumber++;
            } else if(meetingUserBean.getCheckStatus().equals("1")&&meetingUserBean.getType().equals("1")) {
                //个人已签到
                personSignedNumber++;
            }
        }
        int totalSignedNumber = companySignedNumber + personSignedNumber + replaceSignedNumber;
        danweiSignNumberText.setText(String.valueOf(companySignedNumber));
        personSignNumberText.setText(String.valueOf(personSignedNumber));
        daiqianSignNumberText.setText(String.valueOf(replaceSignedNumber));
        totalSignNumberText.setText(String.valueOf(totalSignedNumber));
    }

    /**
     * 点击显示二维码
     */
    public void showSignQCode(){
        Disposable subscribe = Observable.just("ScetiaMeetingCode:" + meetingID)
                .map(s -> {
                    //生成图片是耗时操作，异步执行
                    return QRCodeEncoder.syncEncodeQRCode(s, (int) DensityUtil.screenWidth(MisMeetingActivity.this) / 3 * 2);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    //得到bitmap结果
                    View view = LayoutInflater.from(MisMeetingActivity.this)
                            .inflate(R.layout.dialog_q_code_layout, null, false);
                    ImageView qCodeImage = view.findViewById(R.id.dialog_q_code_image);
                    qCodeImage.setImageBitmap(bitmap);
                    LSAlert.showTransparentDialog(MisMeetingActivity.this, "", view, "", null);
                });

    }
    /**
     * 点击开启扫码签到
     */
    public void scanSign() {
        Intent scanIntent = new Intent(this, ScanActivity.class);
        scanIntent.putExtra(Constants.Setting.SCAN_TYPE, Constants.Setting.SCAN_MEETING_SIGNED);
        startActivityForResult(scanIntent,0x12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x12){
            if(resultCode==RESULT_OK){
                final String result =data.getStringExtra("scanResult");
                if(result==null){
                    return;
                }
                //如果已经签过到了，就提示已签到，不再签到
                if(curMeetingUserBean.getCheckStatus().equals("1")){
                    ToastUtil.showSignedSuccess(this,curMeetingUserBean.getUnitMemberCode(),
                            curMeetingUserBean.getMemberName(),
                            curMeetingUserBean.getUserFullName());
                    return;
                }
                if(curMeetingBean.getMeetingNeedSign().equals("1")){
                    //需要手写签名
                    customDialog = CustomDialog.newInstance(curMeetingUserBean.getUnitMemberCode(),
                            curMeetingUserBean.getMemberName(),curMeetingUserBean.getUserFullName());
                    customDialog.show(getSupportFragmentManager(),"dialog");
                    customDialog.setCustomDialogInterface(new CustomDialog.CustomDialogInterface() {

                        @Override
                        public void clickOkButton(String base64Str) {
                            requestScanResult(result,curMeetingUserBean.getUserId(),base64Str);
                        }

                    });
                }else{
                    requestScanResult(result,curMeetingUserBean.getUserId(),"");
                }
            }
        }
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
        params.put("Token",  Constants.User.GET_TOKEN());
        params.put("s1", scanResult.toUpperCase());
        params.put("s2", userId.toUpperCase());
        params.put("s3",signImage);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.post("/ServiceMis.asmx/ScanCode"),
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
                            LSAlert.showAlert(MisMeetingActivity.this, "签到成功");
                        }else if(data==2){
                            if(customDialog!=null){
                                customDialog.dismiss();
                            }
                            LSAlert.showAlert(MisMeetingActivity.this,"已经签过了,不能再签到了");

                        }else{
                            LSAlert.showAlert(MisMeetingActivity.this,"签到失败");
                        }
                    }
                });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMeeting(MisMeetingFragmentToMis event){
        curMeetingBean = event.getMeetingBean();
        initMeeting(event.getMeetingBean());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMeetingUserBean(MeetingUserBeanToMeetingActivity event){
        curMeetingUserBean = event.getMeetingUserBean();
        if(curMeetingUserBean.getUserId()!=null){
            handleButtonItem2.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}