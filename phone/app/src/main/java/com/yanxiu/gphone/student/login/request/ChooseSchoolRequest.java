package com.yanxiu.gphone.student.login.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/2 17:06.
 * Function :
 */
public class ChooseSchoolRequest extends EXueELianBaseRequest {

    public String provinceName;
    public String provinceid;
    public String cityName;
    public String cityid;
    public String areaName;
    public String areaid;
    public String schoolid;
    public String schoolName;

    @Override
    protected String urlPath() {
        return "/personalData/updateUserInfo.do";
    }
}
