package com.yanxiu.gphone.student.bcresource.bean;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sunpeng on 2017/10/16.
 */

public class TopicBean extends BaseBean {
    private String id;
    private String name;
    private String viewnum;
    private PaperStatusBean paperStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewnum() {
        return viewnum;
    }

    public void setViewnum(String viewnum) {
        this.viewnum = viewnum;
    }

    public PaperStatusBean getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(PaperStatusBean paperStatus) {
        this.paperStatus = paperStatus;
    }

    public static class PaperStatusBean{
        private int status;
        private String ppid;
        private float scoreRate;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPpid() {
            return ppid;
        }

        public void setPpid(String ppid) {
            this.ppid = ppid;
        }

        public float getScoreRate() {
            return scoreRate;
        }

        public void setScoreRate(float scoreRate) {
            this.scoreRate = scoreRate;
        }
    }
}
