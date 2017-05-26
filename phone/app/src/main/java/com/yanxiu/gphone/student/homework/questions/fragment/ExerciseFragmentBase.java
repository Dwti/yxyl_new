package com.yanxiu.gphone.student.homework.questions.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.questions.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.homework.questions.interf.IExercise;
import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;
import com.yanxiu.gphone.student.homework.questions.util.FragmentUserVisibleController;
import com.yanxiu.gphone.student.util.StringUtil;

import static android.graphics.Typeface.DEFAULT_BOLD;

/**
 * Created by 戴延枫 on 2017/5/5.
 * 习题Frament基类
 */

public abstract class ExerciseFragmentBase extends Fragment implements IExercise, FragmentUserVisibleController.UserVisibleCallback {
    public final String TAG = this.getClass().getSimpleName();

    public static final String KEY_NODE = "key_question";

    public BaseQuestion mBaseQuestion;

    public long mTotalTime ;//总计时间
    public long mStartTime ;//开始时间
    public long mEndTime;//结束时间

    private FragmentUserVisibleController userVisibleController;

    public TextView mQaNumber; //题号
    public TextView mQaName; //题目名称

    public ExerciseFragmentBase() {
        userVisibleController = new FragmentUserVisibleController(this, this);
    }

    @Override
    public void setData(BaseQuestion node) {
        mBaseQuestion = node;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userVisibleController.activityCreated();
    }

    public void setQaNumber(View v) {
        mQaNumber = (TextView) v.findViewById(R.id.qa_number);
        String str = mBaseQuestion.numberStringForShow();
        Fragment parentFragment = getParentFragment();
        if(parentFragment instanceof ComplexExerciseFragmentBase){
            mQaNumber.setTextColor(getResources().getColor(R.color.color_999999));
        }
        mQaNumber.setText(str);
    }

    /**
     * 设置题目标题
     * 单题型和复合题的题干部分，默认显示template；
     * 复合题的子题，题目标题如果不显示template,传入name；
     * @param v
     */
    public void setQaName(View v) {
        mQaName = (TextView) v.findViewById(R.id.qa_name);
        String templateName;
        Fragment parentFragment = getParentFragment();
        if(parentFragment instanceof ComplexExerciseFragmentBase){
            templateName = getString(R.string.question);
            TextPaint tp = mQaName.getPaint();
            tp.setTypeface(DEFAULT_BOLD);
            mQaName.setTextColor(getResources().getColor(R.color.color_333333));
        }else{
            templateName = StringUtil.getTemplateName(mBaseQuestion.getTemplate());
        }
        mQaName.setText(templateName);
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

    /**
     * 保存答案
     */
    public void saveAnswer(){

    }
}
