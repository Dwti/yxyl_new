package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class ContentBean extends BaseBean {
    private List<String> answer;
    private List<String> choices;
    private String leftCount;
    private String uselessNode;

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(String leftCount) {
        this.leftCount = leftCount;
    }

    public String getUselessNode() {
        return uselessNode;
    }

    public void setUselessNode(String uselessNode) {
        this.uselessNode = uselessNode;
    }
}
