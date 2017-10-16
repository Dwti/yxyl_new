package com.yanxiu.gphone.student.questions.spoken;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/16 9:43.
 * Function :
 */
public class SpokenQuestion extends BaseQuestion {

    public SpokenQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new SpokenFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new SpokenAnalysisFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return new SpokenWrongFragment();
    }

    @Override
    public Object getAnswer() {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }
}
