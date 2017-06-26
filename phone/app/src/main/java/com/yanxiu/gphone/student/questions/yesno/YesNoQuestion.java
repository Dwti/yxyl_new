package com.yanxiu.gphone.student.questions.yesno;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫 on 2017/6/7.
 */

public class YesNoQuestion extends BaseQuestion {
    private String yesNoAnswer;
    private List<String> choice;
    private List<String> answerList = new ArrayList<>();

    public YesNoQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        yesNoAnswer = String.valueOf(bean.getQuestions().getAnswer().get(0)) ;
//        choice= bean.getQuestions().getContent().getChoices();
        choice = new ArrayList<>(2);
        choice.add("正确");
        choice.add("错误");
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public String getYesNoAnswer() {
        return yesNoAnswer;
    }

    public void setYesNoAnswer(String yesNoAnswer) {
        this.yesNoAnswer = yesNoAnswer;
    }

    public List<String> getChoice() {
        return choice;
    }

    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new YesNoFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new YesNoAnalysisFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

}
