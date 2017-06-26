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
import com.yanxiu.gphone.student.customviews.spantextview.ClozeTextView;
import com.yanxiu.gphone.student.customviews.spantextview.ClozeView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.TopBaseFragment;
import com.yanxiu.gphone.student.util.StemUtil;

/**
 * Created by 戴延枫 on 2017/6/14.
 */

public class ClozeComplexTopFragment extends TopBaseFragment {
    private ClozeComplexQuestion mQuestion;
    private ClozeTextView mClozeTextView;
    private ScrollView mScrollView;
    private View mViewWrapper;

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
        initView();
        initData();
        initListener();
        return mRootView;
    }

    private void initListener() {

        setOnPageChangeListener();

        mRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(mClozeTextView.getSelectedClozeView() == null)
                    return;
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mClozeTextView.getSelectedClozeView().getBottom() + mViewWrapper.getPaddingTop()> mScrollView.getHeight()){
                            mScrollView.scrollTo(0, mClozeTextView.getSelectedClozeView().getBottom() - mScrollView.getHeight() + mViewWrapper.getPaddingTop());
                        }
                    }
                });
            }
        });

        mClozeTextView.setOnClozeClickListener(new ClozeTextView.OnClozeClickListener() {
            @Override
            public void onClozeClick(final ClozeView view, int position) {
                showChildQuestion(false);
                setCurrentItem(position);
            }
        });

        mClozeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChildQuestion(true);
                mClozeTextView.resetSelected();
            }
        });
    }

    private void initData() {
        String text = StemUtil.initClozeStem(mQuestion.getStem());
        mClozeTextView.setText(text);
    }

    private void initView() {
        mClozeTextView = (ClozeTextView) mRootView.findViewById(R.id.cloze_text_view);
        mScrollView = (ScrollView) mRootView.findViewById(R.id.scrollView);
        mViewWrapper = mRootView.findViewById(R.id.ll_wrapper);
    }

    /**
     * 是否显示子题
     * @param isShow
     */
    private void showChildQuestion(boolean isShow) {
        Fragment fragment = getParentFragment();
        if (null != fragment && fragment instanceof ClozeComplexFragment) {
            ClozeComplexFragment parentFragment = (ClozeComplexFragment) fragment;
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
        if (null != fragment && fragment instanceof ClozeComplexFragment) {
            ClozeComplexFragment parentFragment = (ClozeComplexFragment) fragment;
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
        if (null != fragment && fragment instanceof ClozeComplexFragment) {
            ClozeComplexFragment parentFragment = (ClozeComplexFragment) fragment;
            ViewPager viewPager = parentFragment.getmViewPager();
            if (viewPager != null) {
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        mClozeTextView.resetSelected();
                        mClozeTextView.performTranslateAnimation(ClozeView.TextPosition.LEFT,position);
//                        if(mClozeTextView.getSelectedClozeView().getBottom() + mViewWrapper.getPaddingTop() > mScrollView.getHeight()){
//                        }
                        mScrollView.scrollTo(0, mClozeTextView.getSelectedClozeView().getBottom() - mScrollView.getHeight() + mViewWrapper.getPaddingTop());
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

}
