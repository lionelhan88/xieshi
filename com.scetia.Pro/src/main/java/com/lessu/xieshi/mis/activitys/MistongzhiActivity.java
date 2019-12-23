package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.mis.bean.Tongzhibean;
import com.lessu.xieshi.mis.contracts.IMistongzhiContract;
import com.lessu.xieshi.mis.presenters.MisTongzhiPresenter;

import java.util.List;

public class MistongzhiActivity extends NavigationActivity implements IMistongzhiContract.View, AdapterView.OnItemClickListener {

    private BarButtonItem handleButtonItem;
    private IMistongzhiContract.Presenter presenter;
    private ListView lv_tongzi;
    private MyAdapter madapter;
    private List<Tongzhibean.DataBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mistongzhi);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("信息通知");
        handleButtonItem = new BarButtonItem(this , R.drawable.back );
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        initView();
        initData();
    }

    private void initView() {
        lv_tongzi = (ListView) findViewById(R.id.lv_tongzi);
        lv_tongzi.setOnItemClickListener(this);
    }

    private void initData() {
        presenter=new MisTongzhiPresenter(this,this);
        String token = Content.gettoken();
        presenter.getTongzhi(token);
    }

    @Override
    public void getTongzhiCall(boolean issuccess, Tongzhibean tongzhibean) {
        if(issuccess){
            list = tongzhibean.getData();
            if(madapter==null){
                madapter=new MyAdapter();
                lv_tongzi.setAdapter(madapter);
            }else{
                madapter.notifyDataSetChanged();
            }
        }else{
            Toast.makeText(MistongzhiActivity.this,"未能获取到通知",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(IMistongzhiContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ImageView iv= (ImageView) view.findViewById(R.id.iv_itemtz);
        iv.setBackgroundResource(R.drawable.xiaoxi);
        String nr = list.get(i).getNR();
        Intent intent=new Intent(this,TzdetailActivity.class);
        intent.putExtra("tongzhi",nr);
        startActivity(intent);
    }


    public class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int i) {
            return list.get(i);
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
                view= View.inflate(MistongzhiActivity.this, R.layout.item_mistongzhi, null);
                holder.iv = (ImageView) view.findViewById(R.id.iv_itemtz);
                holder.bt = (TextView) view.findViewById(R.id.tv_itemtz_bt);
                holder.sj = (TextView) view.findViewById(R.id.tv_itemtz_sj);
                view.setTag(holder);
            }else{
                holder= (ViewHolder) view.getTag();
            }
            holder.bt.setText(list.get(i).getBT());
            holder.sj.setText(list.get(i).getSJ());
            return view;
        }
    }
    static class ViewHolder{
        ImageView iv;
        TextView bt;
        TextView sj;
    }

}
