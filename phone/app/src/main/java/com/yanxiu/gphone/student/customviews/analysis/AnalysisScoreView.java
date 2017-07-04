package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

/**
 * 评分view
 * Created by 戴延枫 on 2017/7/3.
 */

public class AnalysisScoreView extends LinearLayout {

    private TextView mScore;


    public AnalysisScoreView(Context context) {
        super(context);
        initView(context);
    }

    public AnalysisScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public AnalysisScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.analysis_score_layout, this, false);
        addView(view);
        mScore = (TextView) view.findViewById(R.id.analysis_score);
        TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_BOLDITALIC, mScore);
    }

    /**
     * 设置评分
     *
     * @param score 评分
     */
    public void setText(String score) {
        if (mScore != null) {
            mScore.setText(score);
        }
    }
}
