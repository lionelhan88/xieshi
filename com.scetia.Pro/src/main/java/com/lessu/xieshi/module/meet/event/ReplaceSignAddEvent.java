package com.lessu.xieshi.module.meet.event;

import com.lessu.xieshi.module.meet.bean.MeetingBean;

public class ReplaceSignAddEvent {
    //通知列表页面是否刷新
    private boolean refresh = false;
    private MeetingBean.MeetingUserBean meetingUserBean;
    private String meetingID;

    public ReplaceSignAddEvent(boolean refresh) {
        this.refresh = refresh;
    }


    public ReplaceSignAddEvent(boolean refresh, MeetingBean.MeetingUserBean meetingUserBean, String meetingID) {
        this.refresh = refresh;
        this.meetingUserBean = meetingUserBean;
        this.meetingID = meetingID;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public MeetingBean.MeetingUserBean getMeetingUserBean() {
        return meetingUserBean;
    }

    public void setMeetingUserBean(MeetingBean.MeetingUserBean meetingUserBean) {
        this.meetingUserBean = meetingUserBean;
    }

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }
}
