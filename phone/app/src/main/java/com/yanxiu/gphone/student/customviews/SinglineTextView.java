package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/9 11:54.
 * Function :
 */
public class SinglineTextView extends TextView {

    private int width;

    public SinglineTextView(Context context) {
        super(context);
    }

    public SinglineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SinglineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
    }

    public void setData(String text){
        Paint paint=getPaint();
        int count=paint.breakText(text,true,width,null);
        String sureText=text;
        if (count>0&&count<text.length()) {
            sureText = text.substring(0, count - 1) + "...";
            int measure_width = (int) paint.measureText(sureText);
            while (measure_width > width) {
                count -= 1;
                sureText = text.substring(0, count - 1) + "...";
                measure_width = (int) paint.measureText(sureText);
            }
        }
        setText(sureText);
    }
}
