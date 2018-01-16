package com.yanxiu.gphone.student.questions.fillblank;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.spantextview.BlankView;
import com.yanxiu.gphone.student.customviews.spantextview.FillBlankTextView;
import com.yanxiu.gphone.student.customviews.spantextview.OnReplaceCompleteListener;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.KeyboardObserver;
import com.yanxiu.gphone.student.util.StemUtil;

import java.util.List;

/**
 * Created by sunpeng on 2017/6/21.
 */

public class FillBlankFragment extends AnswerSimpleExerciseBaseFragment implements KeyboardObserver.KeyBoardVisibleChangeListener {

    private FillBlankQuestion mQuestion;

    private FillBlankTextView mFillBlank;
    private String mStem;
    private Button mSend;
    private View mRootView,mActivityRootView, mEditLayout, mBottom;
    private ScrollView mScrollView;
    private EditText mEditText;
    private View mViewWrapper,mComplexStemLayout;

    private KeyboardObserver mKeyboardObserver;

    private boolean mIsKeyboardShowing = false;

    private List<String> mAnswers;

    private InputMethodManager imm ;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mQuestion = (FillBlankQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setData((FillBlankQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser && mIsKeyboardShowing){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_fillblank,container,false);
        mComplexStemLayout = mRootView.findViewById(R.id.complex_stem_layout);
        mFillBlank = (FillBlankTextView) mRootView.findViewById(R.id.tv_fill_blank);
        mEditLayout = mRootView.findViewById(R.id.ll_edit);
        mSend = (Button) mRootView.findViewById(R.id.btnSend);
        mScrollView = (ScrollView) mRootView.findViewById(R.id.scrollView);
        mViewWrapper = mRootView.findViewById(R.id.viewWrapper);
        mActivityRootView = ((AnswerQuestionActivity)getActivity()).getRootView();
        mBottom = mActivityRootView.findViewById(R.id.bottom);
        mEditText = (EditText) mRootView.findViewById(R.id.editText);
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setQaNumber(mRootView);
        setQaName(mRootView);
        initComplexStem(mRootView,mQuestion);
        initData();
        initListener();
        setStem(mStem);

        return mRootView;
    }

    private void initData() {
        mStem = mQuestion.getStem();
        mAnswers = mQuestion.getStringAnswers();
    }

    private void setStem(String text){
        if(mQuestion.getShowType() == QuestionShowType.ANALYSIS){
            mFillBlank.setBlankEditable(false);
        }
        if (!TextUtils.isEmpty(text)) {
            String stem = StemUtil.initFillBlankStem(text,mAnswers);
            mFillBlank.setText(stem);
        }else {
            mFillBlank.setVisibility(View.GONE);
        }
    }

    private void initListener() {

        ((AnswerQuestionActivity)getActivity()).addKeyboardVisibleChangeListener(this);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0 && !mSend.isEnabled()){
                    mSend.setEnabled(true);
                }else if(s.length() == 0 && mSend.isEnabled()){
                    mSend.setEnabled(false);
                }
            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            boolean isOnClick = false;
            @Override
            public void onClick(View v) {
                if(isOnClick)
                    return;
                isOnClick = true;
                //找出当前输入的span的位置
                int currPos = mFillBlank.getCurrentEditBlankPosition();
                if(currPos < 0 ){
                    isOnClick = false;
                    return;
                }

                String answer = mEditText.getText().toString().trim();
                answer = answer.replaceAll("\\u00A0","");
                //更新答案的内容
//                mAnswers.set(currPos,mEditText.getText().toString().trim());
                mAnswers.set(currPos,answer);
                //重新初始化题干
                String stem = StemUtil.initFillBlankStem(mStem,mAnswers);
                //重绘
                mFillBlank.setText(stem);
                //收起键盘
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
                //保存答案
                saveAnswer(mQuestion);
                updateProgress();
                isOnClick = false;
            }
        });


        mFillBlank.setOnBlankClickListener(new FillBlankTextView.OnBlankClickListener() {
            @Override
            public void onBlankClick(BlankView view, String filledContent, final int spanStart) {
                //当键盘为弹起状态时，也就是mFillBlank.getLastClickSpanStart()!=-1时，需要清除掉 上一个点击的空的选中状态
                if(mIsKeyboardShowing && spanStart != mFillBlank.getLastClickSpanStart()){
                    mFillBlank.setBlankTransparent(mFillBlank.getLastClickSpanStart(),true);
                    mFillBlank.setBlankTransparent(spanStart,false);
                }
                if(!mIsKeyboardShowing){
                    mBottom.setVisibility(View.GONE);
                    mEditLayout.setVisibility(View.VISIBLE);
                    mEditText.requestFocus();
                    imm.showSoftInput(mEditText,InputMethodManager.SHOW_FORCED);
                }
                filledContent = filledContent.replaceAll("\\u00A0","");
                mEditText.setText(filledContent);
                mEditText.setSelection(filledContent.length());
            }
        });

        mFillBlank.setOnReplaceCompleteListener(new OnReplaceCompleteListener() {
            @Override
            public void onReplaceComplete() {
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mFillBlank.getCurrClickSpanStart() >= 0){
                            //获取最后一个view 并滑动到它的底部
                            List<BlankView> viewList = mFillBlank.getBlankViews(mFillBlank.getCurrClickSpanStart());
                            if(viewList.size() > 0){
                                final BlankView blankView = viewList.get(viewList.size() -1);
                                if(blankView.getBottom() - mScrollView.getScrollY() + mViewWrapper.getPaddingTop() + mComplexStemLayout.getHeight()> mScrollView.getHeight()){
                                    mScrollView.scrollTo(0,blankView.getBottom() - mScrollView.getHeight() + mViewWrapper.getPaddingTop()+ mComplexStemLayout.getHeight());
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onKeyboardVisibleChange(boolean isShow, int keyboardHeight) {
        Log.i("state", "onKeyboardVisibleChange() called with: " + "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
        mIsKeyboardShowing = isShow;
        if(isShow){
            setTopLayoutMinHeight();
        }else {
            mBottom.setVisibility(View.VISIBLE);
            mEditLayout.setVisibility(View.GONE);
            mFillBlank.setLastClickSpanStart(FillBlankTextView.NONE);
        }
    }

    @Override
    public void saveAnswer(BaseQuestion question) {
        boolean hasAnswer = true;
        if(mAnswers.size() == 0)
            hasAnswer = false;
        for(String str: mAnswers){
            if(TextUtils.isEmpty(str)){
                hasAnswer = false;
                break;
            }
        }
        mQuestion.setHasAnswered(hasAnswer);
        super.saveAnswer(question);
    }

    /**
     * top设置为最小高度
     */
    private void setTopLayoutMinHeight() {
        Fragment fragment = getParentFragment();
        if (null != fragment && fragment instanceof AnswerComplexExerciseBaseFragment) {
            AnswerComplexExerciseBaseFragment parentFragment = (AnswerComplexExerciseBaseFragment) fragment;
            parentFragment.setTopLayoutMinHeight();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AnswerQuestionActivity)getActivity()).removeKeyBoardVisibleChangeListener(this);
    }
}
