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
    public static final String BRAND = DeviceUtil.getBrandName();
    public static final String OPERTYPE = "app.upload.log";
    public static final String PRODUCTLINE = "1";
    public static String token = LoginInfo.getToken();
    public static String trace_uid = LoginInfo.getUID();
    //    public static String deviceId= DeviceUtil.getAppDeviceId();
    public static String deviceId = "-";
    public static String version = String.valueOf(SystemUtil.getVersionCode());

    public static final String PRIVACY_POLICY_URL = "http://mobile.hwk.yanxiu.com/privacy_policy.html";


    public static int[] StageTxtId = {R.string.primary_txt, R.string.juinor_txt, R.string.high_txt};
    public static String[] StageId = {"1202", "1203", "1204"};
    public static int[] SexTxtId = {R.string.sex_man, R.string.sex_woman, R.string.sex_unknown};
    public static int[] SexId = {2, 1, 0};
    public static final String EXTRA_COME = "extra_Come";//数据来源
    public static final String COME_REPORT = "come_report";//在解析页标识来源--来自于答题报告
    public static final String EXTRA_PAPER = "extra_Paper";//传递给答题页的paper数据的key
    public static final String EXTRA_REQUEST = "extra_Request";//从选择章节只是点进入答题页，章节知识点的请求Request
    public static final String EXTRA_QID = "extra_Qid";//传递给报错页的题目的qid
    public static final String EXTRA_FROMTYPE = "extra_fromtype";//答题页的来源
    public static final String EXTRA_ANALYSIS_LEVELPOSITION = "extra_levelPositions";//答题报告传递给解析页的levelPositions
    public static final String URL_SERVER_FILE_NAME = "env_config.json"; //存放server配置的文件名

    public static final String MAINAVTIVITY_REFRESH = "mainActivity_refresh";//跳转到首页需要刷新数据
    public static final String MAINAVTIVITY_FROMTYPE_EXERCISE = "mainActivity_fromtype_exxercise";//从练习页跳转到首页标识
    public static final String STUDENT_UPLOAD = "student/yanxiustudent.apk";
    public static final char CHARACTER_SLASH = '/';
    public static final String DOMYBOXDIR = "YanXiu/app/student";
    public static final String PICTUREDIR = "/YanXiu/app/image/";
    public static final int NOTIFICATION_ID = 0x11;
    /**
     * default it is 0,when the loginactivity checks for updata,it is 1,at this time the mainactivity does't to check for updata
     */
    public static int UPDATA_TYPE = 0;

    public static final String NOTES_KEY = "notes_key";//跳转笔记页key
    public static final String PPID_KEY = "ppid";//跳转答题报告

    /**
     * 解析状态
     */
    public static final String HAS_FINISH_STATUS = "2"; //已完成
    public static final String NOT_FINISH_STATUS = "1";//未完成 不可补做  查看解析报告
    public static final String WAIT_FINISH_STATUS = "0";//待完成  可以做题
    public static final String HAS_FINISH_CHECK_REPORT = "0";   //已完成  可以查看答题报告

    /**
     * 题目状态 0 回答正确， 1 回答错误，  2 半对   3 未作答案  4 标示主观题 已作答
     */
    public static final int ANSWER_STATUS_RIGHT = 0;// 题目状态 0 回答正确
    public static final int ANSWER_STATUS_WRONG = 1;// 题目状态 1 回答错误
    public static final int ANSWER_STATUS_HALFRIGHT = 2;// 题目状态  2 半对
    public static final int ANSWER_STATUS_NOANSWERED = 3;// 题目状态 3 未作答案
    public static final int ANSWER_STATUS_YSUBJECT_ANSWERED = 4;// 题目状态 4 标示主观题 已作答

    public static final class SubjectId {
        public static final int CHINESE = 1102;
        public static final int MATH = 1103;
        public static final int ENGLISH = 1104;
        public static final int PHYSICAL = 1105;
        public static final int CHEMICAL = 1106;
        public static final int BIOLOGY = 1107;
        public static final int GEOGRAPHIC = 1108;
        public static final int POLITICAL = 1109;
        public static final int HISTORY = 1110;
    }

    public static final class Sex {
        public static final int SEX_TYPE_MAN = 2;
        public static final int SEX_TYPE_WOMAN = 1;
        public static final int SEX_TYPE_UNKNOWN = 0;
    }

    /**
     * 推送相关
     */
    public static final String MAINAVTIVITY_PUSHMSGBEAN = "mPushMsgBean";//在首页获取推送数据的key
    public static final int NOTIFICATION_ACTION_HOMEWORK_CORRECTING = 0;//批改作业
    public static final int NOTIFICATION_ACTION_ASSIGN_HOMEWORK = 1;//布置作业
    public static final int NOTIFICATION_ACTION_JOIN_THE_CLASS = 2;//班级加入成功||班级审核未通过
    public static final int NOTIFICATION_ACTION_OPEN_WEBVIEW = 3;//调用内置webView

    public static final class UserEvent {
        public static final String EVENT_ID = "eventID";
        public static final String UID = "uid";
        public static final String APPKEY = "appkey";
        public static final String TIME_STAMP = "timestamp";
        public static final String SOURCE = "source";
        public static final String CLIENT_TYPE = "clientType";
        public static final String MOBILE_MODEL = "mobileModel";
        public static final String BRAND = "brand";
        public static final String SYSTEM = "system";
        public static final String RESOLUTION = "resolution";
        public static final String NET_MODEL = "netModel";
        public static final String IP = "ip";
        public static final String URL = "url";
        public static final String RES_ID = "resID";
        public static final String QUESTION_ID = "questionID";
        public static final String RESERVED = "reserved";
        public static final String CONTENT = "content";
        public static final String EDITION_ID = "editionID";
        public static final String GRADE_ID = "gradeID";
        public static final String CLASS_ID = "classID";
        public static final String SUBJECT_ID = "subjectID";
        public static final String PAPER_TYPE = "paperType";
        public static final String QUES_Num = "quesNum";

        public static final class UserEventID {
            public static final String REGISTER_SUCCESS = "20:event_1";
            public static final String START_APP = "20:event_2";
            public static final String SUBMIT_WORK = "20:event_3";
            public static final String RECEIVE_WORK = "20:event_4";
            public static final String ENTER_WORK = "20:event_5";
            public static final String ENTER_BACK = "20:event_6";
            public static final String ENTER_FRONT = "20:event_7";
            public static final String EXIT_APP = "20:event_8";
            public static final String ENTER_CLASS = "20:event_9";
            public static final String FIRST_START = "20:event_10";
        }
    }
}
