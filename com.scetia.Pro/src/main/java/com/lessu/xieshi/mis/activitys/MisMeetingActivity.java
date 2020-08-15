package com.lessu.xieshi.mis.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.PicSize;
import com.lessu.xieshi.config;
import com.lessu.xieshi.meet.CustomDialog;
import com.lessu.xieshi.meet.MeetingDetailActivity;
import com.lessu.xieshi.meet.MeetingListActivity;
import com.lessu.xieshi.meet.bean.MeetingBean;
import com.lessu.xieshi.meet.event.MeetingUserBeanToMeetingActivity;
import com.lessu.xieshi.meet.event.MisMeetingFragmentToMis;
import com.lessu.xieshi.meet.event.SendMeetingDetailToList;
import com.lessu.xieshi.meet.event.SendMeetingListToDetail;
import com.lessu.xieshi.mis.fragment.CompanySignFragment;
import com.lessu.xieshi.mis.fragment.MisMeetingDetailFragment;
import com.lessu.xieshi.mis.fragment.PersonSignFragment;
import com.lessu.xieshi.mis.fragment.ReplaceSignFragment;
import com.lessu.xieshi.scan.ScanActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    private String[] titles = {"会议概述","单位签到","个人签到","代签记录"};
    private  String meetingID;
    private  BarButtonItem handleButtonItem2;
    private MeetingBean curMeetingBean;
    private MeetingBean.MeetingUserBean curMeetingUserBean;
    private CustomDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_meeting);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        navigationBar.setBackgroundColor(0xFF3598DC);
        handleButtonItem2 = new BarButtonItem(this, R.drawable.icon_scan_white);
        navigationBar.addRightBarItem(handleButtonItem2);
        handleButtonItem2.setVisibility(View.GONE);
        handleButtonItem2.setOnClickMethod(this, "scanSign");
        BarButtonItem qCode = new BarButtonItem(this, R.drawable.icon_q_code);
        navigationBar.addRightBarItem(qCode);
        qCode.setOnClickMethod(this, "showSignQCode");

        setTitle("会议现场");
        initFragment();
        initView();
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
        fragments.add(personSignFragment);
        fragments.add(replaceSignFragment);
        misMeetingViewPager.setOffscreenPageLimit(1);
    }
    private void initView(){
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

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
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
            // 0为单位性质，1为人员性质, 这里各个性质的签到要汇总
            //已经签到的人员或单位
            if(meetingUserBean.getSubstituteSign().equals("1")){
                //代签人数
                replaceSignedNumber++;
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
    @SuppressLint("CheckResult")
    public void showSignQCode(){
        Observable.just("ScetiaMeetingCode:"+meetingID)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        //生成图片是耗时操作，异步执行
                        return QRCodeEncoder.syncEncodeQRCode(s, (int) PicSize.screenWidth(MisMeetingActivity.this)/3*2);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        //得到bitmap结果
                        View view = LayoutInflater.from(MisMeetingActivity.this)
                                .inflate(R.layout.dialog_q_code_layout,null,false);
                        ImageView qCodeImage = view.findViewById(R.id.dialog_q_code_image);
                        qCodeImage.setImageBitmap(bitmap);
                        LSAlert.showTransparentDialog(MisMeetingActivity.this,"",view,"",null);
                    }
                });

    }
    /**
     * 点击开启扫码签到
     */
    public void scanSign() {
        Intent scanIntent = new Intent(this, ScanActivity.class);
        scanIntent.putExtra(config.SCAN_TYPE,config.SCAN_MEETING_SIGNED);
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