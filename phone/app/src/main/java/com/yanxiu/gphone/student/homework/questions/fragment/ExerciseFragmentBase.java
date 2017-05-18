package com.yanxiu.gphone.student.homework.questions.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.yanxiu.gphone.student.homework.questions.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.homework.questions.interf.IExercise;
import com.yanxiu.gphone.student.homework.questions.util.FragmentUserVisibleController;

/**
 * Created by 戴延枫 on 2017/5/5.
 * 习题Frament基类
 */

public abstract class ExerciseFragmentBase extends Fragment implements IExercise, FragmentUserVisibleController.UserVisibleCallback {
    public final String TAG = this.getClass().getSimpleName();

    public static final String KEY_NODE = "key_question";

    public long mTotalTime ;//总计时间
    public long mStartTime ;//开始时间
    public long mEndTime;//结束时间

    private FragmentUserVisibleController userVisibleController;

    public ExerciseFragmentBase() {
        userVisibleController = new FragmentUserVisibleController(this, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userVisibleController.activityCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        userVisibleController.resume(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        userVisibleController.pause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        userVisibleController.setUserVisibleHint(isVisibleToUser);
    }


    public void setUserVisibleHin2(boolean is) {
        userVisibleController.resume(is);
    }

    @Override
    public void setWaitingShowToUser(boolean waitingShowToUser) {
        userVisibleController.setWaitingShowToUser(waitingShowToUser);
    }

    @Override
    public boolean isWaitingShowToUser() {
        return userVisibleController.isWaitingShowToUser();
    }

    @Override
    public boolean isVisibleToUser() {
        return userVisibleController.isVisibleToUser();
    }

    @Override
    public void callSuperSetUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        if(isVisibleToUser){
            //用户可见，计时开始
            mStartTime = System.currentTimeMillis();
        }else{
            //不可见，计时结束
            mEndTime = System.currentTimeMillis();
            calculateExerciseTime();
        }
        onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser       true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param invokeInResumeOrPause true：发生在onResume或onPause方法里；false：本次回调发生在setUserVisibleHintMethod方法里
     */
    public abstract void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause);

    /**
     * 计算题目时间
     */
    public void calculateExerciseTime() {
        long exerciseTime = mEndTime - mStartTime;
        //Todo 计算时间及保存
        mTotalTime += exerciseTime;
    }
    /**
     * 切换下一题目
     */
    public void nextQuestion(){
        if(getActivity() instanceof AnswerQuestionActivity){
            AnswerQuestionActivity acticity = (AnswerQuestionActivity)getActivity();
            acticity.nextQuestion();
        }

    }

    /**
     * 切换上一题目
     */
    public void previousQuestion() {
        if(getActivity() instanceof AnswerQuestionActivity){
            AnswerQuestionActivity acticity = (AnswerQuestionActivity)getActivity();
            acticity.previousQuestion();
        }
    }

}
