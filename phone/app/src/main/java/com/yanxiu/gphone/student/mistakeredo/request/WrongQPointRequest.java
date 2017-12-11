package com.yanxiu.gphone.student.mistakeredo.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by sp on 17-11-21.
 * 获取错题知识点树
 */

public class WrongQPointRequest extends EXueELianBaseRequest {
    protected String stageId ;
    protected String subjectId;

    @Override
    protected String urlPath() {
        return "/q/getWrongQPointList.do";
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
