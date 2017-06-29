package com.yanxiu.gphone.student.constant;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.DeviceUtil;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.SystemUtil;

/**
 * Created by dyf
 * 存储常量
 */

public class Constants {
    public static final int NOT_LOGGED_IN = 99;
    public static final int NET_ERROR = -1;
    public static final String OS = "android";
    public static final String osType = "0";
    public static final String pcode = "010110000";
    public static final String BRAND= DeviceUtil.getBrandName();
    public static final String OPERTYPE = "app.upload.log";
    public static final String PRODUCTLINE = "1";
    public static String token = LoginInfo.getToken();
    public static String trace_uid = LoginInfo.getUID();
    //    public static String deviceId= DeviceUtil.getAppDeviceId();
    public static String deviceId = "-";
    public static String version = String.valueOf(SystemUtil.getVersionCode());



    public static int[] StageTxtId = {R.string.primary_txt, R.string.juinor_txt, R.string.high_txt};
    public static String[] StageId = {"1202", "1203", "1204"};
    public static final String EXTRA_PAPER = "extra_Paper";//传递给答题页的paper数据的key
    public static final String EXTRA_TITLE = "extra_Title";//传递给答题页的paper数据的title
    public static final String URL_SERVER_FILE_NAME = "env_config.json"; //存放server配置的文件名

    public static final String MAINAVTIVITY_REFRESH = "mainActivity_refresh";//跳转到首页需要刷新数据
    public static final String STUDENT_UPLOAD = "student/yanxiustudent.apk";
    public static char CHARACTER_SLASH = '/';
    public static String DOMYBOXDIR = "YanXiu/app/student";
    public static final int NOTIFICATION_ID = 0x11;
    /**
     * default it is 0,when the loginactivity checks for updata,it is 1,at this time the mainactivity does't to check for updata
     * */
    public static int UPDATA_TYPE=0;

    public static final String NOTES_KEY = "notes_key";//跳转笔记页key

    public static final String HAS_FINISH_STATUS = "2"; //已z完成
    public static final String NOT_FINISH_STATUS = "1";//未完成 不可补做  查看解析报告
    public static final String WAIT_FINISH_STATUS = "0";//待完成  可以做题
    public static final String HAS_FINISH_CHECK_REPORT = "0";   //已完成  可以查看答题报告
}
