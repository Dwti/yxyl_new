package com.yanxiu.gphone.student.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.R;

/**
 * 答题卡提交过程Dialog,该Dialog不具有扩展性，只适合答题卡提交进度显示
 * 2017/6/20
 * create by dyf
 */
public class AnswerCardSubmitDialog extends Dialog implements AnswercardSubmitProgressView.DisplacementListener {

    protected RelativeLayout mRootView;//根view
    private AnswercardSubmitProgressView mProgressbar;
    private RelativeLayout mSubmiting_layout, mState_layout;
    private ImageView mPincil;
    private View yy;

    public AnswerCardSubmitDialog(Context context) {
        this(context, R.style.AnswerCarDialog);
        init(context);
    }

    public AnswerCardSubmitDialog(Context context, int theme) {
        super(context, R.style.AnswerCarDialog);
        init(context);
    }

    private AnswerCardSubmitDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(mRootView);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(mRootView);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(mRootView, params);
    }
    int i = 0;
    private void init(Context context) {
        mRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.answercard_dialog, null);
        setContentView(mRootView);
        mSubmiting_layout = (RelativeLayout) mRootView.findViewById(R.id.submiting_layout);
        mState_layout = (RelativeLayout) mRootView.findViewById(R.id.state_layout);

        mProgressbar = (AnswercardSubmitProgressView) mRootView.findViewById(R.id.progressbar);
        mPincil = (ImageView) mRootView.findViewById(R.id.pincil);
        mProgressbar.setDisplacementListener(this);
        mProgressbar.setMaxCount(20);

        yy = mRootView.findViewById(R.id.yy);
        yy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressbar.updateProgress(++i);
            }
        });
    }
    public void showView(int type){
        if(type == 1){
            mSubmiting_layout.setVisibility(View.VISIBLE);
            mState_layout.setVisibility(View.GONE);
        }else{
            mSubmiting_layout.setVisibility(View.GONE);
            mState_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void xPositionChange(int x) {
        mPincil.setX(mPincil.getX() + x);
    }
}