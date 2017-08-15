package com.yanxiu.gphone.student.questions.answerframe.ui.bean;

import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/10 14:46.
 * Function :
 */
public class NoteBean {
    public String qid;
    public String text;
    public ArrayList<String> images = new ArrayList<>();

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
