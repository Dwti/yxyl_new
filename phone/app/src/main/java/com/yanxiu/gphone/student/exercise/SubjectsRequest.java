package com.yanxiu.gphone.student.exercise;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-7-26.
 */

public class SubjectsRequest extends EXueELianBaseRequest {
    protected String stageId = LoginInfo.getStageid();
    @Override
    protected String urlPath() {
        return "common/getSubject.do";
    }
}
