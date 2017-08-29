package com.yanxiu.gphone.student.util.anim;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.view.View;

import com.yanxiu.gphone.student.util.ScreenUtils;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/29 12:16.
 * Function :
 */
public class TranslationYAnimUtil {

    private static final int DEFAULT_DURATION = 200;

    private static TranslationYAnimUtil translationYAnimUtil;
    private TranslationAnim translationAnim = new TranslationAnim();
    private TranslationAnimUpdata translationAnimUpdata = new TranslationAnimUpdata();
    private int mTranslationY = 0;
    private int mDuration = 0;

    private TranslationYAnimUtil() {
        translationAnim.clear();
        translationAnimUpdata.clear();
    }

    public static TranslationYAnimUtil getInstence() {
        translationYAnimUtil = new TranslationYAnimUtil();
        return translationYAnimUtil;
    }

    public TranslationYAnimUtil setAnimViewHeight(Context context, @DimenRes int resId) {
        mTranslationY = context.getResources().getDimensionPixelSize(resId);
        int windowHeight = ScreenUtils.getScreenHeight(context);
        mDuration = DEFAULT_DURATION;
        mDuration = mDuration * (windowHeight / mTranslationY);
        return translationYAnimUtil;
    }

    public TranslationYAnimUtil setDuration(Context context, int duration) {
        this.mDuration = duration;
        int windowHeight = ScreenUtils.getScreenHeight(context);
        mDuration = mDuration * (windowHeight / mTranslationY);
        return translationYAnimUtil;
    }

    public TranslationYAnimUtil setBgGradation(View view, float start, float end) {
        translationAnimUpdata.setParame(view, start, end, mTranslationY);
        translationAnim.setParame(view, start, end,translationAnimUpdata);
        return translationYAnimUtil;
    }

    public void setStartAnim(View view) {
        ViewCompat.setTranslationY(view, mTranslationY);

        ViewCompat.animate(view)
                .translationY(0)
                .setDuration(mDuration)
                .setUpdateListener(translationAnimUpdata)
                .setListener(translationAnim);
    }

    private static class TranslationAnim implements ViewPropertyAnimatorListener {

        private View mWindowView;
        private TranslationAnimUpdata mAnimUpdata;
        private float mStart;
        private float mEnd;

        private TranslationAnim() {

        }

        private void setParame(View view, float start, float end,TranslationAnimUpdata animUpdata) {
            this.mWindowView = view;
            this.mAnimUpdata=animUpdata;
            this.mStart = start;
            this.mEnd = end;
        }

        public void clear() {
            this.mWindowView = null;
            this.mStart = 0f;
            this.mEnd = 0f;
        }

        @Override
        public void onAnimationStart(View view) {
            if (mWindowView != null) {
                mWindowView.setAlpha(mStart);
            }
        }

        @Override
        public void onAnimationEnd(View view) {
            if (mWindowView != null) {
                mWindowView.setAlpha(mEnd);
            }
            clear();
            mAnimUpdata.clear();
        }

        @Override
        public void onAnimationCancel(View view) {
            clear();
            mAnimUpdata.clear();
        }
    }

    private static class TranslationAnimUpdata implements ViewPropertyAnimatorUpdateListener {

        private View mWindowView;
        private float mStart;
        private float mEnd;
        private int mTranslationY;

        private TranslationAnimUpdata() {

        }

        private void setParame(View view, float start, float end, int translationY) {
            this.mWindowView = view;
            this.mStart = start;
            this.mEnd = end;
            this.mTranslationY = translationY;
        }

        public void clear() {
            this.mWindowView = null;
            this.mStart = 0f;
            this.mEnd = 0f;
            this.mTranslationY = 0;
        }

        @Override
        public void onAnimationUpdate(final View view) {
            if (mWindowView != null) {
                int translationY = (int) view.getTranslationY();
//                        Logger.d("anim",translationY+"");
                float ratio = ((float) Math.abs(mTranslationY) - (float) Math.abs(translationY)) / (float) Math.abs(mTranslationY);
//                        Logger.d("anim",(mEnd - mStart) * ratio + mStart+"");
                mWindowView.setAlpha((mEnd - mStart) * ratio + mStart);
            }
        }
    }

}
