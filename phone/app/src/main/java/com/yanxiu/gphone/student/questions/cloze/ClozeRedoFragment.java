package com.yanxiu.gphone.student.questions.cloze;


import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.RedoComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.TopBaseFragment;


/**
 * Created by 戴延枫 on 2017/6/14.
 */

public class ClozeRedoFragment extends RedoComplexExerciseBaseFragment {
    public int mHashCode = this.hashCode();
    private ClozeComplexQuestion mData;
    private int mTopHeight;
    private int mChildQuestionHeight;//子题固定高度是四个单选的高度;
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
        ClozeComplexTopFragment topFragment = new ClozeComplexTopFragment();
        topFragment.setData(mData);
        return topFragment;
    }

    @Override
    protected void setTopFragment(Fragment fragment) {
        super.setTopFragment(fragment);
        hiddenSplitter();
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

    /**
     * 完形填空不显示滑块，隐藏滑块view
     */
    private void hiddenSplitter() {
        final LinearLayout parentView = (LinearLayout) mImageViewSplitter.getParent();
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImageViewSplitter.setVisibility(View.GONE);
                mSplitter_line.setVisibility(View.GONE);
                mChildQuestionHeight = getResources().getDimensionPixelSize(R.dimen.cloze_bottom_layout_height);
                mTopHeight = parentView.getHeight() - mTitleBarHight - mBottom_min_distance - mChildQuestionHeight + mLineHight_chooseLine;
                Log.e("dyf", "parentView.getHeight():=" + parentView.getHeight() + "/n  mTitleBarHight = " + mTitleBarHight
                        + "/n topHeight = " + mTopHeight);
                mTopLayout.setCanChangeHeight();
                LinearLayout.LayoutParams topParams = (LinearLayout.LayoutParams) mTopLayout.getLayoutParams();
                topParams.height = mTopHeight;
                mTopLayout.setLayoutParams(topParams);

                parentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    /**
     * 执行展开布局操作
     */
    public void expand() {
        if(!isExpand)
            getOpenValueAnimator(mTopLayout).setDuration(mDuration).start();
    }

    /**
     * 执行关闭布局操作
     */
    public void collapse() {
        if(isExpand)
            getCloseValueAnimator(mTopLayout).setDuration(mDuration).start();
    }

    /**
     * 获取展开布局执行的动画
     *
     * @return
     */
    private ValueAnimator getOpenValueAnimator(final View v) {
        final int targetHeight = mChildQuestionHeight;
        mOpenAnimator = ValueAnimator.ofFloat(0, 1);
        mOpenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolatedTime = animation.getAnimatedFraction();
                v.getLayoutParams().height = mTopHeight + (int) (targetHeight * interpolatedTime);
                v.requestLayout();
                if(interpolatedTime == 1)
                    isExpand = true;
            }
        });
        return mOpenAnimator;
    }

    /**
     * 获取关闭布局执行的动画
     *
     * @return
     */
    private ValueAnimator getCloseValueAnimator(final View v) {
        final int initialHeight = mChildQuestionHeight;
        mCloseAnimator = ValueAnimator.ofFloat(0, 1);
        mCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolatedTime = animation.getAnimatedFraction();
                v.getLayoutParams().height = mTopHeight + initialHeight - (int) (initialHeight * interpolatedTime);
                v.requestLayout();
                if(interpolatedTime == 1)
                    isExpand = false;
            }
        });
        return mCloseAnimator;
    }
}
