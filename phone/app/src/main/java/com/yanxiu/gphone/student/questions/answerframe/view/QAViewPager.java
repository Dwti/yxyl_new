package com.yanxiu.gphone.student.questions.answerframe.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by 戴延枫 on 2017/5/7.
 */

public class QAViewPager extends ViewPager {
    private float mStartDragX;
    public OnSwipeOutListener mListener;
    private boolean isCanScroll = true;

    public QAViewPager(Context context) {
        super(context);
    }

    public QAViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mListener = listener;
    }

    /**
     * 设置其是否能滑动换页
     * @param isCanScroll false 不能换页， true 可以滑动换页
     */
    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        float x = ev.getRawX();
        if (action == MotionEvent.ACTION_DOWN) {
            mStartDragX = x;
        }
        return isCanScroll&&super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == getAdapter().getCount() - 1) {
            final int action = ev.getAction();
            float x = ev.getRawX();
            switch (action & MotionEventCompat.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (x < mStartDragX && null != mListener) {
                        mListener.onSwipeOutAtEnd();
                    } else {
                        mStartDragX = 0;
                    }
                    break;
            }
        }
        super.onTouchEvent(ev);
        return isCanScroll;
    }

    public interface OnSwipeOutListener {
        public void onSwipeOutAtEnd();
    }
}
