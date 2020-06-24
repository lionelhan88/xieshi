package com.lessu.xieshi.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fhm on 2020/1/19.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(),container,false);
        initView(view);
        initData();
        return view;
    }

    public abstract int layoutId();
    public abstract void initView(View view);
    public abstract void initData();
}
