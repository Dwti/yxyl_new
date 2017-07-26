package com.yanxiu.gphone.student.user.setting.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/26 16:35.
 * Function :
 */
public class CheckMobileRequest extends EXueELianBaseRequest {

    public String token= Constants.token;
    public String mobile;
    public String code;

    @Override
    protected String urlPath() {
        return "/user/checkMobileMsgCode.do";
    }
}
