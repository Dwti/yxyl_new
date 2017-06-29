package com.yanxiu.gphone.student.customviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 9:14.
 * Function :
 */
class ListenDrawable extends Drawable {

    private boolean isZoom=false;

    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF mRectF;
    private int mPadding;

    ListenDrawable(Bitmap bitmap, int padding){
        this.mBitmap=bitmap;
        this.mPadding=padding;
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRectF=new RectF(left,top,right,bottom);
        if (!isZoom) {
            zoomBitmap(mRectF.width()-mPadding*2, mRectF.height()-mPadding*2);
            isZoom=true;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect rect=getBounds();
        canvas.drawBitmap(mBitmap,rect.left+mPadding,rect.top+mPadding,mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mRectF!=null?(int) mRectF.width():mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mRectF!=null?(int) mRectF.height():mBitmap.getHeight();
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private void zoomBitmap(float width, float height) {
        this.mBitmap=Bitmap.createScaledBitmap(mBitmap,(int) width,(int) height,false);
    }

    Bitmap getBitmap(){
        return mBitmap;
    }
}
