package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 10:17.
 * Function :
 */

public class LoginRequest extends BaseLoginRequest {
    public String mobile;
    public String password;
    public String deviceId= Constants.deviceId;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/user/";
    }

    @Override
    protected String urlPath() {
        return "login.do";
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }
}
