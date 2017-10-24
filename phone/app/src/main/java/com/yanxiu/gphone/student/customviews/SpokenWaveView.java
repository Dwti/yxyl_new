package com.yanxiu.gphone.student.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/19 10:48.
 * Function :
 */
public class SpokenWaveView extends View {

    private static final int WAVE_WIDTH=160;
    private static final int WAVE_VOLUME=0;
    private static final int WAVE_DURATION=1000;

    private Path mPath;
    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private int mDx;
    private List<Integer> mPointList=new ArrayList<>(4);
    private List<Integer> mPointList1=new ArrayList<>(4);
    private ValueAnimator animator;

    public SpokenWaveView(Context context) {
        super(context);
        init(context);
    }

    public SpokenWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpokenWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mPath=new Path();
        mPaint=new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
        setVolume(WAVE_VOLUME);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int position=0;
        int nowWidth=-WAVE_WIDTH*4+mDx;
        mPath.reset();
        mPaint.setColor(Color.parseColor("#6689e00d"));
        mPath.moveTo(nowWidth, getPoint1(position++));
        while (nowWidth<mWidth) {
            mPath.quadTo(nowWidth+WAVE_WIDTH,getPoint1(position++),nowWidth+WAVE_WIDTH*2,getPoint1(position++));
            position=position%4;
            nowWidth+=WAVE_WIDTH*2;
        }
        canvas.drawPath(mPath,mPaint);

        position=0;
        nowWidth=-WAVE_WIDTH*5+mDx;
        mPath.reset();
        mPaint.setColor(Color.parseColor("#89e00d"));
        mPath.moveTo(nowWidth, getPoint(position++));
        while (nowWidth<mWidth) {
            mPath.quadTo(nowWidth+WAVE_WIDTH,getPoint(position++),nowWidth+WAVE_WIDTH*2,getPoint(position++));
            position=position%4;
            nowWidth+=WAVE_WIDTH*2;
        }
        canvas.drawPath(mPath,mPaint);
    }

    public void start(){
        animator = new ValueAnimator();
        animator.setFloatValues(0, WAVE_WIDTH*4);
        animator.setDuration(WAVE_DURATION);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float change = (float) animation.getAnimatedValue();
                mDx = (int) change;
                invalidate();
            }
        });
        animator.start();
    }

    public void stop(){
        setVolume(WAVE_VOLUME);
        if (animator!=null){
            animator.cancel();
            animator=null;
        }
        invalidate();
    }

    public void setVolume(int volume){
        int height=(int) (volume*2.5);
        if (height>mHeight/2){
            height=mHeight/2;
        }
        mPointList.add(0,mHeight/2);
        mPointList.add(1,mHeight/2-height);
        mPointList.add(2,mHeight/2);
        mPointList.add(3,mHeight/2+height);

        mPointList1.add(0,mHeight/2);
        mPointList1.add(1,mHeight/2-height*2/3);
        mPointList1.add(2,mHeight/2);
        mPointList1.add(3,mHeight/2+height*2/3);
    }

    private int getPoint(int position){
        return mPointList.get(position%4);
    }

    private int getPoint1(int position){
        return mPointList1.get(position%4);
    }
}
