package com.yanxiu.gphone.student.questions.classify;

import com.yanxiu.gphone.student.base.BaseBean;

/**
 * 归类题的数据封装类
 * Created by 戴延枫 on 2017/7/13.
 */

public class ClassifyItemBean extends BaseBean {
    private String mContent;
    private boolean isRight = false;

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
