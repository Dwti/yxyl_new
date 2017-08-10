package com.yanxiu.gphone.student.user.feedback.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.util.DeviceUtil;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/31 10:06.
 * Function :
 */
public class FeedbackRequest extends EXueELianBaseRequest {

    public String os= Constants.OS;
    public String osversion= DeviceUtil.getOSVersionCode();
    public String brand=DeviceUtil.getBrandName();
    public String content;

    @Override
    protected String urlPath() {
        return "/common/feedback.do";
    }
}
