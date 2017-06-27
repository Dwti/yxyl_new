package com.yanxiu.gphone.student.questions.fillblank;

import android.util.Log;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/6/21.
 */

public class FillBlankQuestion extends BaseQuestion {

    private List<String> mAnswers = new ArrayList<>();
    public FillBlankQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        initAnswer(bean);
    }

    private void initAnswer(PaperTestBean bean){
        for (Object o: bean.getQuestions().getPad().getJsonAnswer()){
            mAnswers.add(String.valueOf(o));
        }
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new FillBlankFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return null;
    }

    public List<String> getStringAnswers(){
        return mAnswers;
    }

    @Override
    public Object getAnswer() {
        return mAnswers;
    }

    public void setAnswer(List<String> list){
        mAnswers = list;
    }
}