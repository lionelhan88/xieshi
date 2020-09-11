package com.lessu.xieshi.meet.event;

import com.lessu.xieshi.meet.bean.MeetingBean;

/**
 * created by ljs
 * on 2020/9/2
 */
public class CompanyListToReplace {
    private MeetingBean.MeetingUserBean meetingUserBean;
    public CompanyListToReplace(MeetingBean.MeetingUserBean meetingUserBean) {
        this.meetingUserBean = meetingUserBean;
    }

    public MeetingBean.MeetingUserBean getMeetingUserBean() {
        return meetingUserBean;
    }

    public void setMeetingUserBean(MeetingBean.MeetingUserBean meetingUserBean) {
        this.meetingUserBean = meetingUserBean;
    }
}
