package com.yanxiu.gphone.student.questionssunpeng.model;

import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.questionssunpeng.QuestionShowType;
import com.yanxiu.gphone.student.questionssunpeng.source.PaperTestBean;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class SingleChoiceQuestion extends BaseQuestion {
    private String url;
    public SingleChoiceQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        this.url = bean.getQuestions().getUrl();
    }

    @Override
    public Fragment getFragment() {
        return null;
    }
}
