package com.lessu.xieshi.meet.event;

/**
 * created by ljs
 * on 2020/9/17
 */
public class OtherConfirmEvent {

    private String userFullName;
    private String userPhone;

    public OtherConfirmEvent(String userFullName, String userPhone) {
        this.userFullName = userFullName;
        this.userPhone = userPhone;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserPhone() {
        return userPhone;
    }
}
