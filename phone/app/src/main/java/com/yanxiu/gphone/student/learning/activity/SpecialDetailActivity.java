package com.yanxiu.gphone.student.learning.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bcresource.bean.TopicBean;
import com.yanxiu.gphone.student.bcresource.response.TopicPaperResponse;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.learning.adapter.RelatedVideoAdapter;
import com.yanxiu.gphone.student.learning.adapter.VideoListAdapter;
import com.yanxiu.gphone.student.learning.bean.VideoDataBean;
import com.yanxiu.gphone.student.learning.request.AddResViewNumRequest;
import com.yanxiu.gphone.student.learning.request.GetRelatedCourseRequest;
import com.yanxiu.gphone.student.learning.response.GetRelatedCourseResponse;
import com.yanxiu.gphone.student.learning.response.GetResourceListDataResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.ScreenUtils;
import com.yanxiu.gphone.student.videoplay.NetworkStateService;
import com.yanxiu.gphone.student.videoplay.PlayerView;
import com.yanxiu.gphone.student.videoplay.VideoManager;
import com.yanxiu.gphone.student.videoplay.VideoModel;

import java.util.List;

import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.LastVideoFinished;
import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.Loading;
import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.Normal;

/**
 * Created by lufengqing on 2018/1/16.
 */

public class SpecialDetailActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private PublicLoadLayout rootView;
    private PlayerView mPlayerView;
    private VideoManager mVideoManager;
    private VideoModel mVideoModel;

    private SpecialDetailActivity.EventListener mListener = new SpecialDetailActivity.EventListener();
    private ImageView video_play;
    private ImageView video_cover;

    VideoListAdapter mRelatedAdapter;
    private VideoListAdapter.OnItemClickListener mOnItemClickListener = new VideoListAdapter.OnItemClickListener() {
        @Override
        public void onClick(VideoDataBean bean, int position) {
            SpecialDetailActivity.invoke(SpecialDetailActivity.this, bean);
            finish();
        }
    };
    private GridView mGridView;
    private View mBack;
    private View layout_cover;
    private RelativeLayout mVideoLayout;
    private RelativeLayout mTitleLayout;
    private TextView mTitle;
    private boolean isRegisterReceiver = false;
    private VideoDataBean mVideoBean;
    private List<VideoDataBean> mRelatedVideoList;
    private HttpCallback<GetRelatedCourseResponse> mGetRelatedListCallback = new EXueELianBaseCallback<GetRelatedCourseResponse>() {
        @Override
        protected void onResponse(RequestBase request, GetRelatedCourseResponse response) {
            if (response.getStatus().getCode() == 0) {
                if (response.getData() != null && response.getData().size() > 0) {
                    mRelatedVideoList = response.getData();
                    mRelatedAdapter = new VideoListAdapter(SpecialDetailActivity.this, mRelatedVideoList, mOnItemClickListener);
                    mGridView.setAdapter(mRelatedAdapter);
                    mRelatedVideoHint.setVisibility(View.VISIBLE);
                    mGridView.setVisibility(View.VISIBLE);
                } else {
//                     mRelatedVideoHint.setVisibility(View.GONE);
                    mGridView.setVisibility(View.GONE);
                    mNoRelatedVideo.setVisibility(View.VISIBLE);
                }
            } else {
//                 mRelatedVideoHint.setVisibility(View.GONE);
                mGridView.setVisibility(View.GONE);
                mNoRelatedVideo.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
//            mRelatedVideoHint.setVisibility(View.GONE);
            mGridView.setVisibility(View.GONE);
            mNoRelatedVideo.setVisibility(View.VISIBLE);
        }
    };
    private TextView mRelatedVideoHint;
    private HttpCallback<EXueELianBaseResponse> mAddResCallback = new HttpCallback<EXueELianBaseResponse>() {
        @Override
        public void onSuccess(RequestBase request, EXueELianBaseResponse ret) {
            if (ret.getStatus().getCode() == 0) {
                mVideoPlayTimes.setText(getResources().getString(R.string.play_times, mVideoBean.getViewnum() + 1));
                setResult(RESULT_OK);
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {

        }
    };
    private TextView mVideoTitle;
    private TextView mVideoPlayTimes;
    private TextView mNoRelatedVideo;

    public static void invoke(Activity activity, VideoDataBean bean) {
        Intent intent = new Intent(activity, SpecialDetailActivity.class);
        intent.putExtra(Constants.VIDEO_BEAN, bean);
        activity.startActivityForResult(intent, 1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = new PublicLoadLayout(this);
        rootView.setContentView(R.layout.activity_special_detail);
        setContentView(rootView);
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
        mVideoBean = (VideoDataBean) getIntent().getSerializableExtra(Constants.VIDEO_BEAN);
        if (mVideoBean == null) {
            this.finish();
            return;
        }
        mTitle.setText(mVideoBean.getTitle());
        mVideoTitle.setText(mVideoBean.getTitle());
        mVideoPlayTimes.setText(getResources().getString(R.string.video_play_times, mVideoBean.getViewnum()));
        setupVideoModel();
        setupNetwork4GWifi();
        initRelatedVideoList();
        mGridView.setFocusable(false);
    }

    private void initRelatedVideoList() {
        //TODO
        if (mVideoBean.getPoint() == null || mVideoBean.getPoint().size() == 0) {
//            mRelatedVideoHint.setVisibility(View.GONE);
            mGridView.setVisibility(View.GONE);
            mNoRelatedVideo.setVisibility(View.VISIBLE);
        } else {
            GetRelatedCourseRequest request = new GetRelatedCourseRequest();
            request.setExcludeId(mVideoBean.getId());
            request.setPoints(mVideoBean.getPoint_string());
            request.startRequest(GetRelatedCourseResponse.class, mGetRelatedListCallback);
        }
//        GetRelatedCourseRequest request = new GetRelatedCourseRequest();
//        request.setExcludeId(mVideoBean.getId());
//        request.setPoints("5000,5001");
//        request.startRequest(GetRelatedCourseResponse.class, mGetRelatedListCallback);
    }

    private void initView() {
        video_play = (ImageView) findViewById(R.id.iv_play);
        video_cover = (ImageView) findViewById(R.id.iv_cover);
        mPlayerView = (PlayerView) findViewById(R.id.player_view);
        mVideoLayout = (RelativeLayout) findViewById(R.id.video_layout);
        mTitleLayout = (RelativeLayout) findViewById(R.id.layout_title);
        mTitle = (TextView) findViewById(R.id.title);
        mVideoTitle = (TextView) findViewById(R.id.video_title);
        mVideoPlayTimes = (TextView) findViewById(R.id.video_play_times);
        layout_cover = findViewById(R.id.video_cover);
        mVideoManager = new VideoManager(this, (PlayerView) findViewById(R.id.player_view));
        mVideoManager.setOnCourseEventListener(mListener);
        mGridView = (GridView) findViewById(R.id.gridView);
        mRelatedVideoHint = (TextView) findViewById(R.id.video_related);
        mNoRelatedVideo = (TextView) findViewById(R.id.no_related_video);
        mBack = findViewById(R.id.back);
    }

    private void setupVideoModel() {
        mVideoModel = new VideoModel();
        mVideoModel.cover = mVideoBean.getRes_thumb();
        mVideoModel.bodyUrl = mVideoBean.getRes_preview_url();
        mVideoModel.bodyPosition = 0;
        mVideoModel.isHeadFinished = false;
        mVideoModel.videoName = mVideoBean.getTitle();
        mVideoModel.videoSizeFormat = mVideoBean.getRes_size_format();

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
            if (!mVideoManager.isPortrait) {
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        isRegisterReceiver = true;
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

    private void playVideo() {
        VideoManager.VideoState lastState = mVideoManager.getState();
        mVideoManager.setupPlayer();
        mVideoManager.setModel(mVideoModel);

        if ((lastState != Normal) && (lastState != Loading)) {
            mVideoManager.setState(lastState);
        }
        submitAddResViewNumRequest();
    }

    private void submitAddResViewNumRequest() {
        AddResViewNumRequest request = new AddResViewNumRequest();
        request.setResId(mVideoBean.getId());
        request.startRequest(EXueELianBaseResponse.class, mAddResCallback);
    }

    private void destoryVideo() {
//        mVideoModel.bodyPosition = 0;
        mVideoManager.clearPlayer();
        mVideoManager.resetAllState();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mVideoManager.setBodyPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoManager.setBodyPlayWhenReady(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryVideo();
        if (isRegisterReceiver) {
            unregisterReceiver(mNotification);
        }
    }


}
