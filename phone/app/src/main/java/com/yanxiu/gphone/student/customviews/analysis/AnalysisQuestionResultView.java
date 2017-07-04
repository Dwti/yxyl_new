package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by 戴延枫 on 2017/7/3.
 */

public class AnalysisQuestionResultView extends LinearLayout {

    private TextView mTitle;

    public AnalysisQuestionResultView(Context context) {
        super(context);
        initView(context);
    }

    public AnalysisQuestionResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public AnalysisQuestionResultView(Context context, AttributeSet attrs) {
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
