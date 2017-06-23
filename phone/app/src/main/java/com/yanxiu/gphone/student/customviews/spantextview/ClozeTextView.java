package com.yanxiu.gphone.student.customviews.spantextview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by sunpeng on 2017/6/15.
 */

public class ClozeTextView extends ReplacementSpanTextView<ClozeView> implements OnReplaceCompleteListener {

    private OnClozeClickListener mOnClozeClickListener;

    private ClozeView mLastClickClozeView ;

    public ClozeTextView(@NonNull Context context) {
        super(context);
    }

    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setOnReplaceCompleteListener(this);
    }
    @Override
    protected ClozeView getView() {
        ClozeView clozeView = new ClozeView(getContext());
        clozeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClozeView cv = (ClozeView) v;
                if(mLastClickClozeView == cv){
                    return;
                }
                if(cv.getTextPosition() == ClozeView.TextPosition.CENTER){
                    cv.performTranslateAnimation(ClozeView.TextPosition.LEFT);
                }
                if(mLastClickClozeView != null){
                    mLastClickClozeView.performTranslateAnimation(ClozeView.TextPosition.CENTER);
                }
                mLastClickClozeView = cv;
                if(mOnClozeClickListener != null){
                    mOnClozeClickListener.onClozeClick(cv,cv.getTextNumber() - 1);
                }
            }
        });
        return clozeView;
    }

    public void resetAnimation(){
        if(mLastClickClozeView != null && mLastClickClozeView.getTextPosition() == ClozeView.TextPosition.LEFT){
            mLastClickClozeView.performTranslateAnimation(ClozeView.TextPosition.CENTER);
            mLastClickClozeView = null;
        }
    }

    public void setOnClozeClickListener(OnClozeClickListener listener){
        mOnClozeClickListener = listener;
    }

    @Override
    public void onReplaceComplete() {
        ClozeView clozeView = null ;
        int i = 1;
        for(Map.Entry<EmptyReplacementSpan,ClozeView> entry:mTreeMap.entrySet()){
            if(i == 1){
                clozeView = entry.getValue();
            }
            entry.getValue().setTextNumber(i);
            i++;
        }
        mLastClickClozeView = clozeView;
        post(new Runnable() {
            @Override
            public void run() {
                mLastClickClozeView.performTranslateAnimation(ClozeView.TextPosition.LEFT);
            }
        });
    }

    public ClozeView getLastClickClozeView(){
        return mLastClickClozeView;
    }
    public interface OnClozeClickListener{
        void onClozeClick(ClozeView view, int position);
    }
}
