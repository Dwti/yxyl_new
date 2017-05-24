package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by 戴延枫 on 2017/5/23.
 * 答题页面的计时view
 */

public class QuestionTimeTextView extends LinearLayout {

    private Context mContext;
    private LinearLayout mHourLayout;//小时布局
    private LinearLayout mMinuteLayout;//分钟布局
    private LinearLayout mSecondLayout;//秒钟布局
    private TextView mHourLeft, mHour;//小时
    private TextView mMinuteLeft, mMinute;//分钟
    private TextView mSecondLeft, mSecond;//秒钟


    public QuestionTimeTextView(Context context) {
        this(context, null);
    }

    public QuestionTimeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuestionTimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化view
     */
    private void initView(Context context) {
        mContext = context;
        inflate(context, R.layout.question_time_layout, this);
        mHourLayout = (LinearLayout) findViewById(R.id.hourlayout);
        mMinuteLayout = (LinearLayout) findViewById(R.id.minutelayout);
        mSecondLayout = (LinearLayout) findViewById(R.id.secondlayout);
        mHourLeft = (TextView) findViewById(R.id.hourleft);
        mHour = (TextView) findViewById(R.id.hour);
        mMinuteLeft = (TextView) findViewById(R.id.minuteleft);
        mMinute = (TextView) findViewById(R.id.minute);
        mSecondLeft = (TextView) findViewById(R.id.secondleft);
        mSecond = (TextView) findViewById(R.id.second);
    }


    /**
     * 设置时间
     *
     * @param totalSeconds 单位为秒
     */
    public void setTime(int totalSeconds) {
        if (totalSeconds < 0)
            return;
//        if (totalSeconds < 60) { //小于一分钟，显示秒钟
//            if (totalSeconds < 10) {
//                mSecond.setText(String.valueOf(totalSeconds));
//                mSecondLeft.setText(String.valueOf(0));
//            } else {
//                int secondLeft = totalSeconds / 10;
//                int second = totalSeconds % 10;
//                mSecond.setText(String.valueOf(second));
//                mSecondLeft.setText(String.valueOf(secondLeft));
//            }
//        } else
        if (totalSeconds < 3600) { //小于一小时，显示分钟
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;

            int minuteLeft = minutes / 10;
            int minute = minutes % 10;

            int secondLeft = seconds / 10;
            int second = seconds % 10;

            mMinuteLayout.setVisibility(View.VISIBLE);
            mMinuteLeft.setText(String.valueOf(minuteLeft));
            mMinute.setText(String.valueOf(minute));
            mSecondLeft.setText(String.valueOf(secondLeft));
            mSecond.setText(String.valueOf(second));

        } else { //大于等于一小时，显示时钟
            int hours = totalSeconds / 3600;
            int minutes = totalSeconds % 3600 / 60;
            int seconds = totalSeconds % 60;

            int hourLeft = hours / 10;
            int hour = hours % 10;

            int minuteLeft = minutes / 10;
            int minute = minutes % 10;

            int secondLeft = seconds / 10;
            int second = seconds % 10;
            mHourLayout.setVisibility(VISIBLE);

            mHourLeft.setText(String.valueOf(hourLeft));
            mHour.setText(String.valueOf(hour));
            mMinuteLayout.setVisibility(View.VISIBLE);
            mMinuteLeft.setText(String.valueOf(minuteLeft));
            mMinute.setText(String.valueOf(minute));
            mSecondLeft.setText(String.valueOf(secondLeft));
            mSecond.setText(String.valueOf(second));
        }
    }

}
