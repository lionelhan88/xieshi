package com.lessu.xieshi.meet.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.meet.bean.MeetingBean;
import com.lessu.xieshi.mis.base.BaseView;

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
        //会议是否确认
        if(item.getMeetingConfirm().equals("0")){
            //会议未确认
            helper.setBackgroundRes(R.id.meeting_list_item_confirm,R.drawable.text_orange_stroke_bg);
            helper.setText(R.id.meeting_list_item_confirm,"确认会议");
        }else{
            //会议已确认
            helper.setBackgroundRes(R.id.meeting_list_item_confirm,R.drawable.text_blue_round_bg);
            helper.setText(R.id.meeting_list_item_confirm,"已确认");
        }
    }
}
