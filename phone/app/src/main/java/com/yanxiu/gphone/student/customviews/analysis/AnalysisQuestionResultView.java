package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * 作答结果view
 * Created by 戴延枫 on 2017/7/3.
 */

public class AnalysisQuestionResultView extends LinearLayout {

    private TextView mYesNo;
    private TextView mResult;


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
        View view = LayoutInflater.from(context).inflate(R.layout.analysis_question_result_layout, this, false);
        addView(view);
        mYesNo = (TextView) view.findViewById(R.id.analysis_yes_no);
        mResult = (TextView) view.findViewById(R.id.analysis_answer_result);
    }

    /**
     * 作答结果设置text
     *
     * @param yesno      "回答错误" or "回答正确"
     * @param yourAnswer 正确答案（没有该数据请传空）
     */
    public void setText(String yesno, String yourAnswer) {
        if (mYesNo != null) {
            mYesNo.setText(yesno);
        }
        if (mResult != null && !TextUtils.isEmpty(yourAnswer)) {
            Spanned string = Html.fromHtml(yourAnswer, new HtmlImageGetter(mResult), null);
            mResult.setText(string);
            mResult.setVisibility(VISIBLE);
        }
    }
}
