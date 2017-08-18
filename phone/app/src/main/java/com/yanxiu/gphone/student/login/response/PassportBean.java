package com.yanxiu.gphone.student.login.response;

import org.litepal.crud.DataSupport;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 10:25.
 * Function :
 */
public class PassportBean extends DataSupport{
    private int id;
    private int uid;
    private String mobile;
    private String password;
    private String token;
    private String deviceId;
    private String type;
    private UserMessageBean bean;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserMessageBean getBean() {
        return bean;
    }

    public void setBean(UserMessageBean bean) {
        this.bean = bean;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
