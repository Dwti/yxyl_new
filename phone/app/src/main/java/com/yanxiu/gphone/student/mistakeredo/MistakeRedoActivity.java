package com.yanxiu.gphone.student.mistakeredo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.LoadingView;
import com.yanxiu.gphone.student.db.SpManager;
import com.yanxiu.gphone.student.exercise.request.GenQuesRequest;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.mistakeredo.adapter.QAMistakeRedoAdapter;
import com.yanxiu.gphone.student.mistakeredo.request.FinishReDoWorkRequest;
import com.yanxiu.gphone.student.mistakeredo.request.WrongQByQidsRequest;
import com.yanxiu.gphone.student.mistakeredo.response.FinishReDoWorkResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.AnswerCardFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.questions.answerframe.view.QAViewPager;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.KeyboardObserver;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-11-24.
 */

public class MistakeRedoActivity extends YanxiuBaseActivity implements View.OnClickListener, OnAnswerCardItemSelectListener {

    private static final String SUBJECTID = "subjectId";
    private static final String STAGEID = "stageId";
    private static final String WRONG_NUM = "wrongNum";
    private static final String QIDS = "qids";

    private FragmentManager mFragmentManager;
    private QAViewPager mViewPager;
    private QAMistakeRedoAdapter mAdapter;
    private String mKey;//获取数据的key
    private String mTitleString;//试卷的title-答题卡需要
    private Paper mPaper;//试卷数据
    private ArrayList<BaseQuestion> mQuestions;//题目数据
    private AnswerCardFragment mAnswerCardFragment;

    private KeyboardObserver mKeyboardObserver;
    private LinearLayout mPrevious_question, mNext_question;//上一题，下一题
    private TextView mNext_text;//下一题textview
    private ImageView mBackView;//返回按钮
    private ImageView mShowAnswerCardView;//显示答题卡
    private View mRootView, mOverlay;
    private LoadingView mLoadingView;

    private int mCurrentPos;
    private int mPageSize = 20;
    private String mStageId;
    private String mSubjectId;
    private int mWrongNum;
    private ArrayList<String> mQids;
    private ArrayList<Integer> mDeletedPos = new ArrayList<>();
    private String mQidsToRemove = "";   //待删除的题

    private boolean isOnLoadMore = false;

    private InputMethodManager mInputMethodManager;

    private boolean isCanClick = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mistake_redo);
        isCanClick = true;
        initData();
        initView();
    }

    private void initData() {
        mKey = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        if (TextUtils.isEmpty(mKey))
            finish();
        mPaper = DataFetcher.getInstance().getPaper(mKey);
        if (mPaper == null) {
            this.finish();
            return;
        }
        mQuestions = mPaper.getQuestions();
        mTitleString = mPaper.getName();

        mSubjectId = getIntent().getStringExtra(SUBJECTID);
        mStageId = getIntent().getStringExtra(STAGEID);
        mWrongNum = getIntent().getIntExtra(WRONG_NUM, 0);
        mQids = getIntent().getStringArrayListExtra(QIDS);

    }

    private void initView() {
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mRootView = findViewById(R.id.fl_qa);
        mOverlay = findViewById(R.id.overlay);
        mPrevious_question = (LinearLayout) findViewById(R.id.previous_question);
        mNext_question = (LinearLayout) findViewById(R.id.next_question);
        mNext_text = (TextView) findViewById(R.id.next_text);
        mBackView = (ImageView) findViewById(R.id.backview);
        mShowAnswerCardView = (ImageView) findViewById(R.id.answercardview);

        initViewPager();
        setListener();
    }

    private void setListener() {
        mPrevious_question.setOnClickListener(this);
        mKeyboardObserver = new KeyboardObserver(mRootView);
        mNext_question.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mShowAnswerCardView.setOnClickListener(this);

    }

    public void addKeyboardVisibleChangeListener(KeyboardObserver.KeyBoardVisibleChangeListener listener) {
        mKeyboardObserver.addKeyBoardVisibleChangeListener(listener);
    }

    public void removeKeyBoardVisibleChangeListener(KeyboardObserver.KeyBoardVisibleChangeListener listener) {
        mKeyboardObserver.removeKeyBoardVisibleChangeListener(listener);
    }

    private void initViewPager() {
        mFragmentManager = getSupportFragmentManager();
        mViewPager = (QAViewPager) findViewById(R.id.vp_viewPager);
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new QAMistakeRedoAdapter(mFragmentManager);
        mAdapter.setSubjectId(mSubjectId);
        mAdapter.setData(mQuestions,mWrongNum);
        mViewPager.setAdapter(mAdapter);
        mCurrentPos = mAdapter.getCount();

        mViewPager.setOnSwipeOutListener(new QAViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtEnd() {
                showAnswerCardFragment();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int totalNum = mAdapter.getCount();
                if (totalNum < mWrongNum) {
                    if (mViewPager.getCurrentItem() + 1 > totalNum - 3 && !isOnLoadMore) {
                        requestData();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onItemSelect(BaseQuestion item) {
        //答题卡跳转逻辑
        // 1, 移除 card getFragment
//        final FragmentManager fm = getSupportFragmentManager();
        if (mAnswerCardFragment != null) {
            mFragmentManager.beginTransaction().remove(mAnswerCardFragment).commit();
        }
        // 2, 跳转
        int index = item.getLevelPositions().get(0);
        FragmentStatePagerAdapter a1 = (FragmentStatePagerAdapter) mViewPager.getAdapter();
        mViewPager.setCurrentItem(index, false);
        ExerciseBaseFragment currentFragment = (ExerciseBaseFragment) a1.instantiateItem(mViewPager, index);
        currentFragment.setUserVisibleHin2(true);


        ArrayList<Integer> remainPositions = new ArrayList<>(item.getLevelPositions());
        remainPositions.remove(0);
        if (remainPositions.size() > 0) { // 表明这层依然是 复合题
            FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            AnswerComplexExerciseBaseFragment f = (AnswerComplexExerciseBaseFragment) a.instantiateItem(mViewPager, index);
            f.setChildrenPositionRecursively(remainPositions);
        }
        controlListenView(false);
    }

    private void deleteQuestions(String qids){
        mLoadingView.showLoadingView();
        FinishReDoWorkRequest request = new FinishReDoWorkRequest();
        request.setDeleteWqidList(qids);
        request.startRequest(FinishReDoWorkResponse.class, new EXueELianBaseCallback<FinishReDoWorkResponse>() {
            @Override
            protected void onResponse(RequestBase request, FinishReDoWorkResponse response) {
                mLoadingView.hiddenLoadingView();
                if(response.getStatus().getCode() == 0){
                    for(Integer pos : mDeletedPos){
                        mWrongNum -= 1;
                        mAdapter.deleteItem(pos, mWrongNum);
                    }
                }
                finish();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mLoadingView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
                finish();
            }
        });
    }

    /**
     * load more
     */
    private void requestData() {
        isOnLoadMore = true;
        WrongQByQidsRequest request = new WrongQByQidsRequest();
        request.setSubjectId(mSubjectId);
        request.setQids(getQids());
        request.startRequest(PaperResponse.class, new EXueELianBaseCallback<PaperResponse>() {
            @Override
            protected void onResponse(RequestBase request, PaperResponse response) {
                if (response.getStatus().getCode() == 0) {
                    String qids = ((WrongQByQidsRequest) request).getQids();
                    int count = qids.split(",").length;
                    mCurrentPos += count;
                    Paper paper = new Paper(response.getData().get(0), QuestionShowType.MISTAKE_REDO);
                    mAdapter.addData(paper.getQuestions(), mWrongNum);
                } else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
                isOnLoadMore = false;
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                isOnLoadMore = false;
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    private String getQids(){
        String qids = "";
        List<String> result;
        if(mCurrentPos == mQids.size() - 1){
            return mQids.get(mCurrentPos);
        }
        result = mQids.size() > (mCurrentPos + mPageSize ) ? mQids.subList(mCurrentPos,mCurrentPos + mPageSize) : mQids.subList(mCurrentPos,mQids.size());
        for(String qid : result){
            if(qids.length() == 0){
                qids += qid;
            }else {
                qids = qids + "," + qid;
            }
        }
        return qids;
    }

    public View getRootView() {
        return mRootView;
    }

    /**
     * 显示答题卡
     */
    private void showAnswerCardFragment() {
        mInputMethodManager.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
        // 可以在这里打个断点，所有Fill Blank的答案均已存入nodes里
        if (mAnswerCardFragment == null) {
            mAnswerCardFragment = new AnswerCardFragment();
            mAnswerCardFragment.setData(mPaper, mTitleString);
            mAnswerCardFragment.setOnCardItemSelectListener(MistakeRedoActivity.this);
        }
        if (mFragmentManager.findFragmentById(R.id.fragment_answercard) == null) {
            mFragmentManager.beginTransaction().add(R.id.fragment_answercard, mAnswerCardFragment).commit();
        }
        controlListenView(true);
    }

    /**
     * 切换下一题目
     */
    public void nextQuestion() {
        ExerciseBaseFragment currentFramgent = null;//当前的Fragment
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentFramgent = (ExerciseBaseFragment) adapter.instantiateItem(mViewPager, index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (adapter == null || index < 0 || size < 1 || mViewPager == null || currentFramgent == null)
            return;

        if (currentFramgent instanceof AnswerComplexExerciseBaseFragment) {
            AnswerComplexExerciseBaseFragment complexExerciseFragment = (AnswerComplexExerciseBaseFragment) currentFramgent;
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

        } else if (currentFramgent instanceof AnswerSimpleExerciseBaseFragment) {
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
        ExerciseBaseFragment currentFramgent = null;//当前的Fragment
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentFramgent = (ExerciseBaseFragment) adapter.instantiateItem(mViewPager, index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (adapter == null || index < 0 || size < 1 || mViewPager == null || currentFramgent == null)
            return;

        if (currentFramgent instanceof AnswerComplexExerciseBaseFragment) {
            AnswerComplexExerciseBaseFragment complexExerciseFragment = (AnswerComplexExerciseBaseFragment) currentFramgent;
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

        } else if (currentFramgent instanceof AnswerSimpleExerciseBaseFragment) {
            if (index >= 1) {
                //不是第一大题时,有上一题
                mViewPager.setCurrentItem(index - 1);
            }
        }
    }

    public View getOverlayView() {
        return mOverlay;
    }

    /**
     * 当处在第一题和最后一题时，隐藏相应切换题目按钮
     */
    public void hiddenSwitchQuestionView() {
        ExerciseBaseFragment currentFramgent = null;//当前的Fragment
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentFramgent = (ExerciseBaseFragment) adapter.instantiateItem(mViewPager, index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (adapter == null || index < 0 || size < 1 || mViewPager == null || currentFramgent == null)
            return;

        if (currentFramgent instanceof AnswerComplexExerciseBaseFragment) {
            AnswerComplexExerciseBaseFragment complexExerciseFragment = (AnswerComplexExerciseBaseFragment) currentFramgent;
            ViewPager innerViewPager = complexExerciseFragment.getmViewPager();
            FragmentStatePagerAdapter innerAdapter = (FragmentStatePagerAdapter) innerViewPager.getAdapter();
            int innerIndex = innerViewPager.getCurrentItem();
            int innerSize = innerViewPager.getAdapter().getCount();

            if (complexExerciseFragment == null || innerViewPager == null || innerAdapter == null || innerIndex < 0 || innerSize < 1)
                return;
            /**
             * 复合题型，切换下一题，共有三种状态：
             * 3.处在最后一个小题，且外部大题也是最后一题，那么判断为是最后一道题，展现答题卡
             */
            if (innerIndex == (innerSize - 1) && index == (size - 1)) { //状态3
                mNext_text.setText(R.string.complete);
            } else {
                mNext_text.setText(R.string.next_question);
            }

            if (innerIndex == 0 && index == 0) { //第一题
                mPrevious_question.setVisibility(View.GONE);
            } else {
                mPrevious_question.setVisibility(View.VISIBLE);
            }

        } else if (currentFramgent instanceof AnswerSimpleExerciseBaseFragment) {
            if (index == (size - 1)) { //最后一题
                mNext_text.setText(R.string.complete);
            } else {
                mNext_text.setText(R.string.next_question);
            }

            if (index == 0) { //第一题
                mPrevious_question.setVisibility(View.GONE);
            } else {
                mPrevious_question.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 答题卡显示或隐藏，回调给fragment，用来控制听力播放控件
     */
    public void controlListenView(boolean answerCardFragmentIsShwon) {
        ExerciseBaseFragment currentFramgent = null;//当前的Fragment
        FragmentStatePagerAdapter adapter;
        int index;//当前Fragment在外层viewPager中的index
        int size;//viewPager的总共的size
        try {
            adapter = (FragmentStatePagerAdapter) mViewPager.getAdapter();
            index = mViewPager.getCurrentItem();
            currentFramgent = (ExerciseBaseFragment) adapter.instantiateItem(mViewPager, index);
            size = adapter.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (adapter == null || index < 0 || size < 1 || mViewPager == null || currentFramgent == null)
            return;

        if (currentFramgent instanceof ExerciseBaseFragment) {
            ExerciseBaseFragment fragment = (ExerciseBaseFragment) currentFramgent;
            fragment.onAnswerCardVisibleToUser(answerCardFragmentIsShwon);
        }

    }

    public Paper getPaper() {
        return mPaper;
    }


    /**
     * 因口语题需求更改，故通过这个方法来设置当前界面上下题、返回、答题卡等按钮是否可以点击
     */
    public void setCanClick(boolean isCanClick) {
        this.isCanClick = isCanClick;
        mViewPager.setScanScroll(isCanClick);
    }

    @Override
    public void onClick(View v) {
        if (!isCanClick) {
            return;
        }
        switch (v.getId()) {
            case R.id.previous_question:
                previousQuestion();
                break;
            case R.id.next_question:
                nextQuestion();
                break;
            case R.id.backview:
                SpManager.setCompleteQuestionCount(mPaper.getId(), QuestionUtil.calculateCompleteCount(mQuestions));
                finish();
                break;
            case R.id.answercardview:
                showAnswerCardFragment();
                break;
            case R.id.tv_delete:
                if (!isOnLoadMore) {
                    //此处需要记录删除的qid 然后在退出时统一删除
                    ToastManager.showMsg(getString(R.string.question_deleted));
                    String qid = mAdapter.getQidByPosition(mViewPager.getCurrentItem());
                    if(mQidsToRemove.length() == 0){
                        mQidsToRemove += qid;
                    }else {
                        mQidsToRemove = mQidsToRemove + "," + qid;
                    }
                    mDeletedPos.add(mViewPager.getCurrentItem());
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mKeyboardObserver.destroy();
        mOverlay.clearAnimation();
        super.onDestroy();
    }


    public static void LuanchActivity(Context context, String key, String subjectId, String stageId, int wrongNum, ArrayList<String> qids) {
        Intent intent = new Intent(context, MistakeRedoActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        intent.putExtra(SUBJECTID, subjectId);
        intent.putExtra(STAGEID, stageId);
        intent.putExtra(WRONG_NUM, wrongNum);
        intent.putStringArrayListExtra(QIDS,qids);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mAnswerCardFragment != null && mAnswerCardFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(mAnswerCardFragment).commit();
            controlListenView(false);
            return;
        }
        SpManager.setCompleteQuestionCount(mPaper.getId(), QuestionUtil.calculateCompleteCount(mQuestions));
        if(!TextUtils.isEmpty(mQidsToRemove)){
            deleteQuestions(mQidsToRemove);
        }else {
            super.onBackPressed();
        }
    }

}
