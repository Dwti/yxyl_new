package com.yanxiu.gphone.student.mistakeredo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.AnswerCardSubmitDialog;
import com.yanxiu.gphone.student.customviews.QuestionProgressView;
import com.yanxiu.gphone.student.customviews.QuestionTimeTextView;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.db.SpManager;
import com.yanxiu.gphone.student.exercise.request.GenQuesRequest;
import com.yanxiu.gphone.student.mistakeredo.adapter.QAMistakeRedoAdapter;
import com.yanxiu.gphone.student.questions.answerframe.adapter.QAViewPagerAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.http.request.SubmitQuesitonTask;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.listener.SubmitAnswerCallback;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.AnswerCardFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.questions.answerframe.view.QAViewPager;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.KeyboardObserver;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.videoplay.NetworkStateService;
import com.yanxiu.gphone.student.videoplay.PlayerView;
import com.yanxiu.gphone.student.videoplay.ScreenOrientationSwitcher;
import com.yanxiu.gphone.student.videoplay.VideoManager;
import com.yanxiu.gphone.student.videoplay.VideoModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.LastVideoFinished;
import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.Loading;
import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.Normal;

/**
 * Created by sp on 17-11-24.
 */

public class MistakeRedoActivity extends YanxiuBaseActivity implements View.OnClickListener, OnAnswerCardItemSelectListener
        {
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
    private View mRootView,mOverlay;

    private InputMethodManager mInputMethodManager;

    private boolean isCanClick=true;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerquestion);
        isCanClick=true;
        initData();
        initView();
    }

    private void initData() {
        mKey = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        if (TextUtils.isEmpty(mKey))
            finish();
        mPaper = DataFetcher.getInstance().getPaper(mKey);
        if (mPaper==null){
            this.finish();
            return;
        }
        mQuestions = mPaper.getQuestions();
        mTitleString = mPaper.getName();


    }

    private void initView() {
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

    public void addKeyboardVisibleChangeListener(KeyboardObserver.KeyBoardVisibleChangeListener listener){
        mKeyboardObserver.addKeyBoardVisibleChangeListener(listener);
    }

    public void removeKeyBoardVisibleChangeListener(KeyboardObserver.KeyBoardVisibleChangeListener listener){
        mKeyboardObserver.removeKeyBoardVisibleChangeListener(listener);
    }
    private void initViewPager() {
        mFragmentManager = getSupportFragmentManager();
        mViewPager = (QAViewPager) findViewById(R.id.vp_viewPager);
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new QAMistakeRedoAdapter(mFragmentManager);
        Paper.generateUsedNumbersForNodes(mQuestions);
        mAdapter.setData(mQuestions);
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnSwipeOutListener(new QAViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtEnd() {
                showAnswerCardFragment();
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
        mViewPager.setCurrentItem(index,false);
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

    public View getRootView(){
        return mRootView;
    }
    /**
     * 显示答题卡
     */
    private void showAnswerCardFragment() {
        mInputMethodManager.hideSoftInputFromWindow(mRootView.getWindowToken(),0);
        // 可以在这里打个断点，所有Fill Blank的答案均已存入nodes里
        if (mAnswerCardFragment == null) {
            mAnswerCardFragment = new AnswerCardFragment();
            mAnswerCardFragment.setData(mPaper,mTitleString);
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

    public View getOverlayView(){
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
            }else{
                mNext_text.setText(R.string.next_question);
            }

            if (innerIndex == 0 && index == 0) { //第一题
                mPrevious_question.setVisibility(View.GONE);
            }else{
                mPrevious_question.setVisibility(View.VISIBLE);
            }

        } else if (currentFramgent instanceof AnswerSimpleExerciseBaseFragment) {
            if (index == (size - 1)) { //最后一题
                mNext_text.setText(R.string.complete);
            }else{
                mNext_text.setText(R.string.next_question);
            }

            if (index == 0) { //第一题
                mPrevious_question.setVisibility(View.GONE);
            }else{
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
     * */
    public void setCanClick(boolean isCanClick){
        this.isCanClick=isCanClick;
        mViewPager.setScanScroll(isCanClick);
    }

    @Override
    public void onClick(View v) {

        if (!isCanClick){
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

    /**
     * 练习跳转答题
     *
     * @param activity
     * @param fromType  练习页面 ：Constants.MAINAVTIVITY_FROMTYPE_EXERCISE
     */
    public static void invoke(Activity activity, String paperId, String fromType, GenQuesRequest request) {
        Intent intent = new Intent(activity, AnswerQuestionActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, paperId);
        intent.putExtra(Constants.EXTRA_FROMTYPE, fromType);
        intent.putExtra(Constants.EXTRA_REQUEST,request);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(mAnswerCardFragment != null && mAnswerCardFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(mAnswerCardFragment).commit();
            controlListenView(false);
            return;
        }
        SpManager.setCompleteQuestionCount(mPaper.getId(),QuestionUtil.calculateCompleteCount(mQuestions));
        super.onBackPressed();
    }

}
