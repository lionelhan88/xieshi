package com.lessu.xieshi.module.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.JieMi;
import com.lessu.xieshi.Utils.LongString;
import com.lessu.xieshi.bean.XalTal;
import com.lessu.xieshi.view.DragLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lessu.xieshi.module.scan.PrintDataActivity.bluetoothSocket;

public class RukuchakanActivity extends NavigationActivity implements View.OnClickListener {
    private DragLayout dl;
    private SwipeMenuCreator creator;
    private LinearLayout ll_shenqingshangbao;
    private LinearLayout ll_shenhexiazai;
    private LinearLayout ll_rukuchakan;
    private LinearLayout ll_shebeixinxi;
    private SeekBar sb_scan;
    private String uidstr;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private boolean isReading = true;
    private boolean isReadingReady = true;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rukuchakan);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("入库查看");
        //设置侧滑菜单
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {
            }

            @Override
            public void onDrag(float percent) {
            }
        });

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
        initData();
    }
    private void initView() {
        tv_saomiaobianhao = (TextView) findViewById(R.id.tv_saomiaobianhao);
        tv_hetongdengjihao = (TextView) findViewById(R.id.tv_hetongdengjihao);
        tv_weituobianhao = (TextView) findViewById(R.id.tv_weituobianhao);
        tv_yangpinbianhao = (TextView) findViewById(R.id.tv_yangpinbianhao);
        tv_biaoshibianhao = (TextView) findViewById(R.id.tv_biaoshibianhao);
        tv_baogaobianhao = (TextView) findViewById(R.id.tv_baogaobianhao);
        tv_baojianbianhao = (TextView) findViewById(R.id.tv_baojianbianhao);
        tv_yangpinzhuangtai = (TextView) findViewById(R.id.tv_yangpinzhuangtai);
        tv_jiancejieguo = (TextView) findViewById(R.id.tv_jiancejieguo);
        tv_rukugongchengmingchen = (TextView) findViewById(R.id.tv_rukugongchengmingchen);
        tv_gongchenbuwei = (TextView) findViewById(R.id.tv_gongchenbuwei);
        tv_gongchendizhi = (TextView) findViewById(R.id.tv_gongchendizhi);
        tv_suoshuqvxian = (TextView) findViewById(R.id.tv_suoshuqvxian);
        tv_jiancezhonglei = (TextView) findViewById(R.id.tv_jiancezhonglei);
        tv_jiancexiangmu = (TextView) findViewById(R.id.tv_jiancexiangmu);
        tv_rukuyangpiningchen = (TextView) findViewById(R.id.tv_rukuyangpiningchen);
        tv_chanpinbiaozhun = (TextView) findViewById(R.id.tv_chanpinbiaozhun);
        tv_jiancecanshu = (TextView) findViewById(R.id.tv_jiancecanshu);
        tv_guigemingchen = (TextView) findViewById(R.id.tv_guigemingchen);
        tv_qiangdudengji = (TextView) findViewById(R.id.tv_qiangdudengji);
        tv_shigongdanwei = (TextView) findViewById(R.id.tv_shigongdanwei);
        tv_jianshedanwei = (TextView) findViewById(R.id.tv_jianshedanwei);
        tv_jianlidanwei = (TextView) findViewById(R.id.tv_jianlidanwei);
        tv_jiancedanwei = (TextView) findViewById(R.id.tv_jiancedanwei);
        tv_beianzhenghao = (TextView) findViewById(R.id.tv_beianzhenghao);
        tv_shengchanchangjia = (TextView) findViewById(R.id.tv_shengchanchangjia);
        tv_zhizuoriqi = (TextView) findViewById(R.id.tv_zhizuoriqi);
        tv_linqi = (TextView) findViewById(R.id.tv_linqi);
        tv_dengjiriqi = (TextView) findViewById(R.id.tv_dengjiriqi);
        tv_weituoriqi = (TextView) findViewById(R.id.tv_weituoriqi);
        tv_baogaoriqi = (TextView) findViewById(R.id.tv_baogaoriqi);
        tv_beizhu = (TextView) findViewById(R.id.tv_beizhu);




        tv_rukuchakan = (TextView) findViewById(R.id.tv_rukuchakan);
        tv_chakan = (TextView) findViewById(R.id.tv_chakan);

        ll_shenqingshangbao = (LinearLayout) findViewById(R.id.ll_shenqingshangbao);
        ll_shenhexiazai = (LinearLayout) findViewById(R.id.ll_shenhexiazai);
        ll_rukuchakan = (LinearLayout) findViewById(R.id.ll_rukuchakan);
        ll_shebeixinxi = (LinearLayout) findViewById(R.id.ll_shebeixinxi);
        sb_scan = (SeekBar) findViewById(R.id.sb_scan);
        sb_scan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ll_shenqingshangbao.setOnClickListener(this);
        ll_shenhexiazai.setOnClickListener(this);
        ll_rukuchakan.setOnClickListener(this);
        ll_shebeixinxi.setOnClickListener(this);
    }

    private void initData() {
        Intent intent=getIntent();
        uidstr = intent.getStringExtra("uidstr");
        if(uidstr==null||uidstr.equals("")){
            uidstr = AppApplication.muidstr;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                read();
            }
        }).start();
    }

    private void Xianshi(XalTal shhujv,String s) {
        tv_chakan.setText("识别完成");
        tv_rukuchakan.setText(s);
        tv_saomiaobianhao.setText(s);
        tv_hetongdengjihao.setText(shhujv.getContract_SignNo());
        tv_weituobianhao.setText(shhujv.getConSign_ID());
        tv_yangpinbianhao.setText(shhujv.getSample_ID());
        tv_biaoshibianhao.setText(shhujv.getSample_BsId());
        tv_baogaobianhao.setText(shhujv.getReportNumber());
        tv_baojianbianhao.setText(shhujv.getBuildingReportNumber());
        tv_yangpinzhuangtai.setText(shhujv.getSample_Status());
        tv_jiancejieguo.setText(shhujv.getExam_Result());
        tv_rukugongchengmingchen.setText(shhujv.getProjectName());
        tv_gongchenbuwei.setText(shhujv.getProJect_Part());
        tv_gongchendizhi.setText(shhujv.getProjectAddress());
        tv_suoshuqvxian.setText(shhujv.getAreaKey());
        tv_jiancezhonglei.setText(shhujv.getKindName());
        tv_jiancexiangmu.setText(shhujv.getItemName());
        tv_rukuyangpiningchen.setText(shhujv.getSampleName());
        tv_chanpinbiaozhun.setText(shhujv.getSampleJudge());
        tv_jiancecanshu.setText(shhujv.getExam_Parameter_Cn());
        tv_guigemingchen.setText(shhujv.getSpecName());
        tv_qiangdudengji.setText(shhujv.getGradeName());
        tv_shigongdanwei.setText(shhujv.getBuildUnitName());
        tv_jianshedanwei.setText(shhujv.getConstructUnitName());
        tv_jianlidanwei.setText(shhujv.getSuperviseUnitName());
        tv_jiancedanwei.setText(shhujv.getDetectionUnitName());
        tv_beianzhenghao.setText(shhujv.getRecord_Certificate());
        tv_shengchanchangjia.setText(shhujv.getProduce_Factory());
        tv_zhizuoriqi.setText(shhujv.getMolding_Date());
        tv_linqi.setText(shhujv.getAgeTime());
        tv_dengjiriqi.setText(shhujv.getCreateDateTime());
        tv_weituoriqi.setText(shhujv.getDetectonDate());
        tv_baogaoriqi.setText(shhujv.getReportDate());
        tv_beizhu.setText(shhujv.getMemo());
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
                startActivity(new Intent(RukuchakanActivity.this,ShenhexiazaiActivity.class));
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
    String s="";
    public void read() {
        try {
            inputStream = bluetoothSocket.getInputStream();
            outputStream=bluetoothSocket.getOutputStream();

            String Ts1 = null;
            String Ts2;
            String Ts;
            boolean saolebiedetiaoma = false;
            byte[] buffer = new byte[1024];
            byte[] buffer2 = new byte[1024];
            //这里会一直等待读取
            while (isReading) {
                if(!isReadingReady) continue;
                int bytes;
                do {
                    bytes = this.inputStream.read(buffer);
                } while(bytes <= 0);
                isReadingReady = false;
                buffer2 = (byte[])buffer.clone();
                //清空缓存
                Arrays.fill(buffer, (byte) 0);
                //将缓冲区读取的字节数组转为字符串
                String ceshishujv;
                ceshishujv = new String(buffer2, 0, buffer2.length);
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(ceshishujv);
                ceshishujv = m.replaceAll("").trim();
                //条形码
                if(ceshishujv.matches("[0-9]*")){
                    if(bytes<10){
                        if (Ts1 == null || Ts1.equals("")){
                            Ts1 = new String(buffer2,0,bytes);
                            if(saolebiedetiaoma) {
                                Ts1=null;
                                saolebiedetiaoma=false;
                            }
                        }else{
                            Ts2 = new String(buffer2,0,bytes);
                            System.out.println("ruku...Ts1..else..."+Ts1);
                            System.out.println("ruku...Ts2..else..."+Ts2);
                            Ts=Ts1+Ts2;
                            String substring = Ts.substring(0, 1);
                            if(Ts.length()==10&&substring.equals("1")){
                                s=Ts;
                                Ts1=null;
                                final XalTal shhujv = getReadData(s);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Xianshi(shhujv,s);
                                    }
                                });
                            }else{
                                Ts1=null;
                                saolebiedetiaoma = true;
                            }
                        }
                        isReadingReady = true;
                    } else if (ceshishujv.length()== 10) {
                        final XalTal shhujv = getReadData(ceshishujv);
                        s = ceshishujv;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(shhujv!=null) {
                                    Xianshi(shhujv, s);
                                }else{
                                    tv_chakan.setText("未能获取信息");
                                }
                            }
                        });
                        isReadingReady = true;
                    }
                    //是芯片
                }else{
                    String s = LongString.bytes2HexString(buffer2);
                    final String jiexinpian = JieMi.jiexinpian(s);
                    if(jiexinpian!=null){
                        final XalTal shhujv = getReadData(jiexinpian);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(shhujv!=null) {
                                    Xianshi(shhujv, jiexinpian);
                                }else{
                                    tv_chakan.setText("未能获取信息");
                                }
                            }
                        });
                    }
                    isReadingReady = true;
                }
            }
        } catch (IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RukuchakanActivity.this,"连接断开了，请重新连接",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    /**
     * 读取数据
     * @param s
     * @return
     */
    private XalTal getReadData(String s){
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
        System.out.println("waimian...."+shujvsoap.toString());
        SoapObject soap3=(SoapObject)shujvsoap.getProperty(0);
        XalTal info=new XalTal();
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
    }
}
