package com.yanxiu.gphone.student.questions.listen;


import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.readingcomplex.ReadingComplexFragment;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/9 14:47.
 * Function :
 */

public class ListenComplexQuestion extends BaseQuestion {

    public String url;

    public ListenComplexQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        url=bean.getQuestions().getUrl();
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ListenComplexFragment();
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
