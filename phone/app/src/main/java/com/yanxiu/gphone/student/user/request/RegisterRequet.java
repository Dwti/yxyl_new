package com.yanxiu.gphone.student.user.request;


import com.yanxiu.gphone.student.base.ExerciseBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 15:39.
 * Function :
 */
public class RegisterRequet extends ExerciseBaseRequest {
    public String mobile;
    public String code;
    public String password;
    public String type;
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected String urlPath() {
        return "/user/firstStepCommitV2.do";
    }
}
