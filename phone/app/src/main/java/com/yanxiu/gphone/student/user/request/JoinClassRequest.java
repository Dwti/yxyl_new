package com.yanxiu.gphone.student.user.request;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/23 11:12.
 * Function :
 */
public class JoinClassRequest extends BaseLoginRequest {

    public String classId;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app";
    }

    @Override
    protected String urlPath() {
        return "/class/searchClass.do";
    }
}
