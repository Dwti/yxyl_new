package com.yanxiu.gphone.student.homework.questions.model;

import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.homework.questions.QuestionShowType;
import com.yanxiu.gphone.student.homework.questions.source.PadBean;
import com.yanxiu.gphone.student.homework.questions.source.PaperTestBean;
import com.yanxiu.gphone.student.homework.questions.source.PointBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public abstract class BaseQuestion {
    protected String id;
    protected String correctRate;
    protected String difficulty;
    protected String isfavorite;
    protected String knowledgepoint;
    protected String parentid;
    protected String pid;
    protected String qid;
    protected String qtype;
    protected String analysis;
    //下面这些字段需要需要具体的题型去设置，并不是每个题型都有的（到时候字段的设置需要再检查一遍）
//    protected List<Object> answer;
//    protected ContentBean content;
//    protected String memo;
    protected PadBean pad;
    protected List<PointBean> point;
    protected String stem;
    protected String submit_way;
    protected String template;
    protected String type_id;
    protected String sectionid;
    protected String typeid;
    protected QuestionShowType showType;

    public BaseQuestion(PaperTestBean bean,QuestionShowType showType){
        this.id = bean.getId();
        this.correctRate = bean.getCorrectRate();
        this.difficulty = bean.getDifficulty();
        this.isfavorite = bean.getIsfavorite();
        this.knowledgepoint = bean.getKnowledgepoint();
        this.parentid = bean.getParentid();
        this.pid = bean.getPid();
        this.qid = bean.getQid();
        this.qtype = bean.getQtype();
        this.analysis = bean.getQuestions().getAnalysis();
        this.pad = bean.getQuestions().getPad();
        this.point = bean.getQuestions().getPoint();
        this.stem = bean.getQuestions().getStem();
        this.submit_way = bean.getQuestions().getSubmit_way();
        this.template = bean.getQuestions().getTemplate();
        this.type_id = bean.getQuestions().getType_id();
        this.sectionid = bean.getSectionid();
        this.typeid = bean.getTypeid();
        this.showType = showType;
    }

    public abstract Fragment getFragment();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public PadBean getPad() {
        return pad;
    }

    public void setPad(PadBean pad) {
        this.pad = pad;
    }

    public List<PointBean> getPoint() {
        return point;
    }

    public void setPoint(List<PointBean> point) {
        this.point = point;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getSubmit_way() {
        return submit_way;
    }

    public void setSubmit_way(String submit_way) {
        this.submit_way = submit_way;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
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

    public QuestionShowType getShowType() {
        return showType;
    }

    public void setShowType(QuestionShowType showType) {
        this.showType = showType;
    }
}
