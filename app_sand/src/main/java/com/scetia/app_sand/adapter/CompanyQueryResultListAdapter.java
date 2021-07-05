package com.scetia.app_sand.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.scetia.Pro.baseapp.basepage.BasePageListAdapter;
import com.scetia.Pro.baseapp.basepage.PageListCommonViewHolder;
import com.scetia.app_sand.R;
import com.scetia.app_sand.bean.SandSalesTargetBean;
import com.scetia.app_sand.viewmodel.SanSalesQueryListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * created by ljs
 * on 2020/11/6
 */
public class CompanyQueryResultListAdapter extends BasePageListAdapter<SandSalesTargetBean> {
    private SanSalesQueryListViewModel viewModel;
    private HashMap<String,String> addedSalesTargets;
    private List<String> delSalesTargetIds = new ArrayList<>();
    //需要添加的销售对象
    private ArrayList<SandSalesTargetBean> selectSalesTargetBeans = new ArrayList<>();
    //需要删除的销售对象
    private ArrayList<SandSalesTargetBean> delSalesTargetBeans = new ArrayList<>();
    private static DiffUtil.ItemCallback<SandSalesTargetBean> diff = new DiffUtil.ItemCallback<SandSalesTargetBean>() {
        @Override
        public boolean areItemsTheSame(@NonNull SandSalesTargetBean oldItem, @NonNull SandSalesTargetBean newItem) {
            return oldItem.getSerialNo().equals(newItem.getSerialNo());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SandSalesTargetBean oldItem, @NonNull SandSalesTargetBean newItem) {
            return oldItem==newItem;
        }
    };
    public CompanyQueryResultListAdapter(SanSalesQueryListViewModel viewModel) {
        super(diff,R.layout.company_query_result_list_item_layout);
        this.viewModel = viewModel;
    }

    public ArrayList<SandSalesTargetBean> getSelectSalesTargetBeans() {
        return selectSalesTargetBeans;
    }

    public ArrayList<SandSalesTargetBean> getDelSalesTargetBeans() {
        return delSalesTargetBeans;
    }

    public void setAddedSalesTargets(HashMap<String, String> addedSalesTargets) {
        this.addedSalesTargets = addedSalesTargets;
    }

    public HashMap<String, String> getAddedSalesTargets() {
        return addedSalesTargets;
    }

    public List<String> getDelSalesTargetIds() {
        return delSalesTargetIds;
    }

    @Override
    public void footerItemClick() {
        viewModel.retry();
    }

    @Override
    public void bindWithCommonItemView(PageListCommonViewHolder vh, SandSalesTargetBean sandSalesTargetBean) {
        TextView tvQueryItemNature = vh.getView(R.id.company_query_result_list_item_select_nature);
        TextView tvQueryItemName = vh.getView(R.id.company_query_result_list_item_select_name);
        ImageView ivItemSelect = vh.getView(R.id.company_query_result_list_item_select);
        tvQueryItemName.setText(sandSalesTargetBean.getUnitName());
        tvQueryItemNature.setText(sandSalesTargetBean.getUnitType());
        //只有初始化的时候才把已经添加的选中，其他时候按照添加和删除的逻辑
        if(addedSalesTargets.containsKey(sandSalesTargetBean.getSerialNo())&&!delSalesTargetBeans.contains(sandSalesTargetBean)){
            sandSalesTargetBean.setSelect(true);
        }
        int resourceId = sandSalesTargetBean.isSelect()? R.drawable.icon_chosen:R.drawable.icon_unchosen;
        ivItemSelect.setImageResource(resourceId);
    }

    /**
     * 添加销售对象
     * @param sandSalesTargetBean
     */
    public void addSelect(SandSalesTargetBean sandSalesTargetBean){
        delSalesTargetBeans.remove(sandSalesTargetBean);
        delSalesTargetIds.remove(addedSalesTargets.get(sandSalesTargetBean.getSerialNo()));
        //当前销售对象单元被选中，如果addedSalesTargets中不存在当前单元，则添加到请求集合中
        if(!addedSalesTargets.containsKey(sandSalesTargetBean.getSerialNo())) {
            selectSalesTargetBeans.add(sandSalesTargetBean);
        }
    }

    /**
     * 删除销售对象
     * @param sandSalesTargetBean
     */
    public void removeSelect(SandSalesTargetBean sandSalesTargetBean){
        selectSalesTargetBeans.remove(sandSalesTargetBean);
        if(addedSalesTargets.containsKey(sandSalesTargetBean.getSerialNo())){
            delSalesTargetBeans.add(sandSalesTargetBean);
            delSalesTargetIds.add(addedSalesTargets.get(sandSalesTargetBean.getSerialNo()));
        }
    }

    public void clearSelectData(){
        selectSalesTargetBeans.clear();
        delSalesTargetBeans.clear();
        delSalesTargetIds.clear();
    }
}
