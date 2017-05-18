package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.util.time.CountDownManager;
import com.yanxiu.gphone.student.util.view.WavesLayout;
@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:26.
 * Function :
 */

public class ForgetPassWordActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private static final String INTENT_MOBILE="mobile";

    private Context mContext;

    private EditText mMobileView;
    private ImageView mClearView;
    private EditText mVerCodeView;
    private TextView mSendVerCodeView;
    private TextView mNextView;
    private WavesLayout mWavesView;
    /**
     * send verification code
     */
    private static final int UUID_VERCODE = 0x000;
    /**
     * do next
     */
    private static final int UUID_NEXT = 0x001;

    private boolean isMobileReady = false;
    private boolean isVerCodeReady = false;

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
        String mobile=getIntent().getStringExtra(INTENT_MOBILE);
        initView();
        initData(mobile);
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        EditTextManger.getManager(mMobileView).setInputOnlyNumber().setTextChangedListener(new EditTextManger.onTextLengthChangedListener() {
            @Override
            public void onChanged(EditText view, String value, boolean isEmpty) {
                if (isEmpty){
                    isMobileReady=false;
                }else {
                    isMobileReady=true;
                }
                setSendVerCodeViewFocusChange(isMobileReady);
                setButtonFocusChange(isMobileReady&&isVerCodeReady);
                setEditMobileIsEmpty(isEmpty);
            }
        });
        EditTextManger.getManager(mVerCodeView).setInputOnlyNumber().setTextChangedListener(new EditTextManger.onTextLengthChangedListener() {
            @Override
            public void onChanged(EditText view, String value, boolean isEmpty) {
                if (isEmpty){
                    isVerCodeReady=false;
                }else {
                    isVerCodeReady=true;
                }
                setButtonFocusChange(isMobileReady&&isVerCodeReady);
            }
        });
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

    public void onHttpStart(int uuid) {

    }

    public void onReturntError(int uuid, BaseBean baseBean) {

    }

    public void onSuccess(int uuid, BaseBean baseBean) {

    }

    public void onNetWorkError(int uuid, String msg) {
        ToastManager.showMsg(getText(R.string.net_null));
    }

    public void onDataError(int uuid, String msg) {
        ToastManager.showMsg(getText(R.string.data_error));
    }

    public void onHttpFinished(int uuid) {

    }

    public void setEditMobileIsEmpty(boolean isEmpty) {
        if (isEmpty){
            mClearView.setEnabled(false);
            mClearView.setVisibility(View.INVISIBLE);
        }else {
            mClearView.setEnabled(true);
            mClearView.setVisibility(View.VISIBLE);
        }
    }

    public void setSendVerCodeViewFocusChange(boolean hasFocus) {
        if (hasFocus){
            mSendVerCodeView.setEnabled(true);
        }else {
            mSendVerCodeView.setEnabled(false);
        }
    }

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

    public void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus){
            mWavesView.setCanShowWave(true);
            mNextView.setEnabled(true);
        }else {
            mWavesView.setCanShowWave(false);
            mNextView.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        String mobileCode;
        String verCode;
        switch (v.getId()){
            case R.id.iv_clear:
                mMobileView.setText("");
                break;
            case R.id.tv_send_verCode:
                mobileCode=mMobileView.getText().toString().trim();
                if (mobileCode.length()!=11||!mobileCode.substring(0,1).equals("1")){
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }
                sendVerCode(mobileCode);
                startTiming(45000);
                break;
            case R.id.tv_next:
                mobileCode=mMobileView.getText().toString().trim();
                verCode=mVerCodeView.getText().toString().trim();
                if (mobileCode.length()!=11||!mobileCode.substring(0,1).equals("1")){
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }
                if (verCode.length()!=4){
                    ToastManager.showMsg(getText(R.string.input_true_verCode));
                    return;
                }
                onNext(mobileCode,verCode);
                ResetPassWordActivity.LaunchActivity(mContext);
                break;
        }
    }

    public void sendVerCode(String mobile) {
        onHttpStart(UUID_VERCODE);
    }

    public void onNext(String mobile, String verCode) {
        onHttpStart(UUID_NEXT);
    }
}
