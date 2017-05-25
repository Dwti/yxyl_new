package com.yanxiu.gphone.student.homework.questions.model;

import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.homework.questions.QuestionShowType;
import com.yanxiu.gphone.student.homework.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.homework.questions.fragment.ExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.SingleChooseFragment;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class SingleChoiceQuestion extends BaseQuestion {
    private String url;
    public SingleChoiceQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        this.url = bean.getQuestions().getUrl();
    }

    @Override
    ExerciseFragmentBase answerFragment() {
        return new SingleChooseFragment();
    }

    @Override
    ExerciseFragmentBase analysisFragment() {
//        return new SingleChooseAnalysisFragment();
        return null;
    }
}
