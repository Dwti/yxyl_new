package com.yanxiu.gphone.student.questions.model;


import com.yanxiu.gphone.student.questions.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.fragment.reading.ReadingComplexFragment;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class ReadingComplexQuestion extends BaseQuestion {
    public ReadingComplexQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
    }

    @Override
    ExerciseBaseFragment answerFragment() {
        return new ReadingComplexFragment();
    }

    @Override
    ExerciseBaseFragment analysisFragment() {
        //解析
        return null;
    }

    @Override
    public Object getAnswer() {
        return null;
    }
}
