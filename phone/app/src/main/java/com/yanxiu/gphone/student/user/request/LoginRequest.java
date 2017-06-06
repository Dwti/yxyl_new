package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.base.ExerciseBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 10:17.
 * Function :
 */

public class LoginRequest extends ExerciseBaseRequest {
    public String mobile;
    public String password;
    public String deviceId= Constants.deviceId;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }


    @Override
    protected String urlPath() {
        return "/user/login.do";
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }
}
