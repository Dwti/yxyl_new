package com.yanxiu.gphone.student.questions.connect;

import com.google.gson.internal.LinkedTreeMap;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.ConnectAnswerBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpeng on 2017/7/12.
 */

public class ConnectQuestion extends BaseQuestion {

    private List<String> mChoices;

    private List<String> mCorrectAnswers = new ArrayList<>();

    private List<String> mFilledAnswers = new ArrayList<>();

    private List<String> mLeftChoices, mRightChoices;


    public ConnectQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
        initAnswer(bean);
    }

    private void initAnswer(PaperTestBean bean) {

        mChoices = bean.getQuestions().getContent().getChoices();

        //处理用户已作答的答案
        if(bean.getQuestions().getPad() != null && bean.getQuestions().getPad().getJsonAnswer() != null){

            List<String> tempFilledAnswers = new ArrayList<>();
            for (Object o : bean.getQuestions().getPad().getJsonAnswer()) {
                tempFilledAnswers.add(String.valueOf(o));
            }

            for(String s : tempFilledAnswers){
                if(!s.contains(",")){
                    mFilledAnswers.add("");
                    continue;
                }
                int leftPos = Integer.parseInt(s.split(",")[0]);
                int rightPos = Integer.parseInt(s.split(",")[1]);
                if(rightPos >= mChoices.size() / 2){
                    rightPos = rightPos - mChoices.size() /2;
                }
                mFilledAnswers.add(leftPos + "," + rightPos);
            }
        }

        //处理本题的正确答案
        if(server_answer != null){
            List<String> tempCorrectAnswers = new ArrayList<>();

            for(Object o : server_answer){
                Map<String,String> map = (Map) o;
                for(Map.Entry<String,String> entry : map.entrySet()){
                    if(entry.getKey().equals("answer")){
                        tempCorrectAnswers.add(entry.getValue());
                    }
                }
            }

            for(String s : tempCorrectAnswers){
                int leftPos = Integer.parseInt(s.split(",")[0]);
                int rightPos = Integer.parseInt(s.split(",")[1]);
                if(rightPos >= mChoices.size() / 2){
                    rightPos = rightPos - mChoices.size() /2;
                }
                mCorrectAnswers.add(leftPos + "," + rightPos);
            }
        }

    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ConnectFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new ConnectAnalysisFragment();
    }

    @Override
    public Object getAnswer() {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    public List<String> getChoices() {
        return mChoices;
    }

    public List<String> getLeftChoices() {
        if (mLeftChoices == null)
            mLeftChoices = mChoices.subList(0, (mChoices.size() / 2));
        return mLeftChoices;
    }

    public List<String> getRightChoices() {
        if (mRightChoices == null)
            mRightChoices = mChoices.subList(mChoices.size() / 2, mChoices.size());
        return mRightChoices;
    }

    public List<String> getCorrectAnswer() {
        return mCorrectAnswers;
    }

    public List<String> getFilledAnswers() {
        return mFilledAnswers;
    }

}
