package com.yanxiu.gphone.student.login.response;

import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 12:17.
 * Function :
 */
public class UserMessageBean extends DataSupport {

    public static final String LOGIN_ACCOUNT = "account";
    public static final String LOGIN_QQ = "qq";
    public static final String LOGIN_WX = "wx";

    private String loginType;

    private int id;
    private String mobile;
    private String nickname;
    private String realname;
    private String loginName;
    private String stageid;
    private String stageName;
    private String schoolid;
    private String schoolName;
    private int sex;
    private String provinceid;
    private String provinceName;
    private String cityid;
    private String cityName;
    private String areaid;
    private String areaName;
    private String status;
    private String createtime;
    private String head;
    private PassportBean passport;
    private List<Integer> subjectIds;
    private String subjectIds_string;

    public String getSubjectIds_string() {
        String string="";
        for (Integer integer:subjectIds){
            if (TextUtils.isEmpty(string)){
                string+=integer;
            }else {
                string+=(","+integer);
            }
        }
        this.subjectIds_string=string;
        return subjectIds_string;
    }

    public void setSubjectIds_string(String subjectIds_string) {
        this.subjectIds_string = subjectIds_string;
        if (subjectIds==null||subjectIds.isEmpty()){
            if (!TextUtils.isEmpty(subjectIds_string)){
                String[] strings=subjectIds_string.split(",");
                subjectIds=new ArrayList<>();
                for (String s:strings){
                    int i=Integer.parseInt(s);
                    subjectIds.add(i);
                }
            }
        }
    }

    public List<Integer> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<Integer> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getStageid() {
        return stageid;
    }

    public void setStageid(String stageid) {
        this.stageid = stageid;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public PassportBean getPassport() {
        return passport;
    }

    public void setPassport(PassportBean passport) {
        this.passport = passport;
    }
}
