package com.yanxiu.gphone.student.mistakeredo.bean;

import com.yanxiu.gphone.student.exercise.bean.Node;

import java.util.List;

/**
 * Created by sp on 17-11-21.
 */

public class WrongQPointBean extends Node<WrongQPointBean> {
    private String id;
    private String name;
    private String question_num;
    private List<String> qids;

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

    public String getQuestion_num() {
        return question_num;
    }

    public void setQuestion_num(String question_num) {
        this.question_num = question_num;
    }

    public List<String> getQids() {
        return qids;
    }

    public void setQids(List<String> qids) {
        this.qids = qids;
    }
}
