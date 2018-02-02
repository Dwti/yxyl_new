package com.yanxiu.gphone.student.learning.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

/**
 * Created by lufengqing on 2018/1/31.
 */

public class GetRelatedCourseRequest extends EXueELianBaseRequest {
    protected String points;
    protected String excludeId;
    protected String limit = "6";

    @Override
    protected String urlPath() {
        return "study/getRelatedCourse.do?";
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getExcludeId() {
        return excludeId;
    }

    public void setExcludeId(String excludeId) {
        this.excludeId = excludeId;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
