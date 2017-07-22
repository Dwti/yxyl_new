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
    private View mScore_layout;
    private TextView mScore,mNoPigai;


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
        mScore_layout = view.findViewById(R.id.score_layout);
        mScore = (TextView) view.findViewById(R.id.analysis_score);
        mNoPigai = (TextView) view.findViewById(R.id.teacher_no_pigai);
        TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_BOLDITALIC, mScore);
    }

    /**
     * 设置评分
     *
     * @param score 评分
     */
    public void setScore(String score) {
        if (mScore != null) {
            mScore.setText(score);
            mScore_layout.setVisibility(VISIBLE);
            mNoPigai.setVisibility(GONE);
        }
    }

    /**
     * 未批改
     * @param content 要显示的内容
     */
    public void setText(String content) {
        if (mNoPigai != null) {
            mNoPigai.setText(content);
            mScore_layout.setVisibility(GONE);
            mNoPigai.setVisibility(VISIBLE);
        }
    }
}
