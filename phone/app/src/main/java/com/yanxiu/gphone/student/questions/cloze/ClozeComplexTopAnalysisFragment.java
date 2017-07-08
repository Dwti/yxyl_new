package com.yanxiu.gphone.student.questions.cloze;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.eventbus.SingleChooseMessage;
import com.yanxiu.gphone.student.customviews.analysis.AnalysisClozeTextView;
import com.yanxiu.gphone.student.customviews.spantextview.ClozeTextView;
import com.yanxiu.gphone.student.customviews.spantextview.ClozeView;
import com.yanxiu.gphone.student.customviews.spantextview.OnReplaceCompleteListener;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.TopBaseFragment;
import com.yanxiu.gphone.student.util.StemUtil;

import de.greenrobot.event.EventBus;


/**
 * Created by 戴延枫 on 2017/6/14.
 */

public class ClozeComplexTopAnalysisFragment extends TopBaseFragment {
    private ClozeComplexQuestion mQuestion;
    private AnalysisClozeTextView mClozeTextView;
    private ScrollView mScrollView;
    private View mViewWrapper;
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
        mRootView = inflater.inflate(R.layout.fragment_cloze_top_analysis, container, false);
        initView();
        initData();
        initListener();
        return mRootView;
    }


    private void initListener() {

        setOnPageChangeListener();

        mClozeTextView.setOnClozeClickListener(new ClozeTextView.OnClozeClickListener() {
            @Override
            public void onClozeClick(final ClozeView view, int position) {
                mClicked = true;
                setCurrentItem(position);
            }
        });

        mClozeTextView.addOnReplaceCompleteListener(new OnReplaceCompleteListener() {
            @Override
            public void onReplaceComplete() {
                if (initIndex != 0) {

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

    }

    private void initData() {
        String text = StemUtil.initClozeStem(mQuestion.getStem());
        mClozeTextView.setSelectedPosition(0);
        mClozeTextView.setCorrectAnswers(mQuestion.getCorrectAnswers());
        mClozeTextView.setText(text, mQuestion.getFilledAnswers());
    }

    private void initView() {
        mClozeTextView = (AnalysisClozeTextView) mRootView.findViewById(R.id.cloze_text_view);
        mScrollView = (ScrollView) mRootView.findViewById(R.id.scrollView);
        mViewWrapper = mRootView.findViewById(R.id.ll_wrapper);
    }

    /**
     * 切换子题
     *
     * @param index
     */
    private void setCurrentItem(int index) {
        Fragment fragment = getParentFragment();
        if (null != fragment && fragment instanceof ClozeAnalysisComplexFragment) {
            ClozeAnalysisComplexFragment parentFragment = (ClozeAnalysisComplexFragment) fragment;
            ViewPager viewPager = parentFragment.getmViewPager();
            if (viewPager != null) {
                viewPager.setCurrentItem(index);
            }
        }
    }

    /**
     * viewpager滑动回调
     */
    private void setOnPageChangeListener() {
        Fragment fragment = getParentFragment();
        if (null != fragment && fragment instanceof ClozeAnalysisComplexFragment) {
            ClozeAnalysisComplexFragment parentFragment = (ClozeAnalysisComplexFragment) fragment;
            ViewPager viewPager = parentFragment.getmViewPager();
            if (viewPager != null) {
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        //此处针对点击之后没有加载完与第一次从答题卡跳转过来没加载完时的逻辑
                        if (!mClozeTextView.isReplaceCompleted()) {
                            initIndex = position;
                            mClozeTextView.setSelectedPosition(position);
                            mClicked = false;
                            return;
                        }
                        if (mClicked) {
                            mClicked = false;
                        } else {
//                            mClozeTextView.resetSelected();
//                            mClozeTextView.setSelected(position);
                            mClozeTextView.setSelectedPosition(position);
//                            mClozeTextView.setSelectedClozeView(mClozeTextView.getReplaceView(position));
                            mClozeTextView.setWidthAndText();
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

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }
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
