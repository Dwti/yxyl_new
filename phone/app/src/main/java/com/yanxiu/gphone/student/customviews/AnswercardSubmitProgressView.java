package com.yanxiu.gphone.student.customviews;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.R;

/**
 * 提交答案时的进度条，不通用
 * Created by 戴延枫 on 2017/6/20.
 */

public class AnswercardSubmitProgressView extends RelativeLayout {

    private RelativeLayout mRootView;//根view
    private ImageView mBackGround;//进度条背景
    private ImageView mProgress;//进度条进度
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
     * 宽度
     */
    private int mWidth;

    public AnswercardSubmitProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public AnswercardSubmitProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AnswercardSubmitProgressView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.custom_progress_view, null);
        mBackGround = (ImageView) mRootView.findViewById(R.id.background);
        mProgress = (ImageView) mRootView.findViewById(R.id.progress);
        mWidth = (int) getResources().getDimensionPixelSize(R.dimen.answer_card_progress_width);
        addView(mRootView);
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
        mPercent = mWidth / maxCount;
    }

    /***
     * 更新当前的进度值
     */
    public void updateProgress(int currentAnswerNumber) {
        if (mCurrentProgress >= 0 && mCurrentProgress <= mWidth) {
            mCurrentProgress = currentAnswerNumber * mPercent;
            if (mCurrentProgress <= mWidth) {
                int lastX = mProgress.getLayoutParams().width;
                mProgress.getLayoutParams().width = (int) mCurrentProgress;
                if (null != mDisplacementListener) {
                    int x = (int) (mCurrentProgress - lastX);
                    mDisplacementListener.xPositionChange(x);
                }
                requestLayout();
            }
        }
    }

    public void reset(){
        mCurrentProgress = 0;
        mPercent = 0;
        maxCount =0;
        mProgress.getLayoutParams().width = 0;
    }


    public interface DisplacementListener {
        void xPositionChange(int x);
    }

    private DisplacementListener mDisplacementListener;

    public void setDisplacementListener(DisplacementListener displacementListener) {
        mDisplacementListener = displacementListener;
    }
}
