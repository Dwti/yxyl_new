package com.yanxiu.gphone.student.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.http.response.NoticeReponse;

/**
 * Created by Canghaixiao.
 * Time : 2018/2/5 16:16.
 * Function :
 */
public class NoticeDialog extends Dialog {

    private TextView mTitleView;
    private TextView mContentView;
    private NoticeReponse.Property mProperty;

    public NoticeDialog(@NonNull Context context, NoticeReponse.Property property) {
        super(context, R.style.AlertDialogStyle);
        this.mProperty = property;
        setOwnerActivity((Activity) context);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notice);
        initView();
        initData();
    }

    private void initView() {
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mContentView = (TextView) findViewById(R.id.tv_content);
    }

    private void initData() {
        mTitleView.setText(mProperty.title);
        mContentView.setText(mProperty.notice);
    }
}
