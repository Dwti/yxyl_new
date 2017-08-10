package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yanxiu.gphone.student.R;

import java.lang.ref.WeakReference;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/3 11:47.
 * Function :
 */
public class UserInfoHeadImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int DEFAULT_TEXT_HEIGHT=100;
    private static final int DEFAULT_TEXT_SIZE=40;
    private static final int DEFAULT_TEXT_BGCOLOR=Color.parseColor("#bf89e00d");
    private static final int DEFAULT_TEXT_COLOR=Color.BLACK;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private WeakReference<Bitmap> mWeakReference;

    private int mTextHeight=DEFAULT_TEXT_HEIGHT;
    private int mTextSize=DEFAULT_TEXT_SIZE;
    private int mTextBgColor=DEFAULT_TEXT_BGCOLOR;
    private int mTextColor=DEFAULT_TEXT_COLOR;
    private String mText="asd";

    public UserInfoHeadImageView(Context context) {
        this(context,null);
    }

    public UserInfoHeadImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UserInfoHeadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.UserInfoHeadImageView);
        mText=array.getString(R.styleable.UserInfoHeadImageView_text);
        mTextBgColor=array.getColor(R.styleable.UserInfoHeadImageView_textBgColor,DEFAULT_TEXT_BGCOLOR);
        mTextColor=array.getColor(R.styleable.UserInfoHeadImageView_textColor,DEFAULT_TEXT_COLOR);
        mTextHeight=array.getDimensionPixelSize(R.styleable.UserInfoHeadImageView_textHeight,DEFAULT_TEXT_HEIGHT);
        mTextSize=array.getDimensionPixelSize(R.styleable.UserInfoHeadImageView_textSize,DEFAULT_TEXT_SIZE);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = mWeakReference != null ? mWeakReference.get() : null;
        if (bitmap == null||bitmap.isRecycled()) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas bitmapCanvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, getWidth(), getHeight());
                drawable.draw(bitmapCanvas);

                mPaint.reset();
                Bitmap maskBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas maskCanvas = new Canvas(maskBitmap);
                maskCanvas.drawOval(new RectF(0, 0, getWidth(), getHeight()), mPaint);

                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                bitmapCanvas.drawBitmap(maskBitmap, 0, 0, mPaint);

                mPaint.reset();
                Bitmap textBitmap = Bitmap.createBitmap(getWidth(), mTextHeight, Bitmap.Config.ARGB_8888);
                Canvas textCanvas = new Canvas(textBitmap);
                mPaint.setColor(mTextBgColor);
                textCanvas.drawPaint(mPaint);

                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                bitmapCanvas.drawBitmap(textBitmap, 0, getHeight() - textBitmap.getHeight(), mPaint);

                if (!TextUtils.isEmpty(mText)) {
                    mPaint.reset();
                    mPaint.measureText(mText);
                    Paint.FontMetricsInt metrics = mPaint.getFontMetricsInt();
                    int paddingTop=getPaddingTop();
                    int baseLine = paddingTop-metrics.top;
                    mPaint.setColor(mTextColor);
                    mPaint.setTextSize(mTextSize);
                    mPaint.setAntiAlias(true);
                    mPaint.setTextAlign(Paint.Align.CENTER);
                    bitmapCanvas.drawText(mText, getWidth() / 2, getHeight() - textBitmap.getHeight()/2+baseLine, mPaint);
                }
                mWeakReference = new WeakReference<>(bitmap);
                maskBitmap.recycle();
                textBitmap.recycle();
            }
        }
        if (bitmap != null) {
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (mWeakReference!=null){
            Bitmap bitmap=mWeakReference.get();
            if (bitmap!=null&&!bitmap.isRecycled()){
                bitmap.recycle();
            }
            mWeakReference=null;
        }
        super.setImageBitmap(bm);
    }
}
