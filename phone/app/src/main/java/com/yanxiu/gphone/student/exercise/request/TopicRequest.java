package com.yanxiu.gphone.student.exercise.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by sp on 17-10-18.
 */

public class TopicRequest extends EXueELianBaseRequest {
    protected String stageId = "";
    @Override
    protected String urlPath() {
        return "/topic/getTopic.do";
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }
}
