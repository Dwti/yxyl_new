package com.yanxiu.gphone.student.questions.dialogSpoken;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2018/1/15 14:51.
 * Function :
 */
public class DialogSpokenQuestion extends BaseQuestion {

    private String spokenAnswer;
    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private String objectiveScore;
    private List<String> answerList = new ArrayList<>();
    private String answer;

    public DialogSpokenQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
        try {
            spokenAnswer = String.valueOf(bean.getQuestions().getContent().getAnswer().get(0));
        } catch (Exception e) {
            e.printStackTrace();
            spokenAnswer = "炸了";
        }
        try {
            answer = String.valueOf(bean.getQuestions().getAnswer().get(0));
        } catch (Exception e) {
            e.printStackTrace();
            answer = "答案异常";
        }
        pointList = bean.getQuestions().getPoint();
        try {
            starCount = Integer.parseInt(bean.getQuestions().getDifficulty());
        } catch (Exception e) {
            e.printStackTrace();
        }
        questionAnalysis = bean.getQuestions().getAnalysis();
        try {
            objectiveScore = bean.getQuestions().getPad().getObjectiveScore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String jsonArray = bean.getQuestions().getPad().getAnswer();
            JSONArray array;
            array = new JSONArray(jsonArray);
            for (int i = 0; i < array.length(); i++) {
                answerList.add(array.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSpokenAnswer() {
        return spokenAnswer;
    }

    public List<PointBean> getPointList() {
        return pointList;
    }

    public int getStarCount() {
        return starCount;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public String getObjectiveScore() {
        return objectiveScore;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new DialogSpokenFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new DialogAnalysisSpokenFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return new DialogWrongSpokenFragment();
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return new DialogRedoSpokenFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

    @Override
    public int getStatus() {
        return 0;
    }
}
