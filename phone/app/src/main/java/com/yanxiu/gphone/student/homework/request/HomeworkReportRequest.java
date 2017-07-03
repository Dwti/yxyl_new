package com.yanxiu.gphone.student.homework.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;

/**
 * Created by sunpeng on 2017/7/3.
 */

public class HomeworkReportRequest extends EXueELianBaseRequest {

    protected String ppid;

    protected String flag = "1";

    @Override
    protected String urlPath() {
        return "q/getQReport.do?";
    }

    public String getPpid() {
        return ppid;
    }

    public void setPpid(String ppid) {
        this.ppid = ppid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
