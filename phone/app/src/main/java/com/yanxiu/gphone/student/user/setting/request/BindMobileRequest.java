package com.yanxiu.gphone.student.user.setting.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/27 10:30.
 * Function :
 */
public class BindMobileRequest extends EXueELianBaseRequest {

    public String newMobile;
    public String code;

    @Override
    protected String urlPath() {
        return "/user/bindNewMobile.do";
    }
}
