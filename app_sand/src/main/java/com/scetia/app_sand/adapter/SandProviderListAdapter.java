package com.scetia.app_sand.adapter;

import android.util.SparseArray;

import androidx.core.app.ActivityCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scetia.app_sand.R;
import com.scetia.app_sand.bean.SandSupplierBean;

/**
 * created by ljs
 * on 2020/11/6
 */
public class SandProviderListAdapter extends BaseQuickAdapter<SandSupplierBean, BaseViewHolder> {
    private SparseArray<SandSupplierBean> selectedBean = new SparseArray<>();

    public SandProviderListAdapter() {
        super(R.layout.sand_provider_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SandSupplierBean item) {
        helper.setText(R.id.sand_provider_list_item_code, item.getPutOnRecordsPassport());
        helper.setText(R.id.sand_provider_list_item_name, item.getProductionUnitName());
        if (selectedBean.size() > 0 && selectedBean.get(helper.getAdapterPosition()) == item) {
            helper.itemView.setBackgroundResource(R.drawable.text_blue_round_bg);
            helper.setTextColor(R.id.sand_provider_list_item_code,ActivityCompat.getColor(mContext,R.color.white));
            helper.setTextColor(R.id.sand_provider_list_item_name,ActivityCompat.getColor(mContext,R.color.white));
        } else {
            helper.itemView.setBackgroundResource(R.drawable.white_round_stroke_bg);
            helper.setTextColor(R.id.sand_provider_list_item_code,ActivityCompat.getColor(mContext,R.color.black));
            helper.setTextColor(R.id.sand_provider_list_item_name,ActivityCompat.getColor(mContext,R.color.black));
        }
    }

    /**
     * 添加选中的项
     */
    public void addSelectedBean(SandSupplierBean bean, int position) {
        if(selectedBean.size()>0) {
            int i = selectedBean.keyAt(0);
            if(i==position){
                return;
            }
            selectedBean.clear();
            notifyItemChanged(i);
        }
        selectedBean.put(position, bean);
        notifyItemChanged(position);
    }

    /**
     * 获取被选中的项
     * @return
     */
    public SandSupplierBean getSelectedBean(){
        if(selectedBean.size()==0){
            return null;
        }
        return selectedBean.valueAt(0);
    }
}
