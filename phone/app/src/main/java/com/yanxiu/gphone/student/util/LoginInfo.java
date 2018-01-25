package com.yanxiu.gphone.student.util;

import com.yanxiu.gphone.student.login.response.PassportBean;
import com.yanxiu.gphone.student.login.response.UserMessageBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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

    public static void setCacheData(UserMessageBean messageBean){
        if (isLogIn()) {
            return;
        }
        bean = messageBean;
    }

    public static void saveCacheData(UserMessageBean messageBean) {
        if (isLogIn()) {
            return;
        }
        bean = messageBean;
        Save();
    }

    public static void Save() {
        if (bean==null||bean.getPassport()==null){
            return;
        }
        LOGIN_STATUS = LOGIN_IN;
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

    /**
     * 账号（手机号）
     * @return 手机号
     */
    public static String getMobile() {
        if (bean == null || bean.getMobile() == null) {
            return "";
        }
        return bean.getMobile();
    }

    public static String getUID() {
        //因登录处有绑定手机操作，需要uid，所以这样处理
        if (bean==null||bean.getPassport()==null) {
            return "";
        }
        return String.valueOf(bean.getPassport().getUid());
    }

    public static String getToken() {
        //因登录处有绑定手机操作，需要token，所以这样处理
        if (bean==null||bean.getPassport()==null) {
            return "";
        }
        return bean.getPassport().getToken();
    }

    /**
     * 账号名称
     * @return 真实姓名
     */
    public static String getRealName() {
        if (!isLogIn()) {
            return "";
        }
        return bean.getRealname();
    }

    /**
     * 获得登录名称
     * */
    public static String getLoginName(){
        if (!isLogIn()) {
            return "";
        }
        return bean.getLoginName();
    }

    public static void saveRealName(String name) {
        if (!isLogIn()) {
            return;
        }
        bean.setRealname(name);
        Save();
    }

    /**
     * 登录方式（账号登录、三方登录）
     * @return 三方类型
     */
    public static String getLoginType(){
        if (!isLogIn()) {
            return "";
        }
        return bean.getLoginType();
    }

    public static void setLoginType(String loginType) {
        if (bean==null) {
            return;
        }
        bean.setLoginType(loginType);
    }

    public static void saveLoginType(String loginType) {
        if (!isLogIn()) {
            return;
        }
        bean.setLoginType(loginType);
        Save();
    }

    /**
     * 学段ID
     * @return 学段ID
     */
    public static String getStageid(){
        if (!isLogIn()){
            return "";
        }
        return bean.getStageid();
    }

    public static void saveStageid(String stageId){
        if (!isLogIn()) {
            return;
        }
        bean.setStageid(stageId);
        Save();
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

    public static void savePassWord(String passWord) {
        if (!isLogIn()) {
            return;
        }
        bean.getPassport().setPassword(passWord);
        Save();
    }
    /**
     * 学段名称
     * @return 学段名称
     */
    public static String getStageName(){
        if (!isLogIn()){
            return "";
        }
        return bean.getStageName();
    }

    public static void saveStageName(String stageName){
        if (!isLogIn()) {
            return;
        }
        bean.setStageName(stageName);
        Save();
    }

    /**
     * 用户头像
     * @return 头像URL
     */
    public static String getHeadIcon(){
        if (!isLogIn()){
            return "";
        }
        return bean.getHead();
    }

    public static void saveHeadIcon(String path){
        if (!isLogIn()) {
            return;
        }
        bean.setHead(path);
        Save();
    }

    /**
     * 学校名称
     * @return 学校名称
     * */
    public static String getSchoolName(){
        if (!isLogIn()){
            return "";
        }
        return bean.getSchoolName();
    }

    public static void saveSchoolMessage(String areaId, String areaName, String cityId, String cityName, String provinceId, String provinceName, String schoolId, String schoolName){
        if (!isLogIn()) {
            return;
        }
        bean.setAreaid(areaId);
        bean.setAreaName(areaName);
        bean.setCityid(cityId);
        bean.setCityName(cityName);
        bean.setProvinceid(provinceId);
        bean.setProvinceName(provinceName);
        bean.setSchoolid(schoolId);
        bean.setSchoolName(schoolName);
        Save();
    }

    /**
     * 获取性别
     * */
    public static int getSex(){
        if (!isLogIn()){
            return 0;
        }
        return bean.getSex();
    }

    public static void saveSex(int sex){
        if (!isLogIn()) {
            return;
        }
        bean.setSex(sex);
        Save();
    }

    /**
     * 获得省份名称
     * */
    public static String getProvinceName(){
        if (!isLogIn()){
            return "";
        }
        return bean.getProvinceName();
    }

    /**
     * 获得城市名称
     * */
    public static String getCityName(){
        if (!isLogIn()){
            return "";
        }
        return bean.getCityName();
    }

    /**
     * 获得区域名称
     * */
    public static String getAreaName(){
        if (!isLogIn()){
            return "";
        }
        return bean.getAreaName();
    }

    /**
     * 可访问的学科ID
     * */
    public static List<Integer> getSubjectIds(){
        if (!isLogIn()){
            return new ArrayList<>();
        }
        List<Integer> list=bean.getSubjectIds();
        if (list==null){
            list=new ArrayList<>();
        }
        return list;
    }
}
