package com.yanxiu.gphone.student.questions.cloze;


import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.choose.SingleChoiceQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/6/14.
 */

public class ClozeComplexQuestion extends BaseQuestion {
    private List<String> correctAnswers = new ArrayList<>();
    private List<String> filledAnswers = new ArrayList<>();
    public ClozeComplexQuestion(PaperTestBean bean, QuestionShowType showType,String paperStatus) {
        super(bean, showType,paperStatus);
        initAnswer();
    }

    private void initAnswer() {
        String correctAnswer,filledAnswer;
        for(BaseQuestion question : children){
            correctAnswer = convertCorrectAnswer((SingleChoiceQuestion) question);
            if(((SingleChoiceQuestion)question).getAnswerList() != null && ((SingleChoiceQuestion)question).getAnswerList().size() > 0){
                filledAnswer = convertFilledAnswer((SingleChoiceQuestion) question);
            }else {
                filledAnswer = "";
            }
            correctAnswers.add(correctAnswer.trim());
            filledAnswers.add(filledAnswer.trim());
        }
    }

    private String convertFilledAnswer(SingleChoiceQuestion question){
        return question.getChoice().get(Integer.parseInt(question.getAnswerList().get(0)));
    }

    private String convertCorrectAnswer(SingleChoiceQuestion question){
        return question.getChoice().get(Integer.parseInt(question.getSingleAnswer()));
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new ClozeAnswerComplexFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        //解析
        return new ClozeAnalysisComplexFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return null;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<String> getFilledAnswers() {
        return filledAnswers;
    }

    public void setFilledAnswers(List<String> filledAnswers) {
        this.filledAnswers = filledAnswers;
    }

    @Override
    public Object getAnswer() {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }
}
