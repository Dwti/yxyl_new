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

    private static MediaPlayerUtil mPlayerUtil;
    private static MediaPlayer mPlayer;
    private static MyHandle mHandle;
    private int mBufferProgress;
    private static boolean isPlaying = false;
    private boolean isPause = false;
    private static MediaPlayerCallBack mCallBack;
    private MediaPlayerBufferUpdateCallBack mUpdateCallBack;

    public static MediaPlayerUtil create() {
        return mPlayerUtil = new MediaPlayerUtil();
    }

    public void start(String url) {
        if (isPlaying|| TextUtils.isEmpty(url)) {
            return;
        }
        Logger.d(TAG, "start");
        mPlayer = new MediaPlayer();
        mHandle = new MyHandle();
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
            e.printStackTrace();
        }
    }

    private static class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.d(TAG, "media  handle");
            if (isPlaying) {
                if (mCallBack != null) {
                    int currentPosition = mPlayer.getCurrentPosition();
                    mCallBack.onProgress(mPlayerUtil, currentPosition);
                }
            }
            if (mHandle != null) {
                mHandle.sendEmptyMessageDelayed(0, delayMillis);
            }
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * move
     */
    public void seekTo(final int msec) {
        if (mPlayer != null) {
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
        if (mPlayer != null) {
            try {
                isPlaying = false;
                isPause = true;
                restore();
                mPlayer.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        if (mPlayer != null) {
            try {
                isPlaying = true;
                isPause = false;
                restore();
                mPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This action can restore the current progress
     */
    private void restore() {
        if (mCallBack != null && mPlayer != null) {
            int currentPosition = mPlayer.getCurrentPosition();
            mCallBack.onProgress(mPlayerUtil, currentPosition);
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
        mHandle = null;
    }

    /**
     * Destruction of data
     */
    public void destory() {
        finish();
        mCallBack = null;
        isPlaying = false;
        this.isPause = false;
        this.mUpdateCallBack = null;
        this.mBufferProgress = 0;
    }

    public void addMediaPlayerCallBack(MediaPlayerCallBack callBack) {
        mCallBack = callBack;
    }

    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Logger.d(TAG, "media  prepared");
            if (mp != null) {
                mp.start();
                isPlaying = true;
                int duration = mp.getDuration();
                if (mCallBack != null) {
                    mCallBack.onStart(mPlayerUtil, duration);
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
            if (mCallBack != null && isPlaying) {
                isPlaying = false;
                mCallBack.onCompletion(mPlayerUtil);
            }
            finish();
            mBufferProgress = 0;
        }
    };

    private MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Logger.d(TAG, "media  error");
            if (mCallBack != null && isPlaying) {
                isPlaying = false;
                mCallBack.onError(mPlayerUtil);
            }
            finish();
            mBufferProgress = 0;
            return true;
        }
    };
}
