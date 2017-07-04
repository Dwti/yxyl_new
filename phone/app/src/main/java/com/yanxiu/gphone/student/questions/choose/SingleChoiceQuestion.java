package com.yanxiu.gphone.student.questions.choose;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.PointBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class SingleChoiceQuestion extends BaseQuestion {
    private String singleAnswer;
    private List<String> choice;
    private List<String> answerList=new ArrayList<>();
    private List<PointBean> pointList=new ArrayList<>();

    public SingleChoiceQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        singleAnswer= String.valueOf(bean.getQuestions().getAnswer().get(0));
        choice= bean.getQuestions().getContent().getChoices();
        pointList=bean.getQuestions().getPoint();
        try {
            String jsonArray=bean.getQuestions().getPad().getAnswer();
            JSONArray array;
            array=new JSONArray(jsonArray);
            for (int i=0;i<array.length();i++){
                answerList.add(array.getString(i));
            }
        } catch (Exception e) {
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

    public List<PointBean> getPointList() {
        return pointList;
    }

    public void setPointList(List<PointBean> pointList) {
        this.pointList = pointList;
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new SingleChooseFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new SingleChooseAnalysisFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

}
