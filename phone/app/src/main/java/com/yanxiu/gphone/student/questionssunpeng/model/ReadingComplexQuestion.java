package com.yanxiu.gphone.student.questionssunpeng.model;

import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.questionssunpeng.QuestionConvertFactory;
import com.yanxiu.gphone.student.questionssunpeng.QuestionShowType;
import com.yanxiu.gphone.student.questionssunpeng.source.PaperTestBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class ReadingComplexQuestion extends BaseQuestion {
    private List<BaseQuestion> children;
    public ReadingComplexQuestion(PaperTestBean bean, QuestionShowType showType) {
        super(bean, showType);
        children = QuestionConvertFactory.convertQuestion(bean.getQuestions().getChildren(),showType);
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    public List<BaseQuestion> getChildren() {
        return children;
    }

    public void setChildren(List<BaseQuestion> children) {
        this.children = children;
    }
}
