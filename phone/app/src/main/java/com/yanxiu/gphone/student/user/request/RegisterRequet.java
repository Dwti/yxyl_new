package com.yanxiu.gphone.student.user.request;


/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 15:39.
 * Function :
 */
public class RegisterRequet extends BaseLoginRequest {
    public String mobile;
    public String code;
    public String password;
    public String type;
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlPath() {
        return "/user/firstStepCommitV2.do";
    }
}
