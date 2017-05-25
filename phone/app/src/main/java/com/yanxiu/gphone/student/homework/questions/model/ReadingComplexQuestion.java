package com.yanxiu.gphone.student.homework.questions.model;


import com.yanxiu.gphone.student.homework.questions.QuestionShowType;
import com.yanxiu.gphone.student.homework.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.homework.questions.fragment.ExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.ReadingComplexExerciseFragment;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class ReadingComplexQuestion extends BaseQuestion {
    public ReadingComplexQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
    }

    @Override
    ExerciseFragmentBase answerFragment() {
        return new ReadingComplexExerciseFragment();
    }

    @Override
    ExerciseFragmentBase analysisFragment() {
        //解析
        return null;
    }
}
