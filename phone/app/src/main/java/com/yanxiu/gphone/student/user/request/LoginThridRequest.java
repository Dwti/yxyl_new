package com.yanxiu.gphone.student.user.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/26 17:13.
 * Function :
 */
public class LoginThridRequest extends RequestBase {

    public String openid;
    public String uniqid;
    public String platform;
    public String deviceId= Constants.deviceId;

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
        return "/user/oauthLogin.do";
    }
}
