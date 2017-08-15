package com.yanxiu.gphone.student.user.setting.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/27 14:41.
 * Function :
 */
public class ChangePassWordRequest extends EXueELianBaseRequest {

    public String mobile;
    public String loginName;
    public String newPass;
    public String oldPass;

    @Override
    protected String urlPath() {
        return "/user/modifyPassword.do";
    }
}
