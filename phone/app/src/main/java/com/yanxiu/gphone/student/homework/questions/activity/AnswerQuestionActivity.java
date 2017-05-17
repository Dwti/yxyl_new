package com.yanxiu.gphone.student.homework.questions.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.questions.adapter.QAViewPagerAdapter;
import com.yanxiu.gphone.student.homework.questions.fragment.AnswerCardFragment;
import com.yanxiu.gphone.student.homework.questions.fragment.ComplexExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.ExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.SimpleExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;
import com.yanxiu.gphone.student.homework.questions.view.QAViewPager;

import java.util.ArrayList;

/**
 * 答题页面
 */
public class AnswerQuestionActivity extends AppCompatActivity implements AnswerCardFragment.OnCardItemSelectListener {
    private static final String EXTRA_NODES = "extra mNodes";

    private FragmentManager mFragmentManager;
    private QAViewPager mViewPager;
    private QAViewPagerAdapter mAdapter;
    private ArrayList<BaseQuestion> mNodes;
    private AnswerCardFragment mCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerquestion);
        mNodes = (ArrayList<BaseQuestion>) getIntent().getSerializableExtra(EXTRA_NODES);
        if (mNodes == null) { // 表明是第一级界面
            mNodes = new ArrayList<>();
        }
//        mNodes = DataClass.getPaper().mChildren;//Todo 获取数据
        initView();
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        mViewPager = (QAViewPager) findViewById(R.id.vp_viewPager);
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new QAViewPagerAdapter(mFragmentManager);
//        mViewPager.setFragmentManager(fm);
        mAdapter.setData(mNodes);
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnSwipeOutListener(new QAViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtEnd() {
                Log.e("TAG", "Outter swipe out at end");
                showAnswerCardFragment();
            }
        });
    }

    /**
     * 给答题卡设置题号
     *
     * @return
     */
    private ArrayList<BaseQuestion> allNodesThatHasNumber() {
        ArrayList<BaseQuestion> retNodes = new ArrayList<>();
        for (BaseQuestion node : mNodes) {
//            retNodes.addAll(node.allNodesThatHasNumber()); // TODO: 2017/5/15  等设置题号逻辑融合进孙鹏的数据里后，再添加
        }
        return retNodes;
    }

    @Override
    public void onItemSelect(BaseQuestion item) {
        // TODO: 2017/5/15  等设置题号逻辑融合进孙鹏的数据里后，再添加
//        //答题卡跳转逻辑
//        // 1, 移除 card getFragment
//        final FragmentManager fm = getSupportFragmentManager();
//        if (mCardFragment != null) {
//            fm.beginTransaction().remove(mCardFragment).commit();
//        }
//
//        // 2, 跳转
//        int index = item.levelPositions.get(0);// TODO: 2017/5/15  等设置题号逻辑融合进孙鹏的数据里后，再添加
//        FragmentStatePagerAdapter a1 = (FragmentStatePagerAdapter) mViewPager.getAdapter();
//        final ExerciseFragmentBase f2 = (ExerciseFragmentBase) a1.getItem(index);
//        mViewPager.setCurrentItem(index);
//        f2.setUserVisibleHin2(true);
//
//
//        ArrayList<Integer> remainPositions = new ArrayList<>(item.levelPositions);
//        remainPositions.remove(0);
//        if (remainPositions.size() > 0) { // 表明这层依然是 复合题
//            FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) mViewPager.getAdapter();
//            ComplexExerciseFragmentBase f = (ComplexExerciseFragmentBase) a.instantiateItem(mViewPager, index);
//            f.setChildrenPositionRecursively(remainPositions);
//        }
    }

    /**
     * 显示答题卡
     */
    private void showAnswerCardFragment() {
        // 可以在这里打个断点，所有Fill Blank的答案均已存入nodes里
        if (mCardFragment == null) {
            mCardFragment = new AnswerCardFragment();
            mCardFragment.nodes = allNodesThatHasNumber();
            mCardFragment.setOnCardItemSelectListener(AnswerQuestionActivity.this);
        }
        if (mFragmentManager.findFragmentById(R.id.fragment_card) == null) {
            // 有一个滑动event的问题，导致card fragment上能滑动下面的viewpager，可能导致重复加载card getFragment
            mFragmentManager.beginTransaction().add(R.id.fragment_card, mCardFragment).commit();
        }
    }

    /**
     * 切换下一题目
     */
    public void nextQuestion() {
        ExerciseFragmentBase currentFramgent = null;//当前的Fragment
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentFramgent = (ExerciseFragmentBase) adapter.instantiateItem(mViewPager, index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (adapter == null || index < 0 || size < 1 || mViewPager == null || currentFramgent == null)
            return;

        if (currentFramgent instanceof ComplexExerciseFragmentBase) {
            ComplexExerciseFragmentBase complexExerciseFragment = (ComplexExerciseFragmentBase) currentFramgent;
            ViewPager innerViewPager = complexExerciseFragment.getViewPager();
            FragmentStatePagerAdapter innerAdapter = (FragmentStatePagerAdapter) innerViewPager.getAdapter();
            int innerIndex = innerViewPager.getCurrentItem();
            int innerSize = innerViewPager.getAdapter().getCount();

            if (complexExerciseFragment == null || innerViewPager == null || innerAdapter == null || innerIndex < 0 || innerSize < 1)
                return;
            /**
             * 复合题型，切换下一题，共有三种状态：
             * 1.正常切换下一小题
             * 2.处在最后一个小题时，且外部大题不是最后一道题，那么外部大题进入下一题
             * 3.处在最后一个小题，且外部大题也是最后一题，那么判断为是最后一道题，展现答题卡
             */
            if (innerIndex == (innerSize - 1) && index == (size - 1)) {
                //状态3
                showAnswerCardFragment();
            } else if (innerIndex == (innerSize - 1)) {
                //状态2
                mViewPager.setCurrentItem(index + 1);
            } else {
                //状态1
                innerViewPager.setCurrentItem(innerIndex + 1);
            }

        } else if (currentFramgent instanceof SimpleExerciseFragmentBase) {
            if (index == (size - 1)) {
                //最后一题,展示答题卡
                showAnswerCardFragment();
            } else {
                //下一题
                mViewPager.setCurrentItem(index + 1);
            }
        }

    }

    /**
     * 切换上一题目
     */
    public void previousQuestion() {
        ExerciseFragmentBase currentFramgent = null;//当前的Fragment
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentFramgent = (ExerciseFragmentBase) adapter.instantiateItem(mViewPager, index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (adapter == null || index < 0 || size < 1 || mViewPager == null || currentFramgent == null)
            return;

        if (currentFramgent instanceof ComplexExerciseFragmentBase) {
            ComplexExerciseFragmentBase complexExerciseFragment = (ComplexExerciseFragmentBase) currentFramgent;
            ViewPager innerViewPager = complexExerciseFragment.getViewPager();
            FragmentStatePagerAdapter innerAdapter = (FragmentStatePagerAdapter) innerViewPager.getAdapter();
            int innerIndex = innerViewPager.getCurrentItem();
            int innerSize = innerViewPager.getAdapter().getCount();

            if (complexExerciseFragment == null || innerViewPager == null || innerAdapter == null || innerIndex < 0 || innerSize < 1)
                return;

            if (innerIndex >= 1) {
                //小题不是第一题时,有上一小题
                innerViewPager.setCurrentItem(innerIndex - 1);
            } else if (index >= 1) {
                //外部大题不是第一题时,有上一大题
                mViewPager.setCurrentItem(index - 1);
            }

        } else if (currentFramgent instanceof SimpleExerciseFragmentBase) {
            if (index >= 1) {
                //不是第一大题时,有上一题
                mViewPager.setCurrentItem(index - 1);
            }
        }
    }
}
