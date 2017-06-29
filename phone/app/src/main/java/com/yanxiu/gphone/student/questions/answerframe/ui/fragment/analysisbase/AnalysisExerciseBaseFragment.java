package com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase;

import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;

/**
 * Created by 戴延枫 on 2017/5/5.
 * 解析Frament基类
 */

public abstract class AnalysisExerciseBaseFragment extends ExerciseBaseFragment {
    public final String TAG = this.getClass().getSimpleName();

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        if (isVisibleToUser) {
            hiddenSwitchQuestionView();//这里有问题--acticity
        }
    }
}
