package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.ScreenUtils;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/9 11:54.
 * Function :
 */
public class SinglineEditText extends EditText {

    private int width;
    private String hint;
    private int hintTextSize=0;

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
        hintTextSize=typedArray.getDimensionPixelSize(R.styleable.SinglineEditText_hintTestSize, (int) getTextSize());
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
            float testSize=paint.getTextSize();
            paint.setTextSize(hintTextSize);
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
            SpannableString ss = new SpannableString(sureText);
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintTextSize,false);
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setHint(new SpannedString(ss));
            paint.setTextSize(testSize);
//            setHint(sureText);
        }else {
            setHint("");
        }
    }

}
