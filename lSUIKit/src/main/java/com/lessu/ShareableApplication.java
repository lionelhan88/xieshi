package com.lessu;

import android.app.Application;

/**
 * Created by lessu on 14-7-15.
 */
public class ShareableApplication extends Application{
    public static Application sharedApplication;
    public ShareableApplication() {
        sharedApplication = this;
    }
}
