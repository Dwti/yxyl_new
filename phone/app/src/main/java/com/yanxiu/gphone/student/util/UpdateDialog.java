package com.yanxiu.gphone.student.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/8 12:03.
 * Function :
 */
class UpdateDialog extends Dialog {

    private static final String UPDATETYPE_MANDATORY="1";
    private static final String UPDATETYPE_UNMANDATORY="2";

    private Context mContext;

    private ProgressBar mPbLoadApkView;
    private TextView mUpdateTitleView;
    private TextView mUpdateCelView;
    private TextView mUpdateSureView;
    private TextView mUpdateContentView;

    private String mUpdateType;
    private UpdateDialogCallBack mCallBack;

    private String mContent,mVersion,mTitle;

    interface UpdateDialogCallBack{
        void update();
        void cancel();
        void exit();
    }

    UpdateDialog(@NonNull Context context,String updateType,UpdateDialogCallBack callBack) {
        super(context, R.style.AlertDialogStyle);
        this.mContext=context;
        this.mUpdateType=updateType;
        this.mCallBack=callBack;
        setOwnerActivity((Activity) mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_popupwindow);
        initView();
        initData();
    }

    private void initData() {
        if (mUpdateType.equals(UPDATETYPE_MANDATORY)){
            mUpdateCelView.setText(mContext.getResources().getText(R.string.app_update_exit));
            mUpdateSureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack!=null){
                        mPbLoadApkView.setVisibility(View.VISIBLE);
                        mCallBack.update();
                    }
                }
            });
            mUpdateCelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack!=null){
                        mCallBack.exit();
                    }
                    dismiss();
                }
            });
        }else if (mUpdateType.equals(UPDATETYPE_UNMANDATORY)){
            mUpdateSureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack!=null){
                        mCallBack.update();
                    }
                    dismiss();
                }
            });
            mUpdateCelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack!=null){
                        mCallBack.cancel();
                    }
                    dismiss();
                }
            });
        }

        mUpdateContentView.setText(mContent);
        if(TextUtils.isEmpty(mTitle)) {
            mUpdateTitleView.setText(mContext.getResources().getString(R.string.app_update, mVersion));
        } else {
            mUpdateTitleView.setText(mTitle);
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.mUpdateType=null;
        this.mCallBack=null;
        this.mContext=null;
    }

    private void initView() {
        mPbLoadApkView= (ProgressBar) findViewById(R.id.pb_loadapk);
        mUpdateTitleView = (TextView)findViewById(R.id.update_title);
        mUpdateContentView = (TextView)findViewById(R.id.update_content);
        mUpdateCelView = (TextView)findViewById(R.id.update_layout_cel);
        mUpdateSureView = (TextView)findViewById(R.id.update_layout_sure);
    }

    public void setTitles(String title,String version){
        this.mTitle=title;
        this.mVersion=version;
    }

    public void setContent(String content){
        this.mContent=content;
    }

    public void setProgress(int progress){
        if (mPbLoadApkView!=null) {
            mPbLoadApkView.setProgress(progress);
        }
    }

}
