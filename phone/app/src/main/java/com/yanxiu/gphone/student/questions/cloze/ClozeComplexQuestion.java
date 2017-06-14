package com.yanxiu.gphone.student.questions.cloze;


import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.readingcomplex.ReadingComplexFragment;

/**
 * Created by sunpeng on 2017/6/14.
 */

public class ClozeComplexQuestion extends BaseQuestion {
    public ClozeComplexQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ClozeComplexFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        //解析
        return new ClozeComplexFragment();
    }

    @Override
    public Object getAnswer() {
        return null;
    }
}
