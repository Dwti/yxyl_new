package com.yanxiu.gphone.student.questions.connect;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

/**
 * Created by sunpeng on 2017/7/12.
 */

public class ConnectQuestion extends BaseQuestion {


    public ConnectQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ConnectFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new ConnectFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
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
