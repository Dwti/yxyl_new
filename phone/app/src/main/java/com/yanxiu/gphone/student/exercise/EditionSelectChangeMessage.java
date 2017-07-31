package com.yanxiu.gphone.student.exercise;

/**
 * Created by sp on 17-7-28.
 */

public class EditionSelectChangeMessage {
    private String subjectId;
    private String editionId;

    public EditionSelectChangeMessage() {
    }

    public EditionSelectChangeMessage(String subjectId, String editionId) {
        this.subjectId = subjectId;
        this.editionId = editionId;
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
}
