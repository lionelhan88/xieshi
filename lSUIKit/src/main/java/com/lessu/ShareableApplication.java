package com.lessu;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

/**
 * Created by lessu on 14-7-15.
 */
public class ShareableApplication extends Application{
    public static Application sharedApplication;
    public static Stack<Activity> activities = new Stack<>();
    public ShareableApplication() {
        sharedApplication = this;
    }
}
