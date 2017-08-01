package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by sp on 17-8-1.
 */

public class ChapterSwitchBar extends FrameLayout {

    private Context mContext;
    private TextView mOff,mOn,mSelected;
    private boolean mIsOff = true;  //默认开关在左边处于关闭状态
    private OnCheckedChangedListener mOnCheckedChangedListener;

    public ChapterSwitchBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ChapterSwitchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChapterSwitchBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChapterSwitchBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_switch_bar,this,true);
        mOn = (TextView) view.findViewById(R.id.text_on);
        mOff = (TextView) view.findViewById(R.id.text_off);
        mSelected = (TextView) view.findViewById(R.id.text_selected);

        mOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performAnimation(true);
            }
        });

        mOn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performAnimation(false);
            }
        });
    }

    private void performAnimation(boolean isOff){
        TranslateAnimation animation = null;
        if(!isOff && mIsOff){
            //从左边滑动到右边
            animation = new TranslateAnimation(mSelected.getLeft(),mSelected.getRight(),0,0);
            mSelected.setText(mOn.getText());
        }else if (isOff && !mIsOff){
            //从右边滑动到左边
            animation = new TranslateAnimation(mSelected.getRight(),mSelected.getLeft(),0,0);
            mSelected.setText(mOff.getText());
        }
        if(animation != null){
            mIsOff = isOff;
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator(1.0f));
            animation.setFillAfter(true);
            mSelected.startAnimation(animation);
            if(mOnCheckedChangedListener != null){
                mOnCheckedChangedListener.onCheckedChanged(mIsOff);
            }
        }
    }

    public void setOnCheckedChangedListener(OnCheckedChangedListener listener){
        mOnCheckedChangedListener = listener;
    }

    public interface OnCheckedChangedListener{
        void onCheckedChanged(boolean isOff);
    }
}
