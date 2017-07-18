package com.yanxiu.gphone.student.mistake.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/17 9:46.
 * Function :
 */
public class MistakeAllRequest extends EXueELianBaseRequest {

    public String stageId;
    public String subjectId;
    public String ptype="2";
    /**
     * not use,but should be has
     * */
    public String currentPage="1";
    public String currentId;
    public String pageSize="10";

    @Override
    protected String urlPath() {
        return "/q/getWrongQsV2.do";
    }
}
