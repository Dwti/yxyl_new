package com.yanxiu.gphone.student.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/9 16:46.
 * Function :
 */
public class MediaPlayerUtil {

    public interface MediaPlayerCallBack {
        void onStart(MediaPlayerUtil mpu, int duration);

        void onProgress(MediaPlayerUtil mu, int progress);

        void onCompletion(MediaPlayerUtil mu);

        void onError(MediaPlayerUtil mu);
    }

    private interface MediaPlayerBufferUpdateCallBack {
        void onUpdate(int percent);
    }

    private static final String TAG = "media_player";
    private static final long delayMillis = 1000;

    private MediaPlayer mPlayer;
    private MyHandle mHandle;
    private int mBufferProgress=-1;
    private boolean isPlaying = false;
    private boolean isPause = false;
    private boolean isPrepared=false;
    private MediaPlayerCallBack mCallBack;
    private MediaPlayerBufferUpdateCallBack mUpdateCallBack;

    public static MediaPlayerUtil create() {
        return new MediaPlayerUtil();
    }

    public void start(String url) {
        if (isPlaying|| TextUtils.isEmpty(url)) {
            return;
        }
        isPrepared=false;
        Logger.d(TAG, "start");
        mPlayer = new MediaPlayer();
        mHandle = new MyHandle(MediaPlayerUtil.this);
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(url);
            mPlayer.setOnPreparedListener(preparedListener);
            mPlayer.setOnSeekCompleteListener(seekCompleteListener);
            mPlayer.setOnCompletionListener(completionListener);
            mPlayer.setOnErrorListener(errorListener);
            mPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
            mPlayer.prepareAsync();
        } catch (Exception e) {
            Logger.d(TAG, e.toString());
            e.printStackTrace();
        }
    }

    private static class MyHandle extends Handler {

        private MediaPlayerUtil playerUtil;

        MyHandle(MediaPlayerUtil playerUtil){
            this.playerUtil=playerUtil;
        }

        public void setClear(){
            playerUtil=null;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.d(TAG, "media  handle");
            if (playerUtil!=null&&playerUtil.isPlaying()) {
                if (playerUtil!=null&&playerUtil.getCallBack() != null) {
                    int currentPosition = playerUtil.getPlayer().getCurrentPosition();
                    playerUtil.getCallBack().onProgress(playerUtil, currentPosition);
                }
            }
            if (playerUtil!=null&&playerUtil.getHandle() != null) {
                playerUtil.getHandle().sendEmptyMessageDelayed(0, delayMillis);
            }
        }
    }

    private MyHandle getHandle(){
        return mHandle;
    }

    private MediaPlayer getPlayer(){
        return mPlayer;
    }

    private MediaPlayerCallBack getCallBack(){
        return mCallBack;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * move
     */
    public void seekTo(final int msec) {
        if (mPlayer != null&&isPrepared) {
            try {
                isPlaying = false;
                mPlayer.pause();
                final int seekProgress = (int) ((msec * 1f / (mPlayer.getDuration() * 1f)) * 100) + 1;
                if (mBufferProgress == 100 || seekProgress < mBufferProgress) {
                    mPlayer.seekTo(msec);
                } else {
                    mUpdateCallBack = new MediaPlayerBufferUpdateCallBack() {
                        @Override
                        public void onUpdate(int percent) {
                            Logger.d(TAG, "onupdate");
                            if (mBufferProgress != 100 && seekProgress < mBufferProgress) {
                                Logger.d(TAG, "seek");
                                mPlayer.seekTo(msec);
                                mUpdateCallBack = null;
                            } else if (mBufferProgress == 100) {
                                mPlayer.seekTo(msec);
                                mUpdateCallBack = null;
                            }
                        }
                    };
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (mPlayer != null&&isPrepared) {
            try {
                isPlaying = false;
                isPause = true;
                mPlayer.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        if (mPlayer != null&&isPrepared) {
            try {
                isPlaying = true;
                isPause = false;
                mPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void finish() {
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.reset();
                mPlayer.release();
                mPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mHandle!=null) {
            mHandle.setClear();
            mHandle = null;
        }
    }

    public void playFinish(){
        isPlaying=false;
        finish();
    }

    /**
     * Destruction of data
     */
    public void destory() {
        finish();
        this.isPlaying=false;
        this.mCallBack = null;
        this.isPause = false;
        this.mUpdateCallBack = null;
    }

    public void addMediaPlayerCallBack(MediaPlayerCallBack callBack) {
        mCallBack = callBack;
    }

    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Logger.d(TAG, "media  prepared");
            if (mp != null) {
                isPrepared=true;
                isPlaying = true;
                int duration = mp.getDuration();
                mp.start();
                if (mCallBack != null) {
                    mCallBack.onStart(MediaPlayerUtil.this, duration);
                }
                if (mHandle != null) {
                    mHandle.sendEmptyMessageDelayed(0, delayMillis);
                }
            }
        }
    };

    private MediaPlayer.OnSeekCompleteListener seekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Logger.d(TAG, "media  seek completion");
            if (mp != null) {
                if (!isPause) {
                    isPlaying = true;
                    mp.start();
                }
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Logger.d(TAG, "media  buffering update" + percent);
            mBufferProgress = percent;
            if (mUpdateCallBack != null) {
                mUpdateCallBack.onUpdate(percent);
            }
        }
    };

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Logger.d(TAG, "media  completion");
            finish();
            mBufferProgress = 0;
            if (mCallBack != null && isPlaying) {
                isPlaying = false;
                mCallBack.onCompletion(MediaPlayerUtil.this);
            }
        }
    };

    private MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Logger.d(TAG, "media  error");
            finish();
            mBufferProgress = 0;
            if (mCallBack != null) {
                isPlaying = false;
                mCallBack.onError(MediaPlayerUtil.this);
            }
            return true;
        }
    };
}
