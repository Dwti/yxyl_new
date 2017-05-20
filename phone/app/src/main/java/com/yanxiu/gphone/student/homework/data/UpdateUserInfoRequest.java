package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.util.ExerciseRequestBase;

/**
 * Created by sp on 17-5-20.
 */

public class UpdateUserInfoRequest extends ExerciseRequestBase {
    protected String realname;
    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/";
    }

    @Override
    protected String urlPath() {
        return "personalData/updateUserInfo.do";
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
