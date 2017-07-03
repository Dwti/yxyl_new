package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class TeacherCheckBean extends BaseBean {
    /**
     * score : 4
     * qcomment : good
     * checktime : 1442633365526
     * padid : 12456
     * pid : 2345678
     * id : 234
     * tid : 34567789
     */
    private int score;
    private String qcomment;
    private long checktime;
    private int padid;
    private int pid;
    private int id;
    private int tid;

    public void setScore(int score) {
        this.score = score;
    }

    public void setQcomment(String qcomment) {
        this.qcomment = qcomment;
    }

    public void setChecktime(long checktime) {
        this.checktime = checktime;
    }

    public void setPadid(int padid) {
        this.padid = padid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getScore() {
        return score;
    }

    public String getQcomment() {
        return qcomment;
    }

    public long getChecktime() {
        return checktime;
    }

    public int getPadid() {
        return padid;
    }

    public int getPid() {
        return pid;
    }

    public int getId() {
        return id;
    }

    public int getTid() {
        return tid;
    }

    @Override
    public String toString() {
        return "TeachercheckEntity{" +
                "score=" + score +
                ", qcomment='" + qcomment + '\'' +
                ", checktime=" + checktime +
                ", padid=" + padid +
                ", pid=" + pid +
                ", id=" + id +
                ", tid=" + tid +
                '}';
    }
}
