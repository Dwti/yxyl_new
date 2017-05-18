package com.yanxiu.gphone.student.user.http;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 15:39.
 * Function :
 */
public class RegisterRequet extends RequestBase {
    public String mobile;
    public String code;
    public String password;
    public String type;
    public String pcode= Constants.pcode;
    public String version=Constants.version;
    public String osType=Constants.osType;
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app";
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
