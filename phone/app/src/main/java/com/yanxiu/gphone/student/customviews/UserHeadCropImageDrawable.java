package com.yanxiu.gphone.student.customviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/4 15:41.
 * Function :
 */
public class UserHeadCropImageDrawable extends Drawable {

    private Bitmap mBitmap;
    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private int mMove_x;
    private int mMove_y;

    private boolean isFirst=true;

    public UserHeadCropImageDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect dstRect=getBounds();

        int width = dstRect.right - dstRect.left;
        int height = dstRect.bottom - dstRect.top;

        int maxSrcMoveX=mBitmap.getWidth()-width;
        int maxSrcMoveY=mBitmap.getHeight()-height;

        int maxDstMoveX=(width-mWidth)/2;
        int maxDstMoveY=(height-mHeight)/2;

        if (isFirst){
            mMove_x=-(mBitmap.getWidth()-width)/2;
            mMove_y=-(mBitmap.getHeight()-height)/2;
            isFirst=false;
        }

        if (mMove_x>maxDstMoveX){
            mMove_x=maxDstMoveX;
        }else if (mMove_x<-(maxSrcMoveX+maxDstMoveX)){
            mMove_x=-(maxSrcMoveX+maxDstMoveX);
        }

        if (mMove_y>maxDstMoveY){
            mMove_y=maxDstMoveY;
        }else if (mMove_y<-(maxSrcMoveY+maxDstMoveY)){
            mMove_y=-(maxSrcMoveY+maxDstMoveY);
        }

        canvas.drawBitmap(mBitmap,mMove_x,mMove_y,mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
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

    public void setLocation(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void move(int x, int y) {
        this.mMove_x += x;
        this.mMove_y += y;
        invalidateSelf();
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }

    public int getMoveX(){
        return mMove_x;
    }

    public int getMoveY(){
        return mMove_y;
    }
}
