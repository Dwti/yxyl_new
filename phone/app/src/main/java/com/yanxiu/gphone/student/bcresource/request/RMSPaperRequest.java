package com.yanxiu.gphone.student.bcresource.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-10-26.
 */

public class RMSPaperRequest extends RequestBase {
    protected String token = LoginInfo.getToken();
    protected String type;
    protected String rmsPaperId;
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
        return "/topic/getTopicPaperQuestion.do";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRmsPaperId() {
        return rmsPaperId;
    }

    public void setRmsPaperId(String rmsPaperId) {
        this.rmsPaperId = rmsPaperId;
    }
}
