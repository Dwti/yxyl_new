package com.yanxiu.gphone.student.questions.operation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.ScreenUtils;

/**
 * Created by sunpeng on 2018/1/13.
 */

public class ColorFrameRelativeLayout extends RelativeLayout {
    private int mColor = Color.BLACK;
    private Paint mPaint;
    private Path mPath;
    private boolean mFrameVisible = false; // 是否绘制边框
    public ColorFrameRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public ColorFrameRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setupAttrs(context,attrs);
    }

    public ColorFrameRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setupAttrs(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorFrameRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        setupAttrs(context,attrs);
    }

    private void init(Context context){
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(ScreenUtils.dpToPx(context,2));

        setWillNotDraw(false);
    }

    private void setupAttrs(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorFrameRelativeLayout);
        mFrameVisible = a.getBoolean(R.styleable.ColorFrameRelativeLayout_frameVisible,false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mFrameVisible){
            if(mPath == null){
                mPath = new Path();
                mPath.moveTo(0,getHeight());
                mPath.lineTo(0,0);
                mPath.lineTo(getWidth(),0);
                mPath.lineTo(getWidth(),getHeight());
            }
            canvas.drawPath(mPath,mPaint);
        }
    }

    public void setColor(int color){
        if(color != mColor){
            mColor = color;
            mPaint.setColor(mColor);
            postInvalidate();
        }
    }

    public void setFrameVisible(boolean visible){
        if(mFrameVisible != visible){
           mFrameVisible = visible;
           postInvalidate();
        }

    }

    public boolean getFrameVisible(){
        return mFrameVisible;
    }
}
