package com.yanxiu.gphone.student.customviews.analysis;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.spantextview.BlankView;
import com.yanxiu.gphone.student.customviews.spantextview.FillBlankTextView;
import com.yanxiu.gphone.student.customviews.spantextview.OnReplaceCompleteListener;
import com.yanxiu.gphone.student.customviews.spantextview.XForegroundColorSpan;
import com.yanxiu.gphone.student.customviews.spantextview.XTextView;
import com.yanxiu.gphone.student.questions.fillblank.AnalysisFillBlankTagHandler;
import com.yanxiu.gphone.student.questions.fillblank.FillBlankTagHandler;
import com.yanxiu.gphone.student.questions.fillblank.SpanInfo;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by sp on 17-2-23.
 */

public class AnalysisFillBlankTextView extends FillBlankTextView {

    public AnalysisFillBlankTextView(Context context) {
        super(context);
    }

    public AnalysisFillBlankTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalysisFillBlankTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected BlankView getView(XForegroundColorSpan span) {
        BlankView view = new BlankView(getContext());
        if(span.getTag().equals("empty") || span.getTag().equals("wrong")){
            view.setUnderlineColor(Color.parseColor("#ff7a05"));
        }else if(span.getTag().equals("right")){
            view.setUnderlineColor(Color.parseColor("#89e00d"));
        }
        return view;
    }

    @Override
    protected Html.TagHandler getTagHandler() {
        return new AnalysisFillBlankTagHandler();
    }
}
