package com.lessu.xieshi.module.scan;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Decrypt;
import com.lessu.xieshi.Utils.LongString;
import com.lessu.xieshi.module.scan.bean.ReceiveSampleInfoBean;
import com.lessu.xieshi.module.scan.util.BluetoothHelper;
import com.lessu.xieshi.module.scan.util.HandleScanData;
import com.lessu.xieshi.view.DragLayout;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RukuchakanActivity extends NavigationActivity implements View.OnClickListener {
    private DragLayout dl;
    private SwipeMenuCreator creator;
    private LinearLayout ll_shenqingshangbao;
    private LinearLayout ll_shenhexiazai;
    private LinearLayout ll_rukuchakan;
    private LinearLayout ll_shebeixinxi;
    private String uidstr;
    private boolean isReading = true;
    private TextView tv_rukuchakan;
    private TextView tv_chakan;
    private TextView tv_saomiaobianhao;
    private TextView tv_hetongdengjihao;
    private TextView tv_weituobianhao;
    private TextView tv_yangpinbianhao;
    private TextView tv_biaoshibianhao;
    private TextView tv_baogaobianhao;
    private TextView tv_baojianbianhao;
    private TextView tv_yangpinzhuangtai;
    private TextView tv_jiancejieguo;
    private TextView tv_rukugongchengmingchen;
    private TextView tv_gongchenbuwei;
    private TextView tv_gongchendizhi;
    private TextView tv_suoshuqvxian;
    private TextView tv_jiancezhonglei;
    private TextView tv_jiancexiangmu;
    private TextView tv_rukuyangpiningchen;
    private TextView tv_chanpinbiaozhun;
    private TextView tv_jiancecanshu;
    private TextView tv_guigemingchen;
    private TextView tv_qiangdudengji;
    private TextView tv_shigongdanwei;
    private TextView tv_jianshedanwei;
    private TextView tv_jianlidanwei;
    private TextView tv_jiancedanwei;
    private TextView tv_beianzhenghao;
    private TextView tv_shengchanchangjia;
    private TextView tv_zhizuoriqi;
    private TextView tv_linqi;
    private TextView tv_dengjiriqi;
    private TextView tv_weituoriqi;
    private TextView tv_baogaoriqi;
    private TextView tv_beizhu;
    private ReceiveSampleInfoBean receiveSampleInfoBean;
    private   HandleScanData handleScanData;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_rukuchakan;
    }

    @Override
    protected void initView() {
        this.setTitle("入库查看");
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

        tv_saomiaobianhao = findViewById(R.id.tv_saomiaobianhao);
        tv_hetongdengjihao = findViewById(R.id.tv_hetongdengjihao);
        tv_weituobianhao = findViewById(R.id.tv_weituobianhao);
        tv_yangpinbianhao = findViewById(R.id.tv_yangpinbianhao);
        tv_biaoshibianhao = findViewById(R.id.tv_biaoshibianhao);
        tv_baogaobianhao = findViewById(R.id.tv_baogaobianhao);
        tv_baojianbianhao = findViewById(R.id.tv_baojianbianhao);
        tv_yangpinzhuangtai = findViewById(R.id.tv_yangpinzhuangtai);
        tv_jiancejieguo = findViewById(R.id.tv_jiancejieguo);
        tv_rukugongchengmingchen = findViewById(R.id.tv_rukugongchengmingchen);
        tv_gongchenbuwei = findViewById(R.id.tv_gongchenbuwei);
        tv_gongchendizhi = findViewById(R.id.tv_gongchendizhi);
        tv_suoshuqvxian = findViewById(R.id.tv_suoshuqvxian);
        tv_jiancezhonglei = findViewById(R.id.tv_jiancezhonglei);
        tv_jiancexiangmu = findViewById(R.id.tv_jiancexiangmu);
        tv_rukuyangpiningchen = findViewById(R.id.tv_rukuyangpiningchen);
        tv_chanpinbiaozhun = findViewById(R.id.tv_chanpinbiaozhun);
        tv_jiancecanshu = findViewById(R.id.tv_jiancecanshu);
        tv_guigemingchen = findViewById(R.id.tv_guigemingchen);
        tv_qiangdudengji = findViewById(R.id.tv_qiangdudengji);
        tv_shigongdanwei = findViewById(R.id.tv_shigongdanwei);
        tv_jianshedanwei = findViewById(R.id.tv_jianshedanwei);
        tv_jianlidanwei = findViewById(R.id.tv_jianlidanwei);
        tv_jiancedanwei = findViewById(R.id.tv_jiancedanwei);
        tv_beianzhenghao = findViewById(R.id.tv_beianzhenghao);
        tv_shengchanchangjia = findViewById(R.id.tv_shengchanchangjia);
        tv_zhizuoriqi = findViewById(R.id.tv_zhizuoriqi);
        tv_linqi = findViewById(R.id.tv_linqi);
        tv_dengjiriqi = findViewById(R.id.tv_dengjiriqi);
        tv_weituoriqi = findViewById(R.id.tv_weituoriqi);
        tv_baogaoriqi = findViewById(R.id.tv_baogaoriqi);
        tv_beizhu = findViewById(R.id.tv_beizhu);
        tv_rukuchakan = findViewById(R.id.tv_rukuchakan);
        tv_chakan = findViewById(R.id.tv_chakan);
        ll_shenqingshangbao = findViewById(R.id.ll_shenqingshangbao);
        ll_shenhexiazai = findViewById(R.id.ll_shenhexiazai);
        ll_rukuchakan = findViewById(R.id.ll_rukuchakan);
        ll_shebeixinxi = findViewById(R.id.ll_shebeixinxi);
        ll_shenqingshangbao.setOnClickListener(this);
        ll_shenhexiazai.setOnClickListener(this);
        ll_rukuchakan.setOnClickListener(this);
        ll_shebeixinxi.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        uidstr = intent.getStringExtra("uidstr");
        if(uidstr==null||uidstr.equals("")){
            uidstr = AppApplication.muidstr;
        }
        handleScanData = new HandleScanData();
        handleScanData.startReadingByNetWork(new HandleScanData.CallBackListener() {
            @Override
            public void success(ReceiveSampleInfoBean readData) {
                showInfo(readData,readData.getCodeNumber());
            }

            @Override
            public void failure(String msg) {
                tv_chakan.setText(msg);
            }
        });
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                read();
            }
        }).start();*/
    }

    /**
     * 显示数据详情
     * @param bean
     * @param codeNumber 标识号
     */
    private void showInfo(ReceiveSampleInfoBean bean, String codeNumber) {
        tv_chakan.setText("识别完成");
        tv_rukuchakan.setText(codeNumber);
        tv_saomiaobianhao.setText(codeNumber);
        tv_hetongdengjihao.setText(bean.getContract_SignNo());
        tv_weituobianhao.setText(bean.getConSign_ID());
        tv_yangpinbianhao.setText(bean.getSample_ID());
        tv_biaoshibianhao.setText(bean.getSample_BsId());
        tv_baogaobianhao.setText(bean.getReportNumber());
        tv_baojianbianhao.setText(bean.getBuildingReportNumber());
        tv_yangpinzhuangtai.setText(bean.getSample_Status());
        tv_jiancejieguo.setText(bean.getExam_Result());
        tv_rukugongchengmingchen.setText(bean.getProjectName());
        tv_gongchenbuwei.setText(bean.getProJect_Part());
        tv_gongchendizhi.setText(bean.getProjectAddress());
        tv_suoshuqvxian.setText(bean.getAreaKey());
        tv_jiancezhonglei.setText(bean.getKindName());
        tv_jiancexiangmu.setText(bean.getItemName());
        tv_rukuyangpiningchen.setText(bean.getSampleName());
        tv_chanpinbiaozhun.setText(bean.getSampleJudge());
        tv_jiancecanshu.setText(bean.getExam_Parameter_Cn());
        tv_guigemingchen.setText(bean.getSpecName());
        tv_qiangdudengji.setText(bean.getGradeName());
        tv_shigongdanwei.setText(bean.getBuildUnitName());
        tv_jianshedanwei.setText(bean.getConstructUnitName());
        tv_jianlidanwei.setText(bean.getSuperviseUnitName());
        tv_jiancedanwei.setText(bean.getDetectionUnitName());
        tv_beianzhenghao.setText(bean.getRecord_Certificate());
        tv_shengchanchangjia.setText(bean.getProduce_Factory());
        tv_zhizuoriqi.setText(bean.getMolding_Date());
        tv_linqi.setText(bean.getAgeTime());
        tv_dengjiriqi.setText(bean.getCreateDateTime());
        tv_weituoriqi.setText(bean.getDetectonDate());
        tv_baogaoriqi.setText(bean.getReportDate());
        tv_beizhu.setText(bean.getMemo());
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
                intent1.setClass(RukuchakanActivity.this,ShenqingshangbaoActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.ll_shenhexiazai:
                startActivity(new Intent(RukuchakanActivity.this, ReviewDownloadActivity.class));
                finish();
                break;
            case R.id.ll_rukuchakan:
                break;
            case R.id.ll_shebeixinxi:
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidstr);
                intent.setClass(RukuchakanActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 准备读取蓝牙数据
     */
    public void read() {
        String receiveData="";
        String receiveDataFirst="";
        String receiveDataSecond="";
        try {
            InputStream inputStream = BluetoothHelper.getInstance().getInputStream();
            byte[] buffer = new byte[64];
            byte[] buffer2;
            //这里会一直等待读取
            while (isReading) {
                int bytes;
                do {
                    bytes = inputStream.read(buffer);
                } while(bytes <= 0);
                buffer2 = buffer.clone();
                //清空缓存
                Arrays.fill(buffer, (byte) 0);
                //将缓冲区读取的字节数组转为字符串
                String ceshishujv;
                ceshishujv = new String(buffer2, 0, buffer2.length);
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(ceshishujv);
                ceshishujv = m.replaceAll("").trim();
                if(ceshishujv.isEmpty()){
                    continue;
                }
                //条形码
                if(ceshishujv.matches("[0-9]*")){
                    if(ceshishujv.length()==1){
                        receiveDataFirst = ceshishujv;
                        receiveData = receiveDataFirst+receiveDataSecond;
                    } else if(ceshishujv.length()==9){
                        receiveDataSecond = ceshishujv;
                        receiveData = receiveDataFirst+receiveDataSecond;
                    } else if (ceshishujv.length()== 10) {
                        receiveData = ceshishujv;
                    }
                    LogUtil.showLogE(receiveDataFirst+"==="+receiveDataSecond);
                }else{
                    //芯片
                    String s = LongString.bytes2HexString(buffer2);
                    receiveData= Decrypt.decodeChip(s);
                }
                LogUtil.showLogE("需要的数据==>"+receiveData);
                if(receiveData!=null&&receiveData.length()==10){
                    String finalReceiveData = receiveData;
                    receiveSampleInfoBean = getReadData(finalReceiveData);
                    runOnUiThread(() -> {
                        if(receiveSampleInfoBean!=null) {
                            showInfo(receiveSampleInfoBean, finalReceiveData);
                        }else{
                            tv_chakan.setText("未能获取信息");
                        }
                    });
                    receiveData = "";
                    receiveDataFirst="";
                    receiveDataSecond = "";
                }
            }
        } catch (IOException e) {
            runOnUiThread(() -> {
                Toast.makeText(RukuchakanActivity.this,"连接断开了，请重新连接",Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    /**
     * 读取数据
     * @param s
     * @return
     */
    private ReceiveSampleInfoBean getReadData(String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_chakan.setText("识别中...");
            }
        });
        String nameSpace1 = "http://tempuri.org/";
        String methodName1 = "CheckSamplesForProject ";
        String endPoint1 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
        String soapAction1= "http://tempuri.org/CheckSamplesForProject ";
        SoapObject soapObject1 = new SoapObject(nameSpace1, methodName1);
        soapObject1.addProperty("coreCodeStr", s);
        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope1.bodyOut = soapObject1;
        envelope1.dotNet = true;
        envelope1.setOutputSoapObject(soapObject1);
        //设置超时时间为40s
        HttpTransportSE transport1 = new HttpTransportSE(endPoint1,40000);
        SoapObject shujvsoap = null;
        try {
            transport1.call(soapAction1, envelope1);
             SoapObject object1 = (SoapObject) envelope1.bodyIn;
             if(object1==null) return null;
             shujvsoap = (SoapObject) object1.getProperty(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        SoapObject soap3=(SoapObject)shujvsoap.getProperty(0);
        ReceiveSampleInfoBean info=new ReceiveSampleInfoBean();
        if(soap3.toString().contains("Contract_SignNo")){
            info.setContract_SignNo(soap3.getProperty("Contract_SignNo").toString());
        }
        if(soap3.toString().contains("ConSign_ID")){
            info.setConSign_ID(soap3.getProperty("ConSign_ID").toString());
        }
        if(soap3.toString().contains("Sample_ID")){
            info.setSample_ID(soap3.getProperty("Sample_ID").toString());
        }
        if(soap3.toString().contains("Sample_BsId")){
            info.setSample_BsId(soap3.getProperty("Sample_BsId").toString());
        }
        if(soap3.toString().contains("；ReportNumber")){
            info.setReportNumber(soap3.getProperty("ReportNumber").toString());
        }
        if(soap3.toString().contains("BuildingReportNumber")){
            info.setBuildingReportNumber(soap3.getProperty("BuildingReportNumber").toString());
        }
        if(soap3.toString().contains("Sample_Status")){
            info.setSample_Status(soap3.getProperty("Sample_Status").toString());
        }
        if(soap3.toString().contains("Exam_Result")){
            info.setExam_Result(soap3.getProperty("Exam_Result").toString());
        }
        if(soap3.toString().contains("ProjectName")){
            info.setProjectName(soap3.getProperty("ProjectName").toString());
        }
        if(soap3.toString().contains("ProJect_Part")){
            info.setProJect_Part(soap3.getProperty("ProJect_Part").toString());
        }
        if(soap3.toString().contains("ProjectAddress")){
            info.setProjectAddress(soap3.getProperty("ProjectAddress").toString());
        }
        if(soap3.toString().contains("AreaKey")){
            info.setAreaKey(soap3.getProperty("AreaKey").toString());
        }
        if(soap3.toString().contains("KindName")){
            info.setKindName(soap3.getProperty("KindName").toString());
        }
        if(soap3.toString().contains("ItemName")){
            info.setItemName(soap3.getProperty("ItemName").toString());
        }
        if(soap3.toString().contains("SampleName")){
            info.setSampleName(soap3.getProperty("SampleName").toString());
        }
        if(soap3.toString().contains("SampleJudge")){
            info.setSampleJudge(soap3.getProperty("SampleJudge").toString());
        }
        if(soap3.toString().contains("Exam_Parameter_Cn")){
            info.setExam_Parameter_Cn(soap3.getProperty("Exam_Parameter_Cn").toString());
        }
        if(soap3.toString().contains("SpecName")){
            info.setSpecName(soap3.getProperty("SpecName").toString());
        }
        if(soap3.toString().contains("GradeName")){
            info.setGradeName(soap3.getProperty("GradeName").toString());
        }
        if(soap3.toString().contains("BuildUnitName")){
            info.setBuildUnitName(soap3.getProperty("BuildUnitName").toString());
        }
        if(soap3.toString().contains("ConstructUnitName")){
            info.setConstructUnitName(soap3.getProperty("ConstructUnitName").toString());
        }
        if(soap3.toString().contains("SuperviseUnitName")){
            info.setBuildUnitName(soap3.getProperty("SuperviseUnitName").toString());
        }
        if(soap3.toString().contains("DetectionUnitName")){
            info.setDetectionUnitName(soap3.getProperty("DetectionUnitName").toString());
        }
        if(soap3.toString().contains("Record_Certificate")){
            info.setRecord_Certificate(soap3.getProperty("Record_Certificate").toString());
        }
        if(soap3.toString().contains("Produce_Factory")){
            info.setProduce_Factory(soap3.getProperty("Produce_Factory").toString());
        }
        if(soap3.toString().contains("Molding_Date")){
            info.setMolding_Date(soap3.getProperty("Molding_Date").toString());
        }
        if(soap3.toString().contains("AgeTime")){
            info.setAgeTime(soap3.getProperty("AgeTime").toString());
        }
        if(soap3.toString().contains("CreateDateTime")){
            info.setCreateDateTime(soap3.getProperty("CreateDateTime").toString());
        }
        if(soap3.toString().contains("DetectonDate")){
            info.setDetectonDate(soap3.getProperty("DetectonDate").toString());
        }
        if(soap3.toString().contains("ReportDate")){
            info.setReportDate(soap3.getProperty("ReportDate").toString());
        }
        if (soap3.toString().contains("Memo")) {
            info.setMemo(soap3.getProperty("Memo").toString());
        }
        return  info;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isReading = false;
        if(handleScanData!=null){
            handleScanData.stopRead();
        }
    }
}
