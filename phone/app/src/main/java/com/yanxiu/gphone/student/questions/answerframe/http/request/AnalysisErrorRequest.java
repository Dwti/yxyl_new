package com.yanxiu.gphone.student.questions.answerframe.http.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * 答题报告
 * Created by dyf on 2017/6/27.
 */

public class AnalysisErrorRequest extends EXueELianBaseRequest {

    public String questionId;
    public String description;//报错文本
    public String type;//报错类型
    public String uid = LoginInfo.getUID();

    public AnalysisErrorRequest(String qid,String content,String type) {
        questionId = qid;
        description = content;
        this.type = type;
    }

    @Override
    protected String urlServer() {
        String url = UrlRepository.getInstance().getServer();
        return url.replace("/app","");
    }

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlPath() {
        return "/internal/uploadWrongQuestion.do";
    }

    @Override
    protected HttpType httpType() {
        return HttpType.GET;
    }


}
