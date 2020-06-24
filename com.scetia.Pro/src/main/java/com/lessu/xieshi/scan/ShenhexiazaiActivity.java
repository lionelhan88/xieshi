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

public class ShenhexiazaiActivity  extends NavigationActivity implements View.OnClickListener{

    private DragLayout dl;
    private SwipeMenuCreator creator;
    private LinearLayout ll_shenqingshangbao;
    private LinearLayout ll_shenhexiazai;
    private LinearLayout ll_rukuchakan;
    private LinearLayout ll_shebeixinxi;
    private SeekBar sb_scan;
    private String uidstr;
    private Button bt_shenhexiazai;
    private Button bt_shenheyaopinqveren;
    private String sss="";
    private ArrayList<XalTal> alll = new ArrayList<XalTal>();;
    private ImageView iv_shenheloding;
    private ListView lv_shenheshujvjiaohu;
    private MyAdapter myAdapter;
    private boolean weidengji;
    private boolean yichang;
    private boolean qveshi;
    private String talxal;
    private String tishistr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenhexiazai);
        navigationBar.setBackgroundColor(0xFF3598DC);
        System.out.println("走到这个activity......");
        this.setTitle("审核下载");

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
        iv_shenheloding = (ImageView) findViewById(R.id.iv_shenheloding);
        lv_shenheshujvjiaohu = (ListView) findViewById(R.id.lv_shenheshujvjiaohu);
        bt_shenhexiazai = (Button) findViewById(R.id.bt_shenhexiazai);
        bt_shenheyaopinqveren = (Button) findViewById(R.id.bt_shenheyaopinqveren);


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
        bt_shenhexiazai.setOnClickListener(this);
        bt_shenheyaopinqveren.setOnClickListener(this);

        lv_shenheshujvjiaohu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position./....."+position);
                Intent intentPut = new Intent(ShenhexiazaiActivity.this, YangpinqverenActivity.class);
                intentPut.putExtra("current",position+"");
                intentPut.putExtra("lstBean", (Serializable)alll);
                intentPut.putExtra("shenhe",true);
                startActivity(intentPut);
            }
        });
    }

    private void initData() {
        sss="";
        Intent intent=getIntent();
        uidstr = intent.getStringExtra("uidstr");
        if(uidstr==null|| uidstr.equals("")){
            //有可能其他页面没有传入这个uid，所以获取全局的uid
            uidstr = AppApplication.muidstr;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String nameSpace1 = "http://tempuri.org/";
                String methodName1 = "GetReportUpState ";
                String endPoint1 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                String soapAction1= "http://tempuri.org/GetReportUpState ";
                SoapObject soapObject1 = new SoapObject(nameSpace1, methodName1);
                soapObject1.addProperty("membercode", Shref.getString(ShenhexiazaiActivity.this,"huiyuanhao",""));
                //soapObject1.addProperty("membercode", "0000");
                SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                        SoapEnvelope.VER10);
                envelope1.bodyOut = soapObject1;
                envelope1.dotNet = true;
                envelope1.setOutputSoapObject(soapObject1);
                HttpTransportSE transport1 = new HttpTransportSE(endPoint1);
                try {
                    transport1.call(soapAction1, envelope1);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                SoapObject object1 = (SoapObject) envelope1.bodyIn;
                String s2 = object1.toString();
               /* String s2 ="GetReportUpStateResult=1010000331~1010000336;" +
                        "1050000175~1050000177;1150080333~1150080347;1150080318~1150080332;" +
                        "1010000359~1010000361;1150080318~1150080332;1050000178;1050000207;" +
                        "1010000356~1010000358;; message=anyType{};";*/
                System.out.println(s2);
                if(s2.contains("GetReportUpStateResult=")){
                    String result = s2.substring(s2.indexOf("=")+1, s2.indexOf("message"));
                    System.out.println("result....."+result);
                    if(result.contains("anyType{}")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showShort("当前无要审核的信息。");
                            }
                        });
                    }else {
                        result = result.substring(0,result.lastIndexOf(";"));
                        String[] numberTeam = result.split(";");
                        for (int j=0;j<numberTeam.length;j++) {
                            //sss = "";
                            String sTeam = numberTeam[j];
                            if (sTeam.contains("~")) {
                                String[] split = sTeam.split("~");
                                String s = split[0];
                                String s1 = split[1];
                                int start = Integer.parseInt(s);
                                int end = Integer.parseInt(s1);
                                for (int i = start; i < end + 1; i++) {
                                    sss = sss + i + ";";
                                }
                            } else {
                                sss = sss+sTeam + ";";
                            }
                        }
                            String nameSpace2 = "http://tempuri.org/";
                            String methodName2 = "CheckSamples";
                            String endPoint2 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                            String soapAction2 = "http://tempuri.org/CheckSamples";
                            SoapObject soapObject2 = new SoapObject(nameSpace2, methodName2);
                            soapObject2.addProperty("coreCodeStr", sss);
                            soapObject2.addProperty("membercode", uidstr);
                            //soapObject2.addProperty("membercode", "35ffd0054843353230782243");
                            SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(
                                    SoapEnvelope.VER10);
                            envelope2.bodyOut = soapObject2;
                            envelope2.dotNet = true;
                            envelope2.setOutputSoapObject(soapObject2);
                            HttpTransportSE transport2 = new HttpTransportSE(endPoint2);
                            try {
                                transport2.call(soapAction2, envelope2);
                            } catch (IOException | XmlPullParserException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        SoapObject object2 = (SoapObject) envelope2.bodyIn;
                            System.out.println(object2.toString());
                            if (object2.getPropertyCount() >= 1) {
                                SoapObject shujvsoap = (SoapObject) object2.getProperty(0);
                                System.out.println("书vjiaohu..........." + shujvsoap.toString());
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
                                    alll.add(info);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        iv_shenheloding.setVisibility(View.GONE);
                                        lv_shenheshujvjiaohu.setVisibility(View.VISIBLE);
                                        if (myAdapter != null) {
                                            myAdapter.notifyDataSetChanged();
                                        } else {
                                            myAdapter = new MyAdapter();
                                        }
                                        lv_shenheshujvjiaohu.setAdapter(myAdapter);
                                    }
                                });
                            } else {
                            }

                    }
                }
            }
        }).start();





    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return alll.size();
        }

        @Override
        public Object getItem(int position) {
            return alll.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                convertView=View.inflate(ShenhexiazaiActivity.this,R.layout.item_shujvjiaohu,null);
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
            holder.tv_biaoshibianhao.setText(alll.get(position).getSample_BsId());
            if(alll.get(position).getRetStatus()==0){
                holder.tv_yangpinzhuangtai.setText("正常");

            }else if(alll.get(position).getRetStatus()==1){
                holder.tv_yangpinzhuangtai.setText("未登记");
                weidengji=true;
                System.out.println("weidengji里面。。。。。。"+weidengji);
            }
            else if(alll.get(position).getRetStatus()==2){
                holder.tv_yangpinzhuangtai.setText("已确认");
            }
            else if(alll.get(position).getRetStatus()==3){
                holder.tv_yangpinzhuangtai.setText("异常");
                yichang=true;
            }
            else if(alll.get(position).getRetStatus()==4){
                holder.tv_yangpinzhuangtai.setText("缺失");
                qveshi=true;
            }

            //holder.tv_yangpinzhuangtai.setText(xalTallist.get(position).getRetStatus());
            holder.tv_xiangmumingchen.setText(alll.get(position).getItemName());
            holder.tv_yangpinmingchen.setText(alll.get(position).getSampleName());
            holder.tv_hetongdengjihao.setText(alll.get(position).getContract_SignNo());
            if(position==alll.size()-1){

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
                MyToast.showShort("共下载"+alll.size()+"条数据。"+tishistr);
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
                intent1.setClass(ShenhexiazaiActivity.this,ShenqingshangbaoActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.ll_shenhexiazai:
                break;

            case R.id.ll_rukuchakan:
                startActivity(new Intent(ShenhexiazaiActivity.this,RukuchakanActivity.class));
                finish();
                break;

            case R.id.ll_shebeixinxi:
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidstr);
                intent.setClass(ShenhexiazaiActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_shenhexiazai:

                initData();
                break;
            case R.id.bt_shenheyaopinqveren:
                Intent intentPut = new Intent(ShenhexiazaiActivity.this, YangpinqverenActivity.class);
                intentPut.putExtra("lstBean",alll);
                System.out.println("数据报告。。。"+talxal);
                intentPut.putExtra("shenhe",true);
                //intentPut.putExtra("talxal", talxal);
                startActivity(intentPut);

                break;
        }
    }
}
