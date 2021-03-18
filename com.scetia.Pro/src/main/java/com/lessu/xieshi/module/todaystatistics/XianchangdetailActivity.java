package com.lessu.xieshi.module.todaystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lessu.navigation.BarButtonItem;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.lessu.xieshi.bean.XianchangshikuaibBean;

import java.util.ArrayList;

public class XianchangdetailActivity extends XieShiSlidingMenuActivity {

    private ListView lv_xianchangdetail;
    private ArrayList<XianchangshikuaibBean.DataBean.SampleInProjectDetailListBean> newdetaillist;
    private xcdetailAdapter madapter;

 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xianchangdetail);
        this.setTitle("样品信息");
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
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xianchangdetail;
    }

    @Override
    protected void initView() {
        this.setTitle("样品信息");
        BarButtonItem backbutton = new BarButtonItem(this , R.drawable.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(backbutton);
        lv_xianchangdetail = (ListView) findViewById(R.id.lv_xianchangdetail);
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        newdetaillist = (ArrayList<XianchangshikuaibBean.DataBean.SampleInProjectDetailListBean>) intent.getSerializableExtra("newdetaillist");
        if (newdetaillist != null) {
            madapter = new xcdetailAdapter();
            lv_xianchangdetail.setAdapter(madapter);
        }


    }

    class xcdetailAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return newdetaillist.size();
        }

        @Override
        public Object getItem(int i) {
            return newdetaillist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder  holder;
            if(view==null){
                holder=new ViewHolder();
                view= View.inflate(XianchangdetailActivity.this,R.layout.item_xianchangdetail,null);
                holder.tv_dengjiriqi= (TextView) view.findViewById(R.id.tv_dengjiriqi);
                holder.tv_zhizuoriqi= (TextView) view.findViewById(R.id.tv_zhizuoriqi);
                holder.tv_grade= (TextView) view.findViewById(R.id.tv_grade);
                holder.tv_xinpiannum= (TextView) view.findViewById(R.id.tv_xinpiannum);
                holder.tv_yanghutiaojian= (TextView) view.findViewById(R.id.tv_yanghutiaojian);
                holder.tv_shengchandanwei= (TextView) view.findViewById(R.id.tv_shengchandanwei);
                view.setTag(holder);
            }else{
                holder= (ViewHolder) view.getTag();
            }
            holder.tv_dengjiriqi.setText(newdetaillist.get(i).getCreateDateTime());
            holder.tv_zhizuoriqi.setText(newdetaillist.get(i).getMolding_Date());
            holder.tv_grade.setText(newdetaillist.get(i).getGradeName());
            holder.tv_xinpiannum.setText(newdetaillist.get(i).getCoreCodeNo());
            holder.tv_yanghutiaojian.setText(newdetaillist.get(i).getYangHuTiaoJian());
            holder.tv_shengchandanwei.setText(newdetaillist.get(i).getShenChanChangJia());
            return view;
        }
    }
    class ViewHolder{
        TextView tv_dengjiriqi,tv_zhizuoriqi,tv_grade,tv_xinpiannum;
        TextView tv_yanghutiaojian,tv_shengchandanwei;

    }
}
