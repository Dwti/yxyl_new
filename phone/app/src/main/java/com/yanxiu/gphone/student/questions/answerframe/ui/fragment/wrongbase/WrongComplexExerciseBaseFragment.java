package com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.MyMaxHeightLinearLayout;
import com.yanxiu.gphone.student.questions.answerframe.adapter.QAViewPagerAdapter;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.TopBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.view.QAViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/18 15:27.
 * Function :
 */
public abstract class WrongComplexExerciseBaseFragment extends WrongExercisbaseFragment {

    protected QAViewPager mViewPager;

    public QAViewPager getmViewPager() {
        return mViewPager;
    }

    protected QAViewPagerAdapter mAdapter;
    protected List<String> datas = new ArrayList<>();
    protected boolean isFromCardSelect;

    protected MyMaxHeightLinearLayout mTopLayout;
    protected ImageView mImageViewSplitter;
    protected View mRootView;
    protected View mSplitter_line;

//    private BaseQuestion mBaseQuestion;

    protected int mMinHight;//topfragment的最小高度
    protected int mBottom_min_distance;//滑动image距离底边最小距离
    protected int mTitleBarHight;//题型题号layout高度
    protected int mLineHight;//滑块下面的view高度
    protected int mLineHight_chooseLine;//完形填空里，单选子题的line高度

//    @Override
//    public void setData(BaseQuestion node) {
//        mBaseQuestion = node;
//    }

    @Override
    public boolean setViewPagerMove(String moveType) {
        int index = mViewPager.getCurrentItem();
        if (TYPE_VIEWPAGER_LAST.equals(moveType)) {
            if (index > 0) {
                mViewPager.setCurrentItem(index - 1);
                return true;
            }
        } else if (TYPE_VIEWPAGER_NEXT.equals(moveType)) {
            if (index < mAdapter.getCount() - 1) {
                mViewPager.setCurrentItem(index + 1);
                return true;
            }
        }
        return super.setViewPagerMove(moveType);
    }

    @Override
    public boolean getIsFirst() {
        int index = mViewPager.getCurrentItem();
        return index == 0;
    }

    @Override
    public boolean getIsEnd() {
        int index = mViewPager.getCurrentItem();
        return index == mAdapter.getCount() - 1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_complex_base, container, false);
        initView();
        setQaNumber(mRootView);
        setQaName(mRootView);
        isFromCardSelect = false;
        return mRootView;
    }

    private void initView() {
        mImageViewSplitter = (ImageView) mRootView.findViewById(R.id.iv_splitter);
        mSplitter_line = mRootView.findViewById(R.id.splitter_line);

        mTopLayout = (MyMaxHeightLinearLayout) mRootView.findViewById(R.id.top_container);
        mMinHight = (int) (getResources().getDimensionPixelSize(R.dimen.question_ll_top_container_minheight));
        mBottom_min_distance = (int) getResources().getDimensionPixelSize(R.dimen.question_bottom_layout_height);
        mTitleBarHight = (int) getResources().getDimensionPixelSize(R.dimen.question_commonnumber_height);
        mLineHight = (int) getResources().getDimensionPixelSize(R.dimen.question_splitter_bottomview_height);
        mLineHight_chooseLine = (int) getResources().getDimensionPixelSize(R.dimen.question_bottom_shadow);

        FragmentManager fm = getChildFragmentManager();

        TopBaseFragment topFragment = (TopBaseFragment) fm.findFragmentById(R.id.top_container);
        if (topFragment == null) {
            topFragment = getTopFragment();
            fm.beginTransaction()
                    .add(R.id.top_container, topFragment)
                    .commit();
        }
        mViewPager = (QAViewPager) mRootView.findViewById(R.id.ll_bottom_container);
        mAdapter = new QAViewPagerAdapter(fm);
        mAdapter.setData(mBaseQuestion.getChildren());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnSwipeOutListener(new QAViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtEnd() {
                Log.e("TAG", "Inner swipe out at end");
            }

        });

//        mTopLayout = (MyMaxHeightLinearLayout) mRootView.findViewById(R.id.top_container);
//        mMinHight = (int)(getResources().getDimensionPixelSize(R.dimen.question_ll_top_container_minheight) + getResources().getDimensionPixelSize(R.dimen.question_commonnumber_height));
//        mMinHight = (int)(getResources().getDimensionPixelSize(R.dimen.question_ll_top_container_minheight));
//        mBottom_min_distance = (int)getResources().getDimensionPixelSize(R.dimen.question_bottom_layout_height);
//        mTitleBarHight = (int)getResources().getDimensionPixelSize(R.dimen.question_commonnumber_height);
//        mLineHight = (int)getResources().getDimensionPixelSize(R.dimen.question_splitter_bottomview_height);
        mImageViewSplitter.setOnTouchListener(new View.OnTouchListener() {
            private float startY;
            private float startTop;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout parentView = (LinearLayout) v.getParent();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTop = v.getTop();
                        startY = event.getRawY();
                        mTopLayout.setCanChangeHeight();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float top = startTop + (event.getRawY() - startY) - mTitleBarHight;
                        top = Math.max(mMinHight, Math.min(top, parentView.getHeight() - v.getHeight() - mBottom_min_distance - mTitleBarHight - mLineHight));
                        ViewGroup.LayoutParams params = v.getLayoutParams();
                        int topHeight = (int) top;
                        int bottomHeight = parentView.getHeight() - topHeight - v.getHeight();

                        LinearLayout.LayoutParams topParams = (LinearLayout.LayoutParams) mTopLayout.getLayoutParams();
                        LinearLayout.LayoutParams bottomParams = (LinearLayout.LayoutParams) mViewPager.getLayoutParams();

                        topParams.height = topHeight;
//                        bottomParams.height = bottomHeight;
                        mTopLayout.setLayoutParams(topParams);
//                        mViewPager.setLayoutParams(bottomParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });
    }

    abstract protected TopBaseFragment getTopFragment();

    /**
     * 递归
     *
     * @param postions
     */
    public void setChildrenPositionRecursively(ArrayList<Integer> postions) {
        int index = postions.get(0);
        if (mViewPager != null) {
            mViewPager.setCurrentItem(index);
        } else {
//            isFromCardSelect = true;
//            mToIndex = index;
        }
        isFromCardSelect = true;
        ArrayList<Integer> remainPositions = new ArrayList<>(postions);
        remainPositions.remove(0);
        if (remainPositions.size() > 0) { // 表明这层依然是 复合题
            FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            AnalysisComplexExerciseBaseFragment f = (AnalysisComplexExerciseBaseFragment) a.instantiateItem(mViewPager, index);
            f.setChildrenPositionRecursively(remainPositions);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && null != mViewPager) {
            try {
                Log.w(TAG, "setCurrent: _______" + this.getClass().getSimpleName());
                mViewPager.setCurrentItem(0);
            } catch (Exception e) {
                e.toString();
            }
        }
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser       true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param invokeInResumeOrPause true：发生在onResume或onPause方法里；false：本次回调发生在setUserVisibleHintMethod方法里
     */
    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser, invokeInResumeOrPause);
    }

}
