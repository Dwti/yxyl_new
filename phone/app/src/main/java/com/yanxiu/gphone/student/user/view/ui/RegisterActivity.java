package com.yanxiu.gphone.student.user.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.user.presenter.impl.RegisterPresenterImpl;
import com.yanxiu.gphone.student.util.time.CountDownManager;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.view.WavesLayout;
import com.yanxiu.gphone.student.user.view.interf.RegisterViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:25.
 * Function :
 */

public class RegisterActivity extends YanxiuBaseActivity implements RegisterViewChangedListener, View.OnClickListener {

    private Context mContext;

    private RegisterPresenterImpl presenter;
    private EditText mMobileView;
    private ImageView mClearView;
    private EditText mVerCodeView;
    private TextView mSendVerCodeView;
    private EditText mPassWordView;
    private ImageView mCipherView;
    private WavesLayout mWavesView;
    private TextView mRegisterView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext=RegisterActivity.this;
        presenter=new RegisterPresenterImpl(RegisterActivity.this);
        initView();
        initData();
        Listener();
    }

     private void initView() {
        mMobileView= (EditText) findViewById(R.id.ed_mobile);
        mClearView= (ImageView) findViewById(R.id.iv_clear);
        mVerCodeView= (EditText) findViewById(R.id.ed_ver_code);
        mSendVerCodeView= (TextView) findViewById(R.id.tv_send_verCode);
        mPassWordView= (EditText) findViewById(R.id.ed_pass_word);
        mCipherView= (ImageView) findViewById(R.id.iv_cipher);
        mWavesView= (WavesLayout) findViewById(R.id.wl_forget_waves);
        mRegisterView= (TextView) findViewById(R.id.tv_register);
    }

    private void initData() {
        mClearView.setEnabled(false);
        mSendVerCodeView.setEnabled(false);
        mRegisterView.setEnabled(false);
        EditTextManger.getManager(mMobileView).setInputOnlyNumber().setTextChangedListener((view, value, isEmpty) -> presenter.setMobileValue(value));
        EditTextManger.getManager(mVerCodeView).setInputOnlyNumber().setTextChangedListener((view, value, isEmpty) -> presenter.setVerCodeValue(value));
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener((view, value, isEmpty) -> presenter.setPassWordValue(value));
    }

    private void Listener() {
        mClearView.setOnClickListener(RegisterActivity.this);
        mCipherView.setOnClickListener(RegisterActivity.this);
        mSendVerCodeView.setOnClickListener(RegisterActivity.this);
        mRegisterView.setOnClickListener(RegisterActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestory();
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
    public void onClick(View v) {
        String mobileCode;
        String verCode;
        String passWord;
        switch (v.getId()){
            case R.id.iv_clear:
                presenter.setMobileChange();
                break;
            case R.id.iv_cipher:
                presenter.setPassWordChange();
                break;
            case R.id.tv_send_verCode:
                mobileCode=mMobileView.getText().toString().trim();

                if (mobileCode.length()!=11||!mobileCode.substring(0,1).equals("1")){
                    Toast.makeText(mContext,getText(R.string.input_true_mobile),Toast.LENGTH_SHORT).show();
                    return;
                }

                presenter.sendVerCode(mobileCode);
                break;
            case R.id.tv_register:
                mobileCode=mMobileView.getText().toString().trim();
                verCode=mVerCodeView.getText().toString().trim();
                passWord=mPassWordView.getText().toString().trim();

                if (mobileCode.length()!=11||!mobileCode.substring(0,1).equals("1")){
                    Toast.makeText(mContext,getText(R.string.input_true_mobile),Toast.LENGTH_SHORT).show();
                    return;
                }

                if (verCode.length()!=4){
                    Toast.makeText(mContext,getText(R.string.input_true_verCode),Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passWord.length()<6||passWord.length()>18){
                    Toast.makeText(mContext,getText(R.string.input_password_error),Toast.LENGTH_SHORT).show();
                    return;
                }

                presenter.onRegister(mobileCode,verCode,passWord);
                break;
        }
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
            mRegisterView.setEnabled(true);
            mRegisterView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_common_button_bg_normal));
        }else {
            mWavesView.setCanShowWave(false);
            mRegisterView.setEnabled(false);
            mRegisterView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_common_button_bg_disable));
        }
    }

    @Override
    public void setEditPassWordChange(boolean isCipher) {
        if (isCipher) {
//            mCipherView.setBackgroundResource();
            mPassWordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mPassWordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        String text = mPassWordView.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            mPassWordView.setSelection(text.length());
        }
    }
}
