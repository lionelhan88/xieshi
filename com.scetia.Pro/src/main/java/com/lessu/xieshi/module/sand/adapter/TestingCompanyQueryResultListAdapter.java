package com.lessu.xieshi.module.sand.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;
import com.lessu.xieshi.module.sand.bean.TestingManageBean;

/**
 * created by ljs
 * on 2020/11/6
 */
public class TestingCompanyQueryResultListAdapter extends BaseQuickAdapter<TestingManageBean, BaseViewHolder> {
    private boolean isShowSelectState;
    public TestingCompanyQueryResultListAdapter(boolean isShowSelectState) {
        super(R.layout.testing_company_query_result_list_item_layout);
        this.isShowSelectState = isShowSelectState;
    }

    @Override
    protected void convert(BaseViewHolder helper, TestingManageBean item) {
        EasySwipeMenuLayout layout = helper.getView(R.id.company_manage_swipe_menLayout);
        layout.setCanLeftSwipe(!isShowSelectState);
        helper.setGone(R.id.testing_company_manage_item_select_img,isShowSelectState);
        helper.addOnClickListener(R.id.company_manage_item_content);
        helper.setText(R.id.testing_company_manage_item_company_name,item.getTestingCompanyName());
        helper.setText(R.id.testing_company_manage_item_company_area,item.getTestingCompanyArea());
        helper.setText(R.id.testing_company_manage_item_company_user,item.getTestingCompanyUser());
        helper.setText(R.id.testing_company_manage_item_company_phone,item.getTestingCompanyPhone());
        helper.setText(R.id.testing_company_manage_item_company_address,item.getTestingCompanyAddress());
        if(item.isSelect()){
            helper.setImageResource(R.id.testing_company_manage_item_select_img,R.drawable.icon_chosen);
        }else{
            helper.setImageResource(R.id.testing_company_manage_item_select_img,R.drawable.icon_unchosen);
        }
    }
}
