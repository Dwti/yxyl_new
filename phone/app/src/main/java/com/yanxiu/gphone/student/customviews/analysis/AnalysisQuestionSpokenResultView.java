package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/24 15:13.
 * Function :
 */
public class AnalysisQuestionSpokenResultView extends LinearLayout {

    public AnalysisQuestionSpokenResultView(Context context) {
        super(context);
        init(context);
    }

    public AnalysisQuestionSpokenResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnalysisQuestionSpokenResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.analysis_question_spoken_result_layout,this);
    }

    public void setData(int score){
        switch (score){
            case 0:
                findViewById(R.id.ll_hand).setVisibility(VISIBLE);
                findViewById(R.id.tv_no_hand).setVisibility(GONE);
                ((ImageView)findViewById(R.id.iv_hand1)).setImageResource(R.drawable.spoken_like_gry);
                ((ImageView)findViewById(R.id.iv_hand2)).setImageResource(R.drawable.spoken_like_gry);
                ((ImageView)findViewById(R.id.iv_hand3)).setImageResource(R.drawable.spoken_like_gry);
                break;
            case 1:
                findViewById(R.id.ll_hand).setVisibility(VISIBLE);
                findViewById(R.id.tv_no_hand).setVisibility(GONE);
                ((ImageView)findViewById(R.id.iv_hand1)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView)findViewById(R.id.iv_hand2)).setImageResource(R.drawable.spoken_like_gry);
                ((ImageView)findViewById(R.id.iv_hand3)).setImageResource(R.drawable.spoken_like_gry);
                break;
            case 2:
                findViewById(R.id.ll_hand).setVisibility(VISIBLE);
                findViewById(R.id.tv_no_hand).setVisibility(GONE);
                ((ImageView)findViewById(R.id.iv_hand1)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView)findViewById(R.id.iv_hand2)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView)findViewById(R.id.iv_hand3)).setImageResource(R.drawable.spoken_like_gry);
                break;
            case 3:
                findViewById(R.id.ll_hand).setVisibility(VISIBLE);
                findViewById(R.id.tv_no_hand).setVisibility(GONE);
                ((ImageView)findViewById(R.id.iv_hand1)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView)findViewById(R.id.iv_hand2)).setImageResource(R.drawable.spoken_like_red);
                ((ImageView)findViewById(R.id.iv_hand3)).setImageResource(R.drawable.spoken_like_red);
                break;
            default:
                findViewById(R.id.ll_hand).setVisibility(GONE);
                findViewById(R.id.tv_no_hand).setVisibility(VISIBLE);
                break;
        }
    }

}
