package com.yanxiu.gphone.student.videoplay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.ScreenUtils;

import static com.yanxiu.gphone.student.videoplay.VideoManager.VideoState.*;

/**
 * Created by sp on 17-10-24.
 */

public class VideoActivity extends Activity{

    private VideoManager mVideoManager;
    private EventListener mListener = new EventListener();

    private VideoModel mVideoModel;

    private void setupMockData(){
//        VideoModel mVideoModel = new VideoModel();
//        m0.headUrl = "http://upload.ugc.yanxiu.com/video/4620490456e684328d4fcf5a920f54a1.mp4";
//        m0.headPosition = 0;
//        m0.bodyUrl = "http://yuncdn.teacherclub.com.cn/course/cf/2010bzr_sd/jz02_wx/video/110_l/110_l.m3u8";
        mVideoModel = new VideoModel();
        mVideoModel.bodyUrl = "http://upload.ugc.yanxiu.com/video/4620490456e684328d4fcf5a920f54a1.mp4";
        mVideoModel.bodyPosition = 0;
        mVideoModel.isHeadFinished = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        setupMockData();
        setupRotation();
        setupNetwork4GWifi();

        mVideoManager = new VideoManager(this, (PlayerView) findViewById(R.id.player_view));
        mVideoManager.setOnCourseEventListener(mListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        VideoManager.VideoState lastState = mVideoManager.getState();

        mVideoManager.setupPlayer();
        mVideoManager.setModel(mVideoModel);

        if ((lastState != Normal) && (lastState != Loading)) {
            mVideoManager.setState(lastState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoModel != null) {
            mVideoManager.recordPlayPauseState();
            mVideoManager.clearPlayer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNotification);
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
                }

                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(requestedOrientation);
                    setLandscapeStyle();
                }

                if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    setRequestedOrientation(requestedOrientation);
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(this, 250));
        mVideoManager.getPlayerView().setLayoutParams(params);
    }

    private void setLandscapeStyle() {
        mVideoManager.isPortrait = false;
        mVideoManager.updatePortraitLandscapeControllerView();
//        findViewById(R.id.play_button).setVisibility(View.GONE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mVideoManager.getPlayerView().setLayoutParams(params);
    }
    //endregion

}
