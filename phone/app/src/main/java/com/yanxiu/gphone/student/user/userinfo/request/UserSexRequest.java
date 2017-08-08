package com.yanxiu.gphone.student.user.userinfo.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/2 16:44.
 * Function :
 */
public class UserSexRequest extends EXueELianBaseRequest {

    public String sex;

    @Override
    protected String urlPath() {
        return "/personalData/updateUserInfo.do";
    }
}
