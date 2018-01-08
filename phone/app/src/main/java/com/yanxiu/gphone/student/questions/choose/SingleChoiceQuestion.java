package com.yanxiu.gphone.student.questions.choose;

import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.questions.bean.JsonNoteBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.PointBean;
import com.yanxiu.gphone.student.util.StringUtil;

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
    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private String answerCompare;

    public SingleChoiceQuestion(PaperTestBean bean, QuestionShowType showType,String paperStatus) {
        super(bean, showType,paperStatus);
        singleAnswer= String.valueOf(bean.getQuestions().getAnswer().get(0));
        choice= bean.getQuestions().getContent().getChoices();
        pointList=bean.getQuestions().getPoint();
        try {
            starCount=Integer.parseInt(bean.getQuestions().getDifficulty());
        }catch (Exception e){
            e.printStackTrace();
        }
        questionAnalysis=bean.getQuestions().getAnalysis();
        try {
            answerCompare=bean.getQuestions().getExtend().getData().getAnswerCompare();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public String getAnswerCompare() {
        return answerCompare;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public int getStarCount() {
        return starCount;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public String getSingleAnswer() {
        return singleAnswer;
    }

    public List<String> getChoice() {
        return choice;
    }

    public List<PointBean> getPointList() {
        return pointList;
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
    public ExerciseBaseFragment wrongFragment() {
        return new SingleChooseWrongFragment();
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return new SingleChooseRedoFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

    @Override
    public int getStatus() {
        if (showType.equals(QuestionShowType.MISTAKE_REDO)||showType.equals(QuestionShowType.ANSWER)||isMisTakeRedo()||getPad().getAnalysis()==null){
            return getSta();
        }else {
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
            return status;
        }
    }

    private int getSta() {
        if (answerList!=null&&answerList.size()>0){
            if (answerList.get(0).equals(singleAnswer)){
                return Constants.ANSWER_STATUS_RIGHT;
            }else {
                return Constants.ANSWER_STATUS_WRONG;
            }
        }
        return Constants.ANSWER_STATUS_NOANSWERED;
    }

    @Override
    public String getMistakeRedoAnswerResult() {
        return "本题答案：" + StringUtil.getChoiceByIndex(Integer.parseInt(singleAnswer));
    }
}
