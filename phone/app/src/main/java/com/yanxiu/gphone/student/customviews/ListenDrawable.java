package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 9:14.
 * Function :
 */
class ListenDrawable extends Drawable {

    private static final int mStartDown= R.drawable.listener_start_press;
    private static final int mStartUp=R.drawable.listener_start_normal;

    private static final int mEndDown=R.drawable.listener_end_press;
    private static final int mEndUp=R.drawable.listener_end_normal;

    static final int TYPE_START=0x000;
    static final int TYPE_END=0x001;

    private boolean isZoom=false;

    private Context mContext;
    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF mRectF;
    private int mPadding;
    private int mType=TYPE_START;

    ListenDrawable(Context context,Bitmap bitmap, int padding){
        this.mContext=context;
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

    public void setType(int type){
        this.mType=type;
        Drawable drawable=null;
        if (mType==TYPE_END){
            drawable = ContextCompat.getDrawable(mContext, mEndUp);
        }else if (mType==TYPE_START){
            drawable = ContextCompat.getDrawable(mContext, mStartUp);
        }
        if (drawable!=null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            mBitmap = bitmapDrawable.getBitmap();
            zoomBitmap(mRectF.width()-mPadding*2, mRectF.height()-mPadding*2);
            invalidateSelf();
        }
    }

    public void setEvent(boolean isDown){
        Drawable drawable;
        if (mType==TYPE_END){
            if (isDown){
                drawable = ContextCompat.getDrawable(mContext, mEndDown);
            }else {
                drawable = ContextCompat.getDrawable(mContext, mEndUp);
            }
        }else {
            if (isDown){
                drawable=ContextCompat.getDrawable(mContext, mStartDown);
            }else {
                drawable=ContextCompat.getDrawable(mContext, mStartUp);
            }
        }
        if (drawable!=null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            mBitmap = bitmapDrawable.getBitmap();
            zoomBitmap(mRectF.width()-mPadding*2, mRectF.height()-mPadding*2);
            invalidateSelf();
        }
    }
}
