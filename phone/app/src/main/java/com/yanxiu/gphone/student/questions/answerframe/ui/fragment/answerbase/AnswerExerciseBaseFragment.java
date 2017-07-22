package com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase;


import android.util.Log;

import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;

/**
 * Created by 戴延枫 on 2017/6/26.
 * 习题Frament基类
 */

public abstract class AnswerExerciseBaseFragment extends ExerciseBaseFragment {
    public final String TAG = this.getClass().getSimpleName();

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        Log.e("onVisibilityChanged", "isVisibleToUser = " + isVisibleToUser + "invokeInResumeOrPause = " + invokeInResumeOrPause);
        if (isVisibleToUser) {
            //用户可见，计时开始
            mStartTime = System.currentTimeMillis();
            hiddenSwitchQuestionView();
            updateProgress();//进入屏幕，更新进度条
        } else {
            //不可见，计时结束
            mEndTime = System.currentTimeMillis();
            calculateExerciseTime();
        }
    }
}
