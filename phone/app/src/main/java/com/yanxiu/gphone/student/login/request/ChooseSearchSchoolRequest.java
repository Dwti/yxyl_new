package com.yanxiu.gphone.student.login.request;


import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/25 11:03.
 * Function :
 */
public class ChooseSearchSchoolRequest extends EXueELianBaseRequest {

    public String school;
    public String regionId;

    @Override
    protected String urlPath() {
        return "/personalData/searchSchool.do";
    }
}
