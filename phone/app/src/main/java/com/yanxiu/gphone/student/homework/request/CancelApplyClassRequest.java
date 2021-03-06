package com.yanxiu.gphone.student.homework.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by sp on 17-5-20.
 */

public class CancelApplyClassRequest extends EXueELianBaseRequest {
    protected String classId;

    @Override
    protected String urlPath() {
        return "/class/cancelReply.do";
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
