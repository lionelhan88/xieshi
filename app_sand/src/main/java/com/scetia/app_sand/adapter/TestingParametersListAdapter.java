package com.scetia.app_sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scetia.app_sand.R;
import com.scetia.app_sand.bean.SandItemParameterBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * created by ljs
 * on 2020/12/25
 */
public class TestingParametersListAdapter extends BaseQuickAdapter<SandItemParameterBean, BaseViewHolder> {
    private ArrayList<String> parameters = new ArrayList<>();
    private ArrayList<String> defaultString =new ArrayList<>();
    public TestingParametersListAdapter() {
        super(R.layout.sand_testing_parameters_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SandItemParameterBean item) {
        helper.setText(R.id.tv_item_parameter_name, item.getParameterName());
        String parameterId = item.getParameterID();
        if(defaultString.size()>0){
            //已经不是第一次选了，就按照普通的选中规则来
            if(parameters.contains(parameterId)){
                helper.setImageResource(R.id.iv_item_parameter_select, R.drawable.icon_chosen);
            }else{
                helper.setImageResource(R.id.iv_item_parameter_select, R.drawable.icon_unchosen );
            }
        }else if(item.getMustBeDetectFlag()==1){
            helper.setImageResource(R.id.iv_item_parameter_select, R.drawable.icon_chosen );
            if(!parameters.contains(item.getParameterID())){
                parameters.add(item.getParameterID());
            }
        } else if(item.isIsDefault()&&item.getSelectedNum()==0){
           //初始化
           parameters.add(item.getParameterID());
           item.setSelectedNum(1);
           helper.setImageResource(R.id.iv_item_parameter_select, R.drawable.icon_chosen );
       }else{
           //已经不是第一次选了，就按照普通的选中规则来
           if(parameters.contains(item.getParameterID())){
               helper.setImageResource(R.id.iv_item_parameter_select, R.drawable.icon_chosen );
           }else{
               helper.setImageResource(R.id.iv_item_parameter_select, R.drawable.icon_unchosen );
           }
       }
        helper.setVisible(R.id.tv_item_parameter_must_state, item.getMustBeDetectFlag() == 1);
    }

    /**
     * 设置默认被选中的项
     *
     * @param parameters
     */
    public void setDefaultSelectBeans(String parameters) {
        defaultString.clear();
        this.getParameters().clear();
        if (parameters != null && !parameters.equals("")) {
            final List<String> strings = Arrays.asList(parameters.split(";"));
            defaultString.clear();
            defaultString.addAll(new LinkedList<String>(strings));
            this.getParameters().clear();
            this.parameters.addAll(new LinkedList<String>(strings));
        }
    }

    /**
     * 用户点击了当前项改变当前项的状态
     */
    public void changedItemState(SandItemParameterBean item, int position) {
        if (parameters.contains(item.getParameterID())) {
            //如果当前项已经处于选中状态，那么从选中列表中删除当前项
            parameters.remove(item.getParameterID());
        } else {
            //如果没有被选中，那么添加到选中列表中
            parameters.add(item.getParameterID());
        }
        notifyItemChanged(position);
    }

    public ArrayList<String> getParameters() {
        Collections.sort(parameters);
        return parameters;
    }
}
