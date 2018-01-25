package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yanxiu.gphone.student.util.ScreenUtils;

/**
 * Created by Administrator on 2016/7/1.
 */
public class RoundedCornersImageView extends ImageView {

    private Matrix mMatrix;
    private Paint mBitmapPaint;
    private BitmapShader mBitmapShader;
    private RectF mRoundRect;
    private float mBorderRadius;
    private int borderRadius=4;

    public RoundedCornersImageView(Context context) {
        super(context);
        init(context);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBorderRadius = ScreenUtils.dpToPx(context, borderRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setUpShader();
        canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                mBitmapPaint);
    }

    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Bitmap bmp = drawableToBitamp(drawable);
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (!(bmp.getWidth() == getWidth() && bmp.getHeight() == getHeight())) {
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(),
                    getHeight() * 1.0f / bmp.getHeight());
        }
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRoundRect = new RectF(0, 0, w, h);
    }
}
