package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/27 11:09.
 * Function :
 */
public class JoinClassSubmitThridRequest extends BaseLoginRequest {

    public String openid;
    public String sex;
    public String headimg;
    public String pltform;
    public String uniqid;
    public String classId;
    public String stageid;
    public String areaid;
    public String cityid;
    public String realname;
    public String schoolid;
    public String schoolName;
    public String provinceid;
    public String validKey;
    public String deviceId= Constants.deviceId;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlPath() {
        return "/user/thirdRegisterByJoinClass.do";
    }
}
