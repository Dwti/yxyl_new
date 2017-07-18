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

    private List<String> mCorrectAnswers = new ArrayList<>();

    private List<String> mFilledAnswers = new ArrayList<>();

    private List<String> mLeftChoices, mRightChoices;


    public ConnectQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
        initAnswer(bean);
    }

    private void initAnswer(PaperTestBean bean) {
        if (bean.getQuestions() == null || bean.getQuestions().getPad() == null || bean.getQuestions().getPad().getJsonAnswer() == null) {
            return;
        }
        mChoices = bean.getQuestions().getContent().getChoices();

        for (Object o : bean.getQuestions().getPad().getJsonAnswer()) {
            mFilledAnswers.add(String.valueOf(o));
        }
        //TODO 需要处理报错
        for(Object o : server_answer){
            mCorrectAnswers.add((String) o);
        }
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ConnectFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new ConnectAnalysisFragment();
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

    public List<String> getLeftChoices() {
        if (mLeftChoices == null)
            mLeftChoices = mChoices.subList(0, (mChoices.size() / 2));
        return mLeftChoices;
    }

    public List<String> getRightChoices() {
        if (mRightChoices == null)
            mRightChoices = mChoices.subList(mChoices.size() / 2, mChoices.size());
        return mRightChoices;
    }

    public List<String> getCorrectAnswer() {
        return mCorrectAnswers;
    }

    public List<String> getFilledAnswers() {
        return mFilledAnswers;
    }

}
