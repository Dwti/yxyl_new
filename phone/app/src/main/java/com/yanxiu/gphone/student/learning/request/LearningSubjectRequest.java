package com.yanxiu.gphone.student.learning.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

import java.util.List;

/**
 * Created by lufengqing on 2018/1/25.
 */

public class LearningSubjectRequest  extends EXueELianBaseRequest {
    protected String stageId = "";
    String subjectIds;

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    @Override
    protected String urlPath() {
        return "study/getSubject.do";
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }
}

