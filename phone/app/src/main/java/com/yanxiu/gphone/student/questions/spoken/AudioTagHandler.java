package com.yanxiu.gphone.student.questions.spoken;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.SpokenAnimDrawable;
import com.yanxiu.gphone.student.util.ScreenUtils;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import java.lang.reflect.Field;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/23 15:04.
 * Function :
 */
public class AudioTagHandler implements Html.TagHandler {

    public interface UrlCallback {
        void urlCallback(String url);
    }

    private Context mContext;
    private View mView;
    private ClickableImageSpan.onSpanClickListener mSpanClickListener;
    private ClickableImageSpan mImageSpan;

    private UrlCallback mUrlCallback;

    public AudioTagHandler(Context context, View view, ClickableImageSpan.onSpanClickListener spanClickListener) {
        this.mContext = context;
        this.mView = view;
        this.mSpanClickListener = spanClickListener;
    }

    public AudioTagHandler(Context context, View view, UrlCallback urlCallback) {
        this.mContext = context;
        this.mView = view;
        this.mUrlCallback = urlCallback;
    }

    public void start() {
        if (mImageSpan != null) {
            mImageSpan.start();
        }
    }

    public void stop() {
        if (mImageSpan != null) {
            try {
                mImageSpan.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("audio")) {
            if (opening) {
                String url = "";
                try {
                    Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
                    elementField.setAccessible(true);
                    Object element = elementField.get(xmlReader);
                    Field attsField = element.getClass().getDeclaredField("theAtts");
                    attsField.setAccessible(true);
                    Object atts = attsField.get(element);
                    Attributes attributes = (Attributes) atts;
                    url = attributes.getValue("", "src");
                    if (mUrlCallback != null && !TextUtils.isEmpty(url)) {
                        mUrlCallback.urlCallback(url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int len = output.length();
                output.append("\uFFFC");
                SpokenAnimDrawable drawable = new SpokenAnimDrawable(mContext);
                int px = (int) ScreenUtils.dpToPx(mContext, 34);
                drawable.setBounds(0, 0, px, px);
                drawable.setView(mView);
                mImageSpan = new ClickableImageSpan(drawable, url, mSpanClickListener);
                output.setSpan(mImageSpan, len, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else if (tag.equalsIgnoreCase("b")) {
            if (opening) {
                int len = output.length();
                output.append("\uFFFC");
                Drawable drawable= ContextCompat.getDrawable(mContext, R.drawable.ic_launcher);
                int px = (int) ScreenUtils.dpToPx(mContext, 34);
                drawable.setBounds(0, 0, px, px);
                ImageSpan span=new ImageSpan(drawable);
                output.setSpan(span, len, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

}
