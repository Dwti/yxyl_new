package com.yanxiu.gphone.student.user.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/23 11:12.
 * Function :
 */
public class JoinClassRequest extends EXueELianBaseRequest {

    public String classId;

    @Override
    protected String urlPath() {
        return "/class/searchClass.do";
    }
}
