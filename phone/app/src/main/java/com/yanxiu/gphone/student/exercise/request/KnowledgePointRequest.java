package com.yanxiu.gphone.student.exercise.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-7-28.
 */

public class KnowledgePointRequest extends EXueELianBaseRequest {
    protected String stageId = LoginInfo.getStageid();
    protected String subjectId;
    @Override
    protected String urlPath() {
        return "anaofstd/listKnpStat.do";
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
