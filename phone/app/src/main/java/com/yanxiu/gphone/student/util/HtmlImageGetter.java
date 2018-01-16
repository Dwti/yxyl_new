package com.yanxiu.gphone.student.util;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yanxiu.gphone.student.R;

import java.util.HashMap;

/**
 * Created by sunpeng on 26/05/2017.
 */

public class HtmlImageGetter implements Html.ImageGetter {
    TextView mTextView;
    HashMap<String, UrlDrawable> mMap;

    /**
     * 控制图片大小，免得变的傻大傻大的
     * cwq
     * */
    private int mWidth=-1;
    private int mHeight=-1;
    public HtmlImageGetter(TextView textview,int width,int height) {
        this(textview);
        this.mWidth=width;
        this.mHeight=height;
    }

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

    private void setDrawable(UrlDrawable urlDrawable,Drawable resource,boolean scaleByDensity){
        float width = resource.getIntrinsicWidth();
        float height = resource.getIntrinsicHeight();
        int maxWidth = mTextView.getWidth() - mTextView.getPaddingLeft() - mTextView.getPaddingRight();
        float scaleRatio = 1.0f;
        if(scaleByDensity){
            scaleRatio = ScreenUtils.getScreenDensity(mTextView.getContext());
        }
        float scaledWidth = width * scaleRatio;
        float scaledHeight = height * scaleRatio;
        if (mWidth!=-1&&mHeight!=-1){
            scaledHeight = mHeight;
            scaledWidth=mWidth;
        }else if (mWidth!=-1){
            float scale = mWidth / scaledWidth;
            scaledHeight = scaledHeight * scale;
            scaledWidth = mWidth;
        }else if (mHeight!=-1){
            float scale = mHeight / scaledHeight;
            scaledHeight = mHeight;
            scaledWidth = scaledWidth * scale;
        }
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
