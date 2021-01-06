package com.lessu;

import org.greenrobot.eventbus.EventBus;

/**
 * created by ljs
 * on 2020/12/11
 */
public class EventBusUtil {

    public static final int A = 0x111111;
    public static final int B = 0x222222;
    public static final int C = 0x333333;
    public static final int D = 0x444444;

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(GlobalEvent event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(GlobalEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    public static boolean isRegistered(Object subscriber){
        return EventBus.getDefault().isRegistered(subscriber);
    }

    public static void removeStickyEvent(Object event){
        EventBus.getDefault().removeStickyEvent(event);
    }
}
