package com.yanxiu.gphone.student.exercise.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.io.Serializable;

/**
 * Created by sunpeng on 2017/8/3.
 */

public class GenQuesByKnpointRequest extends EXueELianBaseRequest implements Serializable {
    protected String stageId = LoginInfo.getStageid();
    protected String subjectId;
    protected String fromType = "2";
    protected String knpId1="";
    protected String knpId2="";
    protected String knpId3="";
    protected String knpId4="";
    @Override
    protected String urlPath() {
        return "q/genKnpointQBlock.do";
    }
}
