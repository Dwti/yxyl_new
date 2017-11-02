package com.yanxiu.gphone.student.bcresource.bean;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sunpeng on 2017/11/2.
 */

public class TopicPaperStatusChangeMessage extends BaseBean {
    private int code;  //0:重新作答；1：提交完成
    private TopicBean.PaperStatusBean paperStatus;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public TopicBean.PaperStatusBean getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(TopicBean.PaperStatusBean paperStatus) {
        this.paperStatus = paperStatus;
    }
}
