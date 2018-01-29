package com.yanxiu.gphone.student.http.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/25 14:55.
 * Function :
 */
public class CheckStudentTokenRequest extends EXueELianBaseRequest {

    @Override
    protected String urlPath() {
        return "/user/checkStdToken.do";
    }
}
