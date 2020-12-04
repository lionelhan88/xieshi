package com.lessu.xieshi.module.meet.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.meet.bean.MeetingBean;

public class ReplaceSignListAdapter extends BaseQuickAdapter<MeetingBean.MeetingUserBean, BaseViewHolder> {
    public ReplaceSignListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetingBean.MeetingUserBean item) {
        helper.setText(R.id.meeting_company_sign_item_code_label,"姓名");
        helper.setText(R.id.meeting_company_sign_item_name_label,"联系电话");
        helper.setText(R.id.meeting_company_user_label,"会员编号");
        helper.setText(R.id.meeting_company_user_phone_label,"单位名称");
        helper.setText(R.id.meeting_company_sign_item_hy,item.getUserFullName());
        helper.setText(R.id.meeting_company_sign_item_name,item.getTel());
        helper.setText(R.id.meeting_company_user,item.getUnitMemberCode());
        helper.setText(R.id.meeting_company_user_phone,item.getMemberName());
        //已经签到的显示 “对勾”图标，未签到的不显示
        helper.setGone(R.id.iv_meeting_company_sign,true);
    }
}
