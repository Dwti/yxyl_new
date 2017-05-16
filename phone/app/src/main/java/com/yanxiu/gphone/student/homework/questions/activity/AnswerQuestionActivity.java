package com.yanxiu.gphone.student.homework.questions.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.questions.adapter.QAViewPagerAdapter;
import com.yanxiu.gphone.student.homework.questions.fragment.AnswerCardFragment;
import com.yanxiu.gphone.student.homework.questions.fragment.ComplexExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.ExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;
import com.yanxiu.gphone.student.homework.questions.view.QAViewPager;

import java.util.ArrayList;

/**
 * 答题页面
 */
public class AnswerQuestionActivity extends AppCompatActivity implements AnswerCardFragment.OnCardItemSelectListener {
    private static final String EXTRA_NODES = "extra mNodes";

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
        final FragmentManager fm = getSupportFragmentManager();
        mViewPager = (QAViewPager) findViewById(R.id.vp_viewPager);
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new QAViewPagerAdapter(fm);
//        mViewPager.setFragmentManager(fm);
        mAdapter.setData(mNodes);
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnSwipeOutListener(new QAViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtEnd() {
                Log.e("TAG", "Outter swipe out at end");
                // 可以在这里打个断点，所有Fill Blank的答案均已存入nodes里
                if (mCardFragment == null) {
                    mCardFragment = new AnswerCardFragment();
                    mCardFragment.nodes = allNodesThatHasNumber();
                    mCardFragment.setOnCardItemSelectListener(AnswerQuestionActivity.this);
                }
                if (fm.findFragmentById(R.id.fragment_card) == null) {
                    // 有一个滑动event的问题，导致card fragment上能滑动下面的viewpager，可能导致重复加载card getFragment
                    fm.beginTransaction().add(R.id.fragment_card, mCardFragment).commit();
                }
            }
        });
    }

    /**
     * 给答题卡设置题号
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
}
