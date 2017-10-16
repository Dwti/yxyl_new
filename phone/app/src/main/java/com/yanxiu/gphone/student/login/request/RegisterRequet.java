package com.yanxiu.gphone.student.login.request;


import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 15:39.
 * Function :
 */
public class RegisterRequet extends EXueELianBaseRequest {
    public String mobile;
    public String code;
    public String password;
    public String type;

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlPath() {
//        return "/user/firstStepCommitV2.do";
        return "/user/firstStepCommitNew.do";
    }
}
