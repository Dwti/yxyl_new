package com.yanxiu.gphone.student.questions.choose;

import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.bean.PointBean;
import com.yanxiu.gphone.student.util.StringUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/26 15:34.
 * Function :
 */
public class MultiChoiceQuestion extends BaseQuestion {
    private List<String> multianswer=new ArrayList<>();
    private List<String> choice;
    private List<String> answerList=new ArrayList<>();
    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private String answerCompare;

    public MultiChoiceQuestion(PaperTestBean bean, QuestionShowType showType,String paperStatus) {
        super(bean, showType,paperStatus);
        multianswer.clear();
        List<Object> data=bean.getQuestions().getAnswer();
        for (Object o:data){
            multianswer.add((String) o);
        }
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

    public List<String> getMultianswer() {
        return multianswer;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public List<String> getChoice() {
        return choice;
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new MultiChooseFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new MultiChooseAnalysisFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return new MultiChooseWrongFragment();
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return new MultiChooseRedoFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

    @Override
    public int getStatus() {
        if (showType.equals(QuestionShowType.MISTAKE_REDO)||showType.equals(QuestionShowType.ANSWER)){
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

    /**
     * 临时处理
     * */
    private int getSta(){
        if (multianswer!=null&&answerList!=null) {
            if (multianswer.size() == answerList.size() && multianswer.containsAll(answerList)) {
                return Constants.ANSWER_STATUS_RIGHT;
            } else {
                return Constants.ANSWER_STATUS_WRONG;
            }
        }
        return Constants.ANSWER_STATUS_NOANSWERED;
    }

    @Override
    public String getMistakeRedoAnswerResult() {
        StringBuilder sb = new StringBuilder();
        for(String str : multianswer){
            sb.append(StringUtil.getChoiceByIndex(Integer.parseInt(str)));
        }
        return "本题答案：" + sb.toString();
    }
}
