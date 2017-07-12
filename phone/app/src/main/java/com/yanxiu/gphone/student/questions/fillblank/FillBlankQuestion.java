package com.yanxiu.gphone.student.questions.fillblank;


import android.text.TextUtils;

import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;
import com.yanxiu.gphone.student.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/6/21.
 */

public class FillBlankQuestion extends BaseQuestion {

    private List<String> mFilledAnswers = new ArrayList<>();
    private List<String> mCorrectAnswers = new ArrayList<>();
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
        if (bean.getQuestions().getPad() == null || bean.getQuestions().getPad().getJsonAnswer() == null) {
            return;
        }
        for (Object o : bean.getQuestions().getPad().getJsonAnswer()) {
            mFilledAnswers.add(String.valueOf(o));
        }
        for(Object o : server_answer){
            mCorrectAnswers.add(String.valueOf(o));
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

    public List<String> getStringAnswers() {
        return mFilledAnswers;
    }

    public List<String> getCorrectAnswers() {
        return mCorrectAnswers;
    }

    @Override
    public Object getAnswer() {
        return mFilledAnswers;
    }

    @Override
    public int getStatus() {
        for (String str : mFilledAnswers) {
            if (TextUtils.isEmpty(str.trim())) {
                return Constants.ANSWER_STATUS_NOANSWERED;
            }
        }
        boolean isRight = QuestionUtil.compareListByOrder(StringUtil.full2half(mFilledAnswers), StringUtil.full2half(mCorrectAnswers));
        if (isRight) {
            return Constants.ANSWER_STATUS_RIGHT;
        } else {
            return Constants.ANSWER_STATUS_WRONG;
        }
    }

    public boolean isRight() {
        for (String str : mFilledAnswers) {
            if (TextUtils.isEmpty(str.trim())) {
                return false;
            }
        }
        boolean isRight = QuestionUtil.compareListByOrder(StringUtil.full2half(mFilledAnswers), StringUtil.full2half(mCorrectAnswers));
        return isRight;
    }

    public void setAnswer(List<String> list) {
        mFilledAnswers = list;
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
