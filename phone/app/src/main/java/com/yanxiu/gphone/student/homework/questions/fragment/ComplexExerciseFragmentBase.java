package com.yanxiu.gphone.student.homework.questions.fragment;

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
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.questions.adapter.QAViewPagerAdapter;
import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;
import com.yanxiu.gphone.student.homework.questions.view.QAViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫 on 2017/5/5.
 * 复合题基类
 */

public abstract class ComplexExerciseFragmentBase extends ExerciseFragmentBase {

    private QAViewPager viewPager;

    public QAViewPager getViewPager() {
        return viewPager;
    }

    private QAViewPagerAdapter mAdapter;
    private List<String> datas = new ArrayList<>();
    private boolean isFromCardSelect;

    private ImageView mImageViewSplitter;
    private View mRootView;

    private BaseQuestion mNode;

    @Override
    public void setNode(BaseQuestion node) {
        mNode = node;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_complex_base, container, false);
        initView();
        setQaNumber(mRootView);
        isFromCardSelect = false;
        return mRootView;
    }

    protected void setQaNumber(View v) {
        TextView tv = (TextView) v.findViewById(R.id.tv_qa_number);
//        String str = mNode.numberStringForShow();// TODO: 2017/5/15  等设置题号逻辑融合进孙鹏的数据里后，再添加
//        tv.setText(str);
    }

    private void initView() {
        mImageViewSplitter = (ImageView) mRootView.findViewById(R.id.iv_splitter);

        FragmentManager fm = getChildFragmentManager();

        TopFragment topFragment = (TopFragment) fm.findFragmentById(R.id.ll_top_container);
        if (topFragment == null) {
            topFragment = getTopFragment();
            fm.beginTransaction()
                    .add(R.id.ll_top_container, topFragment)
                    .commit();
        }
        viewPager = (QAViewPager) mRootView.findViewById(R.id.ll_bottom_container);
        mAdapter = new QAViewPagerAdapter(fm);
//        mAdapter.setData(mNode.mChildren);// TODO: 2017/5/15  等设置题号逻辑融合进孙鹏的数据里后，再添加
        viewPager.setAdapter(mAdapter);
        viewPager.setOnSwipeOutListener(new QAViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtEnd() {
                Log.e("TAG", "Inner swipe out at end");
            }

        });

        final LinearLayout topLayout = (LinearLayout) mRootView.findViewById(R.id.ll_top_container);

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

                        break;
                    case MotionEvent.ACTION_MOVE:
                        float top = startTop + (event.getRawY() - startY);
                        top = Math.max(0, Math.min(top, parentView.getHeight() - v.getHeight()));
                        ViewGroup.LayoutParams params = v.getLayoutParams();
                        float topHeight = top;
                        float bottomHeight = parentView.getHeight() - topHeight - v.getHeight();

                        LinearLayout.LayoutParams topParams = (LinearLayout.LayoutParams) topLayout.getLayoutParams();
                        LinearLayout.LayoutParams bottomParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                        topParams.weight = topHeight;
                        bottomParams.weight = bottomHeight;
                        topLayout.setLayoutParams(topParams);
                        viewPager.setLayoutParams(bottomParams);
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

    abstract protected TopFragment getTopFragment();

    /**
     * 递归
     *
     * @param postions
     */
    public void setChildrenPositionRecursively(ArrayList<Integer> postions) {
        int index = postions.get(0);
        if (viewPager != null) {
            viewPager.setCurrentItem(index);
        } else {
//            isFromCardSelect = true;
//            mToIndex = index;
        }
        isFromCardSelect = true;
        ArrayList<Integer> remainPositions = new ArrayList<>(postions);
        remainPositions.remove(0);
        if (remainPositions.size() > 0) { // 表明这层依然是 复合题
            FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) viewPager.getAdapter();
            ComplexExerciseFragmentBase f = (ComplexExerciseFragmentBase) a.instantiateItem(viewPager, index);
            f.setChildrenPositionRecursively(remainPositions);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && null != viewPager) {
            try {
                Log.w(TAG, "setCurrent: _______" + this.getClass().getSimpleName());
                viewPager.setCurrentItem(0);
            } catch (Exception e) {
                e.toString();
            }
        }
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser                      true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param isHappenedInSetUserVisibleHintMethod true：本次回调发生在setUserVisibleHintMethod方法里；false：发生在onResume或onPause方法里
     */
    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod) {
//        Log.e(TAG, "复合界面: " + mNode.numberStringForShow() + "_______" + this.getClass().getSimpleName() + " == " + isVisibleToUser);
    }
}
