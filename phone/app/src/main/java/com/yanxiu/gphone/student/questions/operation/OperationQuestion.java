package com.yanxiu.gphone.student.questions.operation;

import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.bean.JsonAudioComment;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;
import com.yanxiu.gphone.student.util.StemUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/12/25.
 */

public class OperationQuestion extends SubjectiveQuestion{
    private List<String> mOperateImgUrls;
    public OperationQuestion(PaperTestBean bean, QuestionShowType showType, String paperStatus) {
        super(bean, showType, paperStatus);
        mOperateImgUrls = StemUtil.getImgUrls(stem);
    }

    @Override
    public ExerciseBaseFragment answerFragment() {
        return new OperationFragment();
    }

    @Override
    public ExerciseBaseFragment analysisFragment() {
        return new OperationAnalysisFragment();
    }

    @Override
    public ExerciseBaseFragment wrongFragment() {
        return null;
    }

    @Override
    public ExerciseBaseFragment redoFragment() {
        return null;
    }

    public List<String> getOperateImgUrls() {
        return mOperateImgUrls;
    }

}
