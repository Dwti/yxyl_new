package com.yanxiu.gphone.student.questions.spoken;

import com.google.gson.Gson;
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
 * Created by Canghaixiao.
 * Time : 2017/10/16 9:43.
 * Function :
 */
public class SpokenQuestion extends BaseQuestion {

    private String spokenAnswer;
    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private String objectiveScore;
    private List<String> answerList = new ArrayList<>();
    private String answer;

    public SpokenQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
//        if ("26".equals(getType_id())){
        //朗读题，答案获取方法与另外三种不同
        //需求更改（sever的锅）
        try {
            spokenAnswer = String.valueOf(bean.getQuestions().getContent().getAnswer().get(0));
        } catch (Exception e) {
            e.printStackTrace();
            spokenAnswer = "炸了";
        }
//        }else {
//            try {
//                spokenAnswer = String.valueOf(bean.getQuestions().getAnswer().get(0));
//            } catch (Exception e) {
//                e.printStackTrace();
//                spokenAnswer = "炸了";
//            }
//        }
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

    public String getAnswerResult(){
        return answer;
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
        return new SpokenFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new SpokenAnalysisFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return new SpokenWrongFragment();
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return new SpokenRedoFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

    @Override
    public int getScore() {
        if (answerList != null && !answerList.isEmpty()) {
            SpokenResponse response = getBeanFromJson(answerList.get(0));
            return getScore((int) response.lines.get(0).score);
        } else {
            return super.getScore();
        }
    }

    @Override
    public int getStatus() {
        int status = Constants.ANSWER_STATUS_NOANSWERED;
        if (answerList != null && !answerList.isEmpty()) {
            SpokenResponse response = getBeanFromJson(answerList.get(0));
            int score = getScore((int) response.lines.get(0).score);
            switch (score) {
                case 0:
                case 1:
                    status = Constants.ANSWER_STATUS_WRONG;
                    break;
                case 2:
                case 3:
                    status = Constants.ANSWER_STATUS_RIGHT;
                    break;
            }
        }
        return status;
    }

    public static SpokenResponse getBeanFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SpokenResponse.class);
    }

    public static int getScore(int score) {
        if (score < 30) {
            return 0;
        } else if (score >= 30 && score < 60) {
            return 1;
        } else if (score >= 60 && score < 80) {
            return 2;
        } else {
            return 3;
        }
    }
}
