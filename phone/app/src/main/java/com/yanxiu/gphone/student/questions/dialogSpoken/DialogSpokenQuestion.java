package com.yanxiu.gphone.student.questions.dialogSpoken;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/15 14:51.
 * Function :
 */
public class DialogSpokenQuestion extends BaseQuestion {

    public DialogSpokenQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new DialogSpokenFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new DialogAnalysisSpokenFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return new DialogWrongSpokenFragment();
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return new DialogRedoSpokenFragment();
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
