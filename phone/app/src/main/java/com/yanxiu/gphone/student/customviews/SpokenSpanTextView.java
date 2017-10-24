package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yanxiu.gphone.student.questions.spoken.ClickableImageSpan;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/23 16:51.
 * Function :
 */
public class SpokenSpanTextView extends TextView {

    private Spanned mSpanned;

    public SpokenSpanTextView(Context context) {
        super(context);
    }

    public SpokenSpanTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpokenSpanTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(Spanned spanned){
        this.mSpanned=spanned;
        setText(spanned);
    }

    public void setClick(){
        ClickableImageSpan[] link = mSpanned.getSpans(0, mSpanned.length(), ClickableImageSpan.class);
        if (link.length>0){
            link[0].onClick(this);
        }
    }
}
