package com.lessu.xieshi.module.meet.event;

import com.lessu.xieshi.module.meet.bean.MeetingBean;

public class MisMeetingFragmentToMis {
    public MisMeetingFragmentToMis(MeetingBean meetingBean) {
        this.meetingBean = meetingBean;
    }

    private MeetingBean meetingBean;

    public MeetingBean getMeetingBean() {
        return meetingBean;
    }

    public void setMeetingBean(MeetingBean meetingBean) {
        this.meetingBean = meetingBean;
    }
}
