package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 15:16.
 * Function :
 */
public class ResetPassWordRequest extends EXueELianBaseRequest {
    public String mobile;
    public String password;

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
        return "/user/resetPassword.do";
    }
}
