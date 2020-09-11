package com.lessu.xieshi.scan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.scan.PrintDataActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * created by ljs
 * on 2020/9/2
 */
public class PrintDataFragment extends BaseFragment {
    @BindView(R.id.tv_baocun)
    TextView tvBaocun;
    @BindView(R.id.tv_duqv)
    TextView tvDuqv;
    @BindView(R.id.tv_qingchu)
    TextView tvQingchu;
    @BindView(R.id.tv_shujvjiaohu)
    TextView tvShujvjiaohu;
    @BindView(R.id.tv_tiaoma_num)
    TextView tvTiaomaNum;
    @BindView(R.id.tv_xinpian_num)
    TextView tvXinpianNum;
    @BindView(R.id.lv_tiaoma)
    SwipeMenuListView lvTiaoma;
    @BindView(R.id.lv_xinpian)
    SwipeMenuListView lvXinpian;
    private SwipeMenuCreator creator;
    private ArrayList<String> Tal = new ArrayList<>();
    private ArrayList<String> Xal = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_print_data;
    }

    @Override
    protected void initView() {
        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setWidth((130));
                deleteItem.setIcon(R.drawable.shanchu);
                menu.addMenuItem(deleteItem);
            }
        };

        lvTiaoma.setMenuCreator(creator);
        lvXinpian.setMenuCreator(creator);
        lvTiaoma.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Tal.remove(position);
                        //madaptertiaoma.notifyDataSetChanged();
                        //tv_tiaoma_num.setText(Tal.size() + "");
                        break;
                }
                return false;
            }
        });
        lvXinpian.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        Xal.remove(position);
                        //madapterxinpian.notifyDataSetChanged();
                        //tv_xinpian_num.setText(Xal.size() + "");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initImmersionBar() {

    }

    @OnClick({R.id.tv_baocun, R.id.tv_duqv, R.id.tv_qingchu, R.id.tv_shujvjiaohu, R.id.tv_tiaoma_num, R.id.tv_xinpian_num, R.id.lv_tiaoma, R.id.lv_xinpian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_baocun:
                break;
            case R.id.tv_duqv:
                break;
            case R.id.tv_qingchu:
                break;
            case R.id.tv_shujvjiaohu:
                break;
            case R.id.tv_tiaoma_num:
                break;
            case R.id.tv_xinpian_num:
                break;
            case R.id.lv_tiaoma:
                break;
            case R.id.lv_xinpian:
                break;
        }
    }
}
