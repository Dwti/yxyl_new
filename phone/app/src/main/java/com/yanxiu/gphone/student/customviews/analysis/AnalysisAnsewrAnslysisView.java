package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * 题目解析view
 * Created by 戴延枫 on 2017/7/4.
 */

public class AnalysisAnsewrAnslysisView extends LinearLayout {

    private TextView mAnalysis;


    public AnalysisAnsewrAnslysisView(Context context) {
        super(context);
        initView(context);
    }

    public AnalysisAnsewrAnslysisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public AnalysisAnsewrAnslysisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.analysis_answer_analysis_layout, this, false);
        addView(view);
        mAnalysis = (TextView) view.findViewById(R.id.analysis_answer_analysis);
    }

    /**
     * 题目解析
     *
     * @param analysis 解析
     */
    public void setText(String analysis) {
        if (mAnalysis != null) {
            Spanned string = Html.fromHtml(analysis, new HtmlImageGetter(mAnalysis), null);
            mAnalysis.setText(string);
        }
    }
}
