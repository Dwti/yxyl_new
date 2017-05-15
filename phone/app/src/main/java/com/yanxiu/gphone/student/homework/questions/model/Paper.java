package com.yanxiu.gphone.student.homework.questions.model;

import com.yanxiu.gphone.student.homework.questions.source.PaperStatusBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class Paper {
    private String authorid;
    private String bedition;
    private String begintime;
    private String buildtime;
    private String chapterid;
    private String classid;
    private String editionName;
    private String endtime;
    private String id;
    private String name;
    private PaperStatusBean paperStatus;
    private List<BaseQuestion> questions;
    private String parentId;
    private String ptype;
    private String quesnum;
    private String redoDays;
    private String sectionid;
    private String showana;
    private String stageName;
    private String stageid;
    private String status;
    private String subjectName;
    private String subjectid;
    private String subquesnum;
    private String volume;
    private String volumeName;

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getBedition() {
        return bedition;
    }

    public void setBedition(String bedition) {
        this.bedition = bedition;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getBuildtime() {
        return buildtime;
    }

    public void setBuildtime(String buildtime) {
        this.buildtime = buildtime;
    }

    public String getChapterid() {
        return chapterid;
    }

    public void setChapterid(String chapterid) {
        this.chapterid = chapterid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

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

    public PaperStatusBean getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(PaperStatusBean paperStatus) {
        this.paperStatus = paperStatus;
    }

    public List<BaseQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<BaseQuestion> questions) {
        this.questions = questions;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getQuesnum() {
        return quesnum;
    }

    public void setQuesnum(String quesnum) {
        this.quesnum = quesnum;
    }

    public String getRedoDays() {
        return redoDays;
    }

    public void setRedoDays(String redoDays) {
        this.redoDays = redoDays;
    }

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public String getShowana() {
        return showana;
    }

    public void setShowana(String showana) {
        this.showana = showana;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStageid() {
        return stageid;
    }

    public void setStageid(String stageid) {
        this.stageid = stageid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getSubquesnum() {
        return subquesnum;
    }

    public void setSubquesnum(String subquesnum) {
        this.subquesnum = subquesnum;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }
}
