package com.yanxiu.gphone.student.homework.questions.model;

import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.homework.questions.QuestionShowType;
import com.yanxiu.gphone.student.homework.questions.bean.PaperTestBean;

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
