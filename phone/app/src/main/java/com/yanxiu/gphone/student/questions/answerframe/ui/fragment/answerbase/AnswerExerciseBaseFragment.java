package com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.questions.answerframe.bean.AnswerBean;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.listener.IExercise;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.FragmentUserVisibleController;
import com.yanxiu.gphone.student.util.StringUtil;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

import java.util.ArrayList;

import static android.graphics.Typeface.DEFAULT_BOLD;

/**
 * Created by 戴延枫 on 2017/6/26.
 * 习题Frament基类
 */

public abstract class AnswerExerciseBaseFragment extends ExerciseBaseFragment {
    public final String TAG = this.getClass().getSimpleName();

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        if (isVisibleToUser) {
            //用户可见，计时开始
            mStartTime = System.currentTimeMillis();
            hiddenSwitchQuestionView();
            if (!invokeInResumeOrPause) {
                //是左右滑动
                updateProgress();//进入屏幕，更新进度条
            }
        } else {
            //不可见，计时结束
            mEndTime = System.currentTimeMillis();
            calculateExerciseTime();
        }
    }
}
