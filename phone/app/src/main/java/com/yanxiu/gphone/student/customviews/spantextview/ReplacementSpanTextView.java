package com.yanxiu.gphone.student.customviews.spantextview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.cloze.ClozeTagHandler;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by sunpeng on 2017/6/15.
 */

public abstract class ReplacementSpanTextView<T extends View> extends FrameLayout implements XTextView.OnDrawFinishedListener {
    protected XTextView mTextView;
    protected RelativeLayout mOverLayViewContainer;
    protected Context mContext;
    protected Spanned mSpannedStr;
    protected EmptyReplacementSpan[] mSpans;
    protected String mText;
    protected List<String> mAnswers;
    protected List<T> replaceViews;
    protected TreeMap<EmptyReplacementSpan, T> mTreeMap;
    protected TreeSet<EmptyReplacementSpan> mSpanSet;
    protected boolean mIsReplaceCompleted = false;
    private List<OnReplaceCompleteListener> mOnReplaceCompleteListeners = new ArrayList<>();
    protected int MIN_WIDTH;
    protected int mExtraSpace;
    protected int mSpacing;
    protected int mExtraLineSpacing;

    public ReplacementSpanTextView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ReplacementSpanTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ReplacementSpanTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReplacementSpanTextView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context) {
        mSpacing = ScreenUtils.dpToPxInt(context, 5);
        mExtraSpace = ScreenUtils.dpToPxInt(context,17);
        mContext = context;
        replaceViews = new ArrayList<>();
        mSpanSet = new TreeSet<>();
        mTreeMap = new TreeMap<>();
        View view = LayoutInflater.from(context).inflate(R.layout.replaceable_text_view, this, true);
        mTextView = (XTextView) view.findViewById(R.id.textView);
        MIN_WIDTH = (int) StringUtil.computeStringWidth("oooooo",mTextView.getPaint());
        mOverLayViewContainer = (RelativeLayout) view.findViewById(R.id.relativeLayout);
//        mExtraLineSpacing = mContext.getResources().getDimensionPixelSize(R.dimen.question_line_height);
//        mTextView.setLineSpacing(mExtraLineSpacing,1);
//        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        mTextView.setTextColor(getResources().getColor(R.color.color_333333));
        mTextView.setOnDrawFinishedListener(this);
    }

    public void setText(String text) {
        setText(text, null);
    }

    public void setText(String text, List<String> answers) {
        mText = text;
        mAnswers = answers;
        initText();
    }

    private void initText() {
        post(new Runnable() {
            @Override
            public void run() {
                mIsReplaceCompleted = false;
                mSpannedStr = Html.fromHtml(mText, getImageGetter(), getTagHandler());
                mSpans = mSpannedStr.getSpans(0, mSpannedStr.length(), EmptyReplacementSpan.class);
                sortSpans();
                setWidthAndText();
            }
        });
    }

    public void setWidthAndText(){
        mIsReplaceCompleted = false;
        setSpanWidthAndHeight(mAnswers);
        mTextView.setText(mSpannedStr, TextView.BufferType.SPANNABLE);
    }

    private void sortSpans() {
        for (EmptyReplacementSpan span : mSpans) {
            int start = mSpannedStr.getSpanStart(span);
            span.setSpanStart(start);
            mSpanSet.add(span);
        }
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
                    width = (int) (StringUtil.computeStringWidth(answer, mTextView.getPaint()) + mSpacing + mExtraSpace);
                }
                span.width = width;
                span.height = mTextView.getLineHeight();
                span.standardLineHeight = mTextView.getLineHeight();
                span.answer = answer;
                i++;
            }
        }
    }

    protected void replaceSpanWithViews(final Spanned spanned) {
        if (spanned == null || mSpans.length == 0) {
            return;
        }

        for (final EmptyReplacementSpan span : mSpans) {

            int start = spanned.getSpanStart(span);
            Layout layout = mTextView.getLayout();

            span.setSpanStart(start);

            int lineStart = layout.getLineForOffset(start); //span的起始行

            int topPadding = mTextView.getCompoundPaddingTop();
            float startLeftMargin = layout.getPrimaryHorizontal(start); //span的起始位置的左边距

            int descent = layout.getLineDescent(lineStart);
            int base = layout.getLineBaseline(lineStart);
            int spanTop = base + descent - span.height;
            int topMargin = spanTop + topPadding;

            int width = span.width;
            int height = span.height;

            T view = mTreeMap.get(span);
            if (view == null) {
                view = getView();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                params.leftMargin = (int) startLeftMargin;
                params.topMargin = topMargin;
                mOverLayViewContainer.addView(view, params);
                mTreeMap.put(span, view);
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                params.width = span.width;
                params.height = span.height;
                params.leftMargin = (int) startLeftMargin;
                params.topMargin = topMargin;
                view.setLayoutParams(params);
            }

        }
        if (mOnReplaceCompleteListeners .size() > 0) {
            for(OnReplaceCompleteListener listener : mOnReplaceCompleteListeners){

                listener.onReplaceComplete();
            }
        }
        mIsReplaceCompleted = true;

    }

    public boolean isReplaceCompleted(){
        return mIsReplaceCompleted;
    }

    public int getLineHeight(){
        return mTextView.getLineHeight();
    }

    public void notifyAnswerChanged() {
        setWidthAndText();
    }

    public void addOnReplaceCompleteListener(OnReplaceCompleteListener onReplaceCompleteListener) {
        mOnReplaceCompleteListeners.add(onReplaceCompleteListener);
    }

    public void removeOnReplaceCompleteListener(OnReplaceCompleteListener listener){
        mOnReplaceCompleteListeners.remove(listener);
    }

    public TreeMap<EmptyReplacementSpan, T> getReplacements() {
        return mTreeMap;
    }

    public T getReplaceView(int index) {
        if(replaceViews.size() == 0){
            replaceViews = getReplaceViews();
        }
        return replaceViews.get(index);

    }

    public List<T> getReplaceViews() {
        List<T> viewList = new ArrayList<>();
        for (Map.Entry<EmptyReplacementSpan, T> entry : mTreeMap.entrySet()) {
            viewList.add(entry.getValue());
        }
        return viewList;
    }

    protected abstract T getView();

    protected Html.ImageGetter getImageGetter() {
        return new HtmlImageGetter(mTextView);
    }

    protected Html.TagHandler getTagHandler() {
        return new ClozeTagHandler();
    }

    @Override
    public void onDrawFinished() {
        replaceSpanWithViews(mSpannedStr);
    }

}
