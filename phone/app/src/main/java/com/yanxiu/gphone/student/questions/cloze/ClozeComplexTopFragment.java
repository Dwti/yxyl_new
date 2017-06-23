package com.yanxiu.gphone.student.questions.cloze;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.spantextview.ClozeTextView;
import com.yanxiu.gphone.student.customviews.spantextview.ClozeView;
import com.yanxiu.gphone.student.customviews.spantextview.EmptyReplacementSpan;
import com.yanxiu.gphone.student.customviews.spantextview.OnReplaceCompleteListener;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.TopBaseFragment;
import com.yanxiu.gphone.student.util.StemUtil;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 戴延枫 on 2017/6/14.
 */

public class ClozeComplexTopFragment extends TopBaseFragment {
    private ClozeComplexQuestion mQuestion;
    private ClozeTextView mText;
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

//        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if(mText.getLastClickClozeView() == null)
//                    return;
//                mScrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(mText.getLastClickClozeView().getBottom() > mScrollView.getHeight()){
//                            mScrollView.scrollTo(0,mText.getLastClickClozeView().getBottom() - mScrollView.getHeight() + mViewWrapper.getPaddingTop());
//                        }
//                    }
//                });
//            }
//        });

        mText.setOnClozeClickListener(new ClozeTextView.OnClozeClickListener() {
            @Override
            public void onClozeClick(final ClozeView view, int position) {
                showChildQuestion(false);
                setCurrentItem(position);
            }
        });

        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChildQuestion(true);
                mText.resetAnimation();
            }
        });
    }

    private void initData() {
        String text = StemUtil.initClozeStem(mQuestion.getStem());
        mText.setText(text);
    }

    private void initView() {
        mText = (ClozeTextView) mRootView.findViewById(R.id.cloze_text_view);
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
    private void onPageChange() {
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
                        //TODO @sunpeng  在这里会有viewpager选中page回调
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
