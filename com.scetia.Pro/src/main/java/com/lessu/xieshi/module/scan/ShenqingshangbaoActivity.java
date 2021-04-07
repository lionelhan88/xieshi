package com.lessu.xieshi.module.scan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.lessu.xieshi.view.DragLayout;
import com.scetia.Pro.common.Util.SPUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class ShenqingshangbaoActivity extends NavigationActivity implements View.OnClickListener {
    private DragLayout dl;
    private SwipeMenuCreator creator;
    private LinearLayout ll_shenqingshangbao;
    private LinearLayout ll_shenhexiazai;
    private LinearLayout ll_rukuchakan;
    private LinearLayout ll_shebeixinxi;
    private SeekBar sb_scan;
    private String uidstr;
    private Button bt_add;
    private Button bt_shanchu;
    private Button bt_xinjian;
    private EditText et_shangbao;
    private ListView lv_left;
    private ListView lv_right;
    private ArrayList<String> alleft = new ArrayList<>();
    private ArrayList<String> alright= new ArrayList<>();
    private String s;
    private String s1;
    private boolean left=true;
    private LeftAdapter madapterleft;
    private RightAdapter madapterright;
    private TextView tv_biaoshinum;
    private LinearLayout ll_shenqin;
    private ArrayList alshanchu=new ArrayList();
    private ArrayList<String> temp;
    private String shouwei="";
    private String sampleid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shenqingshangbao;
    }

    @Override
    protected void initView() {
        this.setTitle("申请上报");
        //设置侧滑菜单
        dl = (DragLayout) findViewById(R.id.dl);
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

        ll_shenqin = (LinearLayout) findViewById(R.id.ll_shenqin);
        tv_biaoshinum = (TextView) findViewById(R.id.tv_biaoshinum);
        bt_add = (Button) findViewById(R.id.bt_add);
        bt_shanchu = (Button) findViewById(R.id.bt_shanchu);
        bt_xinjian = (Button) findViewById(R.id.bt_xinjian);
        et_shangbao = (EditText) findViewById(R.id.et_shangbao);
        lv_left = (ListView) findViewById(R.id.lv_left);
        lv_right = (ListView) findViewById(R.id.lv_right);


        ll_shenqingshangbao = (LinearLayout) findViewById(R.id.ll_shenqingshangbao);
        ll_shenhexiazai = (LinearLayout) findViewById(R.id.ll_shenhexiazai);
        ll_rukuchakan = (LinearLayout) findViewById(R.id.ll_rukuchakan);
        ll_shebeixinxi = (LinearLayout) findViewById(R.id.ll_shebeixinxi);
        sb_scan = (SeekBar) findViewById(R.id.sb_scan);
        ll_shenqingshangbao.setOnClickListener(this);
        ll_shenhexiazai.setOnClickListener(this);
        ll_rukuchakan.setOnClickListener(this);
        ll_shebeixinxi.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bt_shanchu.setOnClickListener(this);
        bt_xinjian.setOnClickListener(this);
        ll_shenqin.setOnClickListener(this);
        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!alshanchu.contains(alleft.get(position))) {
                    alshanchu.add(alleft.get(position));
                    view.setBackgroundColor(Color.parseColor("#ecfafd"));
                }else{
                    alshanchu.remove(alleft.get(position));
                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                }

            }
        });
        lv_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!alshanchu.contains(alright.get(position))) {
                    alshanchu.add(alright.get(position));
                    view.setBackgroundColor(Color.parseColor("#ecfafd"));
                }else{
                    alshanchu.remove(alright.get(position));
                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                }

            }
        });
    }

    @Override
    protected void initData() {
        Intent intentGet = getIntent();
        String talxal = intentGet.getStringExtra("talxal");
        sampleid = intentGet.getStringExtra("sampleid");
        if(sampleid==null){
            return;
        }else if(sampleid.contains("~")){
            String[] split = sampleid.split("~");
            s = split[0];
            s1 = split[1];
            handleTalXal(talxal);
        }else{
            alleft.add(sampleid);
        }
        refreshData();
    }

    /**
     * 将数据添加到列表中
     * @param codeStr
     */
    private void handleTalXal(String codeStr) {
        if(codeStr!=null){
            if(!codeStr.contains(";")){
                alleft.add(codeStr);
                return;
            }
            String[] numbers = codeStr.split(";");
            for(String number:numbers){
                if (Integer.parseInt(number) >= Integer.parseInt(s) && Integer.parseInt(number) <= Integer.parseInt(s1)) {
                    if (left) {
                        alleft.add(number);
                        left = false;
                    } else {
                        alright.add(number);
                        left = true;
                    }
                }
            }
        }
    }

    private void refreshData() {
        if (madapterleft != null) {
            madapterleft.notifyDataSetChanged();
        } else {
            madapterleft = new LeftAdapter();
            lv_left.setAdapter(madapterleft);
        }

        if (madapterright != null) {
            madapterright.notifyDataSetChanged();
        } else {
            madapterright = new RightAdapter();
            lv_right.setAdapter(madapterright);
        }

        tv_biaoshinum.setText(alleft.size() + alright.size() + "");
    }

    class LeftAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return alleft.size();
        }

        @Override
        public Object getItem(int position) {
            return alleft.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(ShenqingshangbaoActivity.this, R.layout.item_shenqin, null);
            }
            TextView tv_shenqinitem = (TextView) convertView.findViewById(R.id.tv_shenqinitem);
            tv_shenqinitem.setText(alleft.get(position));
            return convertView;
        }
    }
    class RightAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return alright.size();
        }

        @Override
        public Object getItem(int position) {
            return alright.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(ShenqingshangbaoActivity.this, R.layout.item_shenqin, null);
            }
            TextView tv_shenqinitem = (TextView) convertView.findViewById(R.id.tv_shenqinitem);
            tv_shenqinitem.setText(alright.get(position));
            return convertView;
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
                break;

            case R.id.ll_shenhexiazai:
                startActivity(new Intent(ShenqingshangbaoActivity.this, ReviewDownloadActivity.class));
                finish();
                break;

            case R.id.ll_rukuchakan:
                startActivity(new Intent(ShenqingshangbaoActivity.this,RukuchakanActivity.class));
                finish();
                break;

            case R.id.ll_shebeixinxi:
                Intent intent = new Intent();
                intent.putExtra("uidstr", uidstr);
                intent.setClass(ShenqingshangbaoActivity.this, ShebeixinxiActivity.class);
                startActivity(intent);
                break;

            case R.id.bt_add:
                String s = et_shangbao.getText().toString();
                left = alleft.size()<=alright.size();
                if(!alleft.contains(s)&&!alright.contains(s)&&s.length()==10){
                    if(left){
                        alleft.add(s);
                        left=false;
                    }else{
                        alright.add(s);
                        left=true;
                    }
                }
                refreshData();
                break;
            case R.id.bt_shanchu:
                temp = new ArrayList();
                if(alshanchu.size()==0){
                    return;
                }
                for (int i = 0; i <alshanchu.size(); i++) {

                    if(alleft.contains(alshanchu.get(i))) {
                        alleft.remove(alshanchu.get(i));
                    }else {
                        alright.remove(alshanchu.get(i));
                    }
                }
                if(alleft.size()>alright.size()){
                    int chaleft = alleft.size() - alright.size();
                    for (int i = 0; i < chaleft; i++) {
                        temp.add(alleft.get(i));
                    }
                    for (int i = 0; i < chaleft; i++) {
                        alleft.remove(temp.get(i));
                    }
                    left=true;
                    for (int i = 0; i < chaleft; i++) {
                        if(left){
                            alleft.add(temp.get(i));
                            left=false;
                        }else{
                            alright.add(temp.get(i));
                            left=true;
                        }
                    }
                    temp.clear();
                }else if(alleft.size()<alright.size()){
                    int chaleft = alright.size() - alleft.size();
                    for (int i = 0; i < chaleft; i++) {
                        temp.add(alright.get(i));
                    }
                    for (int i = 0; i < chaleft; i++) {
                        alright.remove(temp.get(i));
                    }
                    left=true;
                    for (int i = 0; i < chaleft; i++) {
                        if(left){
                            alleft.add(temp.get(i));
                            left=false;
                        }else{
                            alright.add(temp.get(i));
                            left=true;
                        }
                    }
                    temp.clear();
                }else{
                    left=true;
                    temp.clear();
                }
                refreshData();
                alshanchu.clear();

                break;
            case R.id.bt_xinjian:
                left=true;
                alleft.clear();
                alright.clear();
                refreshData();
                break;
            case R.id.ll_shenqin:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String ss="";
                        for (int i = 0; i <alleft.size() ; i++) {
                            ss=ss+alleft.get(i)+";";
                        }
                        for (int i = 0; i <alright.size() ; i++) {
                            ss=ss+alright.get(i)+";";
                        }
                        String nameSpace1 = "http://tempuri.org/";
                        String methodName1 = "CheckSamples";
                        String endPoint1 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                        String soapAction1= "http://tempuri.org/CheckSamples";
                        SoapObject soapObject1 = new SoapObject(nameSpace1, methodName1);
                        soapObject1.addProperty("coreCodeStr", ss);
                        soapObject1.addProperty("membercode", AppApplication.muidstr);
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
                        SoapObject object1 = (SoapObject) envelope1.bodyIn;
                        int propertyCount = object1.getPropertyCount();
                        if(propertyCount==1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showShort("服务器未返回任何数据");
                                }
                            });
                        }else{
                            SoapObject property = (SoapObject) object1.getProperty(0);
                            int num=property.getPropertyCount();
                            if(num==1){
                                SoapObject property1 = (SoapObject) property.getProperty(0);
                                String retStatus = property1.getProperty("RetStatus").toString();
                                if(retStatus.equals("1")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showShort("您上报的样品中含有未登记的标识，上报终止!");
                                        }
                                    });
                                }else if(retStatus.equals("2")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showShort("您上报的样品已确认，上报终止!");
                                        }
                                    });

                                }else if(retStatus.equals("3")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showShort("您上报的样品异常，上报终止!");
                                        }
                                    });

                                }else if(retStatus.equals("4")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showShort("您上报的样品缺" +"" + "件"+"，上报终止!");
                                        }
                                    });
                                }else if(retStatus.equals("0")){
                                    String nameSpace2 = "http://tempuri.org/";
                                    String methodName2 = "SetReportUp";
                                    String endPoint2 = "http://www.scetia.com/Scetia.SampleManage.WS/SampleManagement.asmx";
                                    String soapAction2= "http://tempuri.org/SetReportUp";
                                    SoapObject soapObject2 = new SoapObject(nameSpace2, methodName2);
                                    if(sampleid!=null){
                                        soapObject2.addProperty("ccStr", sampleid);
                                    }else{
                                        String s = null;
                                        System.out.println("sss"+ss);
                                        if(ss.contains(";")){
                                            String[] srr = ss.split(";");
                                            if(srr.length==1){
                                                s=srr[0];
                                            }else{
                                                for (int i=0;i<srr.length-1;i++) {
                                                    for (int j = 0; j < srr.length - i - 1; j++) {
                                                        if (Integer.parseInt(srr[j]) > Integer.parseInt(srr[j + 1])) {
                                                            String temp;
                                                            temp = srr[j];
                                                            srr[j] = srr[j + 1];
                                                            srr[j + 1] = temp;
                                                        }
                                                    }
                                                }
                                                for (int i = 0; i < srr.length; i++) {
                                                    s=srr[0]+"~"+srr[srr.length-1]+";";
                                                }
                                            }
                                            System.out.println("s........"+s);
                                            soapObject2.addProperty("ccStr", s);
                                        }

                                    }
                                    System.out.println("huiyuanhao"+ SPUtil.getSPConfig("huiyuanhao",""));
                                    if(SPUtil.getSPConfig("huiyuanhao","")!=null) {
                                        soapObject2.addProperty("membercode", SPUtil.getSPConfig("huiyuanhao", ""));
                                    }
                                    SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(
                                            SoapEnvelope.VER10);
                                    envelope2.bodyOut = soapObject2;
                                    envelope2.dotNet = true;
                                    envelope2.setOutputSoapObject(soapObject2);
                                    HttpTransportSE transport2 = new HttpTransportSE(endPoint2);
                                    try {
                                        transport1.call(soapAction2, envelope2);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    SoapObject object2 = (SoapObject) envelope2.bodyIn;
                                    System.out.println("object2...."+object2);
                                    final Object message = object2.getProperty("message");
                                    System.out.println("message....."+message.toString());
                                    if(message!=null&&!message.toString().equals("anyType{}")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showShort(message.toString());
                                            }
                                        });

                                    }else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showShort("上报成功");
                                            }
                                        });

                                    }
                                }

                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showShort("您上报了多条样品的标识数据，\n请检查后再试");
                                    }
                                });

                            }
                        }
                        System.out.println(object1.toString());
                    }
                }).start();

                break;
        }
    }
}
