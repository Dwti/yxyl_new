package com.yanxiu.gphone.student.startinfoupload;


/**
 * Created by sunpeng on 2017/3/30.
 */

public class AppStartInfoResponse {
    private StatusInfo status = new StatusInfo();
    private String result;

    public StatusInfo getStatus() {
        return status;
    }

    public void setStatus(StatusInfo status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private class StatusInfo{
        private String status;
        private String code;
        private String desc;
    }
}
