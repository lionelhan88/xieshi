package com.lessu.xieshi.module.todaystatistics.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.bean.SiteSearchProject;

import java.util.ArrayList;
import java.util.List;


/**
 * created by Lollipop
 * on 2021/2/7
 */
public class SiteSearchListMapAdapter extends BaseAdapter {

    private List<SiteSearchProject.DataBean.ListContentBean> listContent;

    public SiteSearchListMapAdapter() {
        listContent = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return listContent.size();
    }

    @Override
    public Object getItem(int position) {
        return listContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.admin_today_statistics_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ProjectNameTextView.setText(listContent.get(position).getProjectName());
        viewHolder.ProjectStatusTextView.setText(listContent.get(position).getProjectStatus());
        viewHolder.ProjectNatureTextView.setText(listContent.get(position).getProjectNature());
        viewHolder.ProjectRegionTextView.setText(listContent.get(position).getProjectRegion());
        viewHolder.ProjectAddressTextView.setText(listContent.get(position).getProjectAddress());
        return convertView;
    }

    public void clear() {
        this.listContent.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView ProjectNameTextView;
        TextView ProjectStatusTextView;
        TextView ProjectNatureTextView;
        TextView ProjectRegionTextView;
        TextView ProjectAddressTextView;
        public ViewHolder(View convertView) {
            this.ProjectNameTextView = convertView.findViewById(R.id.ProjectNameTextView);
            this.ProjectStatusTextView = convertView.findViewById(R.id.ProjectStatusTextView);
            this.ProjectNatureTextView = convertView.findViewById(R.id.ProjectNatureTextView);
            this.ProjectRegionTextView = convertView.findViewById(R.id.ProjectRegionTextView);
            this.ProjectAddressTextView = convertView.findViewById(R.id.ProjectAddressTextView);
            convertView.setTag(this);
        }
    }

    /**
     * 添加新的数据，刷新列表
     * @param listContent 数据集合
     */
    public void setNewData(List<SiteSearchProject.DataBean.ListContentBean> listContent){
        this.listContent.clear();
        this.listContent.addAll(listContent);
        notifyDataSetChanged();
    }
}
