package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/16 10:23.
 * Function :
 */
public class NoteGrideView extends GridView {
    public NoteGrideView(Context context) {
        super(context);
    }

    public NoteGrideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteGrideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h!=w/4) {
            getLayoutParams().height = w / 4;
            getLayoutParams().width = w;
            setLayoutParams(getLayoutParams());
        }
    }
}
