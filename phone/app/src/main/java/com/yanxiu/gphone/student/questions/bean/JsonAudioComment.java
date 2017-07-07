package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/6 12:11.
 * Function :
 */
public class JsonAudioComment extends BaseBean{

    private int length;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
