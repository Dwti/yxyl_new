package com.yanxiu.gphone.student.user.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.user.presenter.impl.ForgetPassWordPresenterImpl;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.time.CountDownManager;
import com.yanxiu.gphone.student.util.view.WavesLayout;
import com.yanxiu.gphone.student.user.view.interf.ForgetPassWordViewChangeListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:26.
 * Function :
 */

public class ForgetPassWordActivity extends YanxiuBaseActivity implements ForgetPassWordViewChangeListener, View.OnClickListener {

    private static final String INTENT_MOBILE="mobile";

    private Context mContext;

    private ForgetPassWordPresenterImpl presenter;
    private EditText mMobileView;
    private ImageView mClearView;
    private EditText mVerCodeView;
    private TextView mSendVerCodeView;
    private TextView mNextView;
    private WavesLayout mWavesView;

    public static void LaunchActivity(Context context,String mobile){
        Intent intent=new Intent(context,ForgetPassWordActivity.class);
        intent.putExtra(INTENT_MOBILE,mobile);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mContext=ForgetPassWordActivity.this;
        presenter=new ForgetPassWordPresenterImpl(ForgetPassWordActivity.this);
        String mobile=getIntent().getStringExtra(INTENT_MOBILE);
        initView();
        initData(mobile);
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestory();
    }

    private void initView() {
        mMobileView= (EditText) findViewById(R.id.ed_mobile);
        mClearView= (ImageView) findViewById(R.id.iv_clear);
        mVerCodeView= (EditText) findViewById(R.id.ed_ver_code);
        mSendVerCodeView= (TextView) findViewById(R.id.tv_send_verCode);
        mNextView= (TextView) findViewById(R.id.tv_next);
        mWavesView= (WavesLayout) findViewById(R.id.wl_forget_waves);
    }

    private void initData(String mobile) {
        mClearView.setEnabled(false);
        mSendVerCodeView.setEnabled(false);
        mNextView.setEnabled(false);
        EditTextManger.getManager(mMobileView).setInputOnlyNumber().setTextChangedListener((view, value, isEmpty) -> presenter.setMobileValue(value));
        EditTextManger.getManager(mVerCodeView).setInputOnlyNumber().setTextChangedListener((view, value, isEmpty) -> presenter.setVerCodeValue(value));
        mMobileView.setText(mobile);
        if (mobile.length()>0) {
            mMobileView.setSelection(mobile.length());
        }
    }

    private void initListener() {
        mClearView.setOnClickListener(ForgetPassWordActivity.this);
        mSendVerCodeView.setOnClickListener(ForgetPassWordActivity.this);
        mNextView.setOnClickListener(ForgetPassWordActivity.this);
    }

    @Override
    public void onHttpStart(int uuid) {

    }

    @Override
    public void onReturntError(int uuid, BaseBean baseBean) {

    }

    @Override
    public void onSuccess(int uuid, BaseBean baseBean) {

    }

    @Override
    public void onCancel(int uuid) {

    }

    @Override
    public void onNetWorkError(int uuid, String msg) {

    }

    @Override
    public void onDataError(int uuid, String msg) {

    }

    @Override
    public void onHttpFinished(int uuid) {

    }

    @Override
    public void setEditMobileIsEmpty(boolean isEmpty) {
        if (isEmpty){
            mClearView.setEnabled(false);
            mClearView.setVisibility(View.INVISIBLE);
        }else {
            mClearView.setEnabled(true);
            mClearView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEditMobileClear(String text) {
        mMobileView.setText(text);
    }

    @Override
    public void setSendVerCodeViewFocusChange(boolean hasFocus) {
        if (hasFocus){
            mSendVerCodeView.setEnabled(true);
            mSendVerCodeView.setTextColor(ContextCompat.getColor(mContext,R.color.color_ffffff));
        }else {
            mSendVerCodeView.setEnabled(false);
            mSendVerCodeView.setTextColor(ContextCompat.getColor(mContext,R.color.color_89e00d));
        }
    }

    @Override
    public void startTiming(int totalTime) {
        CountDownManager.getManager().setTotalTime(totalTime).setScheduleListener(new CountDownManager.ScheduleListener() {
            @Override
            public void onProgress(long progress) {
                mSendVerCodeView.setEnabled(false);
                mSendVerCodeView.setText(String.format(getText(R.string.verCode_progress).toString(),(int)progress/1000));
            }

            @Override
            public void onFinish() {
                mSendVerCodeView.setEnabled(true);
                mSendVerCodeView.setText(getText(R.string.send_verCode_more));
            }
        }).start();
    }

    @Override
    public void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus){
            mWavesView.setCanShowWave(true);
            mNextView.setEnabled(true);
            mNextView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_login_click));
        }else {
            mWavesView.setCanShowWave(false);
            mNextView.setEnabled(false);
            mNextView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_login_unclick));
        }
    }

    @Override
    public void onClick(View v) {
        String mobileCode;
        String verCode;
        switch (v.getId()){
            case R.id.iv_clear:
                presenter.setMobileChange();
                break;
            case R.id.tv_send_verCode:
                mobileCode=mMobileView.getText().toString().trim();
                if (mobileCode.length()!=11||!mobileCode.substring(0,1).equals("1")){
                    Toast.makeText(mContext,getText(R.string.input_true_mobile),Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.sendVerCode(mobileCode);
                break;
            case R.id.tv_next:
                mobileCode=mMobileView.getText().toString().trim();
                verCode=mVerCodeView.getText().toString().trim();
                if (mobileCode.length()!=11||!mobileCode.substring(0,1).equals("1")){
                    Toast.makeText(mContext,getText(R.string.input_true_mobile),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verCode.length()!=4){
                    Toast.makeText(mContext,getText(R.string.input_true_verCode),Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.onNext(mobileCode,verCode);
                break;
        }
    }
}
