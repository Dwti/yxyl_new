package com.yanxiu.gphone.student.login.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/26 17:13.
 * Function :
 */
public class LoginThridRequest extends EXueELianBaseRequest {

    public String openid;
    public String uniqid;
    public String platform;
    public String deviceId= Constants.deviceId;

    @Override
    protected String urlPath() {
        return "/user/oauthLogin.do";
    }
}
