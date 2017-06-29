package com.yanxiu.gphone.student.questions.answerframe.http.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * 答题报告
 * Created by dyf on 2017/6/27.
 */

public class AnswerReportRequest extends EXueELianBaseRequest {

    protected String ppid;
    protected String flag = "1";

    public AnswerReportRequest(String ppid) {
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
        return "/q/getQReport.do";
    }

    @Override
    protected HttpType httpType() {
        return HttpType.GET;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
