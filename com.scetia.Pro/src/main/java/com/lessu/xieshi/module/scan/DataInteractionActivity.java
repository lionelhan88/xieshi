package com.lessu.xieshi.module.scan;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.scan.bean.ReceiveSampleInfoBean;
import com.lessu.xieshi.module.scan.adapter.ReviewDownloadListAdapter;
import com.lessu.xieshi.view.DragLayout;
import com.scetia.Pro.common.Util.SPUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class DataInteractionActivity extends NavigationActivity implements View.OnClickListener {
    private DragLayout dl;
    private SwipeMenuCreator creator;
    private LinearLayout ll_shenqingshangbao;
    private LinearLayout ll_shenhexiazai;
    private LinearLayout ll_rukuchakan;
    private LinearLayout ll_shebeixinxi;
    private SeekBar sb_scan;
    private String uidstr;
    private Button bt_xiazai;
    private Button bt_yaopinqveren;
    private ImageView iv_loding;
    private ArrayList<ReceiveSampleInfoBean> xalTallist = new ArrayList();
    private String talxal;
    private ReviewDownloadListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shujvjiaohu);
        this.setTitle("数据交互");
        //设置侧滑菜单
        dl = findViewById(R.id.dl);
        BarButtonItem menuButtonitem = new BarButtonItem(this, R.drawable.icon_navigation_menu);
        menuButtonitem.setOnClickMethod(this, "menuButtonDidClick");
        navigationBar.setLeftBarItem(menuButtonitem);
        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setWidth((130));
                deleteItem.setIcon(R.drawable.shanchu);
                menu.addMenuItem(deleteItem);
            }
        };
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    /**
     * 左上角的抽屉按钮点击事件
     */
    public void menuButtonDidClick() {
        if (dl.getStatus() != DragLayout.Status.Close) {
            dl.close();
        } else {
            dl.open();
        }
    }


    private void initView() {
        iv_loding = findViewById(R.id.iv_loding);
        bt_xiazai = findViewById(R.id.bt_xiazai);
        bt_yaopinqveren = findViewById(R.id.bt_yaopinqveren);
        ll_shenqingshangbao = findViewById(R.id.ll_shenqingshangbao);
        ll_shenhexiazai = findViewById(R.id.ll_shenhexiazai);
        ll_rukuchakan = findViewById(R.id.ll_rukuchakan);
        ll_shebeixinxi = findViewById(R.id.ll_shebeixinxi);
        sb_scan = findViewById(R.id.sb_scan);
        RecyclerView rvDataInteraction = findViewById(R.id.rv_data_interaction);
        ll_shenqingshangbao.setOnClickListener(this);
        ll_shenhexiazai.setOnClickListener(this);
        ll_rukuchakan.setOnClickListener(this);
        ll_shebeixinxi.setOnClickListener(this);
        bt_xiazai.setOnClickListener(this);
        bt_yaopinqveren.setOnClickListener(this);
        listAdapter = new ReviewDownloadListAdapter();
        listAdapter.getToastLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                LSAlert.showAlert(DataInteractionActivity.this, s);
            }
        });
        rvDataInteraction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvDataInteraction.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intentPut = new Intent(DataInteractionActivity.this, SampleInfoCheckConfirmActivity.class);
            intentPut.putExtra("current", position + "");
            intentPut.putExtra("talxal", talxal);
            intentPut.putExtra("lstBean", xalTallist);
            startActivity(intentPut);
        });
    }

    /**
     * 获取数据
     */
    private void initData() {
        xalTallist.clear();
        Intent intent = getIntent();
        uidstr = intent.getStringExtra("uidstr");
        talxal = intent.getStringExtra("TALXAL");
        final String finalTalxal = talxal;

        new Thread(new Runnable() {
            @Override
            public void run() {
                String nameSpace = "http://tempuri.org/";
                String methodName = "CheckHm";
                String endPoint = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                String soapAction = "http://tempuri.org/CheckHm";
                SoapObject soapObject = new SoapObject(nameSpace, methodName);
                soapObject.addProperty("hMIdStr", AppApplication.muidstr);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER10);
                envelope.bodyOut = soapObject;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(soapObject);
                //设置超时时间40S
                HttpTransportSE transport = new HttpTransportSE(endPoint);
                try {
                    transport.call(soapAction, envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> ToastUtil.showShort("获取数据失败，请稍后重试！"));
                    return;
                }
                SoapObject object = (SoapObject) envelope.bodyIn;
                if (object.toString().equals("CheckHmResponse{}")) {
                    runOnUiThread(() -> ToastUtil.showShort("还未绑定会员编号"));
                    return;
                }
                String result = object.getProperty(0).toString();
                SPUtil.setSPConfig("huiyuanhao", result);
                String nameSpace1 = "http://tempuri.org/";
                String methodName1 = "CheckSamples";
                String endPoint1 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                String soapAction1 = "http://tempuri.org/CheckSamples";
                SoapObject soapObject1 = new SoapObject(nameSpace1, methodName1);
                soapObject1.addProperty("coreCodeStr", finalTalxal);
                soapObject1.addProperty("membercode", AppApplication.muidstr);
                SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope1.bodyOut = soapObject1;
                envelope1.dotNet = true;
                envelope1.setOutputSoapObject(soapObject1);
                HttpTransportSE transport1 = new HttpTransportSE(endPoint1, 40000);
                try {
                    transport1.call(soapAction1, envelope1);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> ToastUtil.showShort("获取数据失败，请稍后重试！"));
                    return;
                }
                SoapObject object1 = (SoapObject) envelope1.bodyIn;
                if (object1.getPropertyCount() == 1) {
                    runOnUiThread(() -> ToastUtil.showShort(object1.getProperty(0).toString()));
                    return;
                }
                //解析数据，并转换为对象集合返回
                List<ReceiveSampleInfoBean> receiveSampleInfoBeans = GsonUtil.parseSoapObject(methodName1, object1, ReceiveSampleInfoBean.class);
                xalTallist.clear();
                xalTallist.addAll(receiveSampleInfoBeans);
                runOnUiThread(() -> {
                    iv_loding.setVisibility(View.GONE);
                    listAdapter.setNewData(xalTallist);
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shenqingshangbao:
                Intent intent1 = new Intent();
                intent1.setClass(DataInteractionActivity.this, ShenqingshangbaoActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_shenhexiazai:
                startActivity(new Intent(DataInteractionActivity.this, ReviewDownloadActivity.class));
                break;

            case R.id.ll_rukuchakan:
                startActivity(new Intent(DataInteractionActivity.this, RukuchakanActivity.class));
                break;

            case R.id.ll_shebeixinxi:
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidstr);
                intent.setClass(DataInteractionActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);
                break;

            case R.id.bt_xiazai:
                initData();
                break;

            case R.id.bt_yaopinqveren:
                Intent intentPut = new Intent(DataInteractionActivity.this, SampleInfoCheckConfirmActivity.class);
                intentPut.putExtra("lstBean", xalTallist);
                intentPut.putExtra("talxal", talxal);
                startActivity(intentPut);
                break;


        }

    }

}
