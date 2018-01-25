package com.yanxiu.gphone.student.learning.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by lufengqing on 2018/1/25.
 */

public class GetChannelRequest  extends EXueELianBaseRequest {
    protected String stageId = LoginInfo.getStageid();
    @Override
    protected String urlPath() {
        return "study/getChannel.do?";
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

}
