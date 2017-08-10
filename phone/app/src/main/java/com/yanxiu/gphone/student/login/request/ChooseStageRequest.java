package com.yanxiu.gphone.student.login.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/31 11:43.
 * Function :
 */
public class ChooseStageRequest extends EXueELianBaseRequest {

    public String stageid;

    @Override
    protected String urlPath() {
        return "/personalData/updateUserInfo.do";
    }
}
