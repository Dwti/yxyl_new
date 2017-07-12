package com.yanxiu.gphone.student.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.yanxiu.gphone.student.YanxiuApplication;


public class SpManager {

    public static final String SP_NAME = "srt_sp";
    public static final String SP_NAME_PAPER_ANSWERTIME = "srt_sp_paper_answer_time";
    private static SharedPreferences mySharedPreferences = YanxiuApplication.getInstance().getContext()
            .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

    private static SharedPreferences myAnswerTimeSharedPreferences = YanxiuApplication.getInstance().getContext()
            .getSharedPreferences(SP_NAME_PAPER_ANSWERTIME, Context.MODE_PRIVATE);
    /**
     * 第一次启动
     */
    private static final String FRIST_START_UP = "frist_start_up";
    /**
     * 版本号
     */
    private static final String APP_VERSION_CODE = "version_code";
    /**
     * 第一次进入裁剪页面
     */
    private static final String FRIST_START_LUANCH_CROPIMAGE="frist_start_luanch_crop";

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

    /**
     * 答题总计时间保存
     *
     * @return -1 ：没记录
     */
    public static int getTotlaTime(String paperId) {
        return myAnswerTimeSharedPreferences.getInt(paperId, -1);
    }

    /**
     * 答题总计时间保存
     */
    public static void setTotlaTime(String paperId,int totalTime) {
        SharedPreferences.Editor editor = myAnswerTimeSharedPreferences.edit();
        editor.putInt(paperId, totalTime);
        editor.commit();
    }

    /**
     * 清空答题时间数据
     */
    public static void clearAnswerTime(){
        myAnswerTimeSharedPreferences.edit().clear();
    }

    public static void setCropIsLuanched(boolean isLuanch){
        SharedPreferences.Editor editor=mySharedPreferences.edit();
        editor.putBoolean(FRIST_START_LUANCH_CROPIMAGE,isLuanch).commit();
    }

    public static boolean getCropIsLuanched(){
        return mySharedPreferences.getBoolean(FRIST_START_LUANCH_CROPIMAGE,false);
    }
}
