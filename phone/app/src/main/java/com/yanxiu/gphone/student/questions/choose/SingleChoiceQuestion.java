package com.yanxiu.gphone.student.questions.choose;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class SingleChoiceQuestion extends BaseQuestion {
    private String singleAnswer;
    private List<String> choice;
    private List<String> answerList=new ArrayList<>();

    public SingleChoiceQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        singleAnswer= String.valueOf(bean.getQuestions().getAnswer().get(0));
        choice= bean.getQuestions().getContent().getChoices();
        String jsonArray=bean.getQuestions().getPad().getAnswer();
        JSONArray array;
        try {
            array=new JSONArray(jsonArray);
            for (int i=0;i<array.length();i++){
                answerList.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public String getSingleAnswer() {
        return singleAnswer;
    }

    public void setSingleAnswer(String singleAnswer) {
        this.singleAnswer = singleAnswer;
    }

    public List<String> getChoice() {
        return choice;
    }

    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new SingleChooseFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
//        return new SingleChooseAnalysisFragment();
        return null;
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

}
