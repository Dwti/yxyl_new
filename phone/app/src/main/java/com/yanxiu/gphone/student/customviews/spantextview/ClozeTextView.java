package com.yanxiu.gphone.student.customviews.spantextview;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

/**
 * Created by sunpeng on 2017/6/15.
 */

public class ClozeTextView extends ReplacementSpanTextView<ClozeView> implements OnReplaceCompleteListener {

    protected OnClozeClickListener mOnClozeClickListener;

    protected ClozeView mSelectedClozeView;

    protected boolean mClozeClickable = true;

    private int mSelectedPosition = 0;

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

    private void init() {
        addOnReplaceCompleteListener(this);
    }

    public boolean isClozeClickable() {
        return mClozeClickable;
    }

    public void setClozeClickable(boolean clickable) {
        this.mClozeClickable = clickable;
    }

    @Override
    protected ClozeView getView() {
        ClozeView clozeView = new ClozeView(getContext());
        clozeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mClozeClickable)
                    return;
                ClozeView cv = (ClozeView) v;
                if(cv != mSelectedClozeView){
                    resetSelected();
                    setSelected(cv.getTextNumber() - 1);
                }
                if (mOnClozeClickListener != null) {
                    mOnClozeClickListener.onClozeClick(cv, cv.getTextNumber() - 1);
                }
            }
        });
        return clozeView;
    }

    public void resetSelected() {
        if (mSelectedClozeView != null) {
            if(!mSelectedClozeView.hasFilled()  && mSelectedClozeView.getTextPosition() == ClozeView.TextPosition.LEFT ){
                mSelectedClozeView.performTranslateAnimation(ClozeView.TextPosition.CENTER);
            }
            mSelectedClozeView.setAnswerTextColor(Color.parseColor("#89e00d"));
            mSelectedClozeView = null;
        }
    }

    public void setSelected(int index){
        ClozeView cv = getReplaceView(index);
        mSelectedPosition = index;
        if (!cv.hasFilled() && cv.getTextPosition() == ClozeView.TextPosition.CENTER) {
            cv.performTranslateAnimation(ClozeView.TextPosition.LEFT);
        }
        cv.setAnswerTextColor(Color.parseColor("#333333"));
        mSelectedClozeView = cv;
    }

    public void setOnClozeClickListener(OnClozeClickListener listener) {
        mOnClozeClickListener = listener;
    }

    @Override
    public void onReplaceComplete() {
        int i = 0;
        for (Map.Entry<EmptyReplacementSpan, ClozeView> entry : mTreeMap.entrySet()) {
            ClozeView view = entry.getValue();
            if (!TextUtils.isEmpty(entry.getKey().answer)) {
                view.clearNumberAnimation();
            }
            if(i == mSelectedPosition){
                mSelectedClozeView = view;
            }
            view.setTextNumber(i + 1);
            view.setFilledAnswer(entry.getKey().answer);
            i++;
        }
        post(new Runnable() {
            @Override
            public void run() {
                if (mSelectedClozeView != null){
                    mSelectedClozeView.performTranslateAnimation(ClozeView.TextPosition.LEFT);
                    mSelectedClozeView.setAnswerTextColor(Color.parseColor("#333333"));
                }
            }
        });
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int pos) {
        this.mSelectedPosition = pos;
    }


    public ClozeView getSelectedClozeView() {
        return mSelectedClozeView;
    }

    public void setSelectedClozeView(ClozeView clozeView){
        mSelectedClozeView = clozeView;
    }

    public interface OnClozeClickListener {
        void onClozeClick(ClozeView view, int position);
    }
}
