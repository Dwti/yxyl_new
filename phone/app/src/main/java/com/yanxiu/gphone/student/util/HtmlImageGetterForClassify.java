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
                .load(source).error(R.drawable.image_load_failed)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        UrlDrawable drawable = mMap.get(source);
                        setDrawable(drawable,resource,true);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        UrlDrawable drawable = mMap.get(source);
                        setDrawable(drawable,errorDrawable,false);
                    }
                });
        return drawable;
    }

    public void setDrawable(UrlDrawable urlDrawable,Drawable resource,boolean scaleByDensity){
        float width = resource.getIntrinsicWidth();
        float height = resource.getIntrinsicHeight();
        int maxWidth = mMaxAvaliableWidth - mTextView.getPaddingLeft() - mTextView.getPaddingRight();
        int maxHeight = mMaxAvaliableHeight - mTextView.getPaddingTop() - mTextView.getPaddingBottom();

        float scaleRatio = 1.0f;
        if(scaleByDensity){
            scaleRatio = ScreenUtils.getScreenDensity(mTextView.getContext());
        }
        width = width * scaleRatio;
        height = height * scaleRatio;

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
        urlDrawable.setBounds(0, 0, Math.round(width), Math.round(height));
        urlDrawable.drawable = resource;
        urlDrawable.drawable.setBounds(0, 0, Math.round(width), Math.round(height));
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
