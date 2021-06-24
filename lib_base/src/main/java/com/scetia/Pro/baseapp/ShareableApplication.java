package com.scetia.Pro.baseapp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scetia.Pro.common.Util.SPUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lessu on 14-7-15.
 */
public abstract class ShareableApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static Application sharedApplication;
    private static HashMap<String, Activity> activityHashMap = new HashMap<>();

    public abstract void init();

    @Override
    public void onCreate() {
        super.onCreate();
        sharedApplication = this;
        registerActivityLifecycleCallbacks(this);
        SPUtil.init(this);
        init();
    }

    public static Application getInstance() {
        return sharedApplication;
    }

    public static Activity getActivity(String key) {
        if (activityHashMap.containsKey(key)) {
            return activityHashMap.get(key);
        }
        return null;
    }

    /**
     * 退出程序，清空栈
     */
    public static void exit() {
        Set<Map.Entry<String, Activity>> entries = activityHashMap.entrySet();
        for (Map.Entry<String, Activity> entry : entries) {
            Activity value = entry.getValue();
            value.finish();
        }
        activityHashMap.clear();
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        //如果当前 集合中不存在此activity则存入
        if (!activityHashMap.containsKey(activity.getLocalClassName())) {
            activityHashMap.put(activity.getLocalClassName(), activity);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        activityHashMap.remove(activity.getLocalClassName());
    }
}
