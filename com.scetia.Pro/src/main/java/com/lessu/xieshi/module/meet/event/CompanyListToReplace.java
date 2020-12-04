package com.lessu.xieshi.module.meet.event;

import com.lessu.xieshi.module.meet.bean.MeetingBean;

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
