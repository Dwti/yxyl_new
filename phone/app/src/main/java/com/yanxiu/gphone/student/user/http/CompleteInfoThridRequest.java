package com.yanxiu.gphone.student.user.http;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/27 10:54.
 * Function :
 */
public class CompleteInfoThridRequest extends RequestBase {

    public String openid;
    public String sex;
    public String headimg;
    public String pltform;
    public String uniqid;
    public String mobile;
    public String realname;
    public String provinceid;
    public String cityid;
    public String areaid;
    public String schoolid;
    public String stageid;
    public String schoolName;
    public String validKey;
    public String deviceId= Constants.deviceId;
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
    protected String urlPath() {
        return "/user/thirdRegister.do";
    }
}
