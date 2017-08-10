package com.yanxiu.gphone.student.exercise.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class ChapterBean extends Node<ChapterBean>{
    protected String id;
    protected String name;
    protected String question_num;

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

}
