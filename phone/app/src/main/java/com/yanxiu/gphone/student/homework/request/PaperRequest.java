package com.yanxiu.gphone.student.homework.request;

import com.yanxiu.gphone.student.base.YxylBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class PaperRequest extends YxylBaseRequest {

    private String paperId;

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
        return "/personalData/getQuestionList.do";
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }
}
