package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yanxiu.gphone.student.util.FileUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/4 14:07.
 * Function :
 */
public class UserHeadCropImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int DEFAULT_LINE_STROKEWIDTH = 4;

    public static final int mWidth = 650;
    public static final int mHeight = 650;
    private RectF mDrawRect;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private UserHeadCropImageDrawable drawable;

    private int mDown_X;
    private int mDown_Y;

    public UserHeadCropImageView(Context context) {
        this(context, null);
    }

    public UserHeadCropImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserHeadCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    public void startCrop(CropImageView.onCropFinishedListener cropFinishedListener){
        Bitmap bitmap=drawable.getBitmap();
        int cropStartX= (int) (mDrawRect.left-drawable.getMoveX());
        int cropStartY= (int) (mDrawRect.top-drawable.getMoveY());
        SaveBitmapTask saveBitmapTask=new SaveBitmapTask(bitmap,cropStartX,cropStartY,cropFinishedListener);
        saveBitmapTask.execute(drawable.getBitmap());
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int scale;
        if (width < mWidth) {
            scale = mWidth / width;
            width = mWidth;
            height = height * scale;
        }
        if (height < mHeight) {
            scale = mHeight / height;
            height = mHeight;
            width = width * scale;
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, false);
        drawable = new UserHeadCropImageDrawable(bm);
        drawable.setLocation(mWidth,mHeight);
        setBackgroundDrawable(drawable);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawRect = new RectF(getWidth() / 2 - mWidth / 2, getHeight() / 2 - mHeight / 2, getWidth() / 2 + mWidth / 2, getHeight() / 2 + mHeight / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int layerId = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);

        mPaint.reset();
        mPaint.setColor(Color.parseColor("#4d000000"));
        canvas.drawPaint(mPaint);

        mPaint.reset();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(mDrawRect, mPaint);

        canvas.restoreToCount(layerId);

        drawLines(canvas);
    }

    private void drawLines(Canvas canvas) {
        mPaint.reset();
        mPaint.setColor(Color.parseColor("#89e00d"));
        mPaint.setStrokeWidth(DEFAULT_LINE_STROKEWIDTH);

        Point left_top_t = new Point((int) mDrawRect.left + DEFAULT_LINE_STROKEWIDTH / 2, (int) mDrawRect.top);
        Point left_top_l = new Point((int) mDrawRect.left, (int) mDrawRect.top + DEFAULT_LINE_STROKEWIDTH / 2);

        Point right_top_t = new Point((int) mDrawRect.right - DEFAULT_LINE_STROKEWIDTH / 2, (int) mDrawRect.top);
        Point right_top_r = new Point((int) mDrawRect.right, (int) mDrawRect.top + DEFAULT_LINE_STROKEWIDTH / 2);

        Point left_bottom_b = new Point((int) mDrawRect.left + DEFAULT_LINE_STROKEWIDTH / 2, (int) mDrawRect.bottom);
        Point left_bottom_l = new Point((int) mDrawRect.left, (int) mDrawRect.bottom - DEFAULT_LINE_STROKEWIDTH / 2);

        Point right_bottom_b = new Point((int) mDrawRect.right - DEFAULT_LINE_STROKEWIDTH / 2, (int) mDrawRect.bottom);
        Point right_bottom_r = new Point((int) mDrawRect.right, (int) mDrawRect.bottom - DEFAULT_LINE_STROKEWIDTH / 2);

        canvas.drawLine(left_top_t.x, left_top_t.y, left_bottom_b.x, left_bottom_b.y, mPaint);
        canvas.drawLine(left_top_l.x, left_top_l.y, right_top_r.x, right_top_r.y, mPaint);
        canvas.drawLine(left_bottom_l.x, left_bottom_l.y, right_bottom_r.x, right_bottom_r.y, mPaint);
        canvas.drawLine(right_top_t.x, right_top_t.y, right_bottom_b.x, right_bottom_b.y, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDown_X= (int) event.getX();
                mDown_Y= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int now_x=(int) event.getX();
                int now_y=(int) event.getY();
                drawable.move(now_x-mDown_X,now_y-mDown_Y);
                mDown_X=now_x;
                mDown_Y=now_y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private class SaveBitmapTask extends AsyncTask<Bitmap,Integer,String> {

        private Bitmap mBitmap;
        private int mStartX;
        private int mStartY;
        private CropImageView.onCropFinishedListener mCropFinishedListener;

        SaveBitmapTask(Bitmap bitmap,int startX,int startY,CropImageView.onCropFinishedListener cropFinishedListener){
            this.mBitmap=bitmap;
            this.mStartX=startX;
            this.mStartY=startY;
            this.mCropFinishedListener=cropFinishedListener;
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmap=Bitmap.createBitmap(mBitmap,mStartX,mStartY,mWidth,mHeight);
            String filePath = FileUtil.getSavePicturePath(System.currentTimeMillis() + ".jpg");
            File file = new File(filePath);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                filePath = null;
            }finally {
                try {
                    if (bos != null) {
                        bos.flush();
                        bos.close();
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mCropFinishedListener!=null){
                mCropFinishedListener.onFinished(s);
            }
        }
    }

}
