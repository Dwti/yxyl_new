package com.yanxiu.gphone.student.questions.connect;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by sunpeng on 2017/7/14.
 */

public class ConnectedRecyclerView extends RecyclerView {
    public ConnectedRecyclerView(Context context) {
        super(context);
    }

    public ConnectedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ConnectedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

    }
}
