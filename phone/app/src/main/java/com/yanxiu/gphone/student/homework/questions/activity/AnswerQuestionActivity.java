package com.yanxiu.gphone.student.homework.questions.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.QuestionProgressView;
import com.yanxiu.gphone.student.customviews.QuestionTimeTextView;
import com.yanxiu.gphone.student.homework.questions.adapter.QAViewPagerAdapter;
import com.yanxiu.gphone.student.homework.questions.fragment.AnswerCardFragment;
import com.yanxiu.gphone.student.homework.questions.fragment.ComplexExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.ExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.fragment.SimpleExerciseFragmentBase;
import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;
import com.yanxiu.gphone.student.homework.questions.model.Paper;
import com.yanxiu.gphone.student.homework.questions.view.QAViewPager;
import com.yanxiu.gphone.student.util.DataFetcher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 答题页面
 */
public class AnswerQuestionActivity extends YanxiuBaseActivity implements View.OnClickListener, AnswerCardFragment.OnCardItemSelectListener {
    private FragmentManager mFragmentManager;
    private QAViewPager mViewPager;
    private QAViewPagerAdapter mAdapter;
    private String mKey;//获取数据的key
    private Paper mPaper;//试卷数据
    private ArrayList<BaseQuestion> mQuestions;//题目数据
    private AnswerCardFragment mCardFragment;

    private QuestionTimeTextView mTimer;//计时
    private QuestionProgressView mProgressView;
    private LinearLayout mPrevious_question, mNext_question;//上一题，下一题

    private Handler mHandler;
    private int mTotalTime;//总计时间
    /**
     * 刷新计时
     */
    private static final int HANDLER_TIME = 0x100;
    /**
     * 一秒
     */
    private final int HANDLER_TIME_DELAYED = 1000;
    private int mTotalQuestion;//题目总数量

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerquestion);
        initData();
        initView();
    }

    private void initData() {
        mKey = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        if (TextUtils.isEmpty(mKey))
            finish();
        mPaper = DataFetcher.getInstance().getPaper(mKey);
        mQuestions = mPaper.getQuestions();
        initProgressViewData();
        mTotalTime = 0;
    }

    private void initView() {
        mTimer = (QuestionTimeTextView) findViewById(R.id.timer);
        mProgressView = (QuestionProgressView) findViewById(R.id.progressBar);
        mProgressView.setMaxCount(mTotalQuestion);
        mPrevious_question = (LinearLayout) findViewById(R.id.previous_question);
        mNext_question = (LinearLayout) findViewById(R.id.next_question);
        setListener();
        initViewPager();
        mHandler = new TimingHandler(this);
    }

    private void setListener() {
        mPrevious_question.setOnClickListener(this);
        mNext_question.setOnClickListener(this);
    }

    private void initViewPager() {
        mFragmentManager = getSupportFragmentManager();
        mViewPager = (QAViewPager) findViewById(R.id.vp_viewPager);
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new QAViewPagerAdapter(mFragmentManager);
//        mViewPager.setFragmentManager(fm);
        Paper.generateUsedNumbersForNodes(mQuestions);
        mAdapter.setData(mQuestions);
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
     * 初始化进度条相关数据
     */
    private void initProgressViewData() {
        for (int i = 0; i < mQuestions.size(); i++) { //遍历大题
            ArrayList<BaseQuestion> childrenList = mQuestions.get(i).getChildren();//小题集合
            if (childrenList == null || childrenList.size() < 1) { //单题型
                mTotalQuestion++;
            }else{ //复合题
                if(childrenList == null || childrenList.size() < 1){
                    //出错，尽然是复合题，必须有小题
                    return;
                }else{ //遍历小题
                    for(int j = 0;j < childrenList.size(); j++){
                        mTotalQuestion++;
                    }
                }
            }
        }
    }

    /**
     * 给答题卡设置题号
     *
     * @return
     */
    private ArrayList<BaseQuestion> allNodesThatHasNumber() {
        ArrayList<BaseQuestion> retNodes = new ArrayList<>();
        for (BaseQuestion node : mQuestions) {
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
     * 第一版本不显示答题卡
     */
    private void showAnswerCardFragment() {
        // 可以在这里打个断点，所有Fill Blank的答案均已存入nodes里
//        if (mCardFragment == null) {
//            mCardFragment = new AnswerCardFragment();
//            mCardFragment.nodes = allNodesThatHasNumber();
//            mCardFragment.setOnCardItemSelectListener(AnswerQuestionActivity.this);
//        }
//        if (mFragmentManager.findFragmentById(R.id.fragment_card) == null) {
//            // 有一个滑动event的问题，导致card fragment上能滑动下面的viewpager，可能导致重复加载card getFragment
//            mFragmentManager.beginTransaction().add(R.id.fragment_card, mCardFragment).commit();
//        }
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
            ViewPager innerViewPager = complexExerciseFragment.getmViewPager();
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
            ViewPager innerViewPager = complexExerciseFragment.getmViewPager();
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

    public Paper getPaper() {
        return mPaper;
    }

    public QuestionProgressView getProgressView() {
        return mProgressView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.previous_question:
                previousQuestion();
                break;
            case R.id.next_question:
                nextQuestion();
                break;
        }
    }

    /**
     * 计时用Handler
     */
    private static class TimingHandler extends Handler {
        private WeakReference<AnswerQuestionActivity> mActivity;

        public TimingHandler(AnswerQuestionActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AnswerQuestionActivity activity = mActivity.get();

            if (msg.what == activity.HANDLER_TIME) {
                activity.mHandler.sendEmptyMessageDelayed(activity.HANDLER_TIME, activity.HANDLER_TIME_DELAYED);
                activity.updateTime();
            }
        }
    }

    /**
     * 开始计时
     */
    private void startTiming() {
        if (mHandler != null) {
            mHandler.removeMessages(HANDLER_TIME);
            mHandler.sendEmptyMessageDelayed(HANDLER_TIME, HANDLER_TIME_DELAYED);
        }
    }

    /**
     * 结束计时
     */
    private void endTiming() {
        if (mHandler != null) {
            mHandler.removeMessages(HANDLER_TIME);
        }
    }

    /**
     * 更新计时
     */
    private void updateTime() {
        mTotalTime++;
        //Todo 显示时间
        mTimer.setTime(mTotalTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTiming();
    }

    @Override
    protected void onPause() {
        super.onPause();
        endTiming();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();

    }

    /**
     * 跳转AnswerQuestionActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String key) {
        Intent intent = new Intent(activity, AnswerQuestionActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        activity.startActivity(intent);
    }
}
