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
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by sunpeng on 2017/8/14.
 */

public class ConnectAnimationHelper {

    /**
     * 这写法真逗，遗患无穷
     * */
    public static Bitmap getDrawBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 换种写法,留作纪念
     * */
    public static Bitmap getDrawBitmap2(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return Bitmap.createBitmap(bitmap);
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

    private static AnimationSet setAnim(final ViewGroup viewGroup, final View basket, final View view, int[] start_location, int[] end_location) {

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
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setInterpolator(new LinearInterpolator());
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                basket.startAnimation(scaleAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //  Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(view); // 此处不能设置view.setVisibile(GONE) 会因为Bitmap被回收而崩溃，原因不明
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.2f,1.0f,1.2f,1.0f,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setInterpolator(new LinearInterpolator());
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(false);
                basket.startAnimation(scaleAnimation);
            }
        });

        return set;
    }

    public static void startDropIntoBasketAnimation(Activity activity, View basket, View item, int[] start_location, int[] end_location){
        ImageView imageView = new ImageView(activity);
        imageView.setImageBitmap(getDrawBitmap2(item));
        RelativeLayout relativeLayout = createAnimationLayout(activity);

        addViewToAnimLayout(relativeLayout,imageView,start_location);

        AnimationSet set = setAnim(relativeLayout,basket,imageView,start_location,end_location);

        imageView.startAnimation(set);
    }

    public static void startDeleteAnimation(View view, float fromDegrees, float toDegrees, float pivotX, float pivotY, Animation.AnimationListener listener){
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees,toDegrees,Animation.RELATIVE_TO_SELF,pivotX,Animation.RELATIVE_TO_SELF,pivotY);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setFillAfter(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(350);
        animationSet.setAnimationListener(listener);
        view.startAnimation(animationSet);
    }
}
