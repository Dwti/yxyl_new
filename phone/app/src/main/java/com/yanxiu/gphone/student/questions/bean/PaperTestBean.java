package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class PaperTestBean extends BaseBean {
    private String correctRate;
    private String difficulty;
    private String id;
    private String isfavorite;
    private String knowledgepoint;
    private String parentid;
    private String pid;
    private String qid;
    private String qtype;
    private QuestionBean questions;
    private String sectionid;
    private String typeid;
    private int wqid;
    private int wqnumber;

    public int getWqid() {
        return wqid;
    }

    public void setWqid(int wqid) {
        this.wqid = wqid;
    }

    public int getWqnumber() {
        return wqnumber;
    }

    public void setWqnumber(int wqnumber) {
        this.wqnumber = wqnumber;
    }

    public String getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(String correctRate) {
        this.correctRate = correctRate;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(String isfavorite) {
        this.isfavorite = isfavorite;
    }

    public String getKnowledgepoint() {
        return knowledgepoint;
    }

    public void setKnowledgepoint(String knowledgepoint) {
        this.knowledgepoint = knowledgepoint;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public QuestionBean getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionBean questions) {
        this.questions = questions;
    }

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

}
