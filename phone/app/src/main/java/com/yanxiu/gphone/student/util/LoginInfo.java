package com.yanxiu.gphone.student.util;

import android.database.sqlite.SQLiteDatabase;

import com.yanxiu.gphone.student.user.bean.PassportBean;
import com.yanxiu.gphone.student.user.bean.UserMessageBean;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 12:27.
 * Function :
 */
@SuppressWarnings("unused")
public class LoginInfo {

    /**
     * login status. default,in,out
     * */
    private static final int LOGIN_DEFAULT=0x000;
    private static final int LOGIN_IN=0x001;
    private static final int LOGIN_OUT=0x002;

    private static int LOGIN_STATUS=LOGIN_DEFAULT;
    private static UserMessageBean bean;

    static {
        LOGIN_STATUS=LOGIN_DEFAULT;
        bean=getCacheData();
        if (bean==null){
            LOGIN_STATUS=LOGIN_OUT;
        }else {
            LOGIN_STATUS=LOGIN_IN;
        }
    }

    private static UserMessageBean getCacheData(){
        return DataSupport.findFirst(UserMessageBean.class,true);
    }

    public static void savaCacheData(UserMessageBean messageBean){
        if (!IsLogIn()) {
            LOGIN_STATUS=LOGIN_IN;
            bean = messageBean;
            bean.getPassport().save();
            bean.save();
        }
    }

    /**
     * true login false not login
     * */
    public static boolean IsLogIn(){
        return LOGIN_STATUS==LOGIN_IN;
    }

    public static String getMobile(){
        if (!IsLogIn()){
            return "";
        }
        return bean.getMobile();
    }

    public static String getUID(){
        if (!IsLogIn()){
            return "";
        }
        return String.valueOf(bean.getPassport().getUid());
    }

    public static String getToken(){
        if (!IsLogIn()){
            return "";
        }
        return bean.getPassport().getToken();
    }
}
