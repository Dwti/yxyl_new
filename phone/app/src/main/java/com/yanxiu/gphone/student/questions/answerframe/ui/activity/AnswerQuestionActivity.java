package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

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
import com.yanxiu.gphone.student.questions.answerframe.adapter.QAViewPagerAdapter;
import com.yanxiu.gphone.student.questions.answerframe.http.request.SubmitQuesitonTask;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;
import com.yanxiu.gphone.student.questions.answerframe.listener.SubmitAnswerCallback;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.AnswerCardFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
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
 * 答题页面
 */
public class AnswerQuestionActivity extends YanxiuBaseActivity implements View.OnClickListener, OnAnswerCardItemSelectListener
        , AnswerCardSubmitDialog.AnswerCardSubmitDialogClickListener {
    private FragmentManager mFragmentManager;
    private QAViewPager mViewPager;
    private QAViewPagerAdapter mAdapter;
    private String mKey;//获取数据的key
    private String mFromType;//从哪个页面跳转过来的（练习页面）
    private String mRmsPaperId;
    private GenQuesRequest mGenQuesequest;//从选择章节只是点进入答题页，章节知识点的请求Request
    private String mTitleString;//试卷的title-答题卡需要
    private Paper mPaper;//试卷数据
    private ArrayList<BaseQuestion> mQuestions;//题目数据
    private AnswerCardFragment mAnswerCardFragment;

    private KeyboardObserver mKeyboardObserver;
    private QuestionTimeTextView mTimer;//计时
    private QuestionProgressView mProgressView;//答题进度条
    private LinearLayout mPrevious_question, mNext_question;//上一题，下一题
    private TextView mNext_text;//下一题textview
    private ImageView mBackView;//返回按钮
    private ImageView mShowAnswerCardView;//显示答题卡
    private View mRootView,mOverlay;
    private boolean mHasShowVideoGuide = false;

    private InputMethodManager mInputMethodManager;

    private Handler mHandler;
    private int mTotalTime;//总计时间
    private long mStartTime;//开始答题时间
    /**
     * 刷新计时
     */
    private static final int HANDLER_TIME = 0x100;
    /**
     * 一秒
     */
    private final int HANDLER_TIME_DELAYED = 1000;
    private int mTotalQuestion;//题目总数量
    private boolean isCanClick=true;


    private Button btn_skip_video;
    private ImageView video_float, video_collapse, video_play, video_cover, tips_play, tips_cover;
    private View layout_cover,video_tips;

    private PlayerView mPlayerView;
    private VideoManager mVideoManager;
    private AnswerQuestionActivity.EventListener mListener = new AnswerQuestionActivity.EventListener();

    private VideoModel mVideoModel;

    private boolean mHasVideo = false;

    private void setupVideoModel(){
        mVideoModel = new VideoModel();
        mVideoModel.cover = mPaper.getCover();
        mVideoModel.bodyUrl = mPaper.getVideoUrl();
        mVideoModel.bodyPosition = 0;
        mVideoModel.isHeadFinished = false;
        mVideoModel.videoName = mPaper.getName();
        mVideoModel.videoSize = mPaper.getVideoSize();

        Glide.with(this).load(mVideoModel.cover).asBitmap().placeholder(R.drawable.video_cover_default).error(R.drawable.video_cover_default).into(video_cover);
        Glide.with(this).load(mVideoModel.cover).asBitmap().placeholder(R.drawable.video_cover_default).error(R.drawable.video_cover_default).into(tips_cover);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerquestion);
        isCanClick=true;
        initData();
        initView();

        if(mHasVideo){
            mVideoManager = new VideoManager(this, (PlayerView) findViewById(R.id.player_view));
            mVideoManager.setOnCourseEventListener(mListener);

            setupVideoModel();
//            setupRotation();
            setupNetwork4GWifi();
        }
    }

    private void initData() {
        mKey = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        if (TextUtils.isEmpty(mKey))
            finish();
        mFromType = getIntent().getStringExtra(Constants.EXTRA_FROMTYPE);
        if(Constants.FROM_BC_RESOURCE.equals(mFromType)){
            mRmsPaperId = getIntent().getStringExtra(Constants.EXTRA_RMSPAPER);
        }
        initExerciseData();
        mPaper = DataFetcher.getInstance().getPaper(mKey);
        if (mPaper==null){
            this.finish();
            return;
        }
        mQuestions = mPaper.getQuestions();
        initProgressViewData();
        mTotalTime = (SpManager.getTotlaTime(mPaper.getId()) != -1) ? SpManager.getTotlaTime(mPaper.getId()) : 0;
        mStartTime = System.currentTimeMillis();
        mPaper.getPaperStatus().setBegintime(mStartTime+"");
        mTitleString = mPaper.getName();

        mHasShowVideoGuide = SaveAnswerDBHelper.isHasShowVideTips(mPaper.getId());

        mHasVideo = !TextUtils.isEmpty(mPaper.getVideoUrl());
    }

    /**
     * 从练习页面跳转过来的，初始化相关数据
     */
    private void initExerciseData(){
        if(Constants.MAINAVTIVITY_FROMTYPE_EXERCISE.equals(mFromType)){
            //从练习页面过来
            mGenQuesequest = (GenQuesRequest)getIntent().getSerializableExtra(Constants.EXTRA_REQUEST);
            initDialog();
        }else if(Constants.MAINAVTIVITY_FROMTYPE_EXERCISE_HISTORY.equals(mFromType)){
            //从练习历史页面过来
            mGenQuesequest = null;
            initDialog();
        }
    }

    private void initView() {
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mRootView = findViewById(R.id.fl_qa);
        mOverlay = findViewById(R.id.overlay);
        mTimer = (QuestionTimeTextView) findViewById(R.id.timer);
        mProgressView = (QuestionProgressView) findViewById(R.id.progressBar);
        mProgressView.setMaxCount(mTotalQuestion);
        mPrevious_question = (LinearLayout) findViewById(R.id.previous_question);
        mNext_question = (LinearLayout) findViewById(R.id.next_question);
        mNext_text = (TextView) findViewById(R.id.next_text);
        mBackView = (ImageView) findViewById(R.id.backview);
        mShowAnswerCardView = (ImageView) findViewById(R.id.answercardview);

        video_tips = findViewById(R.id.video_tips);
        tips_play  = (ImageView) findViewById(R.id.tips_play);
        tips_cover = (ImageView) findViewById(R.id.tips_cover);
        btn_skip_video = (Button) findViewById(R.id.btn_skip);
        layout_cover = findViewById(R.id.video_cover);
        video_float = (ImageView) findViewById(R.id.iv_float_play);
        video_collapse = (ImageView) findViewById(R.id.iv_collapse);
        video_play = (ImageView) findViewById(R.id.iv_play);
        video_cover = (ImageView) findViewById(R.id.iv_cover);
        mPlayerView = (PlayerView) findViewById(R.id.player_view);

        if(mHasVideo && mPaper.getQuestions().get(0).isHasVideo()){
            video_float.setVisibility(View.VISIBLE);
            if(!mHasShowVideoGuide){
                video_tips.setVisibility(View.VISIBLE);
                mHasShowVideoGuide = true;
                SaveAnswerDBHelper.setHasShowVideoTips(mPaper.getId(),true);
            }
        }

        initViewPager();
        setListener();
        mHandler = new TimingHandler(this);
        mTimer.setTime(mTotalTime);

    }

    private void setListener() {
        mPrevious_question.setOnClickListener(this);
        mKeyboardObserver = new KeyboardObserver(mRootView);
        mNext_question.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mShowAnswerCardView.setOnClickListener(this);

        video_tips.setOnClickListener(this);
        tips_play.setOnClickListener(this);
        btn_skip_video.setOnClickListener(this);
        video_float.setOnClickListener(this);
        video_collapse.setOnClickListener(this);
        video_play.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(!mHasVideo)
                    return;
                collapseVideo();
                if(mPaper.getQuestions().get(position).isHasVideo()){
                    video_float.setVisibility(View.VISIBLE);
                    if(!mHasShowVideoGuide){
                        video_tips.setVisibility(View.VISIBLE);
                        mHasShowVideoGuide = true;
                        SaveAnswerDBHelper.setHasShowVideoTips(mPaper.getId(),true);
                    }
                }else {
                    video_float.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

//    /**
//     * 给答题卡设置题号
//     *
//     * @return
//     */
//    private ArrayList<BaseQuestion> allNodesThatHasNumber() {
//        ArrayList<BaseQuestion> retNodes = new ArrayList<>();
//        for (BaseQuestion node : mQuestions) {
//            retNodes.addAll(node.allNodesThatHasNumber());
//        }
//        return retNodes;
//    }

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
            mAnswerCardFragment.setOnCardItemSelectListener(AnswerQuestionActivity.this);
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
            ExerciseBaseFragment fragment =  currentFramgent;
            fragment.onAnswerCardVisibleToUser(answerCardFragmentIsShwon);
        }

    }

    public Paper getPaper() {
        return mPaper;
    }

    public QuestionProgressView getProgressView() {
        return mProgressView;
    }

    public String getFromType() {
        return mFromType;
    }

    public String getRmsPaperId(){
        return mRmsPaperId;
    }

    public GenQuesRequest getGenQuesequest() {
        return mGenQuesequest;
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
                if(Constants.MAINAVTIVITY_FROMTYPE_EXERCISE.equals(mFromType)){ // 练习
                    if(checkQuestionHasAnswerd()){
                        quitSubmmitDialog();
                    }else {
                        finish();
                    }
                }else if(Constants.MAINAVTIVITY_FROMTYPE_EXERCISE_HISTORY.equals(mFromType)){
                    mDialog.showLoadingView();
                    requestSubmmit();
                }else if(Constants.FROM_BC_RESOURCE.equals(mFromType)){
                    if(checkQuestionHasAnswerd()){
                        SaveAnswerDBHelper.setTopicPaperAnswered(mRmsPaperId,true);
                    }else {
                        SaveAnswerDBHelper.setTopicPaperAnswered(mRmsPaperId,false);
                    }
                    finish();
                }else {
                    SpManager.setCompleteQuestionCount(mPaper.getId(),QuestionUtil.calculateCompleteCount(mQuestions));
                    finish();
                }
                break;
            case R.id.answercardview:
                if (mVideoManager!=null) {
                    mVideoManager.setBodyPlayWhenReady(false);
                }
                showAnswerCardFragment();
                break;
            case R.id.tips_play:
                video_tips.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                playVideo();
                rotateScreen();
                break;
            case R.id.btn_skip:
                video_tips.setVisibility(View.GONE);
                break;
            case R.id.iv_float_play:
                expandVideo();
                break;
            case R.id.iv_collapse:
                if(layout_cover.getVisibility() == View.VISIBLE){
                    layout_cover.setVisibility(View.GONE);
                    video_collapse.setVisibility(View.GONE);
                }else {
                    collapseVideo();
                }
                break;
            case R.id.iv_play:
                mPlayerView.setVisibility(View.VISIBLE);
                layout_cover.setVisibility(View.GONE);
                if(!mVideoManager.isPortrait){
                    video_collapse.setVisibility(View.GONE);
                }
                playVideo();
                break;
            case R.id.video_tips:
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
        mTimer.setTime(mTotalTime);
    }

    public int getmTotalTime() {
        return mTotalTime;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTiming();

        if(isPlayerViewVisible()){
            playVideo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        endTiming();

        if(isPlayerViewVisible()){
            if (mVideoModel != null) {
                mVideoManager.recordPlayPauseState();
                mVideoManager.clearPlayer();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mPaper!=null) {
            SpManager.setTotlaTime(mPaper.getId(), mTotalTime);
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mKeyboardObserver.destroy();
        mOverlay.clearAnimation();
        super.onDestroy();
        if(mHasVideo){
            unregisterReceiver(mNotification);
        }
    }

    /**
     * 跳转AnswerQuestionActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String paperId) {
        Intent intent = new Intent(activity, AnswerQuestionActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, paperId);
        activity.startActivity(intent);
    }

    public static void invoke(Activity activity, String paperId,String rmsPaperId, String fromType,int flag) {
        Intent intent = new Intent(activity, AnswerQuestionActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, paperId);
        intent.putExtra(Constants.EXTRA_RMSPAPER,rmsPaperId);
        intent.putExtra(Constants.EXTRA_FROMTYPE, fromType);
        activity.startActivity(intent);
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
        if(mPlayerView.getVisibility() == View.VISIBLE && !mVideoManager.isPortrait){
            rotateScreen();
            collapseVideo();
            return;
        }
        if(mAnswerCardFragment != null && mAnswerCardFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(mAnswerCardFragment).commit();
            controlListenView(false);
            return;
        }
        if(Constants.MAINAVTIVITY_FROMTYPE_EXERCISE.equals(mFromType)){ // 练习
            if(checkQuestionHasAnswerd()){
                quitSubmmitDialog();
            }else {
                finish();
            }
        }else if(Constants.FROM_BC_RESOURCE.equals(mFromType)){
            if(checkQuestionHasAnswerd()){
                SaveAnswerDBHelper.setTopicPaperAnswered(mRmsPaperId,true);
            }else {
                SaveAnswerDBHelper.setTopicPaperAnswered(mRmsPaperId,false);
            }
            finish();
        }else if(Constants.MAINAVTIVITY_FROMTYPE_EXERCISE_HISTORY.equals(mFromType)){
            mDialog.showLoadingView();
            requestSubmmit();
        }else{
            SpManager.setCompleteQuestionCount(mPaper.getId(),QuestionUtil.calculateCompleteCount(mQuestions));
            super.onBackPressed();
        }
    }
    //提交练习答案dialog
    private AnswerCardSubmitDialog mDialog;
    //提交练习答案task
    private SubmitQuesitonTask mSubmitQuesitonTask;

    /**
     * 提交练习答案dialog
     */
    private void initDialog() {
        mDialog = new AnswerCardSubmitDialog(AnswerQuestionActivity.this);
        mDialog.setCancelable(true);
        mDialog.setAnswerCardSubmitDialogClickListener(AnswerQuestionActivity.this);
    }

    /**
     * 如果是练习，退出时，需要提交练习答案
     */
    private void quitSubmmitDialog(){
        mDialog.setData(mQuestions);
        mDialog.showExerciseConfirmView();
    }

    /**
     * 提交练习答案
     */
    private void requestSubmmit() {
        if (mSubmitQuesitonTask != null && !mSubmitQuesitonTask.isCancelled()) {
            mSubmitQuesitonTask.cancel(true);
        }
        String endtime = String.valueOf(System.currentTimeMillis());
        mPaper.getPaperStatus().setEndtime(endtime);
        mPaper.getPaperStatus().setCosttime(getmTotalTime() + "");
        mSubmitQuesitonTask = new SubmitQuesitonTask(YanxiuApplication.getContext(), mPaper, mSubmitQuesitonTask.LIVE_CODE, new SubmitAnswerCallback() {

            @Override
            public void onSuccess() {
                //提交练习答案成功
                finish();
            }

            @Override
            public void onFail() {
                mDialog.showRetryView();
            }

            @Override
            public void onUpdate(int count, int index) {

            }

            @Override
            public void onDataError(int responeCode,String msg) {
                ToastManager.showMsg(msg);
                finish();
            }
        });
        mSubmitQuesitonTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    @Override
    public void onDialogButtonClick(View v, AnswerCardSubmitDialog.SubmitState state) {
        switch (v.getId()) {
            case R.id.button_yes:
                switch (state) {
                    case STATE_EXERICSE_CONFIRM:
                    case STATE_RETRY:
                        mDialog.showLoadingView();
                        requestSubmmit();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 练习提交答案，如果试卷完全没有回答，则不提交
     * @return true 有回答的题
     */
    private boolean checkQuestionHasAnswerd(){
        for (int i = 0; i < mQuestions.size(); i++) {
            if (mQuestions.get(i).getHasAnswered()) { //只要有一个作答的就弹框
                return true;
            }
        }
        return false;
    }


   //视频播放部分

    private void expandVideo(){
        video_collapse.setVisibility(View.VISIBLE);
        layout_cover.setVisibility(View.VISIBLE);
    }


    private void collapseVideo(){
        layout_cover.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.GONE);
        video_collapse.setVisibility(View.GONE);
        destoryVideo();
    }

    private boolean isPlayerViewVisible(){
        return mPlayerView.getVisibility() == View.VISIBLE;
    }

    private void playVideo(){
        VideoManager.VideoState lastState = mVideoManager.getState();
        mVideoManager.setupPlayer();
        mVideoManager.setModel(mVideoModel);

        if ((lastState != Normal) && (lastState != Loading)) {
            mVideoManager.setState(lastState);
        }
    }

    private void destoryVideo(){
        mVideoModel.bodyPosition = 0;
        mVideoManager.clearPlayer();
        mVideoManager.resetAllState();
    }

    private final class EventListener implements VideoManager.OnCourseEventListener {
        @Override
        public void onRotate() {
            rotateScreen();
            if((mVideoManager.isPortrait && mPlayerView.getVisibility() == View.VISIBLE) || (!mVideoManager.isPortrait && layout_cover.getVisibility() == View.VISIBLE) ){
                video_collapse.setVisibility(View.VISIBLE);
            }else {
                video_collapse.setVisibility(View.GONE);
            }
        }

        @Override
        public void onBackPressed() {
            rotateScreen();
            collapseVideo();
        }

        @Override
        public void onHeadFinish() {
            mVideoModel.isHeadFinished = true;
        }

        @Override
        public void onBodyFinish() {
            mVideoManager.setState(LastVideoFinished);
            if(!mVideoManager.isPortrait){
                rotateScreen();
            }
            collapseVideo();
        }

        @Override
        public void onReplayFromFirstVideo() {
            goPlay();
        }
    }

    private void goPlay() {
        if (!mVideoModel.isHeadFinished) {
            mVideoModel.headPosition = 0;
        }

        mVideoManager.clearPlayer();
        mVideoManager.setupPlayer();
        mVideoManager.resetAllState();
        mVideoManager.setModel(mVideoModel);
    }

    //region 移动网wifi 相关
    private void setupNetwork4GWifi() {
        Intent intent = new Intent(this, NetworkStateService.class);
        intent.setAction(NetworkStateService.NETWORKSTATE);
        startService(intent);

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(NetworkStateService.NETWORKSTATE);
        registerReceiver(mNotification, mFilter);
    }

    private BroadcastReceiver mNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NetworkStateService.NETWORKSTATE)) {
                int state = intent.getIntExtra("networkStatus", -1);
                // 0 无网
                // 1 移动网络
                // 2 wifi
                if (state == 1) {
                    // 移动网络
                    mVideoManager.networkChangeToFourG();
                }
            }
        }
    };
    //endregion

    //region 全屏半屏 相关
    private ScreenOrientationSwitcher orientationSwitcher;
    private void setupRotation() {
//        getActionBar().hide();
        orientationSwitcher = new ScreenOrientationSwitcher(this);
        orientationSwitcher.setChangeListener(new ScreenOrientationSwitcher.OnChangeListener() {
            // 重力感应旋转
            @Override
            public void onChanged(int requestedOrientation) {
                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(requestedOrientation);
                    setPortraitStyle();
                    if(mPlayerView.getVisibility() == View.VISIBLE || layout_cover.getVisibility() == View.VISIBLE){
                        video_collapse.setVisibility(View.VISIBLE);
                    }
                }

                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(requestedOrientation);
                    setLandscapeStyle();
                    if(layout_cover.getVisibility() != View.VISIBLE){
                        video_collapse.setVisibility(View.GONE);
                    }
                }

                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    setRequestedOrientation(requestedOrientation);
                    setLandscapeStyle();
                    if(layout_cover.getVisibility() != View.VISIBLE){
                        video_collapse.setVisibility(View.GONE);
                    }
                }
            }
        });
        orientationSwitcher.enable();
    }

    // 主动点击旋转
    private void rotateScreen() {
        if (mVideoManager.isPortrait) {
            mInputMethodManager.hideSoftInputFromWindow(mRootView.getWindowToken(),0);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setLandscapeStyle();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setPortraitStyle();
        }
    }

    private void setPortraitStyle() {
        mVideoManager.isPortrait = true;
        mVideoManager.updatePortraitLandscapeControllerView();
//        findViewById(R.id.play_button).setVisibility(View.VISIBLE);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(this, 250));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(this, 250));
        params.topMargin = getResources().getDimensionPixelOffset(R.dimen.question_top_layout_height);
        mVideoManager.getPlayerView().setLayoutParams(params);
    }

    private void setLandscapeStyle() {
        mVideoManager.isPortrait = false;
        mVideoManager.updatePortraitLandscapeControllerView();
//        findViewById(R.id.play_button).setVisibility(View.GONE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = 0;
        mVideoManager.getPlayerView().setLayoutParams(params);
    }
    //endregion

}
