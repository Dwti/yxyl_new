package com.yanxiu.gphone.student.user.http;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/25 11:03.
 * Function :
 */
public class ChooseSchoolRequest extends RequestBase {

    public String school;
    public String regionId;
    public String pcode= Constants.pcode;
    public String version=Constants.version;
    public String osType=Constants.osType;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app";
    }

    @Override
    protected String urlPath() {
        return "/personalData/searchSchool.do";
    }
}
