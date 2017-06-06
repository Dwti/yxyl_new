package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;


/**
 * Created by Canghaixiao.
 * Time : 2017/1/19 17:07.
 * Function :
 */

public class MyMaxHeightLinearLayout extends LinearLayout {

    private int mMaxheight = -1;

    private boolean mCanChangeHeight;

    public MyMaxHeightLinearLayout(Context context) {
        this(context, null);
    }

    public MyMaxHeightLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyMaxHeightLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyMaxHeightLinearLayout);
        mMaxheight = array.getDimensionPixelSize(R.styleable.MyMaxHeightLinearLayout_maxHeight, -1);
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int height = b - t;
        if (mMaxheight < height && !mCanChangeHeight) {
            //当高度大于最大高度时，永远设置为mMaxheight
            super.onLayout(changed, l, t, r, t + mMaxheight);
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }

    /**
     * 调用此方法后，maxHeight无效，使得该view可以改变高度--在复合题使用
     */
    public void setCanChangeHeight() {
        mCanChangeHeight = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!mCanChangeHeight) {
            int height_mode = MeasureSpec.getMode(heightMeasureSpec);
            int height_size = MeasureSpec.getSize(heightMeasureSpec);
            int should_height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child_view = getChildAt(i);
                LayoutParams params = (LayoutParams) child_view.getLayoutParams();
                child_view.measure(widthMeasureSpec, 0);
                int height = child_view.getMeasuredHeight() <= params.height ? params.height : child_view.getMeasuredHeight();
                should_height += height;
            }

            int padding_top = this.getPaddingTop();
            int padding_bottom = this.getPaddingBottom();

            should_height += padding_top;
            should_height += padding_bottom;

            if (mMaxheight != -1) {
                //当高度大于最大高度时，永远设置为mMaxheight
                height_size = should_height <= mMaxheight ? should_height : mMaxheight;
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height_size, height_mode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
