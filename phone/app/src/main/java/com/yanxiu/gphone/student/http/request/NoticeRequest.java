package com.yanxiu.gphone.student.http.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2018/2/5 16:32.
 * Function :
 */
public class NoticeRequest extends EXueELianBaseRequest {
    @Override
    protected String urlPath() {
        return "/user/getNotice.do";
    }
}
