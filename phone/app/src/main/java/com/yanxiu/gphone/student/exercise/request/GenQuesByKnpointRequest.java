package com.yanxiu.gphone.student.exercise.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.io.Serializable;

/**
 * Created by sunpeng on 2017/8/3.
 */

public class GenQuesByKnpointRequest extends GenQuesRequest {
    protected String stageId = LoginInfo.getStageid();
    protected String subjectId;
    protected String fromType = "2";
    protected String knpId1="";
    protected String knpId2="";
    protected String knpId3="";
    protected String knpId4="";
    @Override
    protected String urlPath() {
        return "q/genKnpointQBlockNew.do";
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getKnpId1() {
        return knpId1;
    }

    public void setKnpId1(String knpId1) {
        this.knpId1 = knpId1;
    }

    public String getKnpId2() {
        return knpId2;
    }

    public void setKnpId2(String knpId2) {
        this.knpId2 = knpId2;
    }

    public String getKnpId3() {
        return knpId3;
    }

    public void setKnpId3(String knpId3) {
        this.knpId3 = knpId3;
    }

    public String getKnpId4() {
        return knpId4;
    }

    public void setKnpId4(String knpId4) {
        this.knpId4 = knpId4;
    }
}
