package com.yanxiu.gphone.student.questions.yesno;

import android.text.TextUtils;

import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫 on 2017/6/7.
 */

public class YesNoQuestion extends BaseQuestion {
    private String yesNoAnswer;
    private List<String> choice;
    private List<String> answerList = new ArrayList<>();
    private String answerCompare;
    private int starCount;
    private String questionAnalysis;
    private List<PointBean> pointList;

    public YesNoQuestion(PaperTestBean bean, QuestionShowType showType,String paperStatus) {
        super(bean, showType,paperStatus);
        yesNoAnswer = String.valueOf(bean.getQuestions().getAnswer().get(0));
        if(!TextUtils.isEmpty(yesNoAnswer) && yesNoAnswer.contains(".")){ //系统有可能会把0转化为0.0，导致出错
            yesNoAnswer = yesNoAnswer.substring(0,yesNoAnswer.indexOf("."));
        }
//        choice= bean.getQuestions().getContent().getChoices();
        choice = new ArrayList<>(2);
        choice.add("正确");
        choice.add("错误");
        pointList = bean.getQuestions().getPoint();
        questionAnalysis = bean.getQuestions().getAnalysis();
        try {
            starCount = Integer.parseInt(bean.getQuestions().getDifficulty());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            answerCompare = bean.getQuestions().getExtend().getData().getAnswerCompare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String jsonArray = bean.getQuestions().getPad().getAnswer();
            JSONArray array;
            array = new JSONArray(jsonArray);
            answerList.add(array.getString(0));
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
    public ExerciseBaseFragment wrongFragment() {
        return new YesNoWrongFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

    @Override
    public int getStatus() {
        if (answerList!=null&&answerList.size()>0){
            if (answerList.get(0).equals(yesNoAnswer)){
                return Constants.ANSWER_STATUS_RIGHT;
            }else {
                return Constants.ANSWER_STATUS_WRONG;
            }
        }
        return Constants.ANSWER_STATUS_NOANSWERED;
    }

    public String getAnswerCompare() {
        return answerCompare;
    }

    public int getStarCount() {
        return starCount;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public List<PointBean> getPointList() {
        return pointList;
    }

}
