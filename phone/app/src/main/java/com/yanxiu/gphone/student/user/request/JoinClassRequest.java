package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.base.ExerciseBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/23 11:12.
 * Function :
 */
public class JoinClassRequest extends ExerciseBaseRequest {

    public String classId;

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
        return "/class/searchClass.do";
    }
}
