package com.yanxiu.gphone.student.questions.choose;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/26 15:34.
 * Function :
 */
public class MultiChoiceQuestion extends BaseQuestion {
    private List<String> multianswer=new ArrayList<>();
    private List<String> choice;
    private List<String> answerList=new ArrayList<>();

    public MultiChoiceQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        multianswer.clear();
        List<Object> data=bean.getQuestions().getAnswer();
        for (Object o:data){
            multianswer.add((String) o);
        }
        choice= bean.getQuestions().getContent().getChoices();
    }

    public List<String> getMultianswer() {
        return multianswer;
    }

    public void setMultianswer(List<String> multianswer) {
        this.multianswer = multianswer;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public List<String> getChoice() {
        return choice;
    }

    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new MultiChooseFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return null;
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }
}
