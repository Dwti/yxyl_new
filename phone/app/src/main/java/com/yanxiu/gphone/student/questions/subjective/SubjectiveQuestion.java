package com.yanxiu.gphone.student.questions.subjective;

import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.JsonAudioComment;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:06.
 * Function :
 */
public class SubjectiveQuestion extends BaseQuestion {

    private ArrayList<String> answerList=new ArrayList<>();
    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private List<String> subjectAnswer;
    private int typeId=-1;
    protected int score;
    private List<JsonAudioComment> audioList;
    private boolean isMistakeRedoAnalysisExpand = false;

    public SubjectiveQuestion(PaperTestBean bean, QuestionShowType showType,String paperStatus) {
        super(bean, showType,paperStatus);
        pointList=bean.getQuestions().getPoint();
        try {
            starCount=Integer.parseInt(bean.getQuestions().getDifficulty());
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            audioList=bean.getQuestions().getPad().getJsonAudioComment();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            score=bean.getQuestions().getPad().getTeachercheck().getScore();
        }catch (Exception e){
            e.printStackTrace();
        }
        questionAnalysis=bean.getQuestions().getAnalysis();
        try {
            typeId=Integer.parseInt(bean.getQuestions().getType_id());
        }catch (Exception e){
            e.printStackTrace();
        }
        subjectAnswer=new ArrayList<>();
        for(Object o : bean.getQuestions().getAnswer()){
            subjectAnswer.add(String.valueOf(o));
        }
        String jsonArray=bean.getQuestions().getPad().getAnswer();
        JSONArray array;
        try {
            array=new JSONArray(jsonArray);
            for (int i=0;i<array.length();i++){
                answerList.add(array.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<JsonAudioComment> getAudioList() {
        return audioList;
    }

    public int getScore() {
        return score;
    }

    public int getTypeId() {
        return typeId;
    }

    public List<String> getSubjectAnswer() {
        return subjectAnswer;
    }

    public ArrayList<String> getAnswerList() {
        return answerList;
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

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new SubjectiveFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new SubjectiveAnalysisFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return new SubjectiveWrongFragment();
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return new SubjectiveRedoFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

    public boolean isMistakeRedoAnalysisExpand() {
        return isMistakeRedoAnalysisExpand;
    }

    public void setMistakeRedoAnalysisExpand(boolean mistakeRedoAnalysisExpand) {
        isMistakeRedoAnalysisExpand = mistakeRedoAnalysisExpand;
    }

    @Override
    public int getStatus() {
        if (answerList!=null&&answerList.size()>0){
            return Constants.ANSWER_STATUS_YSUBJECT_ANSWERED;
        }
        return Constants.ANSWER_STATUS_NOANSWERED;
    }
}
