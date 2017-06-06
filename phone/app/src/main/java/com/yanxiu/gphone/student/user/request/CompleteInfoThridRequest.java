package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/27 10:54.
 * Function :
 */
public class CompleteInfoThridRequest extends BaseLoginRequest {

    public String openid;
    public String sex;
    public String headimg;
    public String pltform;
    public String uniqid;
    public String realname;
    public String provinceid;
    public String cityid;
    public String areaid;
    public String schoolid;
    public String stageid;
    public String schoolName;
    public String deviceId= Constants.deviceId;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlPath() {
        return "/user/thirdRegister.do";
    }
}
