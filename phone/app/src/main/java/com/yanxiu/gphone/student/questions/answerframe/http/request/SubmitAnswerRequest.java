package com.yanxiu.gphone.student.questions.answerframe.http.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by dyf on 2017/6/23.
 */

public class SubmitAnswerRequest extends EXueELianBaseRequest {

    protected String ppid;
    protected String answers;


    public SubmitAnswerRequest(String answers,String ppid) {
        this.answers = answers;
        this.ppid = ppid;
    }

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
        return "/q/submitQ.do";
    }//?answers=&token=kk&ppId=344

    @Override
    protected HttpType httpType() {
        return HttpType.GET;
    }

}
