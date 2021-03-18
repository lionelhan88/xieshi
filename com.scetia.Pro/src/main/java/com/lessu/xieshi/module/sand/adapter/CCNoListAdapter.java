package com.lessu.xieshi.module.sand.adapter;

import android.text.TextUtils;
import android.util.SparseArray;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * created by ljs
 * on 2021/1/27
 */
public class CCNoListAdapter extends BaseQuickAdapter<TestingCommissionBean.CCNoBean, BaseViewHolder> {
    private List<String> selectedCCNos;
    private int mSelectPosition = -1;
    public CCNoListAdapter() {
        super(R.layout.sand_testing_parameters_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestingCommissionBean.CCNoBean item) {
        helper.setText(R.id.tv_item_parameter_name, item.getCcNoStr());
        helper.setVisible(R.id.iv_item_parameter_select,false);
        helper.setVisible(R.id.tv_item_parameter_must_state,false);
    }


    public void setDefaultCCNo(String ccNo){
        if(selectedCCNos==null){
            selectedCCNos = new ArrayList<>();
        }
        //清空原来的记录
        selectedCCNos.clear();
        if(!TextUtils.isEmpty(ccNo)) {
            selectedCCNos.addAll(Arrays.asList(ccNo.split(";")));
        }
    }

    /**
     * 获取当前选中的标识号
     * @return
     */
    public String getSelectedCCNos(){
        return  getItem(mSelectPosition).getCcNoStr();
    }

    public void changeItemState(String ccNo,int position){
        mSelectPosition = position;
        notifyDataSetChanged();
    }
}
