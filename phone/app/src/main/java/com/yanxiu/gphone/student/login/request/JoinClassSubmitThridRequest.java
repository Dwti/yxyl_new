package com.yanxiu.gphone.student.login.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/27 11:09.
 * Function :
 */
public class JoinClassSubmitThridRequest extends EXueELianBaseRequest {

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
    public String schoolname;
    public String provinceid;
    public String deviceId= Constants.deviceId;

    @Override
    protected String urlPath() {
        return "/user/thirdRegisterByJoinClass.do";
    }
}
