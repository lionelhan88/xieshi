package com.lessu.xieshi.todaystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.MyToast;
import com.lessu.xieshi.XieShiSlidingMenuActivity;
import com.lessu.xieshi.bean.XianchangshikuaibBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XianchangActivity extends XieShiSlidingMenuActivity {

    private ListView lv_xianchang;
    private XianchangAdapter madapter;
    private List<XianchangshikuaibBean.DataBean.SampleInProjectSummBean> sampleInProjectSumm;
    private List<XianchangshikuaibBean.DataBean.SampleInProjectDetailListBean> sampleInProjectDetailList;
    private ArrayList<XianchangshikuaibBean.DataBean.SampleInProjectDetailListBean> newdetaillist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xianchang);
        this.setTitle("现场试块信息");
        navigationBar.setBackgroundColor(0xFF3598DC);
        BarButtonItem backbutton = new BarButtonItem(this , R.drawable.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(backbutton);
        initView();

        initData();

    }

    private void initView() {
        lv_xianchang = (ListView) findViewById(R.id.lv_xianchang);
        lv_xianchang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                newdetaillist = new ArrayList();
                for (int j = 0; j < sampleInProjectDetailList.size(); j++) {
                    if (sampleInProjectDetailList.get(j).getItemName().equals(sampleInProjectSumm.get(i).getSummItemName())) {
                       newdetaillist.add(sampleInProjectDetailList.get(j));
                    }
                }

                Intent intent=new Intent(XianchangActivity.this,XianchangdetailActivity.class);
                intent.putExtra("newdetaillist", (Serializable) newdetaillist);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        Intent getintent=getIntent();
        String projectid = getintent.getStringExtra("projectid");
        String  token = LSUtil.valueStatic("Token");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Type", 1);
        params.put("ProjectId", projectid);
        params.put("Token", token);
        System.out.println(params);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/GetSampleInProject"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                System.out.println(result);
                XianchangshikuaibBean xianchangbean = GsonUtil.JsonToObject(result.toString(), XianchangshikuaibBean.class);
                int code = xianchangbean.getCode();
                if(code==1000){
                    XianchangshikuaibBean.DataBean data = xianchangbean.getData();
                    sampleInProjectSumm = data.getSampleInProjectSumm();//总列表
                    sampleInProjectDetailList = data.getSampleInProjectDetailList();//具体列表
                        madapter=new XianchangAdapter();
                        lv_xianchang.setAdapter(madapter);

                }
            }

            @Override
            public String onFailed(ApiError error) {
                MyToast.showShort(error.errorMeesage);
                return null;
            }
        });
    }


    class XianchangAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return sampleInProjectSumm.size();
        }

        @Override
        public Object getItem(int i) {
            return sampleInProjectSumm.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view==null){
                holder=new ViewHolder();
                view=View.inflate(XianchangActivity.this,R.layout.item_xianchang,null);
                holder.tv_xiangmu= (TextView) view.findViewById(R.id.tv_xiangmu);
                //holder.tv_zhuangtai= (TextView) view.findViewById(R.id.tv_zhuangtai);
                holder.tv_sum= (TextView) view.findViewById(R.id.tv_sum);
                view.setTag(holder);
            }else{
                holder= (ViewHolder) view.getTag();
            }
            holder.tv_xiangmu.setText(sampleInProjectSumm.get(i).getSummItemName());
           // holder.tv_zhuangtai.setText("未送样");
            holder.tv_sum.setText(sampleInProjectSumm.get(i).getSummSampleCount()+"");
            return view;
        }
    }
    static class ViewHolder{
        TextView tv_xiangmu,tv_zhuangtai,tv_sum;
    }
}

