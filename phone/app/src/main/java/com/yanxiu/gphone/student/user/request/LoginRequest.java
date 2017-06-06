package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 10:17.
 * Function :
 */

public class LoginRequest extends EXueELianBaseRequest {
    public String mobile;
    public String password;
    public String deviceId= Constants.deviceId;

    @Override
    protected String urlPath() {
        return "/user/login.do";
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }
}
