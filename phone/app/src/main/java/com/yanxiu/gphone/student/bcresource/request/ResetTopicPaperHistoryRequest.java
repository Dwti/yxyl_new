package com.yanxiu.gphone.student.bcresource.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-10-26.
 */

public class ResetTopicPaperHistoryRequest extends RequestBase {
    protected String token = LoginInfo.getToken();
    protected String paperId;
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
        return "topic/resetTopicPaperHistory.do";
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }
}
