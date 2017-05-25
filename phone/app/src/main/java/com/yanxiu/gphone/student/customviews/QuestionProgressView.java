package com.yanxiu.gphone.student.customviews;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.yanxiu.gphone.student.util.ScreenUtils;

/**
 * 答题时的进度条，不通用
 * Created by 戴延枫 on 2017/5/22.
 */

public class QuestionProgressView extends View {
    /**
     * 进度条最大值
     */
    private float maxCount;
    /**
     * 进度条当前值
     */
    private float mCurrentProgress;
    /**
     * 每一道题的增长比例
     */
    private float mPercent;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;

    public QuestionProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public QuestionProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public QuestionProgressView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mScreenWidth = ScreenUtils.getScreenWidth(context);
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
        mPercent = mScreenWidth / maxCount;//每一道题的进度比例
    }

    /***
     * 更新当前的进度值
     */
    public void updateProgress() {
        if (mCurrentProgress >= 0 && mCurrentProgress <= mScreenWidth) {
            mCurrentProgress += mPercent;
            if (mCurrentProgress <= mScreenWidth) {
                getLayoutParams().width = (int) mCurrentProgress;
                requestLayout();
            }
        }
    }

}
