package com.yanxiu.gphone.student.mistake.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 14:44.
 * Function :
 */
public class MistakeListRequest extends EXueELianBaseRequest {

    public String stageId;

    @Override
    protected String urlPath() {
        return "/personalData/getSubjectMistakeV2.do";
    }
}
