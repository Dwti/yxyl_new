package com.yanxiu.gphone.student.homework.questions.model;

import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.homework.questions.QuestionConvertFactory;
import com.yanxiu.gphone.student.homework.questions.QuestionShowType;
import com.yanxiu.gphone.student.homework.questions.bean.PaperTestBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class ReadingComplexQuestion extends BaseQuestion {
    public ReadingComplexQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
    }

    @Override
    public Fragment getFragment() {
        return null;
    }
}
