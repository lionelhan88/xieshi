package com.lessu.xieshi.module.sand.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;

/**
 * created by ljs
 * on 2020/10/28
 */
public class SMFlowDeclarationListAdapter extends BaseQuickAdapter<FlowDeclarationBean, BaseViewHolder> {
    private static final String COMMISSIONED_STATE = "已委托";
    private static final String UN_COMMISSION_STATE = "未委托";
    private boolean isCanSlide = false;
    //被选中的项的下标
    private int selectedPosition = -1;
    public SMFlowDeclarationListAdapter() {
        super(R.layout.sm_flow_declaration_list_item);
    }

    public void setCanSlide(boolean canSlide) {
        isCanSlide = canSlide;
    }

    @Override
    protected void convert(BaseViewHolder helper, FlowDeclarationBean item) {
        EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.easy_swipe_menu_layout);
        easySwipeMenuLayout.setCanLeftSwipe(isCanSlide);
        helper.setText(R.id.sm_flow_declaration_item_ship_name,item.getShipName());//船名
        helper.setText(R.id.sm_flow_declaration_item_sand,item.getSampleName());//样品名称
        helper.setText(R.id.sm_flow_declaration_sale_user,item.getCustomerUnitName());//销售对象
        helper.setText(R.id.sm_flow_declaration_sale_number,item.getSalesVolume());//销售数量
        helper.setText(R.id.sm_flow_declaration_declare_date,item.getCreateDatetime());//申报日期
        helper.addOnClickListener(R.id.flow_declaration_item_content);
        helper.addOnClickListener(R.id.right);
        //委托状态
        if(item.getFlowInfoStatus().equals(COMMISSIONED_STATE)){
            helper.setText(R.id.sm_flow_declaration_declare_commission_state,COMMISSIONED_STATE);
            helper.setTextColor(R.id.sm_flow_declaration_declare_commission_state,
                    mContext.getResources().getColor(R.color.green_dark));
            //已经委托的记录禁止删除
            easySwipeMenuLayout.setCanLeftSwipe(false);
        }else{
            helper.setText(R.id.sm_flow_declaration_declare_commission_state,UN_COMMISSION_STATE);
            helper.setTextColor(R.id.sm_flow_declaration_declare_commission_state,
                    mContext.getResources().getColor(R.color.red_dark));
            easySwipeMenuLayout.setCanLeftSwipe(true);
        }
        if(helper.getAdapterPosition()==selectedPosition){
            helper.itemView.setBackgroundResource(R.drawable.text_blue_round_bg);
        }else{
            helper.itemView.setBackgroundResource(R.drawable.white_round_stroke_bg);
        }
    }

    public void addSelect(int index){
        if(selectedPosition==index){
            return;
        }
        if(selectedPosition!=-1){
            notifyItemChanged(selectedPosition);
        }
        selectedPosition=index;
        notifyItemChanged(index);
    }

    public FlowDeclarationBean getSelectBean(){
        if(selectedPosition==-1){
            return null;
        }
        return getItem(selectedPosition);
    }
}
