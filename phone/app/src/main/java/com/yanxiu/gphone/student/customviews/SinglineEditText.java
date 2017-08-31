package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/9 11:54.
 * Function :
 */
public class SinglineEditText extends EditText {

    private int width;
    private String hint;

    public SinglineEditText(Context context) {
        super(context);
        init(context, null);
    }

    public SinglineEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SinglineEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SinglineEditText);
        hint=typedArray.getString(R.styleable.SinglineEditText_hint);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width=right-left;
        setTextHint(hint);
    }

    private void setTextHint(String hint){
        if (!TextUtils.isEmpty(hint)) {
            Paint paint = getPaint();
            int count = paint.breakText(hint, true, width, null);
            String sureText = hint;
            if (count < hint.length()) {
                sureText = hint.substring(0, count - 1) + "...";
                int measure_width = (int) paint.measureText(sureText);
                while (measure_width > width) {
                    count -= 1;
                    sureText = hint.substring(0, count - 1) + "...";
                    measure_width = (int) paint.measureText(sureText);
                }
            }
            setHint(sureText);
        }else {
            setText("");
        }
    }

}
