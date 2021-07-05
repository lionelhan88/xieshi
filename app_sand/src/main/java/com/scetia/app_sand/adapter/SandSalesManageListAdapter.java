package com.scetia.app_sand.adapter;

import androidx.core.app.ActivityCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.scetia.app_sand.R;
import com.scetia.app_sand.bean.AddedSandSalesTargetBean;

/**
 * created by ljs
 * on 2020/12/22
 */
public class SandSalesManageListAdapter extends BaseQuickAdapter<AddedSandSalesTargetBean, BaseViewHolder> {
    private boolean isCanSwipe;
    private int selectPosition = -1;
    public SandSalesManageListAdapter(boolean isCanSwipe) {
        super(R.layout.sand_sales_manage_list_item_layout);
        this.isCanSwipe = isCanSwipe;
    }

    @Override
    protected void convert(BaseViewHolder helper, AddedSandSalesTargetBean item) {
        helper.setText(R.id.sales_manage_list_item_nature,item.getCustomerUnitType());
        helper.setText(R.id.sales_manage_list_item_name,item.getCustomerUnitName());
        EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.sales_manage_list_item_easy_swipe);
        easySwipeMenuLayout.setCanLeftSwipe(isCanSwipe);
        helper.addOnClickListener(R.id.right);
        helper.addOnClickListener(R.id.content);
        if (selectPosition==helper.getAdapterPosition()) {
            helper.itemView.setBackgroundResource(R.drawable.text_blue_round_bg);
            helper.setTextColor(R.id.sales_manage_list_item_nature, ActivityCompat.getColor(mContext,R.color.white));
            helper.setTextColor(R.id.sales_manage_list_item_name,ActivityCompat.getColor(mContext,R.color.white));
        } else {
            helper.itemView.setBackgroundResource(R.drawable.white_round_stroke_bg);
            helper.setTextColor(R.id.sales_manage_list_item_nature,ActivityCompat.getColor(mContext,R.color.black));
            helper.setTextColor(R.id.sales_manage_list_item_name,ActivityCompat.getColor(mContext,R.color.black));
        }
    }


    /**
     * 添加选中的项
     */
    public void addSelectedBean( int position) {
        if(selectPosition==position){
            return;
        }
        if(selectPosition!=-1){
            notifyItemChanged(selectPosition);
        }
        selectPosition = position;
        notifyItemChanged(selectPosition);
    }

    /**
     * 获取被选中的项
     * @return
     */
    public AddedSandSalesTargetBean getSelectedBean(){
        if(selectPosition==-1){
            return null;
        }
        return mData.get(selectPosition);
    }
}
