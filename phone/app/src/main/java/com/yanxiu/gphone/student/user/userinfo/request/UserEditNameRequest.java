package com.yanxiu.gphone.student.user.userinfo.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/2 15:03.
 * Function :
 */
public class UserEditNameRequest extends EXueELianBaseRequest{

    public String realname;

    @Override
    protected String urlPath() {
        return "/personalData/updateUserInfo.do";
    }
}
