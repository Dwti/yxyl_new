package com.yanxiu.gphone.student.homework.response;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sunpeng on 2017/5/8.
 */

public class PaperStatusBean extends BaseBean{
    private String tid;
    private String ppid;
    private int status ;
    private long endtime;
    private int costtime;
    private String gid;
    private int userCount;
    private int scoreRate;
    private String teachercomments;
    private String teacherName;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPpid() {
        return ppid;
    }

    public void setPpid(String ppid) {
        this.ppid = ppid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public int getCosttime() {
        return costtime;
    }

    public void setCosttime(int costtime) {
        this.costtime = costtime;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getScoreRate() {
        return scoreRate;
    }

    public void setScoreRate(int scoreRate) {
        this.scoreRate = scoreRate;
    }

    public String getTeachercomments() {
        return teachercomments;
    }

    public void setTeachercomments(String teachercomments) {
        this.teachercomments = teachercomments;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
