package com.lessu.xieshi.module.meet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.module.meet.bean.OtherMeetingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ljs
 * on 2020/8/25
 */
public class OtherMeetingUserListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<OtherMeetingBean> beans;
    private List<OtherMeetingBean> searchDataSource = new ArrayList<>();
    private MyFilter myFilter;
    private SearchResultListener searchResultListener;
    private TouchConvertViewListener touchConvertViewListener;
    public OtherMeetingUserListAdapter(Context context, List<OtherMeetingBean> beans) {
        this.context = context;
        this.beans = beans;
        this.searchDataSource.addAll( beans);
        myFilter = new MyFilter();
    }

    public interface SearchResultListener{
        void searchResult(List<OtherMeetingBean> searchResultData);
    }
    public interface TouchConvertViewListener{
        void onTouchConvertView(View v, MotionEvent event);
    }

    public void setSearchResultListener(SearchResultListener searchResultListener) {
        this.searchResultListener = searchResultListener;
    }

    public void setTouchConvertViewListener(TouchConvertViewListener touchConvertViewListener) {
        this.touchConvertViewListener = touchConvertViewListener;
    }

    public List<OtherMeetingBean> getBeans() {
        return beans;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.meeting_other_user_list_item_layout,null,false);
            viewHolder = new ViewHolder();
            viewHolder.userName = convertView.findViewById(R.id.other_meeting_user_item_name);
            viewHolder.userTel = convertView.findViewById(R.id.other_meeting_user_item_tel);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchConvertViewListener!=null){
                    touchConvertViewListener.onTouchConvertView(v,event);
                }
                return false;
            }
        });
        viewHolder.userName.setText(beans.get(position).getUserName());
        viewHolder.userTel.setText(beans.get(position).getUserTel());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }


    private static class  ViewHolder{
        TextView userName;
        TextView userTel;
    }

    private class MyFilter extends Filter{
        /**
         * 在后台线程执行，定义过滤算法
         * @param constraint ：就是你在输入框输入的字符串
         * @return 符合条件的数据结果，会在下面的publishResults方法中将数据传给list
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = searchDataSource;
                results.count = searchDataSource.size();
            }else{
                //这个newList是实际搜索出的结果集合，实际上是将该newList的数据赋给了list
                List<OtherMeetingBean> newList = new ArrayList<>();
                for (OtherMeetingBean s : searchDataSource) {
                    //包含就添加到newList中
                    if (s.getUserName().contains(constraint.toString().trim())) {
                        newList.add(s);
                    }
                }

                //将newList传给results
                results.values = newList;
                results.count = newList.size();
                newList = null;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null) {//有符合过滤规则的数据
                beans.clear();
                beans.addAll((List<OtherMeetingBean>) results.values);
                notifyDataSetChanged();
            } else {//没有符合过滤规则的数据
                notifyDataSetInvalidated();
            }
            if(searchResultListener!=null) {
                searchResultListener.searchResult(beans);
            }

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((OtherMeetingBean)resultValue).getUserName();
        }
    }
}
