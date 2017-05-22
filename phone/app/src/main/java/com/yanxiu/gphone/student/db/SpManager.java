package com.yanxiu.gphone.student.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.yanxiu.gphone.student.YanxiuApplication;


public class SpManager {

    public static final String SP_NAME = "srt_sp";
    private static SharedPreferences mySharedPreferences = YanxiuApplication.getInstance().getContext()
            .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    /**
     * 第一次启动
     */
    private static final String FRIST_START_UP = "frist_start_up";
    /**
     * 版本号
     */
    private static final String APP_VERSION_CODE = "version_code";


    public static void setFristStartUp(boolean isFristStartUp) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(FRIST_START_UP, isFristStartUp);
        editor.commit();
    }

    /**
     * 是否第一次启动
     *
     * @return true ： 第一次
     */
    public static boolean isFristStartUp() {
        return mySharedPreferences.getBoolean(FRIST_START_UP, true);
    }

    /**
     * app版本号
     *
     * @return -1 ：没记录
     */
    public static void setAppVersionCode(int versionCode) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(APP_VERSION_CODE, versionCode);
        editor.commit();
    }

    /**
     * app版本号
     *
     * @return -1 ：没记录
     */
    public static int getAppVersionCode() {
        return mySharedPreferences.getInt(APP_VERSION_CODE, -1);
    }

}
