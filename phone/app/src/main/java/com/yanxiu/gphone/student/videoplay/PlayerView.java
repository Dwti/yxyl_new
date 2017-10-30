package com.yanxiu.gphone.student.videoplay;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.yanxiu.gphone.student.R;

import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.*;
import static com.yanxiu.gphone.student.videoplay.PlaybackControllerView.DEFAULT_SHOW_TIMEOUT_MS;


/**
 * Created by cailei on 04/07/2017.
 */

public class PlayerView extends FrameLayout {
    private SimpleExoPlayer player;
    private ComponentListener componentListener = new ComponentListener();
    private VideoManager.VideoState state;
    private SurfaceView surfaceView;
    private View loadingView;
    private View networkErrorView;
    private View notFoundErrorView;
    private View lastVideoFinishedView;
    private View fourGView;
    private View playErrorControlBack,fourGErrorControlBack,networkErrorControlBack;
    private PlaybackControllerView controllerView;
    public boolean isPortrait = true;

    private Context context;
    private AspectRatioFrameLayout contentFrame;

    public PlayerView(@NonNull Context context) {
        super(context);
        setup(context);
    }

    public PlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public PlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    private void setup(Context context) {
        this.context = context;

        int playerLayoutId = R.layout.player_view;
        int resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        LayoutInflater.from(context).inflate(playerLayoutId, this);

        contentFrame = (AspectRatioFrameLayout) findViewById(R.id.content_frame);
        if (contentFrame != null) {
            contentFrame.setResizeMode(resizeMode);
        }

        if (contentFrame != null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            surfaceView = new SurfaceView(context);
            surfaceView.setLayoutParams(params);
            contentFrame.addView(surfaceView, 0);
        }

        loadingView = findViewById(R.id.loading_view);
        networkErrorView = findViewById(R.id.network_error_view);
        notFoundErrorView = findViewById(R.id.not_found_error_view);
        lastVideoFinishedView = findViewById(R.id.last_video_finished_view);
        fourGView = findViewById(R.id.four_g_view);

        playErrorControlBack = findViewById(R.id.play_error_control_back);
        fourGErrorControlBack = findViewById(R.id.four_g_error_control_back);
        networkErrorControlBack = findViewById(R.id._network_error_control_back);
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SimpleExoPlayer player) {
        if (this.player == player) {
            return;
        }

        if (this.player != null) {
            this.player.clearVideoListener(componentListener);
            this.player.clearVideoSurfaceView((SurfaceView) surfaceView);
        }

        this.player = player;
        if (player != null) {
            player.setVideoSurfaceView((SurfaceView) surfaceView);
            player.setVideoListener(componentListener);
        }
    }

    public void setControllerView(PlaybackControllerView controllerView) {
        this.controllerView = controllerView;
    }

    public void setVideoState(VideoManager.VideoState state) {
        this.state = state;

        if (state == SuiTangLian) {
            return;
        }

        if (state == Normal) {
            loadingView.setVisibility(INVISIBLE);
            networkErrorView.setVisibility(INVISIBLE);
            notFoundErrorView.setVisibility(INVISIBLE);
            lastVideoFinishedView.setVisibility(INVISIBLE);
            fourGView.setVisibility(INVISIBLE);
        }

        if (state == Loading) {
            loadingView.setVisibility(VISIBLE);
            networkErrorView.setVisibility(INVISIBLE);
            notFoundErrorView.setVisibility(INVISIBLE);
            lastVideoFinishedView.setVisibility(INVISIBLE);
            fourGView.setVisibility(INVISIBLE);
        }

        if (state == NetworkError) {
            loadingView.setVisibility(INVISIBLE);
            networkErrorView.setVisibility(VISIBLE);
            notFoundErrorView.setVisibility(INVISIBLE);
            lastVideoFinishedView.setVisibility(INVISIBLE);
            fourGView.setVisibility(INVISIBLE);
            if(isPortrait){
               networkErrorControlBack.setVisibility(INVISIBLE);
            }else {
                networkErrorControlBack.setVisibility(VISIBLE);
            }
        }

        if (state == NotFoundError) {
            loadingView.setVisibility(INVISIBLE);
            networkErrorView.setVisibility(INVISIBLE);
            notFoundErrorView.setVisibility(VISIBLE);
            lastVideoFinishedView.setVisibility(INVISIBLE);
            fourGView.setVisibility(INVISIBLE);
            if(isPortrait){
                playErrorControlBack.setVisibility(INVISIBLE);
            }else {
                playErrorControlBack.setVisibility(VISIBLE);
            }
        }

        if (state == LastVideoFinished) {
            loadingView.setVisibility(INVISIBLE);
            networkErrorView.setVisibility(INVISIBLE);
            notFoundErrorView.setVisibility(INVISIBLE);
            lastVideoFinishedView.setVisibility(VISIBLE);
            fourGView.setVisibility(INVISIBLE);
        }

        if (state == FourG) {
            loadingView.setVisibility(INVISIBLE);
            networkErrorView.setVisibility(INVISIBLE);
            notFoundErrorView.setVisibility(INVISIBLE);
            lastVideoFinishedView.setVisibility(INVISIBLE);
            fourGView.setVisibility(VISIBLE);
            if(isPortrait){
                fourGErrorControlBack.setVisibility(INVISIBLE);
            }else {
                fourGErrorControlBack.setVisibility(VISIBLE);
            }
        }
    }

    public void setControlBackVisibility(){

        if (state == NetworkError) {
            if(isPortrait){
                networkErrorControlBack.setVisibility(INVISIBLE);
            }else {
                networkErrorControlBack.setVisibility(VISIBLE);
            }
        }

        if (state == NotFoundError) {
            if(isPortrait){
                playErrorControlBack.setVisibility(INVISIBLE);
            }else {
                playErrorControlBack.setVisibility(VISIBLE);
            }
        }

        if (state == FourG) {
            if(isPortrait){
                fourGErrorControlBack.setVisibility(INVISIBLE);
            }else {
                fourGErrorControlBack.setVisibility(VISIBLE);
            }
        }
    }

    private final class ComponentListener implements SimpleExoPlayer.VideoListener {
        // SimpleExoPlayer.VideoListener implementation

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                       float pixelWidthHeightRatio) {
            if (contentFrame != null) {
                float aspectRatio = height == 0 ? 1 : (width * pixelWidthHeightRatio) / height;
                contentFrame.setAspectRatio(aspectRatio);
            }
        }

        @Override
        public void onRenderedFirstFrame() {
        }
    }

    //region 显示controlview与否
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (player == null || ev.getActionMasked() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        if (!controllerView.isVisible()) {
            maybeShowController(true);
        } else {
            controllerView.hide();
        }
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (player == null) {
            return false;
        }
        maybeShowController(true);
        return true;
    }

    private void maybeShowController(boolean isForced) {
        if (player == null) {
            return;
        }

        if ((state != Normal) && (state != Loading)) {
            return;
        }

        int playbackState = player.getPlaybackState();
        boolean showIndefinitely = playbackState == ExoPlayer.STATE_IDLE
                || playbackState == ExoPlayer.STATE_ENDED || !player.getPlayWhenReady();
        boolean wasShowingIndefinitely = controllerView.isVisible() && controllerView.getShowTimeoutMs() <= 0;
        //controllerView.setShowTimeoutMs(showIndefinitely ? 0 : DEFAULT_SHOW_TIMEOUT_MS);
        controllerView.setShowTimeoutMs(DEFAULT_SHOW_TIMEOUT_MS);
        if (isForced || showIndefinitely || wasShowingIndefinitely) {
            controllerView.show();
        }
    }
    //endregion
}
