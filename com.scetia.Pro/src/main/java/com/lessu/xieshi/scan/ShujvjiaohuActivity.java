package com.lessu.xieshi.scan;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.MyToast;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.bean.XalTal;
import com.lessu.xieshi.customView.DragLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ShujvjiaohuActivity extends NavigationActivity implements View.OnClickListener {
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
    private ListView lv_shujvjiaohu;
    private MyAdapter myAdapter;
    private ArrayList<XalTal> xalTallist = new ArrayList();;
    private String tishistr="";
    private boolean weidengji;
    private boolean yichang;
    private boolean qveshi;
    private String talxal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shujvjiaohu);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("数据交互");

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
        //initData();
    }

    public void menuButtonDidClick() {
        if (dl.getStatus() != DragLayout.Status.Close) {
            dl.close();
        } else {
            dl.open();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initView() {
        lv_shujvjiaohu = (ListView) findViewById(R.id.lv_shujvjiaohu);
        iv_loding = (ImageView) findViewById(R.id.iv_loding);
        bt_xiazai = (Button) findViewById(R.id.bt_xiazai);
        bt_yaopinqveren = (Button) findViewById(R.id.bt_yaopinqveren);
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
        bt_xiazai.setOnClickListener(this);
        bt_yaopinqveren.setOnClickListener(this);
        lv_shujvjiaohu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position./....."+position);
                Intent intentPut = new Intent(ShujvjiaohuActivity.this, YangpinqverenActivity.class);
                intentPut.putExtra("current",position+"");
                intentPut.putExtra("talxal", talxal);
                intentPut.putExtra("lstBean", (Serializable)xalTallist);
                startActivity(intentPut);
            }
        });
    }
    private void initData() {
        Intent intent = getIntent();
        uidstr = intent.getStringExtra("uidstr");
        talxal = intent.getStringExtra("TALXAL");
        //talxal = talxal +"1150080333;1150080318;1150010005;1150010004;1150080303;";
        System.out.println("talxal...."+ talxal);
        System.out.println("muidstr................"+AppApplication.muidstr);
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
                System.out.println(object.toString());
                if(object.toString().equals("CheckHmResponse{}")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ShujvjiaohuActivity.this,"还未绑定会员编号",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    String result = object.getProperty(0).toString();
                    Shref.setString(ShujvjiaohuActivity.this, "huiyuanhao", result);
                    System.out.println("object..." + object);
                    System.out.println("result...." + result);
                }
                String nameSpace1 = "http://tempuri.org/";
                String methodName1 = "CheckSamples";
                String endPoint1 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                String soapAction1= "http://tempuri.org/CheckSamples";
                SoapObject soapObject1 = new SoapObject(nameSpace1, methodName1);
                System.out.println("这里要改finalTalXal...........................*&^$$#%^&**^#@!#^*(())*&^%$#@!@#$%^&.......................................................");
                soapObject1.addProperty("coreCodeStr", finalTalxal);
                soapObject1.addProperty("membercode", AppApplication.muidstr);
                //soapObject1.addProperty("membercode", "35ffd0054843353230782243");
                SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                        SoapEnvelope.VER10);
                envelope1.bodyOut = soapObject1;
                envelope1.dotNet = true;
                envelope1.setOutputSoapObject(soapObject1);
                HttpTransportSE transport1 = new HttpTransportSE(endPoint1);
                try {
                    transport1.call(soapAction1, envelope1);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                SoapObject object1 = (SoapObject) envelope1.bodyIn;
                System.out.println(object1.toString());
                if(object1.getPropertyCount()>=1) {
                    String s = object1.getProperty(0).toString();
                    if(s.equals("没有传入任何标识号")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShujvjiaohuActivity.this,"没有传入任何标识号",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(s.equals("手持机未注册或已经作废")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShujvjiaohuActivity.this,"手持机未注册或已经作废",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        System.out.println("ssswwwss000" + s);
                        SoapObject shujvsoap = (SoapObject) object1.getProperty(0);
                        for (int i = 0; i < shujvsoap.getPropertyCount(); i++) {
                            SoapObject soap3 = (SoapObject) shujvsoap.getProperty(i);
                            System.out.println(i + "......" + soap3.toString());
                            XalTal info = new XalTal();
                            if (soap3.toString().contains("Contract_SignNo")) {
                                info.setContract_SignNo(soap3.getProperty("Contract_SignNo").toString());
                            }
                            if (soap3.toString().contains("Sample_BsId")) {
                                info.setSample_BsId(soap3.getProperty("Sample_BsId").toString());
                            }
                            if (soap3.toString().contains("KindName")) {
                                info.setKindName(soap3.getProperty("KindName").toString());
                            }
                            if (soap3.toString().contains("ItemName")) {
                                info.setItemName(soap3.getProperty("ItemName").toString());
                            }
                            if (soap3.toString().contains("SampleName")) {
                                info.setSampleName(soap3.getProperty("SampleName").toString());
                            }
                            if (soap3.toString().contains("SpecName")) {
                                info.setSpecName(soap3.getProperty("SpecName").toString());
                            }
                            if (soap3.toString().contains("GradeName")) {
                                info.setGradeName(soap3.getProperty("GradeName").toString());
                            }
                            if (soap3.toString().contains("RetStatus")) {
                                info.setRetStatus(Integer.parseInt(soap3.getProperty("RetStatus").toString()));
                            }
                            if (soap3.toString().contains("BuildUnitName")) {
                                info.setBuildUnitName(soap3.getProperty("BuildUnitName").toString());
                            }
                            if (soap3.toString().contains("ProjectName")) {
                                info.setProjectName(soap3.getProperty("ProjectName").toString());
                            }
                            xalTallist.add(info);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_loding.setVisibility(View.GONE);
                                lv_shujvjiaohu.setVisibility(View.VISIBLE);
                                if (myAdapter != null) {
                                    myAdapter.notifyDataSetChanged();
                                } else {
                                    myAdapter = new MyAdapter();
                                }
                                lv_shujvjiaohu.setAdapter(myAdapter);
                                System.out.println("weidengji......" + weidengji);


                            }
                        });
                    }
                    System.out.println(xalTallist);
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showShort("没有上传任何号码");
                        }
                    });
                }

            }
        }).start();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return xalTallist.size();
        }

        @Override
        public Object getItem(int position) {
            return xalTallist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=View.inflate(ShujvjiaohuActivity.this,R.layout.item_shujvjiaohu,null);
                holder=new ViewHolder();
                holder.tv_biaoshibianhao = (TextView) convertView.findViewById(R.id.tv_biaoshibianhao);
                holder.tv_yangpinzhuangtai = (TextView) convertView.findViewById(R.id.tv_yangpinzhuangtai);
                holder.tv_xiangmumingchen = (TextView) convertView.findViewById(R.id.tv_xiangmumingchen);
                holder.tv_yangpinmingchen = (TextView) convertView.findViewById(R.id.tv_yangpinmingchen);
                holder.tv_hetongdengjihao = (TextView) convertView.findViewById(R.id.tv_hetongdengjihao);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tv_biaoshibianhao.setText(xalTallist.get(position).getSample_BsId());
            if(xalTallist.get(position).getRetStatus()==0){
                holder.tv_yangpinzhuangtai.setText("正常");

            }else if(xalTallist.get(position).getRetStatus()==1){
                holder.tv_yangpinzhuangtai.setText("未登记");
                weidengji=true;
                System.out.println("weidengji里面。。。。。。"+weidengji);
            }
            else if(xalTallist.get(position).getRetStatus()==2){
                holder.tv_yangpinzhuangtai.setText("已确认");
            }
            else if(xalTallist.get(position).getRetStatus()==3){
                holder.tv_yangpinzhuangtai.setText("异常");
                yichang=true;
            }
            else if(xalTallist.get(position).getRetStatus()==4){
                holder.tv_yangpinzhuangtai.setText("缺失");
                qveshi=true;
            }

            //holder.tv_yangpinzhuangtai.setText(xalTallist.get(position).getRetStatus());
            holder.tv_xiangmumingchen.setText(xalTallist.get(position).getItemName());
            holder.tv_yangpinmingchen.setText(xalTallist.get(position).getSampleName());
            holder.tv_hetongdengjihao.setText(xalTallist.get(position).getContract_SignNo());
            if(position==xalTallist.size()-1){

                if(weidengji&&!yichang){
                    tishistr="您提交的样品中存在未登记样品";
                } else if(yichang&&!weidengji){
                    tishistr="您提交的样品中存在异常样品";
                }else if(weidengji&&yichang){
                    tishistr="您提交的样品中存在未登记与异常样品";
                }
                if(qveshi){
                    tishistr= tishistr+"，且部分样品缺失唯一性标识，请确认!";
                }
                MyToast.showShort("共下载"+xalTallist.size()+"条数据。"+tishistr);
            }

            return convertView;
        }
    }
    class ViewHolder{
        TextView tv_biaoshibianhao;
        TextView tv_yangpinzhuangtai;
        TextView tv_xiangmumingchen;
        TextView tv_yangpinmingchen;
        TextView tv_hetongdengjihao;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shenqingshangbao:
                Intent intent1=new Intent();
                intent1.setClass(ShujvjiaohuActivity.this,ShenqingshangbaoActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_shenhexiazai:
                startActivity(new Intent(ShujvjiaohuActivity.this,ShenhexiazaiActivity.class));
                break;

            case R.id.ll_rukuchakan:
                startActivity(new Intent(ShujvjiaohuActivity.this,RukuchakanActivity.class));

                break;

            case R.id.ll_shebeixinxi:
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidstr);
                intent.setClass(ShujvjiaohuActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);

                break;

            case R.id.bt_xiazai:
                initData();
                break;

            case R.id.bt_yaopinqveren:
                Intent intentPut = new Intent(ShujvjiaohuActivity.this, YangpinqverenActivity.class);
                intentPut.putExtra("lstBean",xalTallist);
                System.out.println("数据报告。。。"+talxal);
                intentPut.putExtra("talxal", talxal);
                startActivity(intentPut);
                break;



        }

    }

}
