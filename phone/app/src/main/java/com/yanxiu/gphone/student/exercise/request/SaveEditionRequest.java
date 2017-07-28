package com.yanxiu.gphone.student.exercise.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-7-27.
 */

public class SaveEditionRequest extends EXueELianBaseRequest {
    protected String stageId = LoginInfo.getStageid();
    protected String subjectId;
    protected String beditionId;
    @Override
    protected String urlPath() {
        return "common/saveEditionInfo.do";
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getBeditionId() {
        return beditionId;
    }

    public void setBeditionId(String beditionId) {
        this.beditionId = beditionId;
    }
}
