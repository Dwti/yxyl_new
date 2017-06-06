package com.yanxiu.gphone.student.user.response;

import java.io.Serializable;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/6 10:32.
 * Function :
 */
public class ThridMessage implements Serializable {
    public String openid;
    public String platform;
    public String uniqid;
    public String sex;
    public String head;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUniqid() {
        return uniqid;
    }

    public void setUniqid(String uniqid) {
        this.uniqid = uniqid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
