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
import com.yanxiu.gphone.student.R;

import java.util.HashMap;

/**
 * Created by sunpeng on 26/05/2017.
 */

public class HtmlImageGetter1 implements Html.ImageGetter {
    TextView mTextView;
    HashMap<String, UrlDrawable> mMap;

    public HtmlImageGetter1(TextView textview) {
        mTextView = textview;
        mMap = new HashMap<>();
    }

    @Override
    public Drawable getDrawable(final String source) {
        UrlDrawable drawable = new UrlDrawable();
        mMap.put(source, drawable);
        drawable.setBounds(0, 0, 0, 0);
        Glide.with(mTextView.getContext())
                .load(source).error(R.drawable.image_load_failed)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        UrlDrawable urlDrawable = mMap.get(source);
                        setDrawable(urlDrawable,resource,true);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        UrlDrawable urlDrawable = mMap.get(source);
                        setDrawable(urlDrawable,errorDrawable,false);
                    }
                });
        return drawable;
    }

    private void setDrawable(final UrlDrawable urlDrawable, final Drawable resource, final boolean scaleByDensity){
        float width = resource.getIntrinsicWidth();
        float height = resource.getIntrinsicHeight();
        if(mTextView.getWidth() <= 0){
            mTextView.post(new Runnable() {
                @Override
                public void run() {
                    setDrawable(urlDrawable, resource, scaleByDensity);
                }
            });
            return;
        }
        int maxWidth = mTextView.getWidth() - mTextView.getPaddingLeft() - mTextView.getPaddingRight();
        float scaleRatio = 1.0f;
        if(scaleByDensity){
            scaleRatio = ScreenUtils.getScreenDensity(mTextView.getContext());
        }
        float scaledWidth = width * scaleRatio;
        float scaledHeight = height * scaleRatio;
        if(scaledWidth > maxWidth){
            float scale = maxWidth / scaledWidth;
            scaledHeight = scaledHeight * scale;
            scaledWidth = maxWidth;
        }
        urlDrawable.setBounds(0, 0, Math.round(scaledWidth), Math.round(scaledHeight));
        urlDrawable.drawable = resource;
        urlDrawable.drawable.setBounds(0, 0, Math.round(scaledWidth), Math.round(scaledHeight));
        mTextView.setText(mTextView.getText());
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
