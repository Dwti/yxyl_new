package com.yanxiu.gphone.student.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.yanxiu.gphone.student.YanxiuApplication;


public class SpManager {

    public static final String SP_NAME = "srt_sp";
    public static final String SP_NAME_PAPER_ANSWERTIME = "srt_sp_paper_answer_time";
    public static final String SP_NAME_PAPER_COMPLETEANSWER = "srt_sp_paper_complete_answer";
    private static SharedPreferences mySharedPreferences = YanxiuApplication.getInstance().getContext()
            .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

    private static SharedPreferences myAnswerTimeSharedPreferences = YanxiuApplication.getInstance().getContext()
            .getSharedPreferences(SP_NAME_PAPER_ANSWERTIME, Context.MODE_PRIVATE);

    /**
     * 答题完成数量，其他使用paperId为key的数据，切勿使用该表保存
     */
    private static SharedPreferences myCompeleteAnswerSharedPreferences = YanxiuApplication.getInstance().getContext()
            .getSharedPreferences(SP_NAME_PAPER_COMPLETEANSWER, Context.MODE_PRIVATE);
    /**
     * 第一次启动
     */
    private static final String FRIST_START_UP = "frist_start_up";
    /**
     * 第一次启动
     */
    private static final String FRIST_START_UP2 = "frist_start_up2";
    /**
     * 版本号
     */
    private static final String APP_VERSION_CODE = "version_code";
    /**
     * 第一次进入裁剪页面
     */
    private static final String FRIST_START_LUANCH_CROPIMAGE="frist_start_luanch_crop";

    private static final String SOUND_ON = "sound_on"; //声音开关

    private static final String FRIST_ENTER_SPOKEN_QUESTION="spoken_question";

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
     * 是否第一次启动（仅限于埋点用）
     * */
    public static boolean isFristStartUp2(){
        return mySharedPreferences.getBoolean(FRIST_START_UP2, true);
    }

    public static void setFristStartUp2(boolean isFristStartUp) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(FRIST_START_UP2, isFristStartUp);
        editor.commit();
    }

    public static void setFristEnterSpokenQuestion(boolean isFristStartUp) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(FRIST_ENTER_SPOKEN_QUESTION, isFristStartUp);
        editor.commit();
    }

    /**
     * 是否第一次进入口语题
     *
     * @return true ： 第一次
     */
    public static boolean isFristEnterSpokenQuestion() {
        return mySharedPreferences.getBoolean(FRIST_ENTER_SPOKEN_QUESTION, true);
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

    public static boolean isSoundOn(){
        return mySharedPreferences.getBoolean(SOUND_ON,true);
    }

    public static void setSoundOn(boolean on){
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(SOUND_ON, on);
        editor.commit();
    }

    /**
     * 答题总计时间保存
     *
     * @return -1 ：没记录
     */
    /**
     * 数据库分明保存的有全部的各种时间，为啥不用，舍近而求远，再搞这一套
     * */
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
        myAnswerTimeSharedPreferences.edit().clear().commit();
    }

    /**
     * 计算已经完成的题目数
     * 答题没有提交答案的情况下，保存本地数据库。作业列表需要
     */
    public static void setCompleteQuestionCount(String paperId,int count) {
        SharedPreferences.Editor editor = myCompeleteAnswerSharedPreferences.edit();
        editor.putInt(paperId, count);
        editor.commit();
    }

    public static void clearCompleteQuestionCount(){
        myCompeleteAnswerSharedPreferences.edit().clear().commit();
    }
    /**
     * 已经完成的题目数
     * @return 0 ：没记录
     */
    public static int getCompleteQuestionCount(String paperId) {
        return myCompeleteAnswerSharedPreferences.getInt(paperId, 0);
    }

    public static void setCropIsLuanched(boolean isLuanch){
        SharedPreferences.Editor editor=mySharedPreferences.edit();
        editor.putBoolean(FRIST_START_LUANCH_CROPIMAGE,isLuanch).commit();
    }

    public static boolean getCropIsLuanched(){
        return mySharedPreferences.getBoolean(FRIST_START_LUANCH_CROPIMAGE,false);
    }
}
