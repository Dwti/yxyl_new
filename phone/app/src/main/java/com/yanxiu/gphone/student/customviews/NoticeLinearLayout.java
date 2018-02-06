package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.util.ScreenUtils;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/17 14:03.
 * Function :
 */
public class NoticeLinearLayout extends LinearLayout {

    private int Max_Width;

    public NoticeLinearLayout(Context context) {
        super(context);
        Max_Width= (int) ScreenUtils.dpToPx(context,315);
    }

    public NoticeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Max_Width= (int) ScreenUtils.dpToPx(context,315);
    }

    public NoticeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Max_Width= (int) ScreenUtils.dpToPx(context,315);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w>Max_Width) {
            getLayoutParams().width=Max_Width;
            getLayoutParams().height=h;
            setLayoutParams(getLayoutParams());
        }
    }
}
