package com.lessu.xieshi.module.meet.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Constants;
import com.lessu.xieshi.module.meet.bean.MeetingBean;
import com.scetia.Pro.common.Util.SPUtil;

public class MeetingListAdapter extends BaseQuickAdapter<MeetingBean, BaseViewHolder> {
    public MeetingListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetingBean item) {
        //会议名称
        helper.setText(R.id.meeting_list_item_name,item.getMeetingName());
        //会议申请人
        helper.setText(R.id.meeting_list_item_user,item.getCreatePerson());
        //会议开始时间
        helper.setText(R.id.meeting_list_item_start_date,item.getMeetingStartTime());
        //会议结束时间
        helper.setText(R.id.meeting_list_item_end_date,item.getMeetingEndTime());
        helper.addOnClickListener(R.id.meeting_list_item_confirm);
        String curUserId = SPUtil.getSPConfig(Constants.User.USER_ID,"");
        MeetingBean.MeetingUserBean curBean = null;
        for (MeetingBean.MeetingUserBean meetingUserBean:item.getListUserContent()){
            if(meetingUserBean.getUserId().equals(curUserId)){
                curBean = meetingUserBean;
                break;
            }
        }
        //会议是否确认
        if(curBean==null){
            helper.setGone(R.id.meeting_list_item_confirm,false);
        } else if(curBean.getConfirmNotify().equals("0")){
            //会议未确认
            helper.setTextColor(R.id.meeting_list_item_confirm,mContext.getResources().getColor(R.color.orange1));
            helper.setText(R.id.meeting_list_item_confirm,"未确认");
        }else{
            //会议已确认
            helper.setTextColor(R.id.meeting_list_item_confirm,mContext.getResources().getColor(R.color.black));
            helper.setText(R.id.meeting_list_item_confirm,"已确认");
        }
    }
}
