package com.lessu.xieshi.module.meet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.module.meet.bean.MeetingCompanyBean;

import java.util.List;

public class MeetingCompanyListAdapter extends BaseAdapter {
    private Context context;
    private List<MeetingCompanyBean> meetingCompanyBeans;

    public MeetingCompanyListAdapter(Context context, List<MeetingCompanyBean> meetingCompanyBeans) {
        this.context = context;
        this.meetingCompanyBeans = meetingCompanyBeans;
    }

    @Override
    public int getCount() {
        return meetingCompanyBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return meetingCompanyBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.meeting_company_list_item_layout,null,false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.companyName = convertView.findViewById(R.id.meeting_company_list_item_name);
            viewHolder.companyCode = convertView.findViewById(R.id.meeting_company_list_item_code);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.companyName.setText(meetingCompanyBeans.get(position).getMemberName());
        viewHolder.companyCode.setText(meetingCompanyBeans.get(position).getMemberCode());
        return convertView;
    }
    class ViewHolder{
        private TextView companyName;
        private TextView companyCode;
        public ViewHolder(View view) {

        }
    }
}
