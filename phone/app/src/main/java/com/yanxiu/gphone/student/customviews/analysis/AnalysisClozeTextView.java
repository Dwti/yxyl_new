package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yanxiu.gphone.student.customviews.spantextview.ClozeTextView;
import com.yanxiu.gphone.student.customviews.spantextview.ClozeView;
import com.yanxiu.gphone.student.customviews.spantextview.EmptyReplacementSpan;
import com.yanxiu.gphone.student.customviews.spantextview.OnReplaceCompleteListener;
import com.yanxiu.gphone.student.customviews.spantextview.ReplacementSpanTextView;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by sunpeng on 2017/7/5.
 */

public class AnalysisClozeTextView extends ReplacementSpanTextView<ClozeView> implements OnReplaceCompleteListener {

    protected ClozeTextView.OnClozeClickListener mOnClozeClickListener;

    protected ClozeView mSelectedClozeView;

    protected boolean mClozeClickable = true;

    private List<String> mCorrectAnswers;

    private int mSelectedPosition = 0;

    public AnalysisClozeTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public AnalysisClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnalysisClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnalysisClozeTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        addOnReplaceCompleteListener(this);
    }

    public List<String> getCorrectAnswers() {
        return mCorrectAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.mCorrectAnswers = correctAnswers;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int pos) {
        this.mSelectedPosition = pos;
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

                if (mSelectedClozeView == cv) {
                    return;
                }
                mSelectedPosition = cv.getTextNumber() - 1;
                mSelectedClozeView = cv;
                setWidthAndText();
                if (mOnClozeClickListener != null) {
                    mOnClozeClickListener.onClozeClick(cv, mSelectedPosition);
                }
            }
        });
        return clozeView;
    }


    protected void setSpanWidthAndHeight(List<String> answers) {

        if (answers == null || answers.size() == 0) {
            for (EmptyReplacementSpan emptyReplacementSpan : mSpans) {
                emptyReplacementSpan.width = MIN_WIDTH;
                emptyReplacementSpan.height = mTextView.getLineHeight();
                emptyReplacementSpan.standardLineHeight = mTextView.getLineHeight();
            }
        } else {
            String answer;
            int width;
            int i = 0;
            for (EmptyReplacementSpan span : mSpanSet) {
                answer = i < answers.size() ? answers.get(i) : null;
                if (TextUtils.isEmpty(answer)) {
                    width = MIN_WIDTH;
                } else {
                    if (i == mSelectedPosition) {
                        width = (int) StringUtil.computeStringWidth(answer, mTextView.getPaint()) + mSpacing * 3 + mExtraSpace;
                    } else {
                        width = (int) StringUtil.computeStringWidth(answer, mTextView.getPaint()) + mSpacing + mExtraSpace;
                    }
                }
                span.width = width;
                span.height = mTextView.getLineHeight();
                span.standardLineHeight = mTextView.getLineHeight();
                span.answer = answer;
                i++;
            }
        }
    }

    @Override
    public void onReplaceComplete() {
        int i = 0;
        for (Map.Entry<EmptyReplacementSpan, ClozeView> entry : mTreeMap.entrySet()) {
            ClozeView view = entry.getValue();
            if (i == mSelectedPosition && !TextUtils.isEmpty(entry.getKey().answer)) {
                view.setPadding(ScreenUtils.dpToPxInt(getContext(), 5), 0, ScreenUtils.dpToPxInt(getContext(), 5), 0);
            } else {
                view.setPadding(0, 0, 0, 0);
            }
            if(i == mSelectedPosition){
                mSelectedClozeView = view;
            }
            if (!TextUtils.isEmpty(entry.getKey().answer)) {
                view.clearNumberAnimation();
            }
            view.setTextNumber(i + 1);
            view.setFilledAnswer(entry.getKey().answer);
            view.setCorrectAnswer(mCorrectAnswers.get(i));
            if(i == mSelectedPosition){
                view.setChecked(true);
            }else {
                view.setChecked(false);
            }
            i++;
        }
    }

    public ClozeView getSelectedClozeView() {
        return mSelectedClozeView;
    }

    public void setSelectedClozeView(ClozeView clozeView){
        mSelectedClozeView = clozeView;
    }

    public void setOnClozeClickListener(ClozeTextView.OnClozeClickListener listener) {
        mOnClozeClickListener = listener;
    }

    public void resetSelected() {
        if (mSelectedClozeView != null) {
            mSelectedClozeView.setChecked(false);
        }
    }

    public void setSelected(int index){
        ClozeView cv = getReplaceView(index);
        cv.setChecked(true);
    }
}
