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

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YxylBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.user.response.RegisterResponse;
import com.yanxiu.gphone.student.user.response.VerCodeResponse;
import com.yanxiu.gphone.student.user.request.RegisterRequet;
import com.yanxiu.gphone.student.user.request.SendVerCodeRequest;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.util.time.CountDownManager;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.customviews.WavesLayout;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:25.
 * Function :
 */

public class RegisterActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private static final String TYPE = "0";
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
     * the default they are empty
     */
    private boolean isMobileReady = false;
    private boolean isVerCodeReady = false;
    private boolean isPassWordReady = false;

    /**
     * the default password is cipher
     */
    private boolean isCipher = true;
    private SendVerCodeRequest mSendVerCodeRequest;
    private RegisterRequet mRegisterRequet;
    private PublicLoadLayout rootView;
    private ImageView mBackView;
    private TextView mTitleView;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = RegisterActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_register);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        mMobileView = (EditText) findViewById(R.id.ed_mobile);
        mClearView = (ImageView) findViewById(R.id.iv_clear);
        mVerCodeView = (EditText) findViewById(R.id.ed_ver_code);
        mSendVerCodeView = (TextView) findViewById(R.id.tv_send_verCode);
        mPassWordView = (EditText) findViewById(R.id.ed_pass_word);
        mCipherView = (ImageView) findViewById(R.id.iv_cipher);
        mWavesView = (WavesLayout) findViewById(R.id.wl_forget_waves);
        mRegisterView = (TextView) findViewById(R.id.tv_register);
    }

    private void initData() {
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(getText(R.string.iwantregister));
        mClearView.setEnabled(false);
        mSendVerCodeView.setEnabled(false);
        mRegisterView.setEnabled(false);
    }

    private void listener() {
        mBackView.setOnClickListener(RegisterActivity.this);
        mClearView.setOnClickListener(RegisterActivity.this);
        mCipherView.setOnClickListener(RegisterActivity.this);
        mSendVerCodeView.setOnClickListener(RegisterActivity.this);
        mRegisterView.setOnClickListener(RegisterActivity.this);
        EditTextManger.getManager(mMobileView).setInputOnlyNumber().setTextChangedListener(RegisterActivity.this);
        EditTextManger.getManager(mVerCodeView).setInputOnlyNumber().setTextChangedListener(RegisterActivity.this);
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(RegisterActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendVerCodeRequest != null) {
            mSendVerCodeRequest.cancelRequest();
            mSendVerCodeRequest = null;
        }
        if (mRegisterRequet != null) {
            mRegisterRequet.cancelRequest();
            mRegisterRequet = null;
        }
    }

    @Override
    public void onClick(View v) {
        String mobileCode;
        String verCode;
        String passWord;
        switch (v.getId()) {
            case R.id.iv_left:
                RegisterActivity.this.finish();
                EditTextManger.getManager(mTitleView).hideSoftInput(mContext);
                break;
            case R.id.iv_clear:
                mMobileView.setText("");
                break;
            case R.id.iv_cipher:
                setEditPassWordChange();
                break;
            case R.id.tv_send_verCode:
                mobileCode = mMobileView.getText().toString().trim();
                if (mobileCode.length() != 11 || !mobileCode.substring(0, 1).equals("1")) {
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }
                sendVerCode(mobileCode);
                break;
            case R.id.tv_register:
                mobileCode = mMobileView.getText().toString().trim();
                verCode = mVerCodeView.getText().toString().trim();
                passWord = mPassWordView.getText().toString().trim();
                if (mobileCode.length() != 11 || !mobileCode.substring(0, 1).equals("1")) {
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }
                if (verCode.length() != 4) {
                    ToastManager.showMsg(getText(R.string.input_true_verCode));
                    return;
                }
                if (passWord.length() < 6 || passWord.length() > 18) {
                    ToastManager.showMsg(getText(R.string.input_password_error));
                    return;
                }
                onRegister(mobileCode, verCode, passWord);
//                JoinClassActivity.LaunchActivity(mContext);
                break;
        }
    }

    private void sendVerCode(String mobile) {
        rootView.showLoadingView();
        mSendVerCodeRequest = new SendVerCodeRequest();
        mSendVerCodeRequest.mobile = mobile;
        mSendVerCodeRequest.type = TYPE;
        mSendVerCodeRequest.startRequest(VerCodeResponse.class, new YxylBaseCallback<VerCodeResponse>() {

            @Override
            protected void onResponse(RequestBase request, VerCodeResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    startTiming(45000);
                } else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    private void onRegister(final String mobile, String verCode, final String passWord) {
        rootView.showLoadingView();
        mRegisterRequet = new RegisterRequet();
        mRegisterRequet.mobile = mobile;
        mRegisterRequet.code = verCode;
        mRegisterRequet.password = passWord;
        mRegisterRequet.startRequest(RegisterResponse.class, new YxylBaseCallback<RegisterResponse>() {

            @Override
            protected void onResponse(RequestBase request, RegisterResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    LoginInfo.setMobile(mobile);
                    LoginInfo.setPassWord(passWord);
                    JoinClassActivity.LaunchActivity(mContext);
                    RegisterActivity.this.finish();
                } else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    private void setEditMobileIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            mClearView.setEnabled(false);
            mClearView.setVisibility(View.INVISIBLE);
        } else {
            mClearView.setEnabled(true);
            mClearView.setVisibility(View.VISIBLE);
        }
    }

    private void setSendVerCodeViewFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mSendVerCodeView.setEnabled(true);
        } else {
            mSendVerCodeView.setEnabled(false);
        }
    }

    private void startTiming(int totalTime) {
        ToastManager.showMsg(getText(R.string.send_verCode_finish));
        CountDownManager.getManager().setTotalTime(totalTime).setScheduleListener(new CountDownManager.ScheduleListener() {
            @Override
            public void onProgress(long progress) {
                mSendVerCodeView.setClickable(false);
                mSendVerCodeView.setText(String.format(getText(R.string.verCode_progress).toString(), (int) progress / 1000));

            }

            @Override
            public void onFinish() {
                mSendVerCodeView.setClickable(true);
                mSendVerCodeView.setText(getText(R.string.send_verCode_more));
            }
        }).start();
    }

    private void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mWavesView.setCanShowWave(true);
            mRegisterView.setEnabled(true);
        } else {
            mWavesView.setCanShowWave(false);
            mRegisterView.setEnabled(false);
        }
    }

    private void setEditPassWordChange() {
        this.isCipher = !isCipher;
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

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        if (view == mMobileView) {
            if (isEmpty) {
                isMobileReady = false;
            } else {
                isMobileReady = true;
            }
            setEditMobileIsEmpty(isEmpty);
            setSendVerCodeViewFocusChange(isMobileReady);
        } else if (view == mPassWordView) {
            if (isEmpty) {
                isPassWordReady = false;
            } else {
                isPassWordReady = true;
            }
        } else if (view == mVerCodeView) {
            if (isEmpty) {
                isVerCodeReady = false;
            } else {
                isVerCodeReady = true;
            }
        }
        setButtonFocusChange(isMobileReady && isPassWordReady && isVerCodeReady);
    }
}
