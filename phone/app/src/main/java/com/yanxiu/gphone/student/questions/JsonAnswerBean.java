package com.yanxiu.gphone.student.questions;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class JsonAnswerBean {
    private List<String> answer;
    private String qid;

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }
}
