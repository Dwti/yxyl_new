package com.yanxiu.gphone.student.questions;

import com.yanxiu.gphone.student.util.ExerciseRequestBase;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class PaperRequest extends ExerciseRequestBase {
    private String paperId = "279219";
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/personalData/";
    }

    @Override
    protected String urlPath() {
        return "getQuestionList.do";
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }
}
