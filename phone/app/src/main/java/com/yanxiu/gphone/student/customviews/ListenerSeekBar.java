package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/5 15:12.
 * Function :
 */
public class ListenerSeekBar extends android.support.v7.widget.AppCompatSeekBar{

    private boolean isMove=false;
    private float mDownX;
    private int mTouchSlop;

    public ListenerSeekBar(Context context) {
        super(context);
        init(context);
    }

    public ListenerSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListenerSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isMove = false;
            mDownX = event.getX();
            if (checkIsInternal(event)) {
                Rect rect = getThumb().copyBounds();
                event.setLocation((rect.left + rect.right) * 1f / 2, event.getY());
                isMove = true;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float downX = event.getX();
            if (Math.abs(downX - mDownX) <= mTouchSlop) {
                Rect rect = getThumb().copyBounds();
                event.setLocation((rect.left + rect.right) * 1f / 2, event.getY());
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            ListenDrawable drawable = (ListenDrawable) getThumb();
            drawable.setEvent(false);
        }

        return !isMove || super.onTouchEvent(event);
    }

    private boolean checkIsInternal(MotionEvent ev) {
        boolean flag = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        Rect rect = getThumb().copyBounds();
        if (x > rect.left && x < rect.right && y > rect.top && y < rect.bottom) {
            flag = true;
        }
        return flag;
    }

}
