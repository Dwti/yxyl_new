package com.yanxiu.gphone.student.questions.cloze;


import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.TopBaseFragment;


/**
 * Created by 戴延枫 on 2017/6/14.
 */

public class ClozeAnalysisComplexFragment extends AnalysisComplexExerciseBaseFragment {
    public int mHashCode = this.hashCode();
    private ClozeComplexQuestion mData;
    private int mTopHeight;
    private int mChildQuestionHeight = 580;//子题固定高度是四个单选的高度;
    private Integer mDuration = 200;//展开/关闭布局执行的时间
    private ValueAnimator mOpenAnimator, mCloseAnimator;
    private boolean isExpand = false;//题干是否已经完全展开了

    @Override
    public void setData(BaseQuestion baseQuestion) {
        super.setData(baseQuestion);
        mData = (ClozeComplexQuestion) baseQuestion;
    }

    @Override
    protected TopBaseFragment getTopFragment() {
        ClozeComplexTopAnalysisFragment topFragment = new ClozeComplexTopAnalysisFragment();
        topFragment.setData(mData);
        return topFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setData((ClozeComplexQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser       true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param invokeInResumeOrPause true：发生在onResume或onPause方法里；false：本次回调发生在setUserVisibleHintMethod方法里
     */
    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
    }
}
