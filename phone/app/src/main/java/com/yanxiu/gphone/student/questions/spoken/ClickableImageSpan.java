package com.yanxiu.gphone.student.questions.spoken;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.view.View;

import com.yanxiu.gphone.student.customviews.SpokenAnimDrawable;

import static com.yanxiu.gphone.student.customviews.SpokenAnimDrawable.TYPE_ANIM;
import static com.yanxiu.gphone.student.customviews.SpokenAnimDrawable.TYPE_DEFAULT;
import static com.yanxiu.gphone.student.customviews.SpokenAnimDrawable.TYPE_TOUCH;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/19 15:50.
 * Function :
 */
public class ClickableImageSpan extends ImageSpan {

    public interface onSpanClickListener {
        void onClick(String url);
    }

    private String mUrl;
    private onSpanClickListener mSpanClickListener;

    private int mType = TYPE_DEFAULT;

    ClickableImageSpan(Drawable d, String url, onSpanClickListener spanClickListener) {
        super(d);
        this.mUrl = url;
        this.mSpanClickListener = spanClickListener;
    }

    public void start() {
        mType = TYPE_ANIM;
        setTypeToDrawable(mType);
        setAnimStart();
    }

    void stop() {
        mType = TYPE_DEFAULT;
        setTypeToDrawable(mType);
        setAnimStop();
    }

    public void onClick(View view) {
        if (mSpanClickListener != null) {
            mSpanClickListener.onClick(mUrl);
        }
    }

    void down() {
        mType = TYPE_TOUCH;
        setTypeToDrawable(mType);
    }

    public void up() {
        mType = TYPE_DEFAULT;
        setTypeToDrawable(mType);
    }

    private void setAnimStart(){
        SpokenAnimDrawable drawable= (SpokenAnimDrawable) getDrawable();
        drawable.startAnim();
    }

    private void setAnimStop(){
        SpokenAnimDrawable drawable= (SpokenAnimDrawable) getDrawable();
        drawable.stopAnim();
    }

    private void setTypeToDrawable(int type){
        SpokenAnimDrawable drawable= (SpokenAnimDrawable) getDrawable();
        drawable.setType(type);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(true);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint=paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight=rect.bottom-rect.top;
            int top= drHeight/2 - fontHeight/4;
            int bottom=drHeight/2 + fontHeight/4;

            fm.ascent=-bottom;
            fm.top=-bottom;
            fm.bottom=top;
            fm.descent=top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = ((bottom-top) - b.getBounds().bottom)/2+top;
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
