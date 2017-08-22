package com.yanxiu.gphone.student.questions.connect;

import android.text.TextUtils;

import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpeng on 2017/7/12.
 */

public class ConnectQuestion extends BaseQuestion {

    private List<String> choices;
    private List<String> leftChoices, rightChoices;
    private List<String> correctAnswers = new ArrayList<>(); //转换过的，右边以一半计数，比如choices的大小为10，则"0,5"为"0,0";
    private List<String> filledAnswers = new ArrayList<>();//转换过的，右边以一半计数，比如choices的大小为10，则"0,5"为"0,0";
    private List<String> serverCorrectAnswers = new ArrayList<>();
    private List<String> serverFilledAnswers = new ArrayList<>();//为转换过的，同上，"0,5"就是为"0,5"

    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private String answerCompare;


    public ConnectQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
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

        choices = bean.getQuestions().getContent().getChoices();

        //处理用户已作答的答案
        if(bean.getQuestions().getPad() != null && bean.getQuestions().getPad().getAnswer() != null){

            try {
                JSONArray jsonArray = new JSONArray(bean.getQuestions().getPad().getAnswer());
                for(int i =0;i<jsonArray.length();i++){
                    serverFilledAnswers.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(String s : serverFilledAnswers){
                if(!s.contains(",")){
                    filledAnswers.add("");
                    continue;
                }
                int leftPos = Integer.parseInt(s.split(",")[0]);
                int rightPos = Integer.parseInt(s.split(",")[1]);
                if(rightPos >= choices.size() / 2){
                    rightPos = rightPos - choices.size() /2;
                }
                filledAnswers.add(leftPos + "," + rightPos);
            }
        }

        //处理本题的正确答案
        if(server_answer != null){

            for(Object o : server_answer){
                Map<String,String> map = (Map) o;
                for(Map.Entry<String,String> entry : map.entrySet()){
                    if(entry.getKey().equals("answer")){
                        serverCorrectAnswers.add(entry.getValue());
                    }
                }
            }

            for(String s : serverCorrectAnswers){
                int leftPos = Integer.parseInt(s.split(",")[0]);
                int rightPos = Integer.parseInt(s.split(",")[1]);
                if(rightPos >= choices.size() / 2){
                    rightPos = rightPos - choices.size() /2;
                }
                correctAnswers.add(leftPos + "," + rightPos);
            }
        }

    }

    public boolean isRight(){
        if(serverFilledAnswers == null)
            return false;
        if(serverFilledAnswers.containsAll(serverCorrectAnswers) && serverCorrectAnswers.containsAll(serverFilledAnswers))
            return true;
        else return false;
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
    public ExerciseBaseFragment wrongFragment() {
        return new ConnectWrongFragment();
    }

    @Override
    public Object getAnswer() {
        return serverFilledAnswers;
    }

    @Override
    public int getStatus() {
        for (String str : serverFilledAnswers) {
            if (TextUtils.isEmpty(str.trim())) {
                return Constants.ANSWER_STATUS_NOANSWERED;
            }
        }
        if (isRight()) {
            return Constants.ANSWER_STATUS_RIGHT;
        } else {
            return Constants.ANSWER_STATUS_WRONG;
        }
    }

    public List<String> getChoices() {
        return choices;
    }

    public List<String> getLeftChoices() {
        if (leftChoices == null)
            leftChoices = new ArrayList<>(choices.subList(0, (choices.size() / 2)));
        return leftChoices;
    }

    public List<String> getRightChoices() {
        if (rightChoices == null)
            rightChoices = new ArrayList<>(choices.subList(choices.size() / 2, choices.size()));
        return rightChoices;
    }

    public List<String> getCorrectAnswer() {
        return correctAnswers;
    }

    public List<String> getFilledAnswers() {
        return filledAnswers;
    }

    public void setFilledAnswers(List<String> answers){
        filledAnswers = answers;
    }

    public List<String> getServerFilledAnswers() {
        return serverFilledAnswers;
    }

    public void setServerFilledAnswers(List<String> serverFilledAnswers) {
        this.serverFilledAnswers = serverFilledAnswers;
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
