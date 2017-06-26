package com.yanxiu.gphone.student.questions.subjective;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:06.
 * Function :
 */
public class SubjectiveQuestion extends BaseQuestion {

    public ArrayList<String> answerList=new ArrayList<>();

    public SubjectiveQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
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

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new SubjectiveFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return null;
    }

    @Override
    public Object getAnswer() {
        return answerList;
    }
}
