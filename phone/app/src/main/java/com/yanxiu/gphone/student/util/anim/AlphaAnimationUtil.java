package com.yanxiu.gphone.student.util.anim;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by sp on 17-8-29.
 */

public class AlphaAnimationUtil {
    public static void startAlphaAnim(View view, float from, float to, long duration,boolean fillAfter){
        AlphaAnimation alphaAnimation = new AlphaAnimation(from,to);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(fillAfter);
        view.startAnimation(alphaAnimation);
    }

    public static void startPopBgAnimIn(View view){
        startAlphaAnim(view,0.0f,1.0f,200,true);
    }

    public static void startPopBgAnimExit(View view){
        startAlphaAnim(view,1.0f,0.0f,200,true);
    }
}
