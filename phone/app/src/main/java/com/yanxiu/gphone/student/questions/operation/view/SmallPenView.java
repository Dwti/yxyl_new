package com.yanxiu.gphone.student.questions.operation.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;

/**
 * Created by sunpeng on 2018/1/13.
 */

public class SmallPenView extends FrameLayout{
    private TrapezoidalView mTrapezoidalView;
    private ImageView iv_pen;
    public SmallPenView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SmallPenView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SmallPenView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SmallPenView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        View root = LayoutInflater.from(context).inflate(R.layout.smallpen,this,true);
        mTrapezoidalView = (TrapezoidalView) root.findViewById(R.id.trapezoidalView);
        iv_pen = (ImageView) root.findViewById(R.id.iv_pen);
    }

    public void setColor(int color){
        mTrapezoidalView.setColor(color);
    }

    public void setUseStatus(boolean inUse){
        if(inUse){
            iv_pen.setImageResource(R.drawable.smallpen_use);
        }else {
            iv_pen.setImageResource(R.drawable.smallpen_unuse);
        }
    }
}
