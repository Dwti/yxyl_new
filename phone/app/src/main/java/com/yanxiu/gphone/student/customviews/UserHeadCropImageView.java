package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.common.interfaces.onCropFinishedListener;
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

    private static final float DEFAULT_LINE_STROKEWIDTH = 2;
    private static final float DEFAULT_SHARP_STROKEWIDTH = 4;
    private static final float DEFAULT_SHARP_LENG = 44;
    private static final float DEFAULT_PADDING = DEFAULT_LINE_STROKEWIDTH + DEFAULT_SHARP_STROKEWIDTH;
    //正方形边长最小长度
    private static final float MIN_RECT_SIDE_LENGTH=DEFAULT_SHARP_LENG * 2 - DEFAULT_PADDING * 2;

    /**
     * 正比
     */
    private static final float TYPE_SCALE_W_H = DEFAULT_PADDING * 8;
    /**
     * 反比
     */
    private static final float TYPE_MOVE_W_H = DEFAULT_PADDING * 3;
    private static final float CHECK_LINE_OR_SHARP=TYPE_SCALE_W_H+ DEFAULT_SHARP_LENG;

    private static final int DEFAULT_BACKGROUNDCOLOR = Color.parseColor("#4d000000");
    private static final int DEFAULT_LINECOLOR = Color.parseColor("#89e00d");

    private static final String TYPE_DEFAULT = "default";
    private static final String TYPE_SCALE = "scale";
    private static final String TYPE_MOVE = "move";

    private static final String PRESS_DEFAULT = "default";
    private static final String PRESS_LINE_LEFT = "left";
    private static final String PRESS_LINE_TOP = "top";
    private static final String PRESS_LINE_RIGHT = "right";
    private static final String PRESS_LINE_BOTTOM = "bottom";
    private static final String PRESS_ANGLE_L_T = "left_top";
    private static final String PRESS_ANGLE_R_T = "right_top";
    private static final String PRESS_ANGLE_L_B = "left_bottom";
    private static final String PRESS_ANGLE_R_B = "right_bottom";

    private int mWidth;
    private int mHeight;

    private boolean isShowCropBox = false;
    private Paint mCropPaint;
    private Paint mConverPaint;
    private PorterDuffXfermode mXfermode;
    private String mType = TYPE_DEFAULT;
    private String mPressSite = PRESS_DEFAULT;
    private float mEvent_X;
    private float mEvent_Y;

    private Rect mCropRect;
    private Rect mTypeScaleRect;
    private Rect mTypeMoveRect;
    private CropDrawable drawable;

    public UserHeadCropImageView(Context context) {
        this(context, null);
    }

    public UserHeadCropImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserHeadCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
        setPadding(0, 0, 0, 0);
    }

    private void initPaint() {
        isShowCropBox = false;

        mCropPaint = new Paint();
        mCropPaint.setAntiAlias(true);
        mCropPaint.setDither(true);
        mCropPaint.setStrokeWidth(DEFAULT_LINE_STROKEWIDTH);
        mCropPaint.setStyle(Paint.Style.FILL);
        mCropPaint.setColor(DEFAULT_LINECOLOR);

        mConverPaint = new Paint();
        mConverPaint.setStyle(Paint.Style.FILL);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        isShowCropBox = true;
        drawable = new CropDrawable(bm, (int) TYPE_SCALE_W_H, new CropDrawable.onBoundsFinishedListener() {
            @Override
            public void onBoundsFisished(int width, int height) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
                if (params.width != width || params.height != height) {
                    params.width = width;
                    params.height = height;
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    setLayoutParams(params);
                }
            }
        });
        setBackgroundDrawable(drawable);
    }

    public void startCrop(onCropFinishedListener cropFinishedListener) {
        try {
            SaveBitmapTask saveBitmapTask = new SaveBitmapTask(mCropRect, cropFinishedListener);
            saveBitmapTask.execute(drawable.getBitmap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setReset() {
        mCropRect = new Rect((int) TYPE_SCALE_W_H, (int) TYPE_SCALE_W_H, mWidth - (int) TYPE_SCALE_W_H, mHeight - (int) TYPE_SCALE_W_H);
        mTypeScaleRect = new Rect(mCropRect.left - (int) TYPE_SCALE_W_H, mCropRect.top - (int) TYPE_SCALE_W_H, mCropRect.right + (int) TYPE_SCALE_W_H, mCropRect.bottom + (int) TYPE_SCALE_W_H);
        mTypeMoveRect = new Rect(mCropRect.left + (int) TYPE_MOVE_W_H, mCropRect.top + (int) TYPE_MOVE_W_H, mCropRect.right - (int) TYPE_MOVE_W_H, mCropRect.bottom - (int) TYPE_MOVE_W_H);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;

        int left;
        int top;
        int right;
        int bottom;
        if (mWidth > mHeight) {
            left = (mWidth - mHeight) / 2 + (int) TYPE_SCALE_W_H;
            top = (int) TYPE_SCALE_W_H;
            right = mWidth - ((mWidth - mHeight) / 2 + (int) TYPE_SCALE_W_H);
            bottom = mHeight - (int) TYPE_SCALE_W_H;
        } else if (mHeight > mWidth) {
            left = (int) TYPE_SCALE_W_H;
            top = (mHeight - mWidth) / 2 + (int) TYPE_SCALE_W_H;
            right = mWidth - (int) TYPE_SCALE_W_H;
            bottom = mHeight - ((mHeight - mWidth) / 2 + (int) TYPE_SCALE_W_H);
        } else {
            left = (int) TYPE_SCALE_W_H;
            top = (int) TYPE_SCALE_W_H;
            right = mWidth - (int) TYPE_SCALE_W_H;
            bottom = mHeight - (int) TYPE_SCALE_W_H;
        }
        mCropRect = new Rect(left, top, right, bottom);
        mTypeScaleRect = new Rect(mCropRect.left - (int) TYPE_SCALE_W_H, mCropRect.top - (int) TYPE_SCALE_W_H, mCropRect.right + (int) TYPE_SCALE_W_H, mCropRect.bottom + (int) TYPE_SCALE_W_H);
        mTypeMoveRect = new Rect(mCropRect.left + (int) TYPE_MOVE_W_H, mCropRect.top + (int) TYPE_MOVE_W_H, mCropRect.right - (int) TYPE_MOVE_W_H, mCropRect.bottom - (int) TYPE_MOVE_W_H);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowCropBox) {
            int layerId = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            mConverPaint.setColor(DEFAULT_BACKGROUNDCOLOR);
            canvas.drawRect(TYPE_SCALE_W_H, TYPE_SCALE_W_H, mWidth - TYPE_SCALE_W_H, mHeight - TYPE_SCALE_W_H, mConverPaint);
            mConverPaint.setXfermode(mXfermode);
            mConverPaint.setColor(Color.TRANSPARENT);
            canvas.drawRect(mCropRect, mConverPaint);
            mConverPaint.setXfermode(null);
            canvas.restoreToCount(layerId);
            drawLines(canvas, mCropRect);
            drawSharp(canvas, mCropRect);
        }
    }

    private void drawLines(Canvas canvas, final Rect rect) {
        if (rect == null) {
            return;
        }
        int centerToBitmap = (int) DEFAULT_LINE_STROKEWIDTH / 2;
        int sideToBitmap = (int) DEFAULT_LINE_STROKEWIDTH;

        mCropPaint.setStrokeWidth((int) DEFAULT_LINE_STROKEWIDTH);
        canvas.drawLine(rect.left - sideToBitmap, rect.top - centerToBitmap, rect.right + sideToBitmap, rect.top - centerToBitmap, mCropPaint);
        canvas.drawLine(rect.left, rect.height() / 3 + rect.top, rect.right, rect.height() / 3 + rect.top, mCropPaint);
        canvas.drawLine(rect.left, rect.height() * 2 / 3 + rect.top, rect.right, rect.height() * 2 / 3 + rect.top, mCropPaint);
        canvas.drawLine(rect.left - sideToBitmap, rect.bottom + centerToBitmap, rect.right + sideToBitmap, rect.bottom + centerToBitmap, mCropPaint);

        canvas.drawLine(rect.left - centerToBitmap, rect.top - sideToBitmap, rect.left - centerToBitmap, rect.bottom + sideToBitmap, mCropPaint);
        canvas.drawLine(rect.width() / 3 + rect.left, rect.top, rect.width() / 3 + rect.left, rect.bottom, mCropPaint);
        canvas.drawLine(rect.width() * 2 / 3 + rect.left, rect.top, rect.width() * 2 / 3 + rect.left, rect.bottom, mCropPaint);
        canvas.drawLine(rect.right + centerToBitmap, rect.top - sideToBitmap, rect.right + centerToBitmap, rect.bottom + sideToBitmap, mCropPaint);
    }

    private void drawSharp(Canvas canvas, final Rect rect) {
        if (rect == null) {
            return;
        }
        int centerToBitmap = (int) DEFAULT_LINE_STROKEWIDTH + (int) DEFAULT_SHARP_STROKEWIDTH / 2;
        int sideToBitmap = (int) DEFAULT_LINE_STROKEWIDTH + (int) DEFAULT_SHARP_STROKEWIDTH;

        mCropPaint.setStrokeWidth(DEFAULT_SHARP_STROKEWIDTH);
        canvas.drawLine(rect.left - sideToBitmap, rect.top - centerToBitmap, rect.left - sideToBitmap + DEFAULT_SHARP_LENG, rect.top - centerToBitmap, mCropPaint);
        canvas.drawLine(rect.right + sideToBitmap - DEFAULT_SHARP_LENG, rect.top - centerToBitmap, rect.right + sideToBitmap, rect.top - centerToBitmap, mCropPaint);
        canvas.drawLine(rect.left - sideToBitmap, rect.bottom + centerToBitmap, rect.left - sideToBitmap + DEFAULT_SHARP_LENG, rect.bottom + centerToBitmap, mCropPaint);
        canvas.drawLine(rect.right + sideToBitmap - DEFAULT_SHARP_LENG, rect.bottom + centerToBitmap, rect.right + sideToBitmap, rect.bottom + centerToBitmap, mCropPaint);

        canvas.drawLine(rect.left - centerToBitmap, rect.top - sideToBitmap, rect.left - centerToBitmap, rect.top - sideToBitmap + DEFAULT_SHARP_LENG, mCropPaint);
        canvas.drawLine(rect.left - centerToBitmap, rect.bottom + sideToBitmap - DEFAULT_SHARP_LENG, rect.left - centerToBitmap, rect.bottom + sideToBitmap, mCropPaint);
        canvas.drawLine(rect.right + centerToBitmap, rect.top - sideToBitmap, rect.right + centerToBitmap, rect.top - sideToBitmap + DEFAULT_SHARP_LENG, mCropPaint);
        canvas.drawLine(rect.right + centerToBitmap, rect.bottom + sideToBitmap - DEFAULT_SHARP_LENG, rect.right + centerToBitmap, rect.bottom + sideToBitmap, mCropPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEvent_X = event.getX();
                mEvent_Y = event.getY();
                if (mTypeScaleRect != null && mTypeMoveRect != null && mTypeScaleRect.contains((int) mEvent_X, (int) mEvent_Y)) {
                    if (mTypeMoveRect.contains((int) mEvent_X, (int) mEvent_Y)) {
                        mType = TYPE_MOVE;
                    } else {
                        mType = TYPE_SCALE;
                        if (mEvent_X < mTypeMoveRect.left && (mEvent_Y < mTypeScaleRect.bottom - CHECK_LINE_OR_SHARP&& mEvent_Y > mTypeScaleRect.top +CHECK_LINE_OR_SHARP)) {
                            mPressSite = PRESS_LINE_LEFT;
                        } else if (mEvent_X > mTypeMoveRect.right && (mEvent_Y < mTypeScaleRect.bottom - CHECK_LINE_OR_SHARP&& mEvent_Y > mTypeScaleRect.top + CHECK_LINE_OR_SHARP)) {
                            mPressSite = PRESS_LINE_RIGHT;
                        } else if (mEvent_Y < mTypeMoveRect.top && (mEvent_X < mTypeScaleRect.right - CHECK_LINE_OR_SHARP && mEvent_X > mTypeScaleRect.left + CHECK_LINE_OR_SHARP)) {
                            mPressSite = PRESS_LINE_TOP;
                        } else if (mEvent_Y > mTypeMoveRect.bottom && (mEvent_X < mTypeScaleRect.right - CHECK_LINE_OR_SHARP && mEvent_X > mTypeScaleRect.left + CHECK_LINE_OR_SHARP)) {
                            mPressSite = PRESS_LINE_BOTTOM;
                        } else if (mEvent_X < mTypeMoveRect.left && mEvent_Y >= mTypeScaleRect.bottom - CHECK_LINE_OR_SHARP) {
                            mPressSite = PRESS_ANGLE_L_B;
                        } else if (mEvent_X < mTypeMoveRect.left && mEvent_Y <= mTypeScaleRect.top + CHECK_LINE_OR_SHARP) {
                            mPressSite = PRESS_ANGLE_L_T;
                        } else if (mEvent_X > mTypeMoveRect.right && mEvent_Y >= mTypeScaleRect.bottom - CHECK_LINE_OR_SHARP) {
                            mPressSite = PRESS_ANGLE_R_B;
                        } else if (mEvent_X > mTypeMoveRect.right && (mEvent_Y <= mTypeScaleRect.top + CHECK_LINE_OR_SHARP)) {
                            mPressSite = PRESS_ANGLE_R_T;
                        } else if (mEvent_Y < mTypeMoveRect.top && mEvent_X <= mTypeScaleRect.left + CHECK_LINE_OR_SHARP) {
                            mPressSite = PRESS_ANGLE_L_T;
                        } else if (mEvent_Y < mTypeMoveRect.top && mEvent_X >= mTypeScaleRect.right - CHECK_LINE_OR_SHARP) {
                            mPressSite = PRESS_ANGLE_R_T;
                        } else if (mEvent_Y > mTypeMoveRect.bottom && mEvent_X <= mTypeScaleRect.left + CHECK_LINE_OR_SHARP) {
                            mPressSite = PRESS_ANGLE_L_B;
                        } else if (mEvent_Y > mTypeMoveRect.bottom && mEvent_X >= mTypeScaleRect.right - CHECK_LINE_OR_SHARP) {
                            mPressSite = PRESS_ANGLE_R_B;
                        } else {
                            mPressSite = PRESS_DEFAULT;
                        }
                    }
                } else {
                    mType = TYPE_DEFAULT;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float move_X = event.getX();
                float move_Y = event.getY();

                float change_X = move_X - mEvent_X;
                float change_Y = move_Y - mEvent_Y;

                mEvent_X = move_X;
                mEvent_Y = move_Y;
                if (mType.equals(TYPE_SCALE)) {
                    if (!mPressSite.equals(PRESS_DEFAULT)) {
                        setScaleRect(change_X, change_Y, mPressSite);
                    }
                } else if (mType.equals(TYPE_MOVE)) {
                    setMoveRect(change_X, change_Y);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mEvent_X = 0;
                mEvent_Y = 0;
                mType = TYPE_DEFAULT;
                mPressSite = PRESS_DEFAULT;
                break;
        }
        return true;
    }

    private void setScaleRect(final float changeX, final float changeY, final String pressSite) {
        float start_left = mCropRect.left;
        float start_top = mCropRect.top;
        float start_right = mCropRect.right;
        float start_bottom = mCropRect.bottom;

        float end_left = 0;
        float end_top = 0;
        float end_right = 0;
        float end_bottom = 0;
        float changLength;

        switch (pressSite) {
            case PRESS_DEFAULT:
                end_top = start_top;
                end_bottom = start_bottom;
                end_left = start_left;
                end_right = start_right;
                break;
            case PRESS_LINE_LEFT:
                end_top = start_top;
                end_bottom = start_bottom;
                end_right = start_right;
                end_left = start_left + changeX;
                if (end_left < TYPE_SCALE_W_H) {
                    end_left = TYPE_SCALE_W_H;
                }
                if (end_right - end_left < MIN_RECT_SIDE_LENGTH) {
                    end_right = end_left + MIN_RECT_SIDE_LENGTH;
                }
                if (end_right > mWidth - TYPE_SCALE_W_H) {
                    end_right = mWidth - TYPE_SCALE_W_H;
                    end_left = end_right - MIN_RECT_SIDE_LENGTH;
                }
                //set top and bottom
                changLength=end_left-start_left;
                if (changLength!=0){
                    end_top+=changLength/2;
                    end_bottom-=changLength/2;
                }
                //check point
                if (end_top<TYPE_SCALE_W_H){
                    end_top=TYPE_SCALE_W_H;
                }

                if (end_bottom > mHeight - TYPE_SCALE_W_H) {
                    end_bottom = mHeight - TYPE_SCALE_W_H;
                }

                if (end_bottom - end_top < MIN_RECT_SIDE_LENGTH) {
                    float surplus=MIN_RECT_SIDE_LENGTH-(end_bottom-end_top);
                    end_top-=surplus/2;
                    end_bottom+= surplus/2;
                }

                if (end_right-end_left!=end_bottom-end_top){
                    end_left=end_right-(end_bottom-end_top);
                }
                break;
            case PRESS_LINE_TOP:
                end_left = start_left;
                end_right = start_right;
                end_bottom = start_bottom;
                end_top = start_top + changeY;
                if (end_top < TYPE_SCALE_W_H) {
                    end_top = TYPE_SCALE_W_H;
                }
                if (end_bottom - end_top < MIN_RECT_SIDE_LENGTH) {
                    end_bottom = end_top + MIN_RECT_SIDE_LENGTH;
                }
                if (end_bottom > mHeight - TYPE_SCALE_W_H) {
                    end_bottom = mHeight - TYPE_SCALE_W_H;
                    end_top = end_bottom - MIN_RECT_SIDE_LENGTH;
                }
                //set
                changLength=end_top-start_top;
                if (changLength!=0){
                    end_left+=changLength/2;
                    end_right-=changLength/2;
                }

                //check
                if (end_left<TYPE_SCALE_W_H){
                    end_left=TYPE_SCALE_W_H;
                }
                if (end_right>mWidth - TYPE_SCALE_W_H){
                    end_right=mWidth - TYPE_SCALE_W_H;
                }

                if (end_right-end_left<MIN_RECT_SIDE_LENGTH){
                    float surpuls=MIN_RECT_SIDE_LENGTH-(end_right-end_left);
                    end_left-=surpuls/2;
                    end_right+=surpuls/2;
                }

                if (end_right-end_left!=end_bottom-end_top){
                    end_top=end_bottom-(end_right-end_left);
                }
                break;
            case PRESS_LINE_RIGHT:
                end_top = start_top;
                end_bottom = start_bottom;
                end_left = start_left;
                end_right = start_right + changeX;
                if (end_right > mWidth - TYPE_SCALE_W_H) {
                    end_right = mWidth - TYPE_SCALE_W_H;
                }
                if (end_right - end_left < MIN_RECT_SIDE_LENGTH) {
                    end_left = end_right - MIN_RECT_SIDE_LENGTH;
                }
                if (end_left < TYPE_SCALE_W_H) {
                    end_left = TYPE_SCALE_W_H;
                    end_right = end_left + MIN_RECT_SIDE_LENGTH;
                }
                //set
                changLength=end_right-start_right;
                if (changLength!=0){
                    end_top-=changLength/2;
                    end_bottom+=changLength/2;
                }
                //check
                if (end_top<TYPE_SCALE_W_H){
                    end_top=TYPE_SCALE_W_H;
                }

                if (end_bottom > mHeight - TYPE_SCALE_W_H) {
                    end_bottom = mHeight - TYPE_SCALE_W_H;
                }

                if (end_bottom - end_top < MIN_RECT_SIDE_LENGTH) {
                    float surplus=MIN_RECT_SIDE_LENGTH-(end_bottom-end_top);
                    end_top-=surplus/2;
                    end_bottom+= surplus/2;
                }

                if (end_right-end_left!=end_bottom-end_top){
                    end_right=end_left+(end_bottom-end_top);
                }
                break;
            case PRESS_LINE_BOTTOM:
                end_left = start_left;
                end_right = start_right;
                end_top = start_top;
                end_bottom = start_bottom + changeY;
                if (end_bottom > mHeight - TYPE_SCALE_W_H) {
                    end_bottom = mHeight - TYPE_SCALE_W_H;
                }
                if (end_bottom - end_top < MIN_RECT_SIDE_LENGTH) {
                    end_top = end_bottom - MIN_RECT_SIDE_LENGTH;
                }
                if (end_top < TYPE_SCALE_W_H) {
                    end_top = TYPE_SCALE_W_H;
                    end_bottom = end_top + MIN_RECT_SIDE_LENGTH;
                }
                //set
                changLength=end_bottom-start_bottom;
                if (changLength!=0){
                    end_left-=changLength/2;
                    end_right+=changLength/2;
                }
                //check
                if (end_left<TYPE_SCALE_W_H){
                    end_left=TYPE_SCALE_W_H;
                }
                if (end_right>mWidth - TYPE_SCALE_W_H){
                    end_right=mWidth - TYPE_SCALE_W_H;
                }

                if (end_right-end_left<MIN_RECT_SIDE_LENGTH){
                    float surpuls=MIN_RECT_SIDE_LENGTH-(end_right-end_left);
                    end_left-=surpuls/2;
                    end_right+=surpuls/2;
                }

                if (end_right-end_left!=end_bottom-end_top){
                    end_bottom=end_top+(end_right-end_left);
                }
                break;
            case PRESS_ANGLE_L_T:
                end_right = start_right;
                end_bottom = start_bottom;
                end_left = start_left + changeX;
                end_top = start_top + changeY;
                if (end_left < TYPE_SCALE_W_H) {
                    end_left = TYPE_SCALE_W_H;
                }
                if (end_right - end_left < MIN_RECT_SIDE_LENGTH) {
                    end_right = end_left + MIN_RECT_SIDE_LENGTH;
                }
                if (end_right > mWidth - TYPE_SCALE_W_H) {
                    end_right = mWidth - TYPE_SCALE_W_H;
                    end_left = end_right - MIN_RECT_SIDE_LENGTH;
                }
                if (end_top < TYPE_SCALE_W_H) {
                    end_top = TYPE_SCALE_W_H;
                }
                if (end_bottom - end_top < MIN_RECT_SIDE_LENGTH) {
                    end_bottom = end_top + MIN_RECT_SIDE_LENGTH;
                }
                if (end_bottom > mHeight - TYPE_SCALE_W_H) {
                    end_bottom = mHeight - TYPE_SCALE_W_H;
                    end_top = end_bottom - MIN_RECT_SIDE_LENGTH;
                }
                //check
                if (end_right-end_left!=end_bottom-end_top){
                    float changeNum=(end_bottom-end_top)>(end_right-end_left)?(end_right-end_left):(end_bottom-end_top);
                    end_left=end_right-changeNum;
                    end_top=end_bottom-changeNum;
                }
                break;
            case PRESS_ANGLE_R_T:
                end_left = start_left;
                end_bottom = start_bottom;
                end_right = start_right + changeX;
                end_top = start_top + changeY;
                if (end_right > mWidth - TYPE_SCALE_W_H) {
                    end_right = mWidth - TYPE_SCALE_W_H;
                }
                if (end_right - end_left < MIN_RECT_SIDE_LENGTH) {
                    end_left = end_right - MIN_RECT_SIDE_LENGTH;
                }
                if (end_left < TYPE_SCALE_W_H) {
                    end_left = TYPE_SCALE_W_H;
                    end_right = end_left + MIN_RECT_SIDE_LENGTH;
                }
                if (end_top < TYPE_SCALE_W_H) {
                    end_top = TYPE_SCALE_W_H;
                }
                if (end_bottom - end_top < MIN_RECT_SIDE_LENGTH) {
                    end_bottom = end_top + MIN_RECT_SIDE_LENGTH;
                }
                if (end_bottom > mHeight - TYPE_SCALE_W_H) {
                    end_bottom = mHeight - TYPE_SCALE_W_H;
                    end_top = end_bottom - MIN_RECT_SIDE_LENGTH;
                }
                //check
                if (end_right-end_left!=end_bottom-end_top){
                    float changeNum=(end_bottom-end_top)>(end_right-end_left)?(end_right-end_left):(end_bottom-end_top);
                    end_right=end_left+changeNum;
                    end_top=end_bottom-changeNum;
                }
                break;
            case PRESS_ANGLE_L_B:
                end_right = start_right;
                end_top = start_top;
                end_left = start_left + changeX;
                end_bottom = start_bottom + changeY;
                if (end_left < TYPE_SCALE_W_H) {
                    end_left = TYPE_SCALE_W_H;
                }
                if (end_right - end_left < MIN_RECT_SIDE_LENGTH) {
                    end_right = end_left + MIN_RECT_SIDE_LENGTH;
                }
                if (end_right > mWidth - TYPE_SCALE_W_H) {
                    end_right = mWidth - TYPE_SCALE_W_H;
                    end_left = end_right - MIN_RECT_SIDE_LENGTH;
                }
                if (end_bottom > mHeight - TYPE_SCALE_W_H) {
                    end_bottom = mHeight - TYPE_SCALE_W_H;
                }
                if (end_bottom - end_top < MIN_RECT_SIDE_LENGTH) {
                    end_top = end_bottom - MIN_RECT_SIDE_LENGTH;
                }
                if (end_top < TYPE_SCALE_W_H) {
                    end_top = TYPE_SCALE_W_H;
                    end_bottom = end_top + MIN_RECT_SIDE_LENGTH;
                }
                //check
                if (end_right-end_left!=end_bottom-end_top){
                    float changeNum=(end_bottom-end_top)>(end_right-end_left)?(end_right-end_left):(end_bottom-end_top);
                    end_left=end_right-changeNum;
                    end_bottom=end_top+changeNum;
                }
                break;
            case PRESS_ANGLE_R_B:
                end_left = start_left;
                end_top = start_top;
                end_right = start_right + changeX;
                end_bottom = start_bottom + changeY;
                if (end_right > mWidth - TYPE_SCALE_W_H) {
                    end_right = mWidth - TYPE_SCALE_W_H;
                }
                if (end_right - end_left < MIN_RECT_SIDE_LENGTH) {
                    end_left = end_right - MIN_RECT_SIDE_LENGTH;
                }
                if (end_left < TYPE_SCALE_W_H) {
                    end_left = TYPE_SCALE_W_H;
                    end_right = end_left + MIN_RECT_SIDE_LENGTH;
                }
                if (end_bottom > mHeight - TYPE_SCALE_W_H) {
                    end_bottom = mHeight - TYPE_SCALE_W_H;
                }
                if (end_bottom - end_top < MIN_RECT_SIDE_LENGTH) {
                    end_top = end_bottom - MIN_RECT_SIDE_LENGTH;
                }
                if (end_top < TYPE_SCALE_W_H) {
                    end_top = TYPE_SCALE_W_H;
                    end_bottom = end_top + MIN_RECT_SIDE_LENGTH;
                }
                //check
                if (end_right-end_left!=end_bottom-end_top){
                    float changeNum=(end_bottom-end_top)>(end_right-end_left)?(end_right-end_left):(end_bottom-end_top);
                    end_right=end_left+changeNum;
                    end_bottom=end_top+changeNum;
                }
                break;
        }
        mCropRect = new Rect((int) end_left, (int) end_top, (int) end_right, (int) end_bottom);
        mTypeScaleRect = new Rect(mCropRect.left - (int) TYPE_SCALE_W_H, mCropRect.top - (int) TYPE_SCALE_W_H, mCropRect.right + (int) TYPE_SCALE_W_H, mCropRect.bottom + (int) TYPE_SCALE_W_H);
        mTypeMoveRect = new Rect(mCropRect.left + (int) TYPE_MOVE_W_H, mCropRect.top + (int) TYPE_MOVE_W_H, mCropRect.right - (int) TYPE_MOVE_W_H, mCropRect.bottom - (int) TYPE_MOVE_W_H);
    }

    private void setMoveRect(final float changeX, final float changeY) {
        float start_left = mCropRect.left;
        float start_top = mCropRect.top;
        float start_right = mCropRect.right;
        float start_bottom = mCropRect.bottom;

        int width = mCropRect.width();
        int height = mCropRect.height();

        float end_left = start_left + changeX;
        float end_top = start_top + changeY;
        float end_right = start_right + changeX;
        float end_bottom = start_bottom + changeY;

        if (end_left < TYPE_SCALE_W_H) {
            end_left = (int) TYPE_SCALE_W_H;
            end_right = end_left + width;
        }
        if (end_right > mWidth - TYPE_SCALE_W_H) {
            end_right = mWidth - (int) TYPE_SCALE_W_H;
            end_left = end_right - width;
        }
        if (end_top < TYPE_SCALE_W_H) {
            end_top = (int) TYPE_SCALE_W_H;
            end_bottom = end_top + height;
        }
        if (end_bottom > mHeight - TYPE_SCALE_W_H) {
            end_bottom = mHeight - (int) TYPE_SCALE_W_H;
            end_top = end_bottom - height;
        }
        mCropRect = new Rect((int) end_left, (int) end_top, (int) end_right, (int) end_bottom);
        mTypeScaleRect = new Rect(mCropRect.left - (int) TYPE_SCALE_W_H, mCropRect.top - (int) TYPE_SCALE_W_H, mCropRect.right + (int) TYPE_SCALE_W_H, mCropRect.bottom + (int) TYPE_SCALE_W_H);
        mTypeMoveRect = new Rect(mCropRect.left + (int) TYPE_MOVE_W_H, mCropRect.top + (int) TYPE_MOVE_W_H, mCropRect.right - (int) TYPE_MOVE_W_H, mCropRect.bottom - (int) TYPE_MOVE_W_H);
    }

    private class SaveBitmapTask extends AsyncTask<Bitmap, Integer, String> {

        private Rect mRect;
        private onCropFinishedListener mCropFinishedListener;

        SaveBitmapTask(Rect rect, onCropFinishedListener cropFinishedListener) {
            this.mRect = rect;
            this.mCropFinishedListener = cropFinishedListener;
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmap = Bitmap.createBitmap(params[0], mRect.left - (int) TYPE_SCALE_W_H, mRect.top - (int) TYPE_SCALE_W_H, mRect.width(), mRect.height());
            String filePath = FileUtil.getSavePicturePath(System.currentTimeMillis() + ".jpg");
            File file = new File(filePath);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                filePath = null;
            } finally {
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
            if (mCropFinishedListener != null) {
                mCropFinishedListener.onFinished(s);
            }
        }
    }

}
