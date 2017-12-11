package com.yanxiu.gphone.student.mistakeredo.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.util.List;

/**
 * Created by sp on 17-11-23.
 */

public class WrongQByQidsRequest extends EXueELianBaseRequest {

    protected String qids;
    protected String subjectId;

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlPath() {
        return "q/getWrongQByQids.do";
    }

    public String getQids() {
        return qids;
    }

    public void setQids(String qids) {
        this.qids = qids;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
