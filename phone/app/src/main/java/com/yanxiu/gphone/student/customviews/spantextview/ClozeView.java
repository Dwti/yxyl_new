package com.yanxiu.gphone.student.customviews.spantextview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;


/**
 * Created by sunpeng on 2017/6/15.
 */

public class ClozeView extends FrameLayout {

    private TextView mNumber;

    private TextView mAnswer;

    private View mContent;

    private TextPosition mPosition;

    public ClozeView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ClozeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ClozeView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClozeView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.cloze_view,this,true);
        mNumber = (TextView) view.findViewById(R.id.text_number);
        mAnswer = (TextView) view.findViewById(R.id.text_answer);
        mContent = view.findViewById(R.id.content);
        mPosition = TextPosition.CENTER;
    }

    public void performTranslateAnimation(TextPosition toPosition){
        if(!TextUtils.isEmpty(mAnswer.getText()))
            return;
        if(mPosition == toPosition)
            return;
        TranslateAnimation translateAnimation;
        if(toPosition == TextPosition.LEFT){
            translateAnimation = new TranslateAnimation(0,-mNumber.getX(),0,0);
            mPosition = TextPosition.LEFT;
        }else {
            translateAnimation = new TranslateAnimation(-mNumber.getX(),0,0,0);
            mPosition = TextPosition.CENTER;
        }
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new DecelerateInterpolator(1.5f));
        translateAnimation.setFillAfter(true);
        mNumber.startAnimation(translateAnimation);
    }

    public TextPosition getTextPosition(){
        return mPosition;
    }

    public void setTextPosition(TextPosition pos){
        mPosition = pos;
    }
    public void setTextNumber(int num){
        mNumber.setText(String.valueOf(num));
    }

    public int getTextNumber(){
        return Integer.parseInt(mNumber.getText().toString());
    }

    public void setAnswer(String text){
        mAnswer.setText(text);
    }

    public void setContentCenter(boolean b){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContent.getLayoutParams();
        if(b){
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }else {
            layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL);
        }
        mContent.setLayoutParams(layoutParams);
    }

    public String getAnswer(){
        return mAnswer.getText().toString();
    }

    public enum TextPosition{
        LEFT,CENTER
    }
}
