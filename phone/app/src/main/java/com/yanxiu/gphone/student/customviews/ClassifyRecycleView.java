package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/28 11:21.
 * Function :
 */
public class ClassifyRecycleView  extends RecyclerView{
    public ClassifyRecycleView(Context context) {
        super(context);
    }

    public ClassifyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClassifyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(e);
    }
}
