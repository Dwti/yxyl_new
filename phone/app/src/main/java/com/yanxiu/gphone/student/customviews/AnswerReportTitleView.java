package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by dyf
 */
public class AnswerReportTitleView extends LinearLayout {

    private TextView mTitle;

    public AnswerReportTitleView(Context context) {
        super(context);
        initView(context);
    }

    public AnswerReportTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public AnswerReportTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.answer_report_title_layout, this, true);
        mTitle = (TextView) view.findViewById(R.id.titleview);
    }

    public void setTitleText(CharSequence text) {
        if (mTitle != null) {
            mTitle.setText(text);
        }
    }

}
