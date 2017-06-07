package com.yanxiu.gphone.student.questions.readingcomplex;


import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class ReadingComplexQuestion extends BaseQuestion {
    public ReadingComplexQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ReadingComplexFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        //解析
        return null;
    }

    @Override
    public Object getAnswer() {
        return null;
    }
}