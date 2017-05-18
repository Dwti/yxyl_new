package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.user.bean.BaseBean;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.util.time.CountDownManager;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.view.WavesLayout;
@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:25.
 * Function :
 */

public class RegisterActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private Context mContext;

    private EditText mMobileView;
    private ImageView mClearView;
    private EditText mVerCodeView;
    private TextView mSendVerCodeView;
    private EditText mPassWordView;
    private ImageView mCipherView;
    private WavesLayout mWavesView;
    private TextView mRegisterView;
    /**
     * send verification code
     */
    private static final int UUID_VERCODE = 0x000;
    /**
     * do register
     */
    private static final int UUID_REGISTER = 0x001;
    /**
     * the default they are empty
     */
    private boolean isMobileReady = false;
    private boolean isVerCodeReady = false;
    private boolean isPassWordReady = false;

    /**
     * the default password is cipher
     */
    private boolean isCipher = true;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext=RegisterActivity.this;
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
        EditTextManger.getManager(mMobileView).setInputOnlyNumber().setTextChangedListener(new EditTextManger.onTextLengthChangedListener() {
            @Override
            public void onChanged(EditText view, String value, boolean isEmpty) {
                if (isEmpty){
                    isMobileReady=false;
                }   else {
                    isMobileReady=true;
                }
                setEditMobileIsEmpty(isEmpty);
                setSendVerCodeViewFocusChange(isMobileReady);
                setButtonFocusChange(isMobileReady&&isPassWordReady&&isVerCodeReady);
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
                setButtonFocusChange(isMobileReady&&isPassWordReady&&isVerCodeReady);
            }
        });
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(new EditTextManger.onTextLengthChangedListener() {
            @Override
            public void onChanged(EditText view, String value, boolean isEmpty) {
                if (isEmpty){
                    isPassWordReady=false;
                }               else {
                    isPassWordReady=true;
                }
                setButtonFocusChange(isMobileReady&&isPassWordReady&&isVerCodeReady);
            }
        });
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

    @Override
    public void onClick(View v) {
        String mobileCode;
        String verCode;
        String passWord;
        switch (v.getId()){
            case R.id.iv_clear:
                mMobileView.setText("");
                break;
            case R.id.iv_cipher:
                this.isCipher = !isCipher;
                setEditPassWordChange(isCipher);
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
            case R.id.tv_register:
                mobileCode=mMobileView.getText().toString().trim();
                verCode=mVerCodeView.getText().toString().trim();
                passWord=mPassWordView.getText().toString().trim();

                if (mobileCode.length()!=11||!mobileCode.substring(0,1).equals("1")){
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }

                if (verCode.length()!=4){
                    ToastManager.showMsg(getText(R.string.input_true_verCode));
                    return;
                }

                if (passWord.length()<6||passWord.length()>18){
                    ToastManager.showMsg(getText(R.string.input_password_error));
                    return;
                }

                onRegister(mobileCode,verCode,passWord);
                break;
        }
    }

    public void sendVerCode(String mobile) {

    }

    public void onRegister(String mobile, String verCode, String passWord) {

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
            mRegisterView.setEnabled(true);
        }else {
            mWavesView.setCanShowWave(false);
            mRegisterView.setEnabled(false);
        }
    }

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
