package com.yanxiu.gphone.student.homework.questions.model;

import com.yanxiu.gphone.student.homework.questions.QuestionShowType;
import com.yanxiu.gphone.student.homework.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.homework.questions.fragment.ExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.MultiChooseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/26 15:34.
 * Function :
 */
public class MultiChoiceQuestion extends BaseQuestion {
    private List<String> answer=new ArrayList<>();
    private List<String> choice;

    public MultiChoiceQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        answer.clear();
        List<Object> data=bean.getQuestions().getAnswer();
        for (Object o:data){
            answer.add((String) o);
        }
        choice= bean.getQuestions().getContent().getChoices();
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
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
        return new MultiChooseFragment();
    }

    @Override
    ExerciseFragmentBase analysisFragment() {
        return null;
    }
}
