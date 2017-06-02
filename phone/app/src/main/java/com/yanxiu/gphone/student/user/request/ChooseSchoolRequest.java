package com.yanxiu.gphone.student.user.request;


/**
 * Created by Canghaixiao.
 * Time : 2017/5/25 11:03.
 * Function :
 */
public class ChooseSchoolRequest extends BaseLoginRequest {

    public String school;
    public String regionId;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlPath() {
        return "/personalData/searchSchool.do";
    }
}
