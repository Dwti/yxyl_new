package com.yanxiu.gphone.student.questions.fillblank;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;

import com.yanxiu.gphone.student.customviews.spantextview.XForegroundColorSpan;

import org.xml.sax.XMLReader;

/**
 * Created by sp on 17-6-8.
 */

public class AnalysisFillBlankTagHandler implements Html.TagHandler {

    protected int start,end;

    @Override
    public void handleTag(boolean opening, String tag, final Editable output, XMLReader xmlReader) {

        //没填的空
        if (opening && tag.toLowerCase().equals("empty")) {
            start = output.length();
        }
        if (!opening && tag.toLowerCase().equals("empty")) {
            end = output.length();
            if (start != end) {
                XForegroundColorSpan span = new XForegroundColorSpan(Color.TRANSPARENT);
                span.setTag(tag.toLowerCase());
                output.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        //答对的空
        if (opening && tag.toLowerCase().equals("right")) {
            start = output.length();
        }
        if (!opening && tag.toLowerCase().equals("right")) {
            end = output.length();
            if (start != end) {
                XForegroundColorSpan span = new XForegroundColorSpan(Color.parseColor("#89e00d"));
                span.setTag(tag.toLowerCase());
                output.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        //答错的空
        if (opening && tag.toLowerCase().equals("wrong")) {
            start = output.length();
        }
        if (!opening && tag.toLowerCase().equals("wrong")) {
            end = output.length();
            if (start != end) {
                XForegroundColorSpan span = new XForegroundColorSpan(Color.parseColor("#ff7a05"));
                span.setTag(tag.toLowerCase());
                output.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
