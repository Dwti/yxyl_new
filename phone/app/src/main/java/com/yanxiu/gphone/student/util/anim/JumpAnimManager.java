package com.yanxiu.gphone.student.util.anim;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.animation.BounceInterpolator;

import java.lang.ref.WeakReference;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/4 15:22.
 * Function :
 */

public class JumpAnimManager {

    private static final float DEFAULT_DISTANCE=0;
    private static final float DEFAULT_SIZE=1f;
    /**
     * 动画时间
     */
    private static final long DEFAULT_DURATION = 1000;
    /**
     * 跳跃距离
     */
    private static final float DEFAULT_TRANSLATION = 40;
    /**
     * 上跳时变大率
     */
    private static final float DEFAULT_SCALE = 1.1f;
    /**
     * 上跳时的立体感
     */
    private static final float DEFAULT_Z = 20;

    private WeakReference<View> mView;

    public static JumpAnimManager getInstence(View view){
        return new JumpAnimManager(view);
    }

    private JumpAnimManager(View view) {
        mView = new WeakReference<>(view);
        ViewCompat.animate(view).setInterpolator(new BounceInterpolator())
                .translationY(-DEFAULT_TRANSLATION)
                .z(DEFAULT_Z).scaleX(DEFAULT_SCALE)
                .scaleY(DEFAULT_SCALE)
                .setDuration(DEFAULT_DURATION)
                .setListener(new JumpPropertyAnimatorListener(null));
    }

    public JumpAnimManager setDuration(long time) {
        View view;
        if ((view = mView.get()) != null) {
            ViewCompat.animate(view).setDuration(time);
        }
        return this;
    }

    public JumpAnimManager setTranslation(float value) {
        View view;
        if ((view = mView.get()) != null) {
            ViewCompat.animate(view).translationY(value);
        }
        return this;
    }

    public JumpAnimManager setScaleX(float value) {
        View view;
        if ((view = mView.get()) != null) {
            ViewCompat.animate(view).scaleX(value);
        }
        return this;
    }

    public JumpAnimManager setScaleY(float value) {
        View view;
        if ((view = mView.get()) != null) {
            ViewCompat.animate(view).scaleY(value);
        }
        return this;
    }

    public JumpAnimManager setListaner(final JumpAnimListener listaner) {
        View view;
        if ((view = mView.get()) != null) {
            ViewCompat.animate(view).setListener(new JumpPropertyAnimatorListener(listaner));
        }
        return this;
    }

    public JumpAnimManager setZ(final float value) {
        View view;
        if ((view = mView.get()) != null) {
            ViewCompat.animate(view).z(value);
        }
        return this;
    }

    public void start() {
        View view;
        if ((view = mView.get()) != null) {
            ViewCompat.animate(view).start();
        }
    }

    public void setReset() {
        View view;
        if ((view = mView.get()) != null) {
            ViewCompat.setScaleX(view, DEFAULT_SIZE);
            ViewCompat.setScaleY(view, DEFAULT_SIZE);
            ViewCompat.setTranslationY(view,DEFAULT_DISTANCE);
        }
    }

    private class JumpPropertyAnimatorListener implements ViewPropertyAnimatorListener {
        WeakReference<JumpAnimListener> mLinstener;

        JumpPropertyAnimatorListener(JumpAnimListener listener) {
            mLinstener = new WeakReference<>(listener);
        }

        @Override
        public void onAnimationStart(View view) {
            JumpAnimListener listener;
            if ((listener = mLinstener.get()) != null) {
                listener.onAnimStart(JumpAnimManager.this,view);
            }
        }

        @Override
        public void onAnimationEnd(View view) {
            ViewCompat.setZ(view, DEFAULT_DISTANCE);
            JumpAnimListener listener;
            if ((listener = mLinstener.get()) != null) {
                listener.onAnimEnd(JumpAnimManager.this,view);
            }
        }

        @Override
        public void onAnimationCancel(View view) {
            ViewCompat.setZ(view, DEFAULT_DISTANCE);
            JumpAnimListener listener;
            if ((listener = mLinstener.get()) != null) {
                listener.onAnimCancel(JumpAnimManager.this,view);
            }
        }
    }

    public interface JumpAnimListener {
        void onAnimStart(JumpAnimManager manager, View view);

        void onAnimEnd(JumpAnimManager manager, View view);

        void onAnimCancel(JumpAnimManager manager, View view);
    }
}
