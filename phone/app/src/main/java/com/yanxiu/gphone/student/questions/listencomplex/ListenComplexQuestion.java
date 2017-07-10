package com.yanxiu.gphone.student.questions.listencomplex;


import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/9 14:47.
 * Function :
 */

public class ListenComplexQuestion extends BaseQuestion {

    public String url;

    public ListenComplexQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType,paperStatus);
        url=bean.getQuestions().getUrl();
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ListenAnswerComplexFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        //解析
        return new ListenAnalysisComplexFragment();
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
