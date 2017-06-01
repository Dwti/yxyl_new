package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/23 17:13.
 * Function :
 */
public class JoinClassSubmitRequest extends BaseLoginRequest {

    public String classId;
    public String stageid;
    public String areaid;
    public String cityid;
    public String mobile;
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
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app";
    }

    @Override
    protected String urlPath() {
        return "/user/registerByJoinClass.do";
    }
}