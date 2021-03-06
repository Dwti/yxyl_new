package com.yanxiu.gphone.student.login.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/27 10:54.
 * Function :
 */
public class CompleteInfoThridRequest extends EXueELianBaseRequest {

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
    public String schoolname;
    public String deviceId= Constants.deviceId;

    @Override
    protected String urlPath() {
        return "/user/thirdRegister.do";
    }
}
