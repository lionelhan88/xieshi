package com.lessu.xieshi.module.scan.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.module.scan.PrintDataActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Lollipop
 * on 2021/4/2
 */
public class PrintDataNoListAdapter extends BaseAdapter {
    private List<String> noList;

    public PrintDataNoListAdapter(List<String> noList) {
        this.noList = noList;
    }

    public PrintDataNoListAdapter() {
        noList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return noList.size();
    }

    @Override
    public Object getItem(int position) {
        return noList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = View.inflate(parent.getContext(), R.layout.item_scanlistview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(noList.get(position));
        return convertView;
    }
    static class ViewHolder{
        TextView tv;

        public ViewHolder(View itemView) {
           tv = itemView.findViewById(R.id.tv_scanlistview);
        }
    }

    public void addData(String dataNo){
        noList.add(0,dataNo);
        notifyDataSetChanged();
    }

    public void addData(List<String> dataNoList){
        noList.addAll(0,dataNoList);
        notifyDataSetChanged();
    }
    public void remove(int index){
        noList.remove(index);
        notifyDataSetChanged();
    }
    public boolean contains(String dataNo){
        return noList.contains(dataNo);
    }

    public ArrayList<String> getDatas(){
        return (ArrayList<String>) noList;
    }

    public void clearData(){
        noList.clear();
        notifyDataSetChanged();
    }
}
