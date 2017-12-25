package com.yanxiu.gphone.student.questions.answerframe.bean;


import android.text.TextUtils;

import org.litepal.crud.DataSupport;

/**
 * 保存答案的数据库表
 * dyf
 */
public class AnswerBean extends DataSupport {

    private String aid;//key -- id+pid+qid
    private String answerJson;//答案json
    private boolean isAnswerd;//是否作答了
    private long startTime;//答题开始时间
    private long EndTime;//答题结束时间
    private String costTime;//题目累计答题时间
    private int answeredCount;//作答次数

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return EndTime;
    }

    public void setEndTime(long endTime) {
        EndTime = endTime;
    }

//    public long getCostTime() {
//        return TextUtils.isEmpty(costTime)?0:Long.parseLong(costTime);
//    }

//    public void setCostTime(long costTime) {
//        this.costTime = String.valueOf(costTime);
//    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public int getAnsweredCount() {
        return answeredCount;
    }

    public void setAnsweredCount(int answeredCount) {
        this.answeredCount = answeredCount;
    }

    public boolean isAnswerd() {
        return isAnswerd;
    }

    public void setAnswerd(boolean answerd) {
        isAnswerd = answerd;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }
}
