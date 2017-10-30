package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.adapter.QAViewPagerAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.HomeEventMessage;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisComplexExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.analysisbase.AnalysisSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.view.QAViewPager;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.videoplay.NetworkStateService;
import com.yanxiu.gphone.student.videoplay.PlayerView;
import com.yanxiu.gphone.student.videoplay.ScreenOrientationSwitcher;
import com.yanxiu.gphone.student.videoplay.VideoManager;
import com.yanxiu.gphone.student.videoplay.VideoModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.LastVideoFinished;
import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.Loading;
import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.Normal;

/**
 * 解析页面
 */
public class AnalysisQuestionActivity extends YanxiuBaseActivity implements View.OnClickListener {
    private FragmentManager mFragmentManager;
    private QAViewPager mViewPager;
    private QAViewPagerAdapter mAdapter;
    private String mComeFrom;//获取数据的来源--从哪个页面跳转过来的
    private String mKey;//获取数据的key
    private Paper mPaper;//试卷数据
    public ArrayList<BaseQuestion> mQuestions;//题目数据

    private View mBottomLayout,mBottom_line;
    private LinearLayout mPrevious_question, mNext_question;//上一题，下一题
    private TextView mNext_text;//下一题textview
    private ImageView mBackView;//返回按钮
    private TextView mErrorview;//报错

    private HomeEventMessage mHomeEventMessage=new HomeEventMessage();
    private HomeKeyEventBroadCastReceiver mHomeKeyEventBroadCastReceiver=new HomeKeyEventBroadCastReceiver();


    private ImageView video_float, video_collapse, video_play, video_cover;
    private View layout_cover;

    private PlayerView mPlayerView;
    private VideoManager mVideoManager;
    private AnalysisQuestionActivity.EventListener mListener = new AnalysisQuestionActivity.EventListener();

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

        Glide.with(this).load(mVideoModel.cover).asBitmap().placeholder(R.drawable.video_cover_default).into(video_cover);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysisquestion);
        initData();
        initView();
        initReportData();
        registerReceiver(mHomeKeyEventBroadCastReceiver, new IntentFilter(Intent. ACTION_CLOSE_SYSTEM_DIALOGS));

        if(mHasVideo){
            mVideoManager = new VideoManager(this, (PlayerView) findViewById(R.id.player_view));
            mVideoManager.setOnCourseEventListener(mListener);

            setupVideoModel();
            setupRotation();
            setupNetwork4GWifi();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPlayerViewVisible()){
            playVideo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isPlayerViewVisible()){
            if (mVideoModel != null) {
                mVideoManager.recordPlayPauseState();
                mVideoManager.clearPlayer();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHomeKeyEventBroadCastReceiver);
        if(mHasVideo){
            unregisterReceiver(mNotification);
        }
    }

    @Override
    public void onBackPressed() {
        if(mPlayerView.getVisibility() == View.VISIBLE && !mVideoManager.isPortrait){
            rotateScreen();
            collapseVideo();
            return;
        }else {
            super.onBackPressed();
        }
    }

    private class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            EventBus.getDefault().post(mHomeEventMessage);
        }
    }

    private void initData() {
        mKey = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        if (TextUtils.isEmpty(mKey))
            finish();
        mComeFrom = getIntent().getStringExtra(Constants.EXTRA_COME);
        mPaper = DataFetcher.getInstance().getPaper(mKey);
        mQuestions = mPaper.getQuestions();
        mHasVideo = !TextUtils.isEmpty(mPaper.getVideoUrl());
    }

    /**
     * 从答题报告页过来
     */
    private void initReportData() {
        if (Constants.COME_REPORT.equals(mComeFrom)) {
            mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //答题报告过来的
                    ArrayList<Integer> levelPositions = getIntent().getIntegerArrayListExtra(Constants.EXTRA_ANALYSIS_LEVELPOSITION);
                    if (null != levelPositions && !levelPositions.isEmpty()) {
                        // 2, 跳转
                        int index = levelPositions.get(0);
                        FragmentStatePagerAdapter a1 = (FragmentStatePagerAdapter) mViewPager.getAdapter();
                        mViewPager.setCurrentItem(index,false);
                        ExerciseBaseFragment currentFragment = (ExerciseBaseFragment) a1.instantiateItem(mViewPager, index);
                        currentFragment.setUserVisibleHin2(true);


                        ArrayList<Integer> remainPositions = new ArrayList<>(levelPositions);
                        remainPositions.remove(0);
                        if (remainPositions.size() > 0) { // 表明这层依然是 复合题
                            FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) mViewPager.getAdapter();
                            AnalysisComplexExerciseBaseFragment f = (AnalysisComplexExerciseBaseFragment) a.instantiateItem(mViewPager, index);
                            f.setChildrenPositionRecursively(remainPositions);
                        }
                    }
                    mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private void initView() {
        mPrevious_question = (LinearLayout) findViewById(R.id.previous_question);
        mNext_question = (LinearLayout) findViewById(R.id.next_question);
        mNext_text = (TextView) findViewById(R.id.next_text);
        mBackView = (ImageView) findViewById(R.id.backview);
        mErrorview = (TextView) findViewById(R.id.errorview);
        mBottomLayout = findViewById(R.id.bottom);
        mBottom_line = findViewById(R.id.analysis_bottom_line);


        layout_cover = findViewById(R.id.video_cover);
        video_float = (ImageView) findViewById(R.id.iv_float_play);
        video_collapse = (ImageView) findViewById(R.id.iv_collapse);
        video_play = (ImageView) findViewById(R.id.iv_play);
        video_cover = (ImageView) findViewById(R.id.iv_cover);
        mPlayerView = (PlayerView) findViewById(R.id.player_view);

        if(mPaper.getQuestions().get(0).isHasVideo()){
            video_float.setVisibility(View.VISIBLE);
        }

        initViewPager();
        hiddenBottomLayout();
        setListener();
    }

    private void setListener() {
        mPrevious_question.setOnClickListener(this);
        mNext_question.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mErrorview.setOnClickListener(this);

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
                }else {
                    video_float.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
            }
        });
    }

    /**
     * 当只有一个题时，隐藏bottom，并且把解析单题型fragment的bottom去掉
     */
    private void hiddenBottomLayout(){
        if(mQuestions.size() == 1 && (null == mQuestions.get(0).getChildren() || mQuestions.get(0).getChildren().size() <= 1)){
            mBottomLayout.setVisibility(View.GONE);
            mBottom_line.setVisibility(View.GONE);
        }
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

        if (currentFramgent instanceof AnalysisComplexExerciseBaseFragment) {
            AnalysisComplexExerciseBaseFragment complexExerciseFragment = (AnalysisComplexExerciseBaseFragment) currentFramgent;
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
            } else if (innerIndex == (innerSize - 1)) {
                //状态2
                mViewPager.setCurrentItem(index + 1);
            } else {
                //状态1
                innerViewPager.setCurrentItem(innerIndex + 1);
            }

        } else if (currentFramgent instanceof AnalysisSimpleExerciseBaseFragment) {
            if (index == (size - 1)) {
                //最后一题,展示答题卡
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

        if (currentFramgent instanceof AnalysisComplexExerciseBaseFragment) {
            AnalysisComplexExerciseBaseFragment complexExerciseFragment = (AnalysisComplexExerciseBaseFragment) currentFramgent;
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

        } else if (currentFramgent instanceof AnalysisSimpleExerciseBaseFragment) {
            if (index >= 1) {
                //不是第一大题时,有上一题
                mViewPager.setCurrentItem(index - 1);
            }
        }
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

        if (currentFramgent instanceof AnalysisComplexExerciseBaseFragment) {
            AnalysisComplexExerciseBaseFragment complexExerciseFragment = (AnalysisComplexExerciseBaseFragment) currentFramgent;
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
            if (innerIndex == (innerSize - 1) && index == (size - 1)) { //状态3
                mNext_question.setVisibility(View.GONE);
            } else {
                mNext_question.setVisibility(View.VISIBLE);
                mNext_text.setText(R.string.next_question);
            }

            if (innerIndex == 0 && index == 0) { //第一题
                mPrevious_question.setVisibility(View.GONE);
            } else {
                mPrevious_question.setVisibility(View.VISIBLE);
            }

        } else if (currentFramgent instanceof AnalysisSimpleExerciseBaseFragment) {
            if (index == (size - 1)) { //最后一题
                mNext_question.setVisibility(View.GONE);
            } else {
                mNext_question.setVisibility(View.VISIBLE);
                mNext_text.setText(R.string.next_question);
            }

            if (index == 0) { //第一题
                mPrevious_question.setVisibility(View.GONE);
            } else {
                mPrevious_question.setVisibility(View.VISIBLE);
            }
        }
    }

    public Paper getPaper() {
        return mPaper;
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
            case R.id.backview:
                finish();
                break;
            case R.id.errorview:
                BaseQuestion currentQuestion = getCurrentQuestion();
                String qid = currentQuestion.getQid();
                if(currentQuestion != null && !TextUtils.isEmpty(qid)){
                    AnswerErrorActicity.invoke(AnalysisQuestionActivity.this,qid);
                }
                break;
            case R.id.iv_float_play:
                expandVideo();
                break;
            case R.id.iv_collapse:
                collapseVideo();
                break;
            case R.id.iv_play:
                mPlayerView.setVisibility(View.VISIBLE);
                video_collapse.setVisibility(View.VISIBLE);
                layout_cover.setVisibility(View.GONE);
                playVideo();
                break;
        }
    }

    /**
     * 获取当前题目
     */
    public BaseQuestion getCurrentQuestion() {
        BaseQuestion currentQuestion = null;
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
            return null;
        }

        if (adapter == null || index < 0 || size < 1 || mViewPager == null || currentFramgent == null)
            return null;

        if (currentFramgent instanceof AnalysisComplexExerciseBaseFragment) {
            AnalysisComplexExerciseBaseFragment complexExerciseFragment = (AnalysisComplexExerciseBaseFragment) currentFramgent;
            ViewPager innerViewPager = complexExerciseFragment.getmViewPager();
            FragmentStatePagerAdapter innerAdapter = (FragmentStatePagerAdapter) innerViewPager.getAdapter();
            int innerIndex = innerViewPager.getCurrentItem();
            int innerSize = innerViewPager.getAdapter().getCount();

            if (complexExerciseFragment == null || innerViewPager == null || innerAdapter == null || innerIndex < 0 || innerSize < 1)
                return null;
            currentQuestion = mQuestions.get(index).getChildren().get(innerIndex);

        } else if (currentFramgent instanceof AnalysisSimpleExerciseBaseFragment) { // 单题
            currentQuestion = mQuestions.get(index);

        }
        return currentQuestion;
    }


    /**
     * 跳转AnalysisQuestionActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String key) {
        Intent intent = new Intent(activity, AnalysisQuestionActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        activity.startActivity(intent);
    }

    /**
     * 答题报告跳转AnalysisQuestionActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String key, ArrayList<Integer> levelPositions) {
        Intent intent = new Intent(activity, AnalysisQuestionActivity.class);
        intent.putExtra(Constants.EXTRA_COME, Constants.COME_REPORT);
        intent.putExtra(Constants.EXTRA_PAPER, key);
        intent.putIntegerArrayListExtra(Constants.EXTRA_ANALYSIS_LEVELPOSITION, levelPositions);
        activity.startActivity(intent);
    }



    //视频播放部分

    private void expandVideo(){
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
            if(mVideoManager.isPortrait){
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
                    video_collapse.setVisibility(View.VISIBLE);
                    setPortraitStyle();
                }

                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(requestedOrientation);
                    video_collapse.setVisibility(View.GONE);
                    setLandscapeStyle();
                }

                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    setRequestedOrientation(requestedOrientation);
                    video_collapse.setVisibility(View.GONE);
                    setLandscapeStyle();
                }
            }
        });
        orientationSwitcher.enable();
    }

    // 主动点击旋转
    private void rotateScreen() {
        if (mVideoManager.isPortrait) {
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
