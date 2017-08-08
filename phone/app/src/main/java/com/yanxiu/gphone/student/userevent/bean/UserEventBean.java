package com.yanxiu.gphone.student.userevent.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/7 15:55.
 * Function :
 */
public class UserEventBean extends DataSupport{

    public String time;
    public String data;

    public UserEventBean(String data) {
        this.time=String.valueOf(System.currentTimeMillis());
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
