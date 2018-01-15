package com.yanxiu.gphone.student.questions.classify;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.questions.bean.AnalysisBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 戴延枫 on 2017/7/13.
 */

public class ClassifyQuestion extends BaseQuestion {
    private List<List<String>> classifyAnswer = new ArrayList<>();
    private ArrayList<String> choice;
    private ArrayList<String> classifyBasketList = new ArrayList<>();//归类题类别
    private List<List<String>> answerList = new ArrayList<>();
    private List<PointBean> pointList;
    private int starCount;
    private String questionAnalysis;
    private String answerCompare;

    public ClassifyQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
        classifyAnswer.clear();
        List<Object> data = bean.getQuestions().getAnswer();
        for (int i = 0; i < data.size(); i++) {
            LinkedTreeMap map = (LinkedTreeMap) data.get(i);
            String jsonString = map.get("answer").toString();
            try {
                String[] stringArray = jsonString.split(",");
                if (null != stringArray && stringArray.length > 0) {
                    ArrayList list = new ArrayList();
                    for (int j = 0; j < stringArray.length; j++) {
                        list.add(stringArray[j]);
                    }
                    classifyAnswer.add(list);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String name = map.get("name").toString();
            classifyBasketList.add(name);
        }

        choice = (ArrayList) bean.getQuestions().getContent().getChoices();
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
        try {
            String jsonString = bean.getQuestions().getPad().getAnswer();
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                String string = array.getString(i);
                String[] strings = string.split(",");
                ArrayList<String> childList = new ArrayList<>();
                for (String s : strings) {
                    if (!TextUtils.isEmpty(s)) {
                        childList.add(s);
                    }
                }
                answerList.add(childList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dyf", "异常----------->" + e.toString());
        }
    }

    public String getAnalysisviewAnswer() {
        String answer = "";
        for (int i = 0; i < classifyBasketList.size(); i++) {
            answer += classifyBasketList.get(i) + ":";
            List<String> list = classifyAnswer.get(i);
            for (String s : list) {
                int index = Integer.parseInt(s);
                answer += choice.get(index) + " ";
            }
        }
        return answer;
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

    public List<List<String>> getAnswerList() {
        return answerList;
    }

    public ArrayList<String> getChoice() {
        return choice;
    }

    public List<PointBean> getPointList() {
        return pointList;
    }

    public List<List<String>> getClassifyAnswer() {
        return classifyAnswer;
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ClassifyFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new ClassifyAnalysisFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return new ClassifyWrongFragment();
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return new ClassifyRedoFragment();
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }

    @Override
    public int getStatus() {
        if (getPad().getAnalysis()==null) {
            return getSta();
        } else {
            List<AnalysisBean> analysis = getPad().getAnalysis();
            List<Object> answer = getBean().getQuestions().getAnswer();
            int status;

            if (analysis.size() != answer.size()+1) {
                status = Constants.ANSWER_STATUS_WRONG;
            } else {
                status = Constants.ANSWER_STATUS_RIGHT;
            }

            for (AnalysisBean analysisBean : analysis) {
                List<String> list = analysisBean.subStatus;
                for (String s : list) {
                    if (!AnalysisBean.RIGHT.equals(s)) {
                        status = Constants.ANSWER_STATUS_WRONG;
                    }
                }
            }
            return status;
        }
    }

    private int getSta() {
        List<List<String>> rightAnswer = getClassifyAnswer();
        List<List<String>> answerList = getAnswerList();
        if (rightAnswer != null && answerList != null && !rightAnswer.isEmpty() && !answerList.isEmpty()) {
            ArrayList<String> tempList = (ArrayList<String>) getChoice().clone();//未作答选项list
            for (int i = 0; i < answerList.size(); i++) {
                List<String> myAnswerList = answerList.get(i);//自己的答案
                List rightAnswerList = rightAnswer.get(i);//正确的答案
                for (int j = 0; j < myAnswerList.size(); j++) {
                    String id = myAnswerList.get(j);
                    if (rightAnswerList.contains(id)) { //我的答案是正确的

                    } else {
                        return Constants.ANSWER_STATUS_WRONG;
                    }
                    if (tempList.contains(getChoiceContent(id))) { //该选项已经作答了
                        tempList.remove(getChoiceContent(id));//移除已经作答的，剩下的就是未归类的选项
                    }
                }
            }
            if (tempList.size() > 0) {
                return Constants.ANSWER_STATUS_NOANSWERED;
            }
            return Constants.ANSWER_STATUS_RIGHT;
        } else {
            return Constants.ANSWER_STATUS_NOANSWERED;
        }
    }

    public ArrayList<String> getClassifyBasket() {
        return classifyBasketList;
    }

    /**
     * 通过index，获取对应的choice的字符串内容
     *
     * @param index
     * @return
     */
    private String getChoiceContent(String index) {
        int id = -1;
        String chioce = null;
        try {
            id = Integer.parseInt(index);//获取id
            chioce = getChoice().get(id);//获取chioce的name
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chioce;
    }
}
