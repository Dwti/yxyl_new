package com.yanxiu.gphone.student.learning.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by lufengqing on 2018/1/31.
 */

public class AddResViewNumRequest extends EXueELianBaseRequest {
    protected String resId;
    @Override
    protected String urlPath() {
        return "study/addResViewNum.do?";
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }
}
