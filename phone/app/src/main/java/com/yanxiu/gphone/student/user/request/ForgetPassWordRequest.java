package com.yanxiu.gphone.student.user.request;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 15:02.
 * Function :
 */
public class ForgetPassWordRequest extends BaseLoginRequest {
    public String mobile;
    public String code;
    public String type;

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
        return "/user/firstStepCommit.do";
    }
}
