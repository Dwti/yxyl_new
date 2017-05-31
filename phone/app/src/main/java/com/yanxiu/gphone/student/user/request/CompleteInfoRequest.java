package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/25 12:16.
 * Function :
 */
public class CompleteInfoRequest extends BaseLoginRequest {

    public String mobile;
    public String realname;
    public String provinceid;
    public String cityid;
    public String areaid;
    public String schoolid;
    public String stageid;
    public String schoolName;
    public String validKey;
    public String deviceId=Constants.deviceId;

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
        return "/user/registerV2.do";
    }
}
