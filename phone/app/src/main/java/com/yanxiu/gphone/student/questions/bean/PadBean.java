package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class PadBean extends BaseBean {
    private String answer;
    private String costtime;
    private String id;
    private List<Object> jsonAnswer;
    private String ptid;
    private int status;
    private String uid;
    private String audioComment;
    private String objectiveScore;
    private TeacherCheckBean teachercheck;
    private List<JsonAudioComment> jsonAudioComment;
    private List<AnalysisBean> analysis;

    public List<AnalysisBean> getAnalysis() {
        return analysis;
    }

    public void setAnalysis(List<AnalysisBean> analysis) {
        this.analysis = analysis;
    }

    public String getObjectiveScore() {
        return objectiveScore;
    }

    public void setObjectiveScore(String objectiveScore) {
        this.objectiveScore = objectiveScore;
    }

    public List<JsonAudioComment> getJsonAudioComment() {
        return jsonAudioComment;
    }

    public void setJsonAudioComment(List<JsonAudioComment> jsonAudioComment) {
        this.jsonAudioComment = jsonAudioComment;
    }

    public String getAudioComment() {
        return audioComment;
    }

    public void setAudioComment(String audioComment) {
        this.audioComment = audioComment;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCosttime() {
        return costtime;
    }

    public void setCosttime(String costtime) {
        this.costtime = costtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getJsonAnswer() {
        return jsonAnswer;
    }

    public void setJsonAnswer(List<Object> jsonAnswer) {
        this.jsonAnswer = jsonAnswer;
    }

    public String getPtid() {
        return ptid;
    }

    public void setPtid(String ptid) {
        this.ptid = ptid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public TeacherCheckBean getTeachercheck() {
        return teachercheck;
    }

    public void setTeachercheck(TeacherCheckBean teachercheck) {
        this.teachercheck = teachercheck;
    }
}
