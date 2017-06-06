package com.yanxiu.gphone.student.user.request;


import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 12:36.
 * Function :
 */
public class SendVerCodeRequest extends EXueELianBaseRequest {
    public String mobile;
    public String type;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlPath() {
        return "/user/produceCode.do";
    }
}
