package com.yanxiu.gphone.student.learning.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by lufengqing on 2018/1/25.
 */

public class LearningSaveEditionRequest  extends EXueELianBaseRequest {
    protected String stageId = LoginInfo.getStageid();
    protected String subjectId;
    protected String beditionId;
    @Override
    protected String urlPath() {
        return "study/saveEditionInfo.do";
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

