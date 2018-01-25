package com.yanxiu.gphone.student.learning.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bcresource.bean.TopicBean;
import com.yanxiu.gphone.student.bcresource.response.TopicPaperResponse;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.learning.adapter.RelatedVideoAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.videoplay.NetworkStateService;
import com.yanxiu.gphone.student.videoplay.PlayerView;
import com.yanxiu.gphone.student.videoplay.VideoManager;
import com.yanxiu.gphone.student.videoplay.VideoModel;

import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.LastVideoFinished;
import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.Loading;
import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.Normal;

/**
 * Created by lufengqing on 2018/1/16.
 */

public class SpecialDetailActivity extends YanxiuBaseActivity implements  View.OnClickListener{

    private PublicLoadLayout rootView;
    private PlayerView mPlayerView;
    private VideoManager mVideoManager;
    private VideoModel mVideoModel;

    private SpecialDetailActivity.EventListener mListener = new SpecialDetailActivity.EventListener();
    private ImageView video_play;
    private ImageView video_cover;

    private Paper mPaper;//试卷数据
    RelatedVideoAdapter mRelatedAdapter;
    private TopicPaperResponse mMockData;
    private RelatedVideoAdapter.OnItemClickListener mOnItemClickListener = new RelatedVideoAdapter.OnItemClickListener() {
        @Override
        public void onClick(TopicBean bean, int position) {
            Paper paper = new Paper(mMockRelatedVideoData.getData().get(0), QuestionShowType.ANSWER);
            DataFetcher.getInstance().save(paper.getId(), paper);
            SpecialDetailActivity.invoke(SpecialDetailActivity.this, paper.getId(),"413602",Constants.FROM_BC_RESOURCE);
        }
    };
    private String mPaperId;
    private GridView mGridView;
    private View mBack;
    private View layout_cover;
    private PaperResponse mMockRelatedVideoData;
    private RelativeLayout mVideoLayout;
    private RelativeLayout mTitleLayout;

    public static void invoke(Activity activity){
        Intent intent = new Intent(activity,SpecialDetailActivity.class);
        activity.startActivity(intent);
    }

    public static void invoke(Activity activity, String paperId, String rmsPaperId, String fromType) {
        Intent intent = new Intent(activity, SpecialDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PAPER, paperId);
        intent.putExtra(Constants.EXTRA_RMSPAPER,rmsPaperId);
        intent.putExtra(Constants.EXTRA_FROMTYPE, fromType);
        activity.startActivity(intent);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView=new PublicLoadLayout(this);
        rootView.setContentView(R.layout.activity_special_detail);
        setContentView(rootView);
        mockBCData();
        mockRelatedVideoDataData();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        video_play.setOnClickListener(this);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destoryVideo();
                finish();
            }
        });
    }

    private void initData() {
        mPaperId = getIntent().getStringExtra(Constants.EXTRA_PAPER);
        mPaper = DataFetcher.getInstance().getPaper(mPaperId);
        if (mPaper==null){
            this.finish();
            return;
        }
        setupVideoModel();
        setupNetwork4GWifi();
        mRelatedAdapter = new RelatedVideoAdapter(this, mMockData.getData(), mOnItemClickListener );
        mGridView.setAdapter(mRelatedAdapter);
        mGridView.setFocusable(false);
    }

    private void initView() {
        video_play = (ImageView) findViewById(R.id.iv_play);
        video_cover = (ImageView) findViewById(R.id.iv_cover);
        mPlayerView = (PlayerView) findViewById(R.id.player_view);
        mVideoLayout = (RelativeLayout) findViewById(R.id.video_layout);
        mTitleLayout = (RelativeLayout) findViewById(R.id.layout_title);
        layout_cover = findViewById(R.id.video_cover);
        mVideoManager = new VideoManager(this, (PlayerView) findViewById(R.id.player_view));
        mVideoManager.setOnCourseEventListener(mListener);
        mGridView = (GridView) findViewById(R.id.gridView);
        mBack = findViewById(R.id.back);
    }

    private void setupVideoModel(){
        mVideoModel = new VideoModel();
        mVideoModel.cover = mPaper.getCover();
        mVideoModel.bodyUrl = mPaper.getVideoUrl();
        mVideoModel.bodyPosition = 0;
        mVideoModel.isHeadFinished = false;
        mVideoModel.videoName = mPaper.getName();
        mVideoModel.videoSize = mPaper.getVideoSize();

        Glide.with(this).load(mVideoModel.cover).asBitmap().placeholder(R.drawable.video_cover_default).error(R.drawable.video_cover_default).into(video_cover);
    }

    private final class EventListener implements VideoManager.OnCourseEventListener {
        @Override
        public void onRotate() {
            rotateScreen();
        }

        @Override
        public void onBackPressed() {
            rotateScreen();
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
        }

        @Override
        public void onReplayFromFirstVideo() {
            goPlay();
        }
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
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(this, 216));
        mVideoManager.getPlayerView().setLayoutParams(params);
        mTitleLayout.setVisibility(View.VISIBLE);
    }

    private void setLandscapeStyle() {
        mVideoManager.isPortrait = false;
        mVideoManager.updatePortraitLandscapeControllerView();
//        findViewById(R.id.play_button).setVisibility(View.GONE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.topMargin = 0;
        mVideoManager.getPlayerView().setLayoutParams(params);
        mTitleLayout.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        if (!mVideoManager.isPortrait) {
            rotateScreen();
            return;
        } else {
            destoryVideo();
            finish();
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_play:
                layout_cover.setVisibility(View.GONE);
                playVideo();
                break;
        }
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
//        mVideoModel.bodyPosition = 0;
        mVideoManager.clearPlayer();
        mVideoManager.resetAllState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         unregisterReceiver(mNotification);
    }

    public void mockBCData() {
        String json = FileUtil.getDataFromAssets(this, "Mock_BC.json");
        mMockData = RequestBase.gson.fromJson(json, TopicPaperResponse.class);
    }

    public void mockRelatedVideoDataData() {
        String json = FileUtil.getDataFromAssets(this, "Mock_relatedVideo.json");
        mMockRelatedVideoData = RequestBase.gson.fromJson(json, PaperResponse.class);
    }
}
