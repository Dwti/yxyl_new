package com.yanxiu.gphone.student.util;

import com.yanxiu.gphone.student.login.response.PassportBean;
import com.yanxiu.gphone.student.login.response.UserMessageBean;

import org.litepal.crud.DataSupport;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 12:27.
 * <p>
 * Function :添加方法时需要注意，方法名规则  :
 * <p>
 * getXXX: 获取XXX数据
 * setXXX：临时保存XXX数据,退出程序即清除
 * saveXXX：真正保存XXX数据到数据库
 */
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

    private static void Save() {
        bean.getPassport().save();
        bean.save();
    }

    public static void LogOut() {
        if (!isLogIn()) {
            return;
        }
        LOGIN_STATUS = LOGIN_OUT;
        bean.delete();
        bean = null;
    }

    /**
     * true login false not login
     */
    public static boolean isLogIn() {
        return LOGIN_STATUS == LOGIN_IN;
    }

    private static void init() {
        if (bean == null) {
            PassportBean passportBean = new PassportBean();
            bean = new UserMessageBean();
            bean.setPassport(passportBean);
        }
    }

    public static void setMobile(String mobile) {
        init();
        bean.setMobile(mobile);
        bean.getPassport().setMobile(mobile);
    }

    public static void saveMobile(String mobile) {
        bean.setMobile(mobile);
        bean.getPassport().setMobile(mobile);
        Save();
    }

    public static void setPassWord(String passWord) {
        init();
        bean.getPassport().setPassword(passWord);
    }

    public static String getMobile() {
        if (bean == null || bean.getMobile() == null) {
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

    public static void saveRealName(String name) {
        if (!isLogIn()) {
            return;
        }
        bean.setRealname(name);
        Save();
    }

    public static String getLoginType() {
        if (!isLogIn()) {
            return "";
        }
        return bean.getLoginType();
    }

    public static void saveLoginType(String loginType) {
        if (!isLogIn()) {
            return;
        }
        bean.setLoginType(loginType);
        Save();
    }

    public static String getStageid() {
        if (!isLogIn()) {
            return "";
        }
        return bean.getStageid();
    }

    public static void saveUid(int uid){
        if (!isLogIn()) {
            return;
        }
        bean.getPassport().setUid(uid);
        Save();
    }

    public static void saveToken(String token){
        if (!isLogIn()) {
            return;
        }
        bean.getPassport().setToken(token);
        Save();
    }

    public static void savePassWord(String passWord){
        if (!isLogIn()) {
            return;
        }
        bean.getPassport().setPassword(passWord);
        Save();
    }
}
