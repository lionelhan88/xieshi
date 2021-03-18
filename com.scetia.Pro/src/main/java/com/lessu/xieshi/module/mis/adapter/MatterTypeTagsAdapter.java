package com.lessu.xieshi.module.mis.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lessu.xieshi.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * created by Lollipop
 * on 2021/3/16
 */
public class MatterTypeTagsAdapter  extends TagAdapter<String> {
    public MatterTypeTagsAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seal_manage_detail_type_tag_layout, parent, false);
        TextView tvTag = view.findViewById(R.id.seal_detail_type_tag);
        tvTag.setText(s);
        return view;
    }
}
