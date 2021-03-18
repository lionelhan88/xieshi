package com.lessu.xieshi.module.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.scan.bean.ReceiveSampleInfoBean;
import com.lessu.xieshi.view.DragLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class SampleInfoCheckConfirmActivity extends NavigationActivity implements View.OnClickListener {
    private DragLayout dl;
    private SwipeMenuCreator creator;
    private LinearLayout ll_shenqingshangbao;
    private LinearLayout ll_shenhexiazai;
    private LinearLayout ll_rukuchakan;
    private LinearLayout ll_shebeixinxi;
    private SeekBar sb_scan;
    private String uidstr;
    private TextView tv_hetongdengjihao;
    private TextView tv_shigongdanwei;
    private TextView tv_gongchenmingchen;
    private TextView tv_dengjiyanpin;
    private TextView tv_yangpinmingchen;
    private TextView tv_qiangdumingchen;
    private TextView tv_guigemingchen;
    private TextView tv_yangpinzhuangtai;
    private TextView tv_shangyitiao;
    private TextView tv_xiayitiao;
    private TextView tv_yichu;
    private TextView tv_qveren;
    private ArrayList<ReceiveSampleInfoBean> sampleInfoBeans;
    private int current;
    String coreCodeStr="";
    private TextView tv_qveshishangbao;
    private String talxal;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yangpinqveren;
    }

    @Override
    protected void initView() {
        this.setTitle("样品信息确认");
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
        tv_qveshishangbao = findViewById(R.id.tv_qveshishangbao);
        tv_hetongdengjihao = findViewById(R.id.tv_hetongdengjihao);
        tv_shigongdanwei = findViewById(R.id.tv_shigongdanwei);
        tv_gongchenmingchen = findViewById(R.id.tv_gongchenmingchen);
        tv_dengjiyanpin = findViewById(R.id.tv_dengjiyanpin);
        tv_yangpinmingchen = findViewById(R.id.tv_yangpinmingchen);
        tv_qiangdumingchen = findViewById(R.id.tv_qiangdumingchen);
        tv_guigemingchen = findViewById(R.id.tv_guigemingchen);
        tv_yangpinzhuangtai = findViewById(R.id.tv_yangpinzhuangtai);
        tv_shangyitiao = findViewById(R.id.tv_shangyitiao);
        tv_xiayitiao = findViewById(R.id.tv_xiayitiao);
        tv_yichu = findViewById(R.id.tv_yichu);
        tv_qveren = findViewById(R.id.tv_qveren);


        ll_shenqingshangbao = findViewById(R.id.ll_shenqingshangbao);
        ll_shenhexiazai = findViewById(R.id.ll_shenhexiazai);
        ll_rukuchakan = findViewById(R.id.ll_rukuchakan);
        ll_shebeixinxi = findViewById(R.id.ll_shebeixinxi);
        sb_scan = findViewById(R.id.sb_scan);
        ll_shenqingshangbao.setOnClickListener(this);
        ll_shenhexiazai.setOnClickListener(this);
        ll_rukuchakan.setOnClickListener(this);
        ll_shebeixinxi.setOnClickListener(this);
        tv_shangyitiao.setOnClickListener(this);
        tv_xiayitiao.setOnClickListener(this);
        tv_yichu.setOnClickListener(this);
        tv_qveren.setOnClickListener(this);
        tv_qveshishangbao.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intentGet = getIntent();
        String index=intentGet.getStringExtra("current");
        talxal = intentGet.getStringExtra("talxal");
        sampleInfoBeans = (ArrayList<ReceiveSampleInfoBean>) intentGet.getSerializableExtra("lstBean");
        current = index!=null?Integer.parseInt(index):0;
        showCurrentIndexInfo(current);
    }

    /**
     * 展示当前下标索引的样品数据详情
     * @param i 下标索引
     */
    private void showCurrentIndexInfo(int i) {
        if(sampleInfoBeans.size()>0) {
            ReceiveSampleInfoBean receiveSampleInfoBean = sampleInfoBeans.get(i);
            tv_hetongdengjihao.setText(receiveSampleInfoBean.getContract_SignNo());
            tv_shigongdanwei.setText(receiveSampleInfoBean.getBuildUnitName());
            tv_gongchenmingchen.setText(receiveSampleInfoBean.getProjectName());
            tv_dengjiyanpin.setText(receiveSampleInfoBean.getSample_BsId());
            tv_yangpinmingchen.setText(receiveSampleInfoBean.getSampleName());
            tv_qiangdumingchen.setText(receiveSampleInfoBean.getGradeName());
            tv_guigemingchen.setText(receiveSampleInfoBean.getSpecName());
            String state = String.valueOf(receiveSampleInfoBean.getRetStatus()).substring(0,1);
            switch (state){
                case "0":
                    tv_yangpinzhuangtai.setText("正常");
                    break;
                case "1":
                    tv_yangpinzhuangtai.setText("未登记");
                    break;
                case "2":
                    tv_yangpinzhuangtai.setText("已确认");
                    break;
                case "3":
                    tv_yangpinzhuangtai.setText("异常");
                    break;
                case "4":
                    //缺失的数量
                    String lostNum =String.valueOf(receiveSampleInfoBean.getRetStatus()).substring(1);
                    tv_yangpinzhuangtai.setText("缺"+lostNum+"件");
                    break;
            }
            tv_qveshishangbao.setVisibility(state.equals("4")?View.VISIBLE:View.GONE);
            tv_qveren.setVisibility(state.equals("0")?View.VISIBLE:View.GONE);
        }
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
                intent1.setClass(SampleInfoCheckConfirmActivity.this,ShenqingshangbaoActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.ll_shenhexiazai:
                startActivity(new Intent(SampleInfoCheckConfirmActivity.this, ReviewDownloadActivity.class));
                finish();
                break;
            case R.id.ll_rukuchakan:
                startActivity(new Intent(SampleInfoCheckConfirmActivity.this,RukuchakanActivity.class));
                finish();
                break;
            case R.id.ll_shebeixinxi:
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidstr);
                intent.setClass(SampleInfoCheckConfirmActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_shangyitiao:
                if(current-1>=0&&current-1< sampleInfoBeans.size()){
                    current--;
                    showCurrentIndexInfo(current);
                }else if(current-1<0){
                    ToastUtil.showShort("已经是第一条数据了");
                }

                break;
            case R.id.tv_xiayitiao:
                if(current+1>=0&&current+1< sampleInfoBeans.size()){
                    current++;
                    showCurrentIndexInfo(current);
                }else if(current+1>= sampleInfoBeans.size()){
                    ToastUtil.showShort("已经是最后一条数据了");
                }
                break;
            case R.id.tv_yichu:
                if(sampleInfoBeans.size()>1) {
                    sampleInfoBeans.remove(current);
                    if (current == sampleInfoBeans.size()) {
                        current--;
                    }
                    showCurrentIndexInfo(current);
                }else{
                    ToastUtil.showShort("只剩最后一条数据了");
                }
                break;

            case R.id.tv_qveshishangbao:
                if(sampleInfoBeans.size()==0){
                    ToastUtil.showShort("未有需要上报的信息！");
                    return;
                }
                Intent intentPut = new Intent(SampleInfoCheckConfirmActivity.this, ShenqingshangbaoActivity.class);
                intentPut.putExtra("talxal", talxal);
                intentPut.putExtra("sampleid", sampleInfoBeans.get(current).getSample_BsId());
                startActivity(intentPut);
                break;
            case R.id.tv_qveren:
                for (int i = 0; i < sampleInfoBeans.size() ; i++) {
                    coreCodeStr=coreCodeStr+ sampleInfoBeans.get(i).getSample_BsId()+";";
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String nameSpace = "http://tempuri.org/";
                        String methodName = "ChangeSampleStatus2";
                        String endPoint = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                        String soapAction= "http://tempuri.org/ChangeSampleStatus2";
                        SoapObject soapObject = new SoapObject(nameSpace, methodName);
                        soapObject.addProperty("coreCodeStr", coreCodeStr);
                        soapObject.addProperty("membercode", AppApplication.muidstr);
                        SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(
                                SoapEnvelope.VER10);
                        envelope.bodyOut = soapObject;
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(soapObject);
                        HttpTransportSE transport = new HttpTransportSE(endPoint);
                        try {
                            transport.call(soapAction, envelope);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SoapObject object = (SoapObject) envelope.bodyIn;
                        final String result = object.getProperty(0).toString();
                        System.out.println("object..."+object);
                        System.out.println("result...."+result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog.Builder normalDialog =
                                        new AlertDialog.Builder(SampleInfoCheckConfirmActivity.this);
                                normalDialog.setTitle("上传成功");
                                normalDialog.setMessage("共确认(" + sampleInfoBeans.size() + ")条样品信息,批号为(" + result + ")上传成功!");
                                normalDialog.setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                normalDialog.setNegativeButton("关闭",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                // 显示
                                normalDialog.show();
                            }
                        });
                    }
                }).start();


                break;
        }
    }
}
