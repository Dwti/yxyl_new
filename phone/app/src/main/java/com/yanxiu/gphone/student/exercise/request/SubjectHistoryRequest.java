package com.yanxiu.gphone.student.exercise.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-8-2.
 */

public class SubjectHistoryRequest extends EXueELianBaseRequest {
    protected String stageId = LoginInfo.getStageid();
    @Override
    protected String urlPath() {
        return "personalData/getPracticeEdition.do";
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }
}
