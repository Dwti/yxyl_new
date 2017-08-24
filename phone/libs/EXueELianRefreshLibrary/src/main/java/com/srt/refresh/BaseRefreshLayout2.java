package com.srt.refresh;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by 戴延枫 on 2017/8/1.
 */
public class BaseRefreshLayout2 extends FrameLayout {
    private Context mContext;
    //波浪的高度
    protected float mWaveHeight;

    //头部的高度
    protected float mHeadHeight;

    //子控件
    private View mChildView;

    //刷新的状态
    protected boolean isRefreshing;

    //触摸获得Y的位置
    private float mTouchY;

    //当前Y的位置
    private float mCurrentY;

    //自动收回headView的动画
    public ObjectAnimator pullReleasingOA;

    private static int MOVE_DISTANCE_LIMIT;  //被认为是滑动的最小距离


    public BaseRefreshLayout2(Context context) {
        this(context, null, 0);
    }

    public BaseRefreshLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRefreshLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mContext = context;
        //使用isInEditMode解决可视化编辑器无法识别自定义控件的问题
        MOVE_DISTANCE_LIMIT = ViewConfiguration.get(context).getScaledTouchSlop();
        if (isInEditMode()) {
            return;
        }

        if (getChildCount() > 1) {
            throw new RuntimeException("只能拥有一个子控件哦");
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //获得子控件
        mChildView = getChildAt(0);
    }

    //由于animate().setUpdateListener必须在API 19以上才能使用，故使用ObjectAnimator代替
    private void setChildViewTransLationY(float... values) {
        pullReleasingOA = ObjectAnimator.ofFloat(mChildView, View.TRANSLATION_Y, values);
        pullReleasingOA.setInterpolator(new DecelerateInterpolator());
        pullReleasingOA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) mChildView.getTranslationY();//获得mChildView当前y的位置
//                    mHeadLayout.getLayoutParams().height = height;
//                    mHeadLayout.requestLayout();//重绘
            }
        });
        pullReleasingOA.start();
    }

    /**
     * 当用户松开手后，但是还没有出发刷新时，已经显示的head自动收回的动画
     *
     * @param values
     */
    private void setPullReleasingTransLationY(float... values) {
        pullReleasingOA = ObjectAnimator.ofFloat(mChildView, View.TRANSLATION_Y, values);
        pullReleasingOA.setDuration(500);
        pullReleasingOA.setInterpolator(new DecelerateInterpolator());
        pullReleasingOA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) mChildView.getTranslationY();//获得mChildView当前y的位置

//                mHeadLayout.getLayoutParams().height = height;
//                mHeadLayout.requestLayout();//重绘
//                if (pullStateListener != null) {
//                    pullStateListener.onPullReleasing(BaseRefreshLayout2.this, height);
//                }
            }
        });
        pullReleasingOA.start();
    }

    /**
     * 拦截事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isRefreshing) return true;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                mCurrentY = mTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                float dy = currentY - mTouchY;
                if (dy > MOVE_DISTANCE_LIMIT && !canChildScrollUp()) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 响应事件
     *
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isRefreshing) {
            return super.onTouchEvent(e);
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mCurrentY = e.getY();

                float dy = mCurrentY - mTouchY;
                dy = Math.max(0, dy);
                dy = Math.min(mHeadHeight*5, dy);

                if (mChildView != null) {
                    float offsetY = dy / 8;
                    mChildView.setTranslationY(dy);
                    mFlyView.setTranslationX(offsetY);
//                    mHillView.setTranslationY(offsetY);
//                    mHead_layout.getLayoutParams().height = mHead_view_height + (int)offsetY;
                    if (mPullListener != null) {
                        mPullListener.onPulling(offsetY);
                    }
                }

                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mChildView != null) {
                    setPullReleasingTransLationY(0);
                }
                return true;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 用来判断顶部是否可以滚动
     *
     * @return boolean
     */
    public boolean canChildScrollUp() {
        if (mChildView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 14) {
            if (mChildView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mChildView;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mChildView, -1) || mChildView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mChildView, -1);
        }
    }

    /**
     * 判断底部是否可以滚动
     *
     * @return
     */
    public boolean canChildScrollDown() {
        if (mChildView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 14) {
            if (mChildView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mChildView;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mChildView, 1) || mChildView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mChildView, 1);
        }
    }


    /**
     * 设置下拉监听
     */
    private PullListener mPullListener;

    public void setPullListener(PullListener pullListener) {
        this.mPullListener = pullListener;
    }

    public interface PullListener {
        void onPulling(float offsetY);
    }

    /**
     * 设置wave的下拉高度
     *
     * @param waveHeight
     */
    public void setWaveHeight(float waveHeight) {
        this.mWaveHeight = waveHeight;
    }

    /**
     * 设置下拉头的高度
     *
     * @param headHeight
     */
    public void setHeaderHeight(float headHeight) {
//        this.mHeadHeight = headHeight;
        mFlyView.post(new Runnable() {
            @Override
            public void run() {
                mHeadHeight = mFlyView.getTop();
            }
        });
    }

    private View mHead_layout;
    private View mFlyView;
    private View mHillView;
    private int mHead_view_height;

    public void setHeadLayout(View layout){
        mHead_layout = layout;
        mHead_layout.post(new Runnable() {
            @Override
            public void run() {
                mHead_view_height = mHead_layout.getLayoutParams().height;
            }
        });
    }
    public void setFlyView(View flyView) {
        mFlyView = flyView;
    }

    public void setHillView(View hillView) {
        mHillView = hillView;
    }

}
