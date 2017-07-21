package com.yanxiu.gphone.student.util;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.HashMap;

/**
 * Created by sunpeng on 26/05/2017.
 */

public class HtmlImageGetterForClassify implements Html.ImageGetter {
    TextView mTextView;
    HashMap<String, UrlDrawable> mMap;
    int mMaxAvaliableWidth,mMaxAvaliableHeight;

    public HtmlImageGetterForClassify(TextView textview,int maxAvaliableWidth,int maxAvaliableHeight) {
        mTextView = textview;
        mMap = new HashMap<>();
        mMaxAvaliableWidth = maxAvaliableWidth;
        mMaxAvaliableHeight = maxAvaliableHeight;
    }

    @Override
    public Drawable getDrawable(final String source) {
        UrlDrawable drawable = new UrlDrawable();
        mMap.put(source, drawable);
        drawable.setBounds(0, 0, 0, 0);
        Glide.with(mTextView.getContext())
                .load(source)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        UrlDrawable drawable = mMap.get(source);
                        float width = resource.getIntrinsicWidth();
                        float height = resource.getIntrinsicHeight();
                        int maxWidth = mMaxAvaliableWidth - mTextView.getPaddingLeft() - mTextView.getPaddingRight();
                        int maxHeight = mMaxAvaliableHeight - mTextView.getPaddingTop() - mTextView.getPaddingBottom();
                        if(width > maxWidth){
                            float scale = maxWidth / width;
                            height = height * scale;
                            width = maxWidth;
                        }
                        if(height > maxHeight){
                            float scale = maxHeight / height;
                            width = width * scale;
                            height = maxHeight;
                        }
                        drawable.setBounds(0, 0, Math.round(width), Math.round(height));
                        drawable.drawable = resource;
                        drawable.drawable.setBounds(0, 0, Math.round(width), Math.round(height));
                        mTextView.setText(mTextView.getText());
                    }
                });
        return drawable;
    }

    public class UrlDrawable extends BitmapDrawable {
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            if(drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}
