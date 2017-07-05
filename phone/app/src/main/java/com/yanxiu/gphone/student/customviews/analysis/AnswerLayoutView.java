package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * 答案
 * Created by Canghaixiao.
 * Time : 2017/7/5 10:01.
 * Function :
 */
public class AnswerLayoutView extends RelativeLayout {

    private TextView mAnswerView;

    public AnswerLayoutView(Context context) {
        this(context,null);
    }

    public AnswerLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AnswerLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layoutview_answer,this);
        mAnswerView= (TextView) findViewById(R.id.tv_answer);
    }

    public void setText(String text){
        Spanned string= Html.fromHtml(text,new HtmlImageGetter(mAnswerView),null);
        mAnswerView.setText(string);
    }

}
