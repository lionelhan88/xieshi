package com.lessu.xieshi.module.sand.fragment;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lessu.BaseFragment;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.SandSalesManageListAdapter;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;
import com.lessu.xieshi.module.sand.viewmodel.MaintainSalesViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/5
 */
public class SandInfoMaintainSalesFragment extends BaseFragment {
    @BindView(R.id.sand_common_rv)
    RecyclerView sandInfoMaintainSalesRv;
    @BindView(R.id.sand_common_fab)
    FloatingActionButton sandInfoMaintainSalesFab;
    private SandSalesManageListAdapter listAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_common_layout;
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.sand_manage_info_sales_manage_text));

        listAdapter = new SandSalesManageListAdapter();
        sandInfoMaintainSalesRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sandInfoMaintainSalesRv.setAdapter(listAdapter);
    }

    @Override
    protected void initData() {
        for (int i = 0; i <= 10; i++) {
            SandSalesTargetBean bean = new SandSalesTargetBean();
            bean.setNum(i);
            if (i % 2 == 0) {
                bean.setGetSalesTargetNature("私营企业" + i);
            } else {
                bean.setGetSalesTargetNature("联营企业" + i);
            }
            bean.setSalesTargetName("砂石供应商" + i);
            listAdapter.addData(bean);
        }
    }

    @OnClick({R.id.sand_common_fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sand_common_fab:
                Navigation.findNavController(view).navigate(R.id.actionSalesFragmentToCompanyResult);
                break;
        }
    }
}
