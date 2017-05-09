package com.yanxiu.gphone.student.login.util;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/9 12:06.
 * Function :
 */

public class ContentViewLoadingLayout extends FrameLayout{

    private Context mContext;

    public ContentViewLoadingLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ContentViewLoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContentViewLoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext=context;
    }
}
