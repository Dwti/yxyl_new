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
    protected String urlPath() {
        return "/class/searchClass.do";
    }
}
