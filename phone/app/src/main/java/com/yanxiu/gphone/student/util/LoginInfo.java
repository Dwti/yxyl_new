package com.yanxiu.gphone.student.util;

import com.yanxiu.gphone.student.user.response.PassportBean;
import com.yanxiu.gphone.student.user.response.UserMessageBean;

import org.litepal.crud.DataSupport;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 12:27.
 * Function :
 */
@SuppressWarnings("unused")
public class LoginInfo {

    /**
     * login status. default,in,out
     */
    private static final int LOGIN_DEFAULT = 0x000;
    private static final int LOGIN_IN = 0x001;
    private static final int LOGIN_OUT = 0x002;

    private static int LOGIN_STATUS = LOGIN_DEFAULT;
    private static UserMessageBean bean;

    static {
        LOGIN_STATUS = LOGIN_DEFAULT;
        try {
            bean = getCacheData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bean == null) {
            LOGIN_STATUS = LOGIN_OUT;
        } else {
            LOGIN_STATUS = LOGIN_IN;
        }
    }

    private static UserMessageBean getCacheData() {
        return DataSupport.findFirst(UserMessageBean.class, true);
    }

    public static void saveCacheData(UserMessageBean messageBean) {
        if (isLogIn()) {
            return;
        }
        LOGIN_STATUS = LOGIN_IN;
        bean = messageBean;
        Save();
    }

    public static void Save(){
        bean.getPassport().save();
        bean.save();
    }

    public static void LogOut(){
        if (!isLogIn()){
            return;
        }
        LOGIN_STATUS = LOGIN_OUT;
        bean.delete();
        bean=null;
    }

    /**
     * true login false not login
     */
    public static boolean isLogIn() {
        return LOGIN_STATUS == LOGIN_IN;
    }

    private static void init(){
        if (bean==null){
            PassportBean passportBean=new PassportBean();
            bean=new UserMessageBean();
            bean.setPassport(passportBean);
        }
    }

    public static void setMobile(String mobile){
        init();
        bean.setMobile(mobile);
        bean.getPassport().setMobile(mobile);
    }

    public static void setPassWord(String passWord){
        init();
        bean.getPassport().setPassword(passWord);
    }

    public static void setToken(String token){
        init();
        bean.getPassport().setToken(token);
    }

    public static String getMobile() {
        if (bean==null||bean.getMobile()==null) {
            return "";
        }
        return bean.getMobile();
    }

    public static String getUID() {
        if (!isLogIn()) {
            return "";
        }
        return String.valueOf(bean.getPassport().getUid());
    }

    public static String getToken() {
        if (!isLogIn()) {
            return "";
        }
        return bean.getPassport().getToken();
    }

    public static String getRealName() {
        if (!isLogIn()) {
            return "";
        }
        return bean.getRealname();
    }

    public static void setRealName(String name) {
        if (!isLogIn()) {
            return;
        }
        bean.setRealname(name);
        bean.save();
    }
}
