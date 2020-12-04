package com.lessu.xieshi.module.sand.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.SandProviderListAdapter;
import com.lessu.xieshi.module.sand.bean.SandProviderBean;

import butterknife.BindView;

/**
 * created by ljs
 * on 2020/11/5
 */
public class SandInfoMaintainProviderFragment extends BaseFragment {

    @BindView(R.id.sand_manage_provider_rv)
    RecyclerView sandManageProviderRv;
    private SandProviderListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_info_maintain;
    }

    @Override
    protected void initView() {
        setTitle("供应商列表");
        listAdapter = new SandProviderListAdapter();
        sandManageProviderRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        sandManageProviderRv.setAdapter(listAdapter);
    }

    @Override
    protected void initData() {
        for (int i=0;i<=10;i++){
            SandProviderBean bean = new SandProviderBean();
            bean.setRecordCardCode("1111111"+i);
            bean.setRecordCardName("备案证名称"+i);
            listAdapter.addData(bean);
        }

    }

}
