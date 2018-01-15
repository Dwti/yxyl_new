package com.yanxiu.gphone.student.questions.operation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by sunpeng on 2018/1/12.
 */

public class CircleView extends View {
    private int mColor;
    private Paint mPaintOuterRing,mPaintInnerRing;
    private CircleStyle mCircleStyle = CircleStyle.SOLID;

    public enum CircleStyle{
        SOLID,
        RING
    }
    public CircleView(Context context) {
        super(context);
        init(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupAttrs(context,attrs);
        init(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttrs(context,attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupAttrs(context,attrs);
        init(context);
    }

    private void init(Context context){

        mPaintOuterRing = new Paint();
        mPaintOuterRing.setColor(mColor);
        mPaintOuterRing.setStyle(Paint.Style.STROKE);
        mPaintOuterRing.setStrokeWidth(ScreenUtils.dpToPx(context,2));

        mPaintInnerRing = new Paint();
        mPaintInnerRing.setColor(mColor);
        mPaintInnerRing.setStyle(Paint.Style.FILL);

    }

    private void setupAttrs(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = a.getColor(R.styleable.CircleView_circle_color,Color.BLACK);
        int circleStyle = a.getInt(R.styleable.CircleView_circle_style,0);
        if(circleStyle == 0){
            mCircleStyle = CircleStyle.SOLID;
        }else {
            mCircleStyle = CircleStyle.RING;
        }
        a.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int outerRadius = Math.min(getWidth(),getHeight()) / 2 - (int) mPaintOuterRing.getStrokeWidth() / 2;
        int innerRadius = outerRadius - ScreenUtils.dpToPxInt(getContext(),4);
        if(mCircleStyle == CircleStyle.SOLID){
            mPaintOuterRing.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(getWidth() /2, getHeight() /2 ,outerRadius, mPaintOuterRing);
        }else if(mCircleStyle == CircleStyle.RING){
            mPaintOuterRing.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(getWidth() /2, getHeight() /2 ,outerRadius, mPaintOuterRing);
            canvas.drawCircle(getWidth() /2, getHeight() /2 ,innerRadius, mPaintInnerRing);
        }
    }

    public void setCircleStyle(CircleStyle style){
        if(style != mCircleStyle){
            mCircleStyle = style;
            postInvalidate();
        }
    }

    public void setCircleColor(int color){
        if(color != mColor){
            mColor = color;
            mPaintOuterRing.setColor(mColor);
            mPaintInnerRing.setColor(mColor);
            postInvalidate();
        }
    }

    public int getCircleColor(){
        return mColor;
    }
}
