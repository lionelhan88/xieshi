package com.lessu.xieshi.module.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.ToastUtil;
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

public class ReviewDownloadActivity extends NavigationActivity implements View.OnClickListener{
    public static final String SAMPLE_STATE = "shenhe";
    private RecyclerView rvReviewDownload;
    private DragLayout dl;
    private LinearLayout ll_shenqingshangbao;
    private LinearLayout ll_shenhexiazai;
    private LinearLayout ll_rukuchakan;
    private LinearLayout ll_shebeixinxi;
    private String uidstr;
    private Button bt_shenhexiazai;
    private Button bt_shenheyaopinqveren;
    private ArrayList<ReceiveSampleInfoBean> sampleInfoList = new ArrayList<>();
    private ImageView iv_shenheloding;
    private ReviewDownloadListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shenhexiazai;
    }

    @Override
    protected void initView() {
        setTitle("审核下载");
        dl = findViewById(R.id.dl);
        BarButtonItem menuButtonitem = new BarButtonItem(this, R.drawable.icon_navigation_menu);
        menuButtonitem.setOnClickMethod(this, "menuButtonDidClick");
        navigationBar.setLeftBarItem(menuButtonitem);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setWidth((130));
                deleteItem.setIcon(R.drawable.shanchu);
                menu.addMenuItem(deleteItem);
            }
        };
        listAdapter = new ReviewDownloadListAdapter();
        listAdapter.getToastLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                LSAlert.showAlert(ReviewDownloadActivity.this,s);
            }
        });
        rvReviewDownload = findViewById(R.id.rv_review_download);
        rvReviewDownload.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvReviewDownload.setAdapter(listAdapter);
        iv_shenheloding = findViewById(R.id.iv_shenheloding);
        bt_shenhexiazai = findViewById(R.id.bt_shenhexiazai);
        bt_shenheyaopinqveren = findViewById(R.id.bt_shenheyaopinqveren);
        ll_shenqingshangbao = findViewById(R.id.ll_shenqingshangbao);
        ll_shenhexiazai = findViewById(R.id.ll_shenhexiazai);
        ll_rukuchakan = findViewById(R.id.ll_rukuchakan);
        ll_shebeixinxi = findViewById(R.id.ll_shebeixinxi);
        SeekBar sb_scan = findViewById(R.id.sb_scan);
        ll_shenqingshangbao.setOnClickListener(this);
        ll_shenhexiazai.setOnClickListener(this);
        ll_rukuchakan.setOnClickListener(this);
        ll_shebeixinxi.setOnClickListener(this);
        bt_shenhexiazai.setOnClickListener(this);
        bt_shenheyaopinqveren.setOnClickListener(this);

        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intentPut = new Intent(ReviewDownloadActivity.this, SampleInfoCheckConfirmActivity.class);
            intentPut.putExtra("current", position + "");
            intentPut.putExtra("lstBean", sampleInfoList);
            intentPut.putExtra(SAMPLE_STATE, true);
            startActivity(intentPut);
        });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        Intent intent=getIntent();
        uidstr = intent.getStringExtra("uidstr");
        if(uidstr==null|| uidstr.equals("")){
            //有可能其他页面没有传入这个uid，所以获取全局的uid
            uidstr = AppApplication.muidstr;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                getReportUpState();
            }
        }).start();
    }

    /**
     * 获取需要审核的标识编号
     */
    private void getReportUpState() {
        StringBuilder codes = new StringBuilder();
        String nameSpace1 = "http://tempuri.org/";
        String methodName1 = "GetReportUpState";
        String endPoint1 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
        String soapAction1 = "http://tempuri.org/GetReportUpState ";
        SoapObject soapObject1 = new SoapObject(nameSpace1, methodName1);
        soapObject1.addProperty("membercode", SPUtil.getSPConfig("huiyuanhao", ""));
        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                SoapEnvelope.VER10);
        envelope1.bodyOut = soapObject1;
        envelope1.dotNet = true;
        envelope1.setOutputSoapObject(soapObject1);
        HttpTransportSE transport1 = new HttpTransportSE(endPoint1);
        try {
            transport1.call(soapAction1, envelope1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SoapObject reportUpstateObject = (SoapObject) envelope1.bodyIn;
        //测试
       /* SoapObject testObj = new SoapObject();
        testObj.addProperty("GetReportUpStateResult","1760019572~1760019575;1258672952;");
        testObj.addProperty("message","anyType{};");
        String reportUpstateResult = String.valueOf(testObj.getProperty(methodName1 + "Result"));*/
        String reportUpstateResult = String.valueOf(reportUpstateObject.getProperty(methodName1 + "Result"));
        if (reportUpstateResult.equals("anyType{}")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort("当前无要审核的信息。");
                }
            });
            return;
        }
        reportUpstateResult = reportUpstateResult.substring(0, reportUpstateResult.lastIndexOf(";"));
        String[] numberTeam = reportUpstateResult.split(";");
        for (String number:numberTeam){
            if(number.contains("~")){
                //如果是区间字段，需要自己分解添加
                String[] numberTeam2 = number.split("~");
                int startNumber = Integer.parseInt(numberTeam2[0]);
                int endNumber = Integer.parseInt(numberTeam2[1]);
                for (int i=startNumber;i<=endNumber;i++){
                    codes.append(i).append(";");
                }
            }else{
                codes.append(number).append(";");
            }
        }
        checkSamples(codes.toString());
    }
    /**
     * 获取标识编号的信息和状态
     * @param coreCodeStr
     */
    private void checkSamples(String coreCodeStr){
        String nameSpace2 = "http://tempuri.org/";
        String methodName2 = "CheckSamples";
        String endPoint2 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
        String soapAction2 = "http://tempuri.org/CheckSamples";
        SoapObject soapObject2 = new SoapObject(nameSpace2, methodName2);
        soapObject2.addProperty("coreCodeStr", coreCodeStr);
        soapObject2.addProperty("membercode", uidstr);
        SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(
                SoapEnvelope.VER10);
        envelope2.bodyOut = soapObject2;
        envelope2.dotNet = true;
        envelope2.setOutputSoapObject(soapObject2);
        HttpTransportSE transport2 = new HttpTransportSE(endPoint2);
        try {
            transport2.call(soapAction2, envelope2);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() ->ToastUtil.showShort("获取数据失败，请稍后重试！"));
            return;
        }
        //获取返回的原始数据
        SoapObject object2 = (SoapObject) envelope2.bodyIn;
        if(object2.getPropertyCount()==1){
            runOnUiThread(()->ToastUtil.showShort(object2.getProperty(0).toString()));
            return;
        }
        //解析数据，并转换为对象集合返回
        List<ReceiveSampleInfoBean> receiveSampleInfoBeans = GsonUtil.parseSoapObject(methodName2, object2, ReceiveSampleInfoBean.class);
        //显示列表
        sampleInfoList.clear();
        sampleInfoList.addAll(receiveSampleInfoBeans);
        runOnUiThread(() -> {
            listAdapter.setNewData(sampleInfoList);
            iv_shenheloding.setVisibility(View.GONE);
        });
    }

    public void menuButtonDidClick() {
        if (dl.getStatus() != DragLayout.Status.Close) {
            dl.close();
        } else {
            dl.open();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shenqingshangbao:
                Intent intent1=new Intent();
                intent1.setClass(ReviewDownloadActivity.this,ShenqingshangbaoActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.ll_shenhexiazai:
                break;
            case R.id.ll_rukuchakan:
                startActivity(new Intent(ReviewDownloadActivity.this,RukuchakanActivity.class));
                finish();
                break;
            case R.id.ll_shebeixinxi:
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidstr);
                intent.setClass(ReviewDownloadActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_shenhexiazai:
                initData();
                break;
            case R.id.bt_shenheyaopinqveren:
                Intent intentPut = new Intent(ReviewDownloadActivity.this, SampleInfoCheckConfirmActivity.class);
                intentPut.putExtra("lstBean", sampleInfoList);
                intentPut.putExtra("shenhe",true);
                startActivity(intentPut);
                break;
        }
    }
}
