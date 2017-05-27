package com.yanxiu.gphone.student.homework.questions.model;

import com.yanxiu.gphone.student.homework.questions.QuestionShowType;
import com.yanxiu.gphone.student.homework.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.homework.questions.fragment.ExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.SingleChooseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class SingleChoiceQuestion extends BaseQuestion {
    private String answer;
    private List<String> choice;
    private List<String> AnswerList=new ArrayList<>();

    public SingleChoiceQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        answer= (String) bean.getQuestions().getAnswer().get(0);
        choice= bean.getQuestions().getContent().getChoices();
    }

    public List<String> getAnswerList() {
        return AnswerList;
    }

    public void setAnswerList(List<String> answerList) {
        AnswerList = answerList;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getChoice() {
        return choice;
    }

    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

    @Override
    ExerciseFragmentBase answerFragment() {
        return new SingleChooseFragment();
    }

    @Override
    ExerciseFragmentBase analysisFragment() {
//        return new SingleChooseAnalysisFragment();
        return null;
    }
}
