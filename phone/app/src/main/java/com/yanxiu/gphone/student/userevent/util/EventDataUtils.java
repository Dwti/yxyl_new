package com.yanxiu.gphone.student.userevent.util;

import android.os.Build;

import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.userevent.bean.WorkBean;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.NetWorkUtils;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/7 11:08.
 * Function :
 */
public class EventDataUtils {

    private static final String APPKEY="20001";
    private static final String SOURCE="0";//0，移动端，1，页面
    private static final String CLIENT_TYPE="1";//0，iOS，1，android
    private static final String URL="www.yanxiu.com";

    private static HashMap<String,String> getEventDataMap(){
        HashMap<String,String> map=new HashMap<>();
        map.put(Constants.UserEvent.UID, LoginInfo.getUID());
        map.put(Constants.UserEvent.APPKEY,APPKEY);
        map.put(Constants.UserEvent.TIME_STAMP,String.valueOf(System.currentTimeMillis()));
        map.put(Constants.UserEvent.SOURCE,SOURCE);
        map.put(Constants.UserEvent.CLIENT_TYPE,CLIENT_TYPE);
        map.put(Constants.UserEvent.IP,"");
        map.put(Constants.UserEvent.URL,URL);
        map.put(Constants.UserEvent.RES_ID,"");
        map.put(Constants.UserEvent.MOBILE_MODEL, Build.MODEL);
        map.put(Constants.UserEvent.BRAND,Build.BRAND);
        map.put(Constants.UserEvent.SYSTEM,Build.VERSION.RELEASE);
        map.put(Constants.UserEvent.RESOLUTION, ScreenUtils.getScreenHeight(YanxiuApplication.getInstance())+"*"+ScreenUtils.getScreenWidth(YanxiuApplication.getInstance()));
        map.put(Constants.UserEvent.NET_MODEL, NetWorkUtils.getNetType());
        return map;
    }

    public static String getRegistSuccessMap(){
        HashMap<String,String> map=getEventDataMap();
        map.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.REGISTER_SUCCESS);
        return eventMapToJsonMap(map);
    }

    public static String getStartAppMap(){
        HashMap<String,String> map=getEventDataMap();
        map.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.START_APP);
        return eventMapToJsonMap(map);
    }

    /**
     * @param bedition 教材版本
     * @param gradeId 年级ID
     * @param paperType 试卷类型 0，练习，1，作业
     * @param questionNum 题目数量
     * @param subjectId 学科ID
     * @param questionId [qid,qid,qid...]
     * */
    public static String getSubmitWorkMap(String bedition,String gradeId,String subjectId,String paperType,String questionNum,String questionId){
        HashMap<String,String> map=getEventDataMap();
        HashMap<String,String> reserved=new HashMap<>();
        reserved.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.SUBMIT_WORK);
        reserved.put(Constants.UserEvent.EDITION_ID,bedition);
        reserved.put(Constants.UserEvent.GRADE_ID,gradeId);
        reserved.put(Constants.UserEvent.SUBJECT_ID,subjectId);
        reserved.put(Constants.UserEvent.PAPER_TYPE,paperType);
        reserved.put(Constants.UserEvent.QUES_Num,questionNum);
        reserved.put(Constants.UserEvent.QUESTION_ID,questionId);
        map.put(Constants.UserEvent.RESERVED,eventMapToJsonString(reserved));
        return eventMapToJsonMap(map);
    }

    public static String getReceiveWorkMap(List<WorkBean> list){
        List<HashMap<String,String>> hashMaps=new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            WorkBean bean=list.get(i);
            HashMap<String, String> map = getEventDataMap();
            HashMap<String, String> reserved = new HashMap<>();
            reserved.put(Constants.UserEvent.EVENT_ID, Constants.UserEvent.UserEventID.RECEIVE_WORK);
            reserved.put(Constants.UserEvent.CLASS_ID, bean.classId);
            reserved.put(Constants.UserEvent.QUES_Num, bean.questionNum);
            map.put(Constants.UserEvent.RESERVED, eventMapToJsonString(reserved));
            hashMaps.add(map);
        }
        return eventMapToJsonMap(hashMaps);
    }

    public static String getEnterWorkMap(){
        HashMap<String,String> map=getEventDataMap();
        map.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.ENTER_WORK);
        return eventMapToJsonMap(map);
    }

    public static String getEnterBackMap(){
        HashMap<String,String> map=getEventDataMap();
        map.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.ENTER_BACK);
        return eventMapToJsonMap(map);
    }

    public static String getEnterFrontMap(){
        HashMap<String,String> map=getEventDataMap();
        map.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.ENTER_FRONT);
        return eventMapToJsonMap(map);
    }

    public static String getExitAppMap(){
        HashMap<String,String> map=getEventDataMap();
        map.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.EXIT_APP);
        return eventMapToJsonMap(map);
    }

    public static String getEnterClassMap(){
        HashMap<String,String> map=getEventDataMap();
        map.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.ENTER_CLASS);
        return eventMapToJsonMap(map);
    }

    public static String getFirstStartMap(){
        HashMap<String,String> map=getEventDataMap();
        map.put(Constants.UserEvent.EVENT_ID,Constants.UserEvent.UserEventID.FIRST_START);
        return eventMapToJsonMap(map);
    }

    private static String eventMapToJsonMap(HashMap<String,String> map){
        List<HashMap<String,String>> list=new ArrayList<>();
        list.add(map);
        return eventMapToJsonMap(list);
    }

    private static String eventMapToJsonMap(List<HashMap<String,String>> list) {
        Map<String,String> contentMap=new HashMap<>();
        String string = "[";
        for (int i=0; i<list.size(); i++) {
            string = string + eventMapToJsonString(list.get(i));
            string += ",";
        }
        string = string.substring(0, string.lastIndexOf(",")) + "]";
        contentMap.put(Constants.UserEvent.CONTENT,string);
        return setMapToJson(contentMap);
    }

    private static String eventMapToJsonString(Map<String,String> map) {
        String string = "{";
        for (Object o : map.entrySet()) {
            Map.Entry e = (Map.Entry) o;
            if (e.getKey().equals(Constants.UserEvent.RESERVED) || e.getKey().equals(Constants.UserEvent.QUESTION_ID)) {
                string += "\"" + e.getKey() + "\":";
                string += "" + e.getValue() + ",";
            } else {
                string += "\"" + e.getKey() + "\":";
                string += "\"" + e.getValue() + "\",";
            }
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    private static String setMapToJson(Map<String,String> map) {
        String string = "{";
        for (Object o : map.entrySet()) {
            Map.Entry e = (Map.Entry) o;
            string += "\"" + e.getKey() + "\":";
            string += "" + e.getValue() + ",";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }
}
