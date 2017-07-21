package com.yanxiu.gphone.student.questions.connect;

import android.support.annotation.NonNull;

/**
 * Created by sunpeng on 2017/7/13.
 */

public class ConnectItemBean implements Comparable<ConnectItemBean> {
    private String mText;
    private int mOriginPosition;

    public ConnectItemBean() {

    }

    public ConnectItemBean(String text, int position) {
        this.mText = text;
        this.mOriginPosition = position;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public int getOriginPosition() {
        return mOriginPosition;
    }

    public void setOriginPosition(int position) {
        this.mOriginPosition = position;
    }

    @Override
    public int compareTo(@NonNull ConnectItemBean o) {
        return this.mOriginPosition - o.mOriginPosition;
    }
}
