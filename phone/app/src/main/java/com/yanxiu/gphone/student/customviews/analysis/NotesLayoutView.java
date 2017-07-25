package com.yanxiu.gphone.student.customviews.analysis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.NotesActicity;
import com.yanxiu.gphone.student.questions.bean.JsonNoteBean;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/22 10:36.
 * Function :
 */
public class NotesLayoutView extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mNoteLayout;
    private TextView mNoteContentView;
    private ImageView mImgLeftView;
    private ImageView mImgCenterLeftView;
    private ImageView mImgCenterRightView;
    private ImageView mImgRightView;

    public NotesLayoutView(Context context) {
        this(context,null);
    }

    public NotesLayoutView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NotesLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        listenr();
    }

    private void initView(Context context){
        this.mContext=context;
        LayoutInflater.from(context).inflate(R.layout.layoutview_notes,this);
        mNoteLayout= (LinearLayout) findViewById(R.id.ll_note);
        mNoteContentView= (TextView) findViewById(R.id.tv_note_content);
        mImgLeftView= (ImageView) findViewById(R.id.iv_left);
        mImgCenterLeftView= (ImageView) findViewById(R.id.iv_center_left);
        mImgCenterRightView= (ImageView) findViewById(R.id.iv_center_right);
        mImgRightView= (ImageView) findViewById(R.id.iv_right);
    }

    private void listenr() {
        mNoteLayout.setOnClickListener(NotesLayoutView.this);
    }

    public void setData(JsonNoteBean jsonNoteBean){
        if (jsonNoteBean==null){
            return;
        }
        mNoteContentView.setText(jsonNoteBean.getText());
        for (int i=0;i<jsonNoteBean.getImages().size();i++){
            switch (i){
                case 0:
                    Glide.with(mContext).load(jsonNoteBean.getImages().get(0)).asBitmap().into(new CornersImageTarget(mImgLeftView));
                    break;
                case 1:
                    Glide.with(mContext).load(jsonNoteBean.getImages().get(1)).asBitmap().into(new CornersImageTarget(mImgCenterLeftView));
                    break;
                case 2:
                    Glide.with(mContext).load(jsonNoteBean.getImages().get(2)).asBitmap().into(new CornersImageTarget(mImgCenterRightView));
                    break;
                case 3:
                    Glide.with(mContext).load(jsonNoteBean.getImages().get(3)).asBitmap().into(new CornersImageTarget(mImgRightView));
                    break;
            }
        }
    }

    private class CornersImageTarget extends BitmapImageViewTarget {

        CornersImageTarget(ImageView view) {
            super(view);
        }

        @Override
        protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable cornersBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
            cornersBitmapDrawable.setCornerRadius(12);
            view.setImageDrawable(cornersBitmapDrawable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(NotesActicity.NotesMessage notesMessage) {
        int viewHashCode = notesMessage.mViewHashCode;
        String notesContent = notesMessage.mNotesContent;
        if (viewHashCode == NotesLayoutView.this.hashCode())
            mNoteContentView.setText(notesContent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_note:
                NotesActicity.invoke((Activity) mContext,NotesLayoutView.this.hashCode());
                break;
        }
    }
}
