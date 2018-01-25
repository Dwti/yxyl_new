package com.yanxiu.gphone.student.learning;

/**
 * Created by lufengqing on 2018/1/25.
 */

public class LearningEditionSelectChangeMessage {
    private String subjectId;
    private String editionId;

    public LearningEditionSelectChangeMessage() {
    }

    public LearningEditionSelectChangeMessage(String subjectId, String editionId) {
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

