package com.yanxiu.gphone.student.exercise.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.util.List;

/**
 * Created by sp on 17-7-26.
 */

public class SubjectsRequest extends EXueELianBaseRequest {
    protected String stageId = "";
    protected String subjectIds;
    @Override
    protected String urlPath() {
        return "common/getSubject.do";
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }
}
