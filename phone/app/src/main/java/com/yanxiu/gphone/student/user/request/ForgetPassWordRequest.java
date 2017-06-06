package com.yanxiu.gphone.student.user.request;


import com.yanxiu.gphone.student.base.ExerciseBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 15:02.
 * Function :
 */
public class ForgetPassWordRequest extends ExerciseBaseRequest {
    public String mobile;
    public String code;
    public String type;

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlPath() {
        return "/user/firstStepCommit.do";
    }
}
