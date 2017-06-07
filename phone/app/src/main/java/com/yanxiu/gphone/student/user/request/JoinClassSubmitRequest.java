package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/23 17:13.
 * Function :
 */
public class JoinClassSubmitRequest extends EXueELianBaseRequest {

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
    protected String urlPath() {
        return "/user/registerByJoinClass.do";
    }
}
