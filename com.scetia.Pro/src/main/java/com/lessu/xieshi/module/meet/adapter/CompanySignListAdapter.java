package com.lessu.xieshi.module.meet.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.meet.bean.MeetingBean;

public class CompanySignListAdapter extends BaseQuickAdapter<MeetingBean.MeetingUserBean, BaseViewHolder> {
    public CompanySignListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetingBean.MeetingUserBean item) {
        helper.setText(R.id.meeting_company_sign_item_hy,item.getUnitMemberCode());
        helper.setText(R.id.meeting_company_sign_item_name,item.getMemberName());
        helper.setText(R.id.meeting_company_user,item.getUserFullName());
        helper.setText(R.id.meeting_company_user_phone,item.getTel());
        helper.addOnClickListener(R.id.tv_meeting_company_guid_none_sign);
        //已经签到的显示 “对勾”图标，未签到的不显示
        helper.setGone(R.id.iv_meeting_company_sign,item.getCheckStatus().equals("1"));
        //如果AccountNumber是1的话，如果还没有签到，右上角显示一个签到按钮
        if(!item.getCheckStatus().equals("1")&&item.getAccountNumber().equals("1")){
            helper.setGone(R.id.tv_meeting_company_guid_none_sign,true);
        }else{
            helper.setGone(R.id.tv_meeting_company_guid_none_sign,false);
        }
    }
}
