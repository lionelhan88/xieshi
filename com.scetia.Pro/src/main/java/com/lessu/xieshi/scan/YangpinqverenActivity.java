package com.lessu.xieshi.scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.MyToast;
import com.lessu.xieshi.bean.XalTal;
import com.lessu.xieshi.customView.DragLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;


public class YangpinqverenActivity extends NavigationActivity implements View.OnClickListener {
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
    private ArrayList<XalTal> list;
    private int current;
    String coreCodeStr="";
    private TextView tv_qveshishangbao;
    private String talxal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yangpinqveren);
        navigationBar.setBackgroundColor(0xFF3598DC);
        System.out.println("走到这个activity......");
        this.setTitle("样品信息确认");

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
        tv_qveshishangbao = (TextView) findViewById(R.id.tv_qveshishangbao);
        tv_hetongdengjihao = (TextView) findViewById(R.id.tv_hetongdengjihao);
        tv_shigongdanwei = (TextView) findViewById(R.id.tv_shigongdanwei);
        tv_gongchenmingchen = (TextView) findViewById(R.id.tv_gongchenmingchen);
        tv_dengjiyanpin = (TextView) findViewById(R.id.tv_dengjiyanpin);
        tv_yangpinmingchen = (TextView) findViewById(R.id.tv_yangpinmingchen);
        tv_qiangdumingchen = (TextView) findViewById(R.id.tv_qiangdumingchen);
        tv_guigemingchen = (TextView) findViewById(R.id.tv_guigemingchen);
        tv_yangpinzhuangtai = (TextView) findViewById(R.id.tv_yangpinzhuangtai);
        tv_shangyitiao = (TextView) findViewById(R.id.tv_shangyitiao);
        tv_xiayitiao = (TextView) findViewById(R.id.tv_xiayitiao);
        tv_yichu = (TextView) findViewById(R.id.tv_yichu);
        tv_qveren = (TextView) findViewById(R.id.tv_qveren);


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
        tv_shangyitiao.setOnClickListener(this);
        tv_xiayitiao.setOnClickListener(this);
        tv_yichu.setOnClickListener(this);
        tv_qveren.setOnClickListener(this);
        tv_qveshishangbao.setOnClickListener(this);
    }

    private void initData() {
        Intent intentGet = getIntent();
        boolean shenhe = intentGet.getBooleanExtra("shenhe",false);
        if(shenhe){
            tv_qveshishangbao.setVisibility(View.GONE);
        }else{
            tv_qveshishangbao.setVisibility(View.VISIBLE);
        }
        String index=intentGet.getStringExtra("current");
        talxal = intentGet.getStringExtra("talxal");
        System.out.println("Ynagpinqveren..."+ talxal);
        list = (ArrayList<XalTal>) intentGet.getSerializableExtra("lstBean");
        System.out.println("list的长度。。。。。"+ list.size());
        if(index!=null){
            current= Integer.parseInt(index);
        }else {
            current = 0;
        }
        Xianshi(current);

    }

    private void Xianshi(int i) {
        if(list.size()>0) {
            XalTal xalTal = list.get(i);
            tv_hetongdengjihao.setText(xalTal.getContract_SignNo());
            tv_shigongdanwei.setText(xalTal.getBuildUnitName());
            tv_gongchenmingchen.setText(xalTal.getProjectName());
            tv_dengjiyanpin.setText(xalTal.getSample_BsId());
            tv_yangpinmingchen.setText(xalTal.getSampleName());
            tv_qiangdumingchen.setText(xalTal.getGradeName());
            tv_guigemingchen.setText(xalTal.getSpecName());
            if (xalTal.getRetStatus() == 0) {
                tv_yangpinzhuangtai.setText("正常");
            } else if (xalTal.getRetStatus() == 1) {
                tv_yangpinzhuangtai.setText("未登记");
            } else if (xalTal.getRetStatus() == 2) {
                tv_yangpinzhuangtai.setText("已确认");
            } else if (xalTal.getRetStatus() == 3) {
                tv_yangpinzhuangtai.setText("异常");
            } else if (xalTal.getRetStatus() == 4) {
                tv_yangpinzhuangtai.setText("缺失");
            }
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
                intent1.setClass(YangpinqverenActivity.this,ShenqingshangbaoActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.ll_shenhexiazai:
                startActivity(new Intent(YangpinqverenActivity.this,ShenhexiazaiActivity.class));
                finish();
                break;
            case R.id.ll_rukuchakan:
                startActivity(new Intent(YangpinqverenActivity.this,RukuchakanActivity.class));
                finish();
                break;
            case R.id.ll_shebeixinxi:
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidstr);
                intent.setClass(YangpinqverenActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_shangyitiao:
                if(current-1>=0&&current-1<list.size()){
                    current--;
                    Xianshi(current);
                }else if(current-1<0){
                    MyToast.showShort("已经是第一条数据了");
                }

                break;
            case R.id.tv_xiayitiao:
                if(current+1>=0&&current+1<list.size()){
                    current++;
                    Xianshi(current);
                }else if(current+1>=list.size()){
                    MyToast.showShort("已经是最后一条数据了");
                }
                break;
            case R.id.tv_yichu:
                if(list.size()>1) {
                    list.remove(current);
                    if (current  < list.size()) {

                        Xianshi(current);
                    } else {
                        current--;
                        Xianshi(current);
                    }
                }else{
                    MyToast.showShort("只剩最后一条数据了");
                }

                break;

            case R.id.tv_qveshishangbao:
                if(list.get(current).getRetStatus()==4) {
                    Intent intentPut = new Intent(YangpinqverenActivity.this, ShenqingshangbaoActivity.class);
                    intentPut.putExtra("talxal", talxal);
                    intentPut.putExtra("sampleid",list.get(current).getSample_BsId());
                    startActivity(intentPut);
                }

                break;

            case R.id.tv_qveren:

                for (int i = 0; i <list.size() ; i++) {
                    coreCodeStr=coreCodeStr+list.get(i).getSample_BsId()+";";
                }
                System.out.println("coreCodeStr......"+coreCodeStr);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String nameSpace = "http://tempuri.org/";
                        String methodName = "ChangeSampleStatus2";
                        String endPoint = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                        String soapAction= "http://tempuri.org/ChangeSampleStatus2";
                        SoapObject soapObject = new SoapObject(nameSpace, methodName);
                        soapObject.addProperty("coreCodeStr", coreCodeStr);
                        //soapObject.addProperty("membercode", "35ffd0054843353230782243");
                        soapObject.addProperty("membercode", AppApplication.muidstr);
                        SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(
                                SoapEnvelope.VER10);
                        envelope.bodyOut = soapObject;
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(soapObject);
                        HttpTransportSE transport = new HttpTransportSE(endPoint);
                        try {
                            transport.call(soapAction, envelope);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            // TODO Auto-generated catch block
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
                                        new AlertDialog.Builder(YangpinqverenActivity.this);
                                normalDialog.setTitle("上传成功");
                                normalDialog.setMessage("共确认(" + list.size() + ")条样品信息,批号为(" + result + ")上传成功!");
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
                             //   MyToast.showShort("共确认(" + list.size() + ")条样品信息,批号为(" + result + ")上传成功!");
                            }
                        });
                    }
                }).start();


                break;
        }
    }
}
