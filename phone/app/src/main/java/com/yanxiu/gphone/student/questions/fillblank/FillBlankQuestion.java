package com.yanxiu.gphone.student.questions.fillblank;


import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;
import com.yanxiu.gphone.student.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/6/21.
 */

public class FillBlankQuestion extends BaseQuestion {

    private List<String> filledAnswers = new ArrayList<>();
    private List<String> correctAnswers = new ArrayList<>();
    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private String answerCompare;

    public FillBlankQuestion(PaperTestBean bean, QuestionShowType showType,String paperStatus) {
        super(bean, showType,paperStatus);
        pointList = bean.getQuestions().getPoint();
        try {
            starCount = Integer.parseInt(bean.getQuestions().getDifficulty());
        } catch (Exception e) {
            e.printStackTrace();
        }
        questionAnalysis = bean.getQuestions().getAnalysis();
        try {
            answerCompare = bean.getQuestions().getExtend().getData().getAnswerCompare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initAnswer(bean);
    }

    private void initAnswer(PaperTestBean bean) {
        for(Object o : server_answer){
            correctAnswers.add(String.valueOf(o));
        }

        if (bean.getQuestions().getPad() != null && bean.getQuestions().getPad().getAnswer() != null) {
            try {
                JSONArray jsonArray = new JSONArray(bean.getQuestions().getPad().getAnswer());
                for(int i =0;i<jsonArray.length();i++){
                    filledAnswers.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new FillBlankFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new FillBlankAnalysisFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return new FillBlankWrongFragment();
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return new FillBlankRedoFragment();
    }

    public List<String> getStringAnswers() {
        return filledAnswers;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    @Override
    public Object getAnswer() {
        return filledAnswers;
    }

    @Override
    public int getStatus() {
        if (showType.equals(QuestionShowType.MISTAKE_REDO) || showType.equals(QuestionShowType.ANSWER)||isMisTakeRedo()||getPad().getAnalysis()==null) {
            return getSta();
        } else {
            List<AnalysisBean> analysis=getPad().getAnalysis();
            List<Object> answer= getBean().getQuestions().getAnswer();
            int status;
            if (analysis.size()!=answer.size()){
                status=Constants.ANSWER_STATUS_WRONG;
            }else {
                status=Constants.ANSWER_STATUS_RIGHT;
            }

            for (AnalysisBean analysisBean:analysis){
                if (!AnalysisBean.RIGHT.equals(analysisBean.status)){
                    status=Constants.ANSWER_STATUS_WRONG;
                }
            }

            if (status==Constants.ANSWER_STATUS_WRONG){
                int flag=0;
                for (int j=0;j<analysis.size();j++){
                    AnalysisBean analysisBean=analysis.get(j);
                    boolean b=false;
                    if (AnalysisBean.RIGHT.equals(analysisBean.status)){
                        b=true;
                    }
                    if (j==0){
                        if (b){
                            flag=1;
                        }else {
                            flag=2;
                        }
                    }else {
                        if (b){
                            if (flag==2){
                                flag=3;
                            }
                        }else {
                            if (flag==1){
                                flag=3;
                            }
                        }
                    }
                }
                if (flag==3) {
                    status=Constants.ANSWER_STATUS_HALFRIGHT;
                }
            }
            return status;
        }
    }

    private int getSta() {
        for (String str : filledAnswers) {
            if (TextUtils.isEmpty(str.trim())) {
                return Constants.ANSWER_STATUS_NOANSWERED;
            }
        }
        boolean isRight = QuestionUtil.compareListByOrder(StringUtil.full2half(filledAnswers), StringUtil.full2half(correctAnswers));
        if (isRight) {
            return Constants.ANSWER_STATUS_RIGHT;
        } else {
            return Constants.ANSWER_STATUS_WRONG;
        }
    }

    /**
     * 这是项目，不是你的玩具，你想咋样就咋样，项目不要特立独行，留下一堆坑
     * */
    public boolean isRight() {
        for (String str : filledAnswers) {
            if (TextUtils.isEmpty(str.trim())) {
                return false;
            }
        }
        boolean isRight = QuestionUtil.compareListByOrder(StringUtil.full2half(filledAnswers), StringUtil.full2half(correctAnswers));
        return isRight;
    }

    public void setAnswer(List<String> list) {
        filledAnswers = list;
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

    public String getAnswerCompare() {
        return answerCompare;
    }
}
