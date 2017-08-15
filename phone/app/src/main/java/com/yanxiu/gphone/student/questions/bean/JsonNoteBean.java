package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/22 11:51.
 * Function :
 */
public class JsonNoteBean extends BaseBean{

    private ArrayList<String> images=new ArrayList<>();
    private String qid;
    private String wqid;
    private String text;

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getWqid() {
        return wqid;
    }

    public void setWqid(String wqid) {
        this.wqid = wqid;
    }

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
}
