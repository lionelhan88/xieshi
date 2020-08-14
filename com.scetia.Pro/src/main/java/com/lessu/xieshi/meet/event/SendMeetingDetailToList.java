package com.lessu.xieshi.meet.event;

import com.lessu.xieshi.meet.bean.MeetingBean;

public class SendMeetingDetailToList {
    //通知列表页面是否刷新
   private boolean refresh = false;

    public SendMeetingDetailToList(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
