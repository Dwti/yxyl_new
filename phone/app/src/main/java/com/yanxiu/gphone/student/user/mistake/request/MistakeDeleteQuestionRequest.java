package com.yanxiu.gphone.student.user.mistake.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/19 11:19.
 * Function :
 */
public class MistakeDeleteQuestionRequest extends EXueELianBaseRequest {

    public String questionId;

    @Override
    protected String urlPath() {
        return "/personalData/delMistake.do";
    }
}
