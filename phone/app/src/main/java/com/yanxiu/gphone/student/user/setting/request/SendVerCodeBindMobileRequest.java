package com.yanxiu.gphone.student.user.setting.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/26 16:08.
 * Function :
 */
public class SendVerCodeBindMobileRequest extends EXueELianBaseRequest {

    public String mobile;
    public String type;

    @Override
    protected String urlPath() {
        return "/user/produceCodeByBind.do";
    }
}
