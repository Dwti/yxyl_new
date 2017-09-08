package com.yanxiu.gphone.student.questions.classify;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ClassifyChoice;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 归类抽屉view
 *
 * @author dyf
 */
public class ClassifyDrawerLayout extends FrameLayout implements OnClickListener {

    private Activity activity;
    private ViewGroup mViewGroup;
    private ClassifyChoice mClassify_choice;
    private ImageView mCircle_icon;
    private TextView mTitle, mNoAnswer;

    private LayoutInflater mInflater;
    private RelativeLayout mRootLayout;
    private Integer mDuration = 200;//展开/关闭布局执行的时间
    private int mHeight;//高度;
    private ValueAnimator mOpenAnimator, mCloseAnimator;
    private boolean isExpand = false;//题干是否已经完全展开了

    public ClassifyDrawerLayout(Context context) {
        this(context, null);
    }

    public ClassifyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;
        mInflater = LayoutInflater.from(context);
        mHeight = ScreenUtils.getScreenHeight(context);
        initView();
    }

    private void initView() {
        mRootLayout = (RelativeLayout) mInflater.inflate(R.layout.classify_drawer_layout, null);
        View mFlQa= mRootLayout.findViewById(R.id.v_click);
        RelativeLayout mChioceContainer= (RelativeLayout) mRootLayout.findViewById(R.id.chioce_container);
        mClassify_choice = (ClassifyChoice) mRootLayout.findViewById(R.id.classify_choice);
        mCircle_icon = (ImageView) mRootLayout.findViewById(R.id.circle_icon);
        mTitle = (TextView) mRootLayout.findViewById(R.id.classfy_drawer_title);
        mNoAnswer = (TextView) mRootLayout.findViewById(R.id.classfy_drawer_nodata);
        mCircle_icon.setOnClickListener(this);
        mFlQa.setOnClickListener(this);
        mChioceContainer.setOnClickListener(this);
        addView(mRootLayout);
        setBackgroundResource(R.color.color_66000000);
    }

    /**
     * 显示
     */
    public void show(String title, ArrayList<String> choiceList, List<String> classfyItem, ClassifyChoice.OnClassifyChoiceItemLitener clickLitener) {
        try {
            mTitle.setText(title);
            if (null == classfyItem || classfyItem.isEmpty()) {
//                mNoAnswer.setVisibility(VISIBLE);
                mClassify_choice.setVisibility(GONE);
            } else {
                mClassify_choice.setDataForDrawer(choiceList, classfyItem, clickLitener);
                mClassify_choice.setVisibility(VISIBLE);
//                mNoAnswer.setVisibility(GONE);
            }
            mViewGroup = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
            mViewGroup.addView(this);
            LayoutParams lp = (LayoutParams) getLayoutParams();
            lp.width = LayoutParams.MATCH_PARENT;
            lp.height = LayoutParams.MATCH_PARENT;
            setLayoutParams(lp);
            setVisibility(View.VISIBLE);
            expand();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击叉号，要隐藏掉整个布局
     */
    public void dismiss() {
        if (mOpenAnimator != null) {
            mOpenAnimator.cancel();
        }
        if (mCloseAnimator != null) {
            mCloseAnimator.cancel();
        }
        setVisibility(View.GONE);
        mViewGroup.removeView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_click:
            case R.id.circle_icon:
                collapse();
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mOpenAnimator != null) {
            mOpenAnimator.cancel();
        }
        if (mCloseAnimator != null) {
            mCloseAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    /**
     * 执行展开布局操作
     */
    public void expand() {
        if (!isExpand)
            openValueAnimator(mRootLayout).setDuration(mDuration).start();
    }

    /**
     * 执行关闭布局操作
     */
    public void collapse() {
        if (isExpand)
            closeValueAnimator(mRootLayout).setDuration(mDuration).start();
    }

    /**
     * 关闭布局执行的动画
     *
     * @return
     */
    private ValueAnimator closeValueAnimator(final View v) {
        final int targetHeight = mHeight;
        mOpenAnimator = ValueAnimator.ofFloat(1, 0);
        mOpenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolatedTime = animation.getAnimatedFraction();
                v.setY((int) (targetHeight * interpolatedTime));
//                v.requestLayout();
                if (interpolatedTime == 1) {
                    isExpand = false;
                    dismiss();
                }
            }
        });
        return mOpenAnimator;
    }

    /**
     * 展开布局执行的动画
     *
     * @return
     */
    private ValueAnimator openValueAnimator(final View v) {
        final int initialHeight = mHeight;
        mCloseAnimator = ValueAnimator.ofFloat(0, 1);
        mCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolatedTime = animation.getAnimatedFraction();
                v.setY(initialHeight - (int) (initialHeight * interpolatedTime));
//                v.requestLayout();
                if (interpolatedTime == 1) {
                    isExpand = true;
                }
            }
        });
        return mCloseAnimator;
    }

}
