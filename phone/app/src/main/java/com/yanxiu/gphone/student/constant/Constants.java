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
    public static final String URL_SERVER_FILE_NAME = "env_config.json"; //存放server配置的文件名

    public static final String MAINAVTIVITY_REFRESH = "mainActivity_refresh";//跳转到首页需要刷新数据

}
