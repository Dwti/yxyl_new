package com.yanxiu.gphone.student.questions.classify;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ClassifyChoice;
import com.yanxiu.gphone.student.util.HtmlImageGetter;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

import java.util.ArrayList;

/**
 * 归类题目解析view
 * Created by 戴延枫 on 2017/7/20.
 */

public class ClassifyAnslysisItemView extends LinearLayout {
    private LayoutInflater mLayoutInflater;

    public ClassifyAnslysisItemView(Context context) {
        super(context);
        init(context);
    }

    public ClassifyAnslysisItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ClassifyAnslysisItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<ClassifyBean> list) {
        for (int i = 0; i < list.size(); i++) {
            ClassifyBean classifyBean = list.get(i);
            String titleString = classifyBean.getTitle();
            ArrayList<ClassifyItemBean> classifyItemBeanArrayList = classifyBean.getClassifyBeanArrayList();

            LinearLayout itemView = (LinearLayout) mLayoutInflater.inflate(R.layout.classify_analysis_item_layout, this, false);
            TextView title = (TextView) itemView.findViewById(R.id.classify_analysis_item_title);
            ClassifyChoice choiceView = (ClassifyChoice) itemView.findViewById(R.id.classify_analysis_choice);

            Spanned string = Html.fromHtml(titleString, new HtmlImageGetter(title), null);
            title.setText(string + "  (" + classifyItemBeanArrayList.size() + ")");
            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_MEDIUM_PLAY, title);
            choiceView.setDataForAnalysis(classifyItemBeanArrayList);
            addView(itemView);
        }
    }

}
