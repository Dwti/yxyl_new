package com.yanxiu.gphone.student.customviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
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

    ListenDrawable(Bitmap bitmap){
        this.mBitmap=bitmap;
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRectF=new RectF(left,top,right,bottom);
        if (!isZoom) {
            zoomBitmap(right - left, bottom - top);
            isZoom=true;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect rect=getBounds();
        canvas.drawBitmap(mBitmap,rect.left,rect.top,mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) mRectF.width();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) mRectF.height();
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

    private void zoomBitmap(int width, int height) {
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        this.mBitmap=Bitmap.createBitmap(mBitmap, 0, 0, w, h, matrix, true);
    }
}
