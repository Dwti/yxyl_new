package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;

/**
 * 难度view
 * Created by 戴延枫 on 2017/7/3.
 */

public class AnalysisDifficultyView extends LinearLayout {

    private ImageView mOne, mTwo, mThree, mFour, mFive;


    public AnalysisDifficultyView(Context context) {
        super(context);
        initView(context);
    }

    public AnalysisDifficultyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public AnalysisDifficultyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.analysis_difficulty_layout, this, false);
        addView(view);
        mOne = (ImageView) view.findViewById(R.id.one);
        mTwo = (ImageView) view.findViewById(R.id.two);
        mThree = (ImageView) view.findViewById(R.id.three);
        mFour = (ImageView) view.findViewById(R.id.four);
        mFive = (ImageView) view.findViewById(R.id.five);
    }

    /**
     * 设置评分
     *
     * @param score 评分
     */
    public void setScore(int score) {
        switch(score){
            case 1:
                mOne.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                break;
            case 2:
                mOne.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mTwo.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                break;
            case 3:
                mOne.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mTwo.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mThree.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                break;
            case 4:
                mOne.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mTwo.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mThree.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mFour.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                break;
            case 5:
                mOne.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mTwo.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mThree.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mFour.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                mFive.setImageDrawable(getResources().getDrawable(R.drawable.rathing_src));
                break;
            default:
                mOne.setImageDrawable(null);
                mTwo.setImageDrawable(null);
                mThree.setImageDrawable(null);
                mFour.setImageDrawable(null);
                mFive.setImageDrawable(null);
                break;
        }
    }
}
