package com.yanxiu.gphone.student.videoplay;


import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.NetWorkUtils;

import static com.google.android.exoplayer2.C.TIME_UNSET;
import static com.google.android.exoplayer2.ExoPlayer.EventListener;
import static com.google.android.exoplayer2.ExoPlayer.STATE_ENDED;

/**
 * Created by cailei on 04/07/2017.
 */
public class VideoManager {
    public enum VideoState {
        Normal,
        Loading,
        NetworkError,
        NotFoundError,
        LastVideoFinished,
        FourG,
        SuiTangLian
    }
    private VideoState state = VideoState.Normal;
    private boolean userWantPlayWhenReady = true;
    private boolean realPlayWhenReady = true;

    public interface OnCourseEventListener {
        void onRotate();
        void onHeadFinish();
        void onBodyFinish();
        void onReplayFromFirstVideo();
    }
    private OnCourseEventListener listener;
    public void setOnCourseEventListener(OnCourseEventListener listener) {
        this.listener = listener;
    }

    private VideoModel model;

    private SimpleExoPlayer headPlayer;
    private SimpleExoPlayer bodyPlayer;
    public PlayerView getPlayerView() {
        return playerView;
    }

    private PlayerView playerView;
    private PlaybackControllerView controllerPlaceholderView;

    private Context context;
    private Handler mainHandler;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;

    final private HeadListener headListener = new HeadListener();
    final private BodyListener bodyListener = new BodyListener();

    public VideoManager(Context context, PlayerView playerView) {
        this.context = context;
        this.playerView = playerView;

        setupVideoStateViewClickListeners();
    }

    public void setupPlayer() {
        // 1. Create a default TrackSelector
        mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        TrackSelector trackSelector2 =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        headPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), trackSelector);
        headPlayer.addListener(headListener);

        //bodyPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), trackSelector2);
        DefaultLoadControl lc = new DefaultLoadControl(new DefaultAllocator(true, 64 * 1024), 3000, 30000, 3000, 3000);
        bodyPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), trackSelector2, lc);
        bodyPlayer.addListener(bodyListener);

        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "aVideoPlay"), (TransferListener<? super DataSource>) bandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        extractorsFactory = new DefaultExtractorsFactory();
    }

    public void clearPlayer() {
        if (headPlayer != null) {
            headPlayer.release();
            headPlayer = null;
        }
        if (bodyPlayer != null) {
            bodyPlayer.release();
            bodyPlayer = null;
        }

        handler.removeCallbacks(updateProgressAction);
    }

    public void recordPlayPauseState() {
        realPlayWhenReady = playerView.getPlayer().getPlayWhenReady();
    }

    public void resetAllState() {
        state = VideoState.Normal;
        userWantPlayWhenReady = true;
        realPlayWhenReady = true;
    }

    public VideoState getState() {
        return state;
    }

    public void setState(VideoState state) {
        this.state = state;
        playerView.setVideoState(state);

        if(playerView.getPlayer() == null)
            return;

        if ((state != VideoState.Normal) && (state != VideoState.Loading)) {
            playerView.getPlayer().setPlayWhenReady(false);
        }

        // 每次状态变化后，记录当下playWhenReady
        realPlayWhenReady = playerView.getPlayer().getPlayWhenReady();

        if (state == VideoState.FourG) {
            // 为了一点流量都不偷跑
            playerView.getPlayer().stop();
        }
    }

    private void setupVideoStateViewClickListeners() {
        playerView.findViewById(R.id.network_error_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAndRecoverCurrentVideo(true);
            }
        });

        playerView.findViewById(R.id.not_found_error_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAndRecoverCurrentVideo(true);
            }
        });

        playerView.findViewById(R.id.last_video_finished_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReplayFromFirstVideo();
                }
            }
        });

        playerView.findViewById(R.id.four_g_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAndRecoverCurrentVideo(false);
            }
        });
    }

    private void playWithModel() {
        if ((model.headUrl != null) && (!model.isHeadFinished)) {
            headPlayer.prepare(mediaSourceFromUrl(model.headUrl), true, true);
            headPlayer.seekTo(model.headPosition);
        }

        if (model.bodyUrl != null) {
            bodyPlayer.prepare(mediaSourceFromUrl(model.bodyUrl), true, true);
            bodyPlayer.seekTo(model.bodyPosition);
        }

        if ((model.headUrl != null) && (!model.isHeadFinished)) {
            playerView.setPlayer(headPlayer);
            headPlayer.setPlayWhenReady(true);
            bodyPlayer.setPlayWhenReady(false);
        } else {
            playerView.setPlayer(bodyPlayer);
            bodyPlayer.setPlayWhenReady(true);
        }

        updatePortraitLandscapeControllerView();
    }

    public VideoModel getModel() {
        return model;
    }

    public void setModel(VideoModel model) {
        if ((model.bodyDuration != -1) && (model.bodyDuration <= model.bodyPosition + 1000) && (model.bodyDuration != TIME_UNSET)) {
            model.bodyPosition = 0;
        }
        this.model = model;
        playWithModel();

        stopAndRecoverCurrentVideo(true);
    }

    private void replaceControllerView(PlaybackControllerView controllerView) {
        if (controllerPlaceholderView == null) {
            controllerPlaceholderView = (PlaybackControllerView) playerView.findViewById(R.id.exo_controller_placeholder);
        }

        controllerView.setLayoutParams(controllerPlaceholderView.getLayoutParams());
        ViewGroup parent = ((ViewGroup) controllerPlaceholderView.getParent());
        int controllerIndex = parent.indexOfChild(controllerPlaceholderView);
        parent.removeView(controllerPlaceholderView);
        parent.addView(controllerView, controllerIndex);

        controllerPlaceholderView = controllerView;
        playerView.setControllerView(controllerPlaceholderView);

        controllerView.findViewById(R.id.rotate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRotate();
                }
            }
        });

        controllerView.setControlDispatcher(new PlaybackControllerView.ControlDispatcher() {
            @Override
            public boolean dispatchSetPlayWhenReady(ExoPlayer player, boolean playWhenReady) {
                userWantPlayWhenReady = playWhenReady;
                if (playWhenReady) {
                    if (hasDealtWithFourG()) {
                        return false;
                    }
                }

                player.setPlayWhenReady(playWhenReady);
                return true;
            }

            @Override
            public boolean dispatchSeekTo(ExoPlayer player, int windowIndex, long positionMs) {
                player.seekTo(windowIndex, positionMs);
                return true;
            }
        });
    }

    private void goPlayBody(boolean isHeadFinished) {
        model.isHeadFinished = isHeadFinished;
        bodyPlayer.prepare(mediaSourceFromUrl(model.bodyUrl), false, true);
        playerView.setPlayer(bodyPlayer);
        bodyPlayer.setPlayWhenReady(true);

        updatePortraitLandscapeControllerView();

        if ((listener != null) && (isHeadFinished)) {
            listener.onHeadFinish();
        }
    }

    public void setBodyPlayWhenReady(boolean play){
        bodyPlayer.setPlayWhenReady(play);
    }

    private void stopAndRecoverCurrentVideo(boolean considerFourG) {
        if (considerFourG && hasDealtWithFourG()) {
            return;
        }

        // 由于以下bug，所以每次都stop了再重新来，否则网络切换过程中，一定概率一直显示加载 （STATE_BUFFERING）
        // bug : https://github.com/google/ExoPlayer/issues/911
        playerView.getPlayer().stop();
        if (playerView.getPlayer() == headPlayer) {
            playerView.getPlayer().prepare(mediaSourceFromUrl(model.headUrl), true, true);
            playerView.getPlayer().seekTo(model.headPosition);
        }
        if (playerView.getPlayer() == bodyPlayer) {
            playerView.getPlayer().prepare(mediaSourceFromUrl(model.bodyUrl), true, true);
            playerView.getPlayer().seekTo(model.bodyPosition);
        }
        setState(VideoState.Normal);
        playerView.getPlayer().setPlayWhenReady(userWantPlayWhenReady);
    }

    //region 片头，正片状态listeners
    class LSTVideoListener implements EventListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            updateProgress();

            if (playbackState == ExoPlayer.STATE_BUFFERING) {
                state = VideoState.Loading;
                playerView.setVideoState(VideoState.Loading);
            } else {
                state = VideoState.Normal;
                playerView.setVideoState(VideoState.Normal);
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            // 展示Error界面
            if (error.getCause() instanceof FileDataSource.FileDataSourceException) {
                setState(VideoState.NotFoundError);
            }

            if (error.getCause() instanceof UnrecognizedInputFormatException) {
                setState(VideoState.NotFoundError);
            }

            if (error.getCause() instanceof HttpDataSource.HttpDataSourceException) {
                setState(VideoState.NetworkError);
            }
        }

        //region 没用
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
        }

        @Override
        public void onPositionDiscontinuity() {
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        }
        //endregion
    }

    // 片头
    final class HeadListener extends LSTVideoListener {
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.e("cailei", "head error");
            if (NetWorkUtils.isNetworkConnected(context)) {
                // 跳过片头，播放正片
                goPlayBody(false);
                return;
            }

            if (playerView.getPlayer() == headPlayer) {
                // 只处理当前player的error
                super.onPlayerError(error);
            }
        }

        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if ((state != VideoState.Loading) && (state != VideoState.Normal)) {
                return;
            }

            Log.e("cailei", "head state change to : " + playbackState);
            super.onPlayerStateChanged(playWhenReady, playbackState);
            if (playbackState == STATE_ENDED) {
                goPlayBody(true);
            }
        }
    }

    // 正片
    final class BodyListener extends LSTVideoListener {
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.e("cailei", "error");
            if (playerView.getPlayer() == bodyPlayer) {
                // 只处理当前player的error
                super.onPlayerError(error);
            }
        }

        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if ((state != VideoState.Loading) && (state != VideoState.Normal)) {
                return;
            }

            Log.e("cailei", "state change to : " + playbackState);
            super.onPlayerStateChanged(playWhenReady, playbackState);
            if ((playbackState == STATE_ENDED) && (listener != null)) {
                listener.onBodyFinish();
            }
        }
    }
    //endregion

    //region 旋转屏
    public boolean isPortrait = true;
    public void updatePortraitLandscapeControllerView() {
        boolean isHead = (playerView.getPlayer() == headPlayer ? true : false);
        if (isPortrait && isHead) {
            setupPortraitHeadControllerView();
        }

        if (!isPortrait && isHead) {
            setupLandscapeHeadControllerView();
        }

        if (isPortrait && !isHead) {
            setupPortraitBodyControllerView();
        }

        if (!isPortrait && !isHead) {
            setupLandscapeBodyControllerView();
        }
    }

    private void setupPortraitHeadControllerView() {
        HeadPlaybackControllerView controllerView = new HeadPlaybackControllerView(context);
        controllerView.findViewById(R.id.top_layout).setVisibility(View.GONE);

        replaceControllerView(controllerView);
        controllerView.setPlayer(headPlayer);
        controllerView.show();
    }

    private void setupLandscapeHeadControllerView() {
        HeadPlaybackControllerView controllerView = new HeadPlaybackControllerView(context);
        controllerView.findViewById(R.id.top_layout).setVisibility(View.VISIBLE);

        replaceControllerView(controllerView);
        controllerView.setPlayer(headPlayer);
        controllerView.show();
    }

    private void setupPortraitBodyControllerView() {
        BodyPlaybackControllerView controllerView = new BodyPlaybackControllerView(context);
        controllerView.findViewById(R.id.top_layout).setVisibility(View.GONE);

        replaceControllerView(controllerView);
        controllerView.setPlayer(bodyPlayer);
        controllerView.show();
    }

    private void setupLandscapeBodyControllerView() {
        BodyPlaybackControllerView controllerView = new BodyPlaybackControllerView(context);
        controllerView.findViewById(R.id.top_layout).setVisibility(View.VISIBLE);

        replaceControllerView(controllerView);
        controllerView.setPlayer(bodyPlayer);
        controllerView.show();
    }
    //endregion

    //region 随堂练 相关
    private Handler handler = new Handler();
    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private void updateProgress() {
        long position = bodyPlayer.getCurrentPosition();
        model.headPosition = headPlayer.getCurrentPosition();
        model.bodyPosition = position;
        model.bodyDuration = bodyPlayer.getDuration();
        handler.removeCallbacks(updateProgressAction);
        int playbackState = bodyPlayer == null ? ExoPlayer.STATE_IDLE : bodyPlayer.getPlaybackState();
        if (playbackState != ExoPlayer.STATE_IDLE && playbackState != ExoPlayer.STATE_ENDED) {
            long delayMs = 1000;
            handler.postDelayed(updateProgressAction, delayMs);
        }
    }



    //endregion

    //region 移动网wifi 相关
    private boolean hasDealtWithFourG() {
        if (NetWorkUtils.isNetworkConnected(context) && !NetWorkUtils.isWifi(context)) {
            setState(VideoState.FourG);
            return true;
        }

        return false;
    }

    public void networkChangeToFourG() {
        if ((state != VideoState.Normal) && (state != VideoState.Loading)) {
            return;
        }

        setState(VideoState.FourG);
    }
    //endregion

    //region Utils
    private MediaSource mediaSourceFromUrl(String url) {
        Uri uri = Uri.parse(url);
        if (VideoTypeParser.Type.M3U8 == VideoTypeParser.getTypeForUri(uri)) {
            MediaSource m3u8 = new HlsMediaSource(uri, dataSourceFactory, mainHandler, null);
            return m3u8;
        }

        if (VideoTypeParser.Type.MP4 == VideoTypeParser.getTypeForUri(uri)) {
            MediaSource mp4 = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
            return mp4;
        }

        // 希望Extractor能捕获其余漏网之鱼
        MediaSource defaultMediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
        return defaultMediaSource;
    }
    //endregion
}