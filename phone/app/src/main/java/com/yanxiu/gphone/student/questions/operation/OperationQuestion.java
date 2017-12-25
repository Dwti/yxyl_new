package com.yanxiu.gphone.student.questions.operation;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

/**
 * Created by sunpeng on 2017/12/25.
 */

public class OperationQuestion extends BaseQuestion{
    public OperationQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new OperationFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return null;
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return null;
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return null;
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
