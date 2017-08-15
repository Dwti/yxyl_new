package com.yanxiu.gphone.student.questions.connect;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by sunpeng on 2017/8/14.
 */

public class ConnectAnimationHelper {

    private static Bitmap getDrawBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    private static RelativeLayout createAnimationLayout(Activity activity){
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(relativeLayout);
        return relativeLayout;
    }

    private static void addViewToAnimLayout(ViewGroup parent, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        parent.addView(view);
    }

    private static AnimationSet setAnim(final View basket, final View view, int[] start_location, int[] end_location) {

        int startX = start_location[0];
        int startY = start_location[1];
        int endX = end_location[0];
        int endY = end_location[1];
        TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX - startX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY - startY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.5f);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(0);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,0.2f,1.0f,0.2f);
        scaleAnimation.setInterpolator(new LinearInterpolator());
        scaleAnimation.setRepeatCount(0);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(translateAnimationY);   //位移动画不能放到缩放动画前面，否则会导致位置计算错误
        set.addAnimation(translateAnimationX);
        set.setDuration(600);
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);

                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setInterpolator(new LinearInterpolator());
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                basket.startAnimation(scaleAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);

                ScaleAnimation scaleAnimation = new ScaleAnimation(1.2f,1.0f,1.2f,1.0f,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setInterpolator(new LinearInterpolator());
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(false);
                basket.startAnimation(scaleAnimation);
            }
        });

        return set;
    }

    public static void startAnimation(Activity activity,View basket,View item,int[] start_location, int[] end_location){
        ImageView imageView = new ImageView(activity);
        imageView.setImageBitmap(getDrawBitmap(item));
        RelativeLayout relativeLayout = createAnimationLayout(activity);

        addViewToAnimLayout(relativeLayout,imageView,start_location);

        AnimationSet set = setAnim(basket,imageView,start_location,end_location);

        imageView.startAnimation(set);
    }
}
