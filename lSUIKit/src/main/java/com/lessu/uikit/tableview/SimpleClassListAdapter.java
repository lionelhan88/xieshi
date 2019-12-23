package com.lessu.uikit.tableview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.ParameterizedType;

/**
 * Created by lessu on 14-7-5.
 */
public abstract class SimpleClassListAdapter<T extends View> extends BaseAdapter{
//    private final Context context;
//    private Class<T> entityClass;

    protected SimpleClassListAdapter() {
//        this.entityClass =(Class<T>) ((ParameterizedType) getClass() .getGenericSuperclass()) .getActualTypeArguments()[0];;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view!=null){
            setData(i,(T) view);
            return view;
        }
        T retView = newInstance(i);
        setData(i,retView);
        return retView;
    }
    public abstract T newInstance(int position);
    public abstract void setData(int position,T cell);
}
