package com.yanxiu.gphone.student.util.time;

import android.os.CountDownTimer;

import java.lang.ref.SoftReference;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/11 17:02.
 * Function :
 */

@SuppressWarnings("unused")
public class CountDownManager {

    private static final long DEFAULT_TOTALTIME=60000;
    private static final long DEFAULT_INTERVATIME=1000;

    public interface ScheduleListener {
        void onProgress(long progress);

        void onFinish();
    }
    /**
     * the countdown total time
     * */
    private long totalTime = DEFAULT_TOTALTIME;
    /**
     * the countdown interval time
     * */
    private long intervalTime = DEFAULT_INTERVATIME;
    private ScheduleListener listener;

    public static CountDownManager getManager() {
        return new CountDownManager();
    }

    /**
     * the totalTime in milliseconds
     * */
    public CountDownManager setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    /**
     * the intervalTime in milliseconds
     * */
    public CountDownManager setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
        return this;
    }

    public CountDownManager setScheduleListener(ScheduleListener listener) {
        this.listener=listener;
        return this;
    }

    public void start() {
        countDownTimer timer = new countDownTimer(totalTime, intervalTime, listener);
        timer.start();
    }

    private class countDownTimer extends CountDownTimer {

        private SoftReference<ScheduleListener> reference;

        countDownTimer(long millisInFuture, long countDownInterval, ScheduleListener listener) {
            super(millisInFuture, countDownInterval);
            if (listener != null) {
                reference = new SoftReference<>(listener);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (reference != null) {
                ScheduleListener listener = reference.get();
                if (listener != null) {
                    listener.onProgress(millisUntilFinished);
                }
            }
        }

        @Override
        public void onFinish() {
            if (reference != null) {
                ScheduleListener listener = reference.get();
                if (listener != null) {
                    listener.onFinish();
                }
            }
        }
    }
}

