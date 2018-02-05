package com.yanxiu.gphone.student.questions.operation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sunpeng on 2018/1/12.
 * 三角形View
 */

public class TriangleView extends View {
    private int mColor = Color.BLACK;
    private Paint mPaint;
    private Path mPath;
    public TriangleView(Context context) {
        super(context);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mPath == null){
            mPath = new Path();
            mPath.moveTo(getWidth() / 2, 0);
            mPath.lineTo(0,getHeight());
            mPath.lineTo(getWidth(),getHeight());
            mPath.lineTo(getWidth() /2 ,0);
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
