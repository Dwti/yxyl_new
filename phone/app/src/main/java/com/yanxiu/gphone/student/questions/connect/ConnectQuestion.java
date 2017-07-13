package com.yanxiu.gphone.student.questions.connect;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.ConnectAnswerBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/7/12.
 */

public class ConnectQuestion extends BaseQuestion {

    private List<String> mChoices;

    private List<ConnectAnswerBean> mCorrectAnswer = new ArrayList<>();


    public ConnectQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
        mChoices = bean.getQuestions().getContent().getChoices();
        //TODO 需要处理报错
//        for(Object o : bean.getQuestions().getAnswer()){
//            mCorrectAnswer.add((ConnectAnswerBean) o);
//        }
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
    public Object getAnswer() {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    public List<String> getChoices() {
        return mChoices;
    }

    public List<ConnectAnswerBean> getCorrectAnswer() {
        return mCorrectAnswer;
    }

}
