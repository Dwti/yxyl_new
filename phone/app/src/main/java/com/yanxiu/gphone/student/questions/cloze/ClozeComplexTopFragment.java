package com.yanxiu.gphone.student.questions.cloze;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.eventbus.SingleChooseMessage;
import com.yanxiu.gphone.student.customviews.spantextview.ClozeTextView;
import com.yanxiu.gphone.student.customviews.spantextview.ClozeView;
import com.yanxiu.gphone.student.customviews.spantextview.OnReplaceCompleteListener;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.TopBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.StemUtil;

import de.greenrobot.event.EventBus;


/**
 * Created by 戴延枫 on 2017/6/14.
 */

public class ClozeComplexTopFragment extends TopBaseFragment {
    private ClozeComplexQuestion mQuestion;
    private ClozeTextView mClozeTextView;
    private ScrollView mScrollView;
    private View mViewWrapper;
    private int mCurrentIndex = 0;
    private boolean mClicked = false;
    private int initIndex = 0;

    @Override
    public void setData(BaseQuestion data) {
        mQuestion = (ClozeComplexQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mQuestion = (ClozeComplexQuestion) savedInstanceState.getSerializable(ExerciseBaseFragment.KEY_NODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_cloze_top, container, false);
        EventBus.getDefault().register(ClozeComplexTopFragment.this);
        initView();
        initData();
        initListener();
        return mRootView;
    }

    public void onEventMainThread(SingleChooseMessage message){
        if (getClozeAnsweHashCode()==message.hascode){
            mQuestion.getFilledAnswers().set(mCurrentIndex,message.answer.trim());
            if(!TextUtils.isEmpty(message.answer.trim())){
                setCurrentItem(mCurrentIndex + 1);
            }
            //空中的答案有变化，开始重绘空
            mClozeTextView.notifyAnswerChanged();
        }
    }

    private void initListener() {

        setOnPageChangeListener();

        mClozeTextView.addOnReplaceCompleteListener(new OnReplaceCompleteListener() {
            @Override
            public void onReplaceComplete() {
                if (initIndex != 0) {
                    //主要针对在从答题卡选择某个小题跳过去时，该小题并没有预加载绘制出来的情况
                    mClozeTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            int bottom = mClozeTextView.getSelectedClozeView().getBottom() + mViewWrapper.getPaddingTop() - mScrollView.getScrollY();
                            if (bottom > mScrollView.getHeight() || bottom < mClozeTextView.getLineHeight()) {
                                mScrollView.scrollTo(0, mClozeTextView.getSelectedClozeView().getBottom() - mScrollView.getHeight() + mViewWrapper.getPaddingTop());
                            }
                        }
                    });

                }
                mClozeTextView.removeOnReplaceCompleteListener(this);
            }
        });

        mRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(mClozeTextView.getSelectedClozeView() == null)
                    return;
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        int bottom = mClozeTextView.getSelectedClozeView().getBottom() + mViewWrapper.getPaddingTop() - mScrollView.getScrollY();
                        if(bottom > mScrollView.getHeight() || bottom < mClozeTextView.getLineHeight()){
                            mScrollView.scrollTo(0, mClozeTextView.getSelectedClozeView().getBottom() - mScrollView.getHeight() + mViewWrapper.getPaddingTop());
                        }
                    }
                });
            }
        });

        mClozeTextView.setOnClozeClickListener(new ClozeTextView.OnClozeClickListener() {
            @Override
            public void onClozeClick(final ClozeView view, int position) {
                if(position != mCurrentIndex){
                    //当点击的位置跟上一次的位置相同的时候 ，并不会回调onPageSelected();
                    mClicked = true;
                }
                mCurrentIndex = position;
                showChildQuestion(false);
                setCurrentItem(position);
            }
        });

        mClozeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChildQuestion(true);
            }
        });
    }

    private void initData() {
        mClozeTextView.setBaseIndex(getFirstChildQuestionNumber());
        String text = StemUtil.initClozeStem(mQuestion.getStem());
        mClozeTextView.setText(text,mQuestion.getFilledAnswers());
    }

    private void initView() {
        mClozeTextView = (ClozeTextView) mRootView.findViewById(R.id.cloze_text_view);
        mScrollView = (ScrollView) mRootView.findViewById(R.id.scrollView);
        mViewWrapper = mRootView.findViewById(R.id.ll_wrapper);
    }

    /**
     * 是否显示子题
     *
     * @param isShow
     */
    private void showChildQuestion(boolean isShow) {
        Fragment fragment = getParentFragment();
        if (null != fragment && fragment instanceof ClozeAnswerComplexFragment) {
            ClozeAnswerComplexFragment parentFragment = (ClozeAnswerComplexFragment) fragment;
            if (isShow) {
                parentFragment.expand();
            } else {
                parentFragment.collapse();
            }
        }
    }

    /**
     * 切换子题
     * @param index
     */
    private void setCurrentItem(int index) {
        Fragment fragment = getParentFragment();
        if (null != fragment && fragment instanceof ClozeAnswerComplexFragment) {
            ClozeAnswerComplexFragment parentFragment = (ClozeAnswerComplexFragment) fragment;
            ViewPager viewPager = parentFragment.getmViewPager();
            if(viewPager != null){
                viewPager.setCurrentItem(index);
            }
        }
    }

    /**
     * viewpager滑动回调
     */
    private void setOnPageChangeListener() {
        Fragment fragment = getParentFragment();
        if (null != fragment && fragment instanceof ClozeAnswerComplexFragment) {
            ClozeAnswerComplexFragment parentFragment = (ClozeAnswerComplexFragment) fragment;
            ViewPager viewPager = parentFragment.getmViewPager();
            if (viewPager != null) {
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (!mClozeTextView.isReplaceCompleted()) {
                            mClozeTextView.setSelectedPosition(position);
                            initIndex = position;
                            mCurrentIndex = position;
                            mClicked = false;
                            return;
                        }
                        if (mClicked) {
                            mClicked = false;
                        } else {
                            mCurrentIndex = position;
                            mClozeTextView.resetSelected();
                            mClozeTextView.setSelected(position);
                        }
                        int bottom = mClozeTextView.getSelectedClozeView().getBottom() + mViewWrapper.getPaddingTop() - mScrollView.getScrollY();
                        if(bottom > mScrollView.getHeight() || bottom < mClozeTextView.getLineHeight()){
                            mScrollView.scrollTo(0, mClozeTextView.getSelectedClozeView().getBottom() - mScrollView.getHeight() + mViewWrapper.getPaddingTop());
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }
    }

    /**
     * EventBus需要的hashcode
     * @return
     */
    private int getClozeAnsweHashCode(){
        Fragment parentFragment = getParentFragment();
        if(null != parentFragment && parentFragment instanceof ClozeAnswerComplexFragment){
            return ((ClozeAnswerComplexFragment) parentFragment).mHashCode;
        }
        return -1;
    }

    /**
     * 获取第一个子题的题号
     * @return
     */
    private int getFirstChildQuestionNumber(){
        int questionNumber = 0;
        try{
            BaseQuestion firstChild = mQuestion.getChildren().get(0);
            questionNumber = Integer.parseInt(firstChild.getAnswerCardSimpleNumber());
        }catch (Exception e){
            e.printStackTrace();
        }
        return questionNumber;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
