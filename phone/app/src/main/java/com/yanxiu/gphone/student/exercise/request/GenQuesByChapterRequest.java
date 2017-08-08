package com.yanxiu.gphone.student.exercise.request;

import com.yanxiu.gphone.student.base.EXueELianBaseRequest;
import com.yanxiu.gphone.student.util.LoginInfo;

import java.io.Serializable;

/**
 * Created by sunpeng on 2017/8/3.
 */

public class GenQuesByChapterRequest extends EXueELianBaseRequest implements Serializable {
    protected String stageId = LoginInfo.getStageid();
    protected String subjectId;
    protected String editionId;
    protected String volumeId;
    protected String chapterId;
    protected String sectionId ="0";
    protected String cellId = "0";
    protected String questNum="10";
    @Override
    protected String urlPath() {
        return "q/genSectionQBlock.do";
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getQuestNum() {
        return questNum;
    }

    public void setQuestNum(String questNum) {
        this.questNum = questNum;
    }
}
