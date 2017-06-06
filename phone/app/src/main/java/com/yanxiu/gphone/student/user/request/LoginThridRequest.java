package com.yanxiu.gphone.student.user.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.ExerciseBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/26 17:13.
 * Function :
 */
public class LoginThridRequest extends ExerciseBaseRequest {

    public String openid;
    public String uniqid;
    public String platform;
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
        return "/user/oauthLogin.do";
    }
}
