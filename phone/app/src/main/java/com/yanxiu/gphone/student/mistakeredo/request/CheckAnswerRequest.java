package com.yanxiu.gphone.student.mistakeredo.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/12/20 16:51.
 * Function :
 */
public class CheckAnswerRequest extends EXueELianBaseRequest {

    public String answers;

    @Override
    protected HttpType httpType() {
        return HttpType.GET;
    }

    @Override
    protected String urlPath() {
        return "/q/checkAnswer";
    }
}
