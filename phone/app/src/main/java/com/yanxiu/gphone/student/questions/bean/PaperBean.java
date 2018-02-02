package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class PaperBean extends BaseBean {
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
    private PaperStatusBean paperStatus = new PaperStatusBean();
    private List<PaperTestBean> paperTest;
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
    private String cover;
    private long videoSize;
    private String videoUrl;
    private List<PointBean> weakPoints;

    public List<PointBean> getWeakPoints() {
        return weakPoints;
    }

    public void setWeakPoints(List<PointBean> weakPoints) {
        this.weakPoints = weakPoints;
    }

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

    public List<PaperTestBean> getPaperTest() {
        return paperTest;
    }

    public void setPaperTest(List<PaperTestBean> paperTest) {
        this.paperTest = paperTest;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
