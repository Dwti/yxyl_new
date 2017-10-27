package com.yanxiu.gphone.student.bcresource.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by sp on 17-10-27.
 */

public class TopicPaperStatusBean extends DataSupport {
    private boolean hasAnswered = false;
    private String aid;

    public boolean isHasAnswered() {
        return hasAnswered;
    }

    public void setHasAnswered(boolean hasAnswered) {
        this.hasAnswered = hasAnswered;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }
}
