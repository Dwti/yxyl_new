package com.yanxiu.gphone.student.questions.model;


import com.yanxiu.gphone.student.questions.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.fragment.ExerciseFragmentBase;
import com.yanxiu.gphone.student.questions.fragment.ReadingComplexFragment;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class ReadingComplexQuestion extends BaseQuestion {
    public ReadingComplexQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
    }

    @Override
    ExerciseFragmentBase answerFragment() {
        return new ReadingComplexFragment();
    }

    @Override
    ExerciseFragmentBase analysisFragment() {
        //解析
        return null;
    }

    @Override
    public Object getAnswer() {
        return null;
    }
}
