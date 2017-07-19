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

public class HtmlImageGetter implements Html.ImageGetter {
    TextView mTextView;
    HashMap<String, UrlDrawable> mMap;

    public HtmlImageGetter(TextView textview) {
        mTextView = textview;
        mMap = new HashMap<>();
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
                        int maxWidth = mTextView.getWidth() - mTextView.getPaddingLeft() - mTextView.getPaddingRight();
                        if(width > maxWidth){
                            float scale = maxWidth / width;
                            height = height * scale;
                            width = maxWidth;
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
