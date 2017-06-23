package com.yanxiu.gphone.student.questions.answerframe.bean;


import org.litepal.crud.DataSupport;

/**
 * 保存答案的数据库表
 * dyf
 */
public class AnswerBean extends DataSupport {

    private String aid;//key -- id+pid+qid
    private String answerJson;//答案json

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
