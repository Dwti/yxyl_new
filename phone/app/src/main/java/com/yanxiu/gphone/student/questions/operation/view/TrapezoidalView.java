package com.yanxiu.gphone.student.questions.operation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.yanxiu.gphone.student.R;

/**
 * Created by sunpeng on 2018/1/12.
 * 梯形View
 */

public class TrapezoidalView extends View {
    private int mColor = Color.BLACK;
    private Paint mPaint;
    private Path mPath;
    private float mRatio = 0.5f; //梯形的上底占宽度的比率
    public TrapezoidalView(Context context) {
        super(context);
        init();
    }

    public TrapezoidalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        setupRatio(context,attrs);
    }

    public TrapezoidalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setupRatio(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TrapezoidalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        setupRatio(context,attrs);
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void setupRatio(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrapezoidalView);
        mRatio = a.getFloat(R.styleable.TrapezoidalView_ratio,0.5f);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mPath == null){
            mPath = new Path();

            float topWidth = getWidth() * mRatio;
            mPath.moveTo((getWidth() - topWidth )/ 2, 0);
            mPath.lineTo(0,getHeight());
            mPath.lineTo(getWidth(),getHeight());
            mPath.lineTo( (getWidth() + topWidth ) /2 ,0);
            mPath.lineTo((getWidth() - topWidth )/ 2, 0);
        }
        canvas.drawPath(mPath,mPaint);
    }

    public void setColor(int color){
        if(mColor != color){
            mColor = color;
            mPaint.setColor(mColor);
            postInvalidate();
        }
    }
}
