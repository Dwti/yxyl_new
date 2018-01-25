package com.yanxiu.gphone.student.learning.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by lufengqing on 2018/1/25.
 */

public class LearningSubjectRequest  extends EXueELianBaseRequest {
    protected String stageId = "";
    @Override
    protected String urlPath() {
        return "common/getSubject.do";
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }
}

