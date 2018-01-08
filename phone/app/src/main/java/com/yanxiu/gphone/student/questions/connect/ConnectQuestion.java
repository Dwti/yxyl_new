package com.yanxiu.gphone.student.questions.connect;

import android.text.TextUtils;

import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
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
    private List<ConnectAnalysisItemBean> leftChoices, rightChoices;
    private List<String> correctAnswers = new ArrayList<>(); //转换过的，右边以一半计数，比如choices的大小为10，则"0,5"为"0,0";
    private List<String> filledAnswers = new ArrayList<>();//转换过的，右边以一半计数，比如choices的大小为10，则"0,5"为"0,0";
    private List<String> serverCorrectAnswers = new ArrayList<>();
    private List<String> serverFilledAnswers = new ArrayList<>();//为转换过的，同上，"0,5"就是为"0,5"

    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private String answerCompare;

    private String mLeftCount;
    private String mUselessNode;

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
        mLeftCount=bean.getQuestions().getContent().getLeftCount();
        mUselessNode=bean.getQuestions().getContent().getUselessNode();

        //处理用户已作答的答案
        if (bean.getQuestions().getPad() != null && bean.getQuestions().getPad().getAnswer() != null) {

            try {
                JSONArray jsonArray = new JSONArray(bean.getQuestions().getPad().getAnswer());
                for (int i = 0; i < jsonArray.length(); i++) {
                    serverFilledAnswers.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (String s : serverFilledAnswers) {
                if (!s.contains(",")) {
                    filledAnswers.add("");
                    continue;
                }
                int leftPos = Integer.parseInt(s.split(",")[0]);
                int rightPos = Integer.parseInt(s.split(",")[1]);
                int leftCount=TextUtils.isEmpty(mLeftCount)?choices.size()/2:Integer.parseInt(mLeftCount);
                if (rightPos >= leftCount) {
                    rightPos = rightPos - leftCount;
                }
                filledAnswers.add(leftPos + "," + rightPos);
            }
        }

        //处理本题的正确答案
        if (server_answer != null) {

            for (Object o : server_answer) {
                Map<String, String> map = (Map) o;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals("answer")) {
                        serverCorrectAnswers.add(entry.getValue());
                    }
                }
            }

            for (String s : serverCorrectAnswers) {
                int leftPos = Integer.parseInt(s.split(",")[0]);
                int rightPos = Integer.parseInt(s.split(",")[1]);
                int leftCount=TextUtils.isEmpty(mLeftCount)?choices.size()/2:Integer.parseInt(mLeftCount);
                if (rightPos >= leftCount) {
                    rightPos = rightPos - leftCount;
                }
                correctAnswers.add(leftPos + "," + rightPos);
            }
        }

    }

    /**
     * 这叫项目？
     * */
    public boolean isRight() {
        if (serverFilledAnswers == null)
            return false;
        if (serverFilledAnswers.containsAll(serverCorrectAnswers) && serverCorrectAnswers.containsAll(serverFilledAnswers))
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
    public ExerciseBaseFragment redoFragment() {
        return new ConnectRedoFragment();
    }

    @Override
    public Object getAnswer() {
        return serverFilledAnswers;
    }

    @Override
    public int getStatus() {
        if (showType.equals(QuestionShowType.MISTAKE_REDO) || showType.equals(QuestionShowType.ANSWER)||isMisTakeRedo()||getPad().getAnalysis()==null) {
            return getSta();
        } else {
            List<AnalysisBean> analysis = getPad().getAnalysis();
            List<Object> answer = getBean().getQuestions().getAnswer();
            int status;

            if (analysis.size() != answer.size()) {
                status = Constants.ANSWER_STATUS_WRONG;
            } else {
                status = Constants.ANSWER_STATUS_RIGHT;
            }

            for (AnalysisBean analysisBean : analysis) {
                if (!AnalysisBean.RIGHT.equals(analysisBean.status)) {
                    status = Constants.ANSWER_STATUS_WRONG;
                }
            }
            return status;
        }
    }

    private int getSta() {
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

    public int getLineNumber(){
        int leftCount=Integer.parseInt(mLeftCount);
        if (leftCount*2>choices.size()){
            return choices.size()-leftCount;
        }
        return leftCount;
    }

    public List<String> getChoices() {
        return choices;
    }

    public List<ConnectAnalysisItemBean> getLeftChoices() {
        if (leftChoices == null&&!TextUtils.isEmpty(mLeftCount)) {
            String[] strings=new String[0];
            if (!TextUtils.isEmpty(mUselessNode)) {
                strings = mUselessNode.split(",");
            }
            leftChoices = new ArrayList<>();
            int count=Integer.parseInt(mLeftCount);
            for (int i=0;i<count;i++){
                boolean isExtra=false;
                for (String s:strings){
                    int position=Integer.parseInt(s);
                    if (i==position){
                        isExtra=true;
                    }
                }
                leftChoices.add(new ConnectAnalysisItemBean(choices.get(i),isExtra));
            }
        }
        return leftChoices;
    }

    public List<ConnectAnalysisItemBean> getRightChoices() {
        if (rightChoices == null&&!TextUtils.isEmpty(mLeftCount)){
            String[] strings=new String[0];
            if (!TextUtils.isEmpty(mUselessNode)) {
                strings = mUselessNode.split(",");
            }
            rightChoices = new ArrayList<>();
            int count=Integer.parseInt(mLeftCount);
            for (int i=count;i<choices.size();i++){
                boolean isExtra=false;
                for (String s:strings){
                    int position=Integer.parseInt(s);
                    if (i==position){
                        isExtra=true;
                    }
                }
                rightChoices.add(new ConnectAnalysisItemBean(choices.get(i),isExtra));
            }
        }
        return rightChoices;
    }

    public List<String> getCorrectAnswer() {
        return correctAnswers;
    }

    public List<String> getFilledAnswers() {
        return filledAnswers;
    }

    public void setFilledAnswers(List<String> answers) {
        filledAnswers = answers;
    }

    public List<String> getServerFilledAnswers() {
        return serverFilledAnswers;
    }

    public void setServerFilledAnswers(List<String> serverFilledAnswers) {
        this.serverFilledAnswers = serverFilledAnswers;
    }

    public int getLeftCount() {
        int leftCount=TextUtils.isEmpty(mLeftCount)?choices.size()/2:Integer.parseInt(mLeftCount);
        return leftCount;
    }

    public void setmLeftCount(String mLeftCount) {
        this.mLeftCount = mLeftCount;
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
