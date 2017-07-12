package com.yanxiu.gphone.student.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by 戴延枫 on 2017/6/9.
 */

public class ActivityManger {

    protected static ArrayList<Activity> activityList = new ArrayList<Activity>();

    /**
     * 每个activity在oncreate()里，必须调用该方法
     * @param activity
     */
    public static void addActicity(Activity activity) {
        if (activityList != null) {
            activityList.add(activity);
        }
    }
    /**
     * 每个activity在onDestory()里，必须调用该方法
     * @param activity
     */
    public static void destoryActivity(Activity activity) {
        if (activityList != null) {
            activityList.remove(activity);
        }
    }
    /**
     * 在退出程序时，必须调用该方法
     */
    public static void destoryAll() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                activity.finish();
            }
            activityList.clear();
        }
    }

    /**
     * 获取当前Activity
     * @return
     */
    public static Activity getTopActivity(){
        if (activityList != null && activityList.size() > 0) {
            return activityList.get(activityList.size() - 1);
        }
        return null;
    }
}
