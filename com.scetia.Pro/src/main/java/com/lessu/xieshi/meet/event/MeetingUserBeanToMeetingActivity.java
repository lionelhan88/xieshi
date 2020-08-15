package com.lessu.xieshi.meet.event;

import com.lessu.xieshi.meet.bean.MeetingBean;

public class MeetingUserBeanToMeetingActivity {
    private MeetingBean.MeetingUserBean meetingUserBean;

    public MeetingUserBeanToMeetingActivity(MeetingBean.MeetingUserBean meetingUserBean) {
        this.meetingUserBean = meetingUserBean;
    }

    public MeetingBean.MeetingUserBean getMeetingUserBean() {
        return meetingUserBean;
    }

    public void setMeetingUserBean(MeetingBean.MeetingUserBean meetingUserBean) {
        this.meetingUserBean = meetingUserBean;
    }
}
