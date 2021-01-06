package com.lessu.xieshi.module.sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;

/**
 * created by ljs
 * on 2020/12/23
 */
public class SandTestingCompanyListAdapter extends BaseQuickAdapter<AddedTestingCompanyBean, BaseViewHolder> {
    private boolean isCanSwipe;
    private int selectPosition = -1;
    public SandTestingCompanyListAdapter() {
        super(R.layout.testing_company_query_result_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddedTestingCompanyBean item) {
        EasySwipeMenuLayout layout = helper.getView(R.id.company_manage_swipe_menLayout);
        layout.setCanLeftSwipe(isCanSwipe);
        helper.setGone(R.id.testing_company_manage_item_select_img,false);
        helper.setText(R.id.testing_company_manage_item_company_name,item.getDetectionAgencyUnitName());
        helper.setText(R.id.testing_company_manage_item_company_area,item.getDetectionAgencyCounties());
        helper.setText(R.id.testing_company_manage_item_company_user,item.getDetectionAgencyContactPerson());
        helper.setText(R.id.testing_company_manage_item_company_phone,item.getDetectionAgencyContactPersonPhoneNo());
        helper.setText(R.id.testing_company_manage_item_company_address,item.getDetectionAgencyUnitAddress());
        helper.addOnClickListener(R.id.right);
        helper.addOnClickListener(R.id.company_manage_item_content);
        helper.itemView.setBackgroundResource(helper.getAdapterPosition()==selectPosition?R.drawable.text_blue_round_bg:R.drawable.white_round_stroke_bg);
    }

    public void setCanSwipe(boolean canSwipe) {
        isCanSwipe = canSwipe;
    }

    public void addSelect(int index){
        if(selectPosition==index){
            return;
        }
        if(index!=-1){
            notifyItemChanged(selectPosition);
            selectPosition = index;
        }
        notifyItemChanged(selectPosition);
    }

    public AddedTestingCompanyBean getSelectBean(){
        if(selectPosition==-1){
            return null;
        }
        return mData.get(selectPosition);
    }
}
