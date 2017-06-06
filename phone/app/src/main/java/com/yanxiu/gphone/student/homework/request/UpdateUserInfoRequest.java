package com.yanxiu.gphone.student.homework.request;

import com.yanxiu.gphone.student.base.YxylBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by sp on 17-5-20.
 */

public class UpdateUserInfoRequest extends YxylBaseRequest {
    protected String realname;
    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
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
