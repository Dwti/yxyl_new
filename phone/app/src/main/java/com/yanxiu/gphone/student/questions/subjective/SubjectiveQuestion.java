package com.yanxiu.gphone.student.questions.subjective;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:06.
 * Function :
 */
public class SubjectiveQuestion extends BaseQuestion {

    public SubjectiveQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new SubjectiveFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return null;
    }

    @Override
    public Object getAnswer() {
        return null;
    }
}
