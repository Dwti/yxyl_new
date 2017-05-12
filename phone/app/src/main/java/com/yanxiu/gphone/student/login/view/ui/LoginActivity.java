package com.yanxiu.gphone.student.login.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.login.bean.LoginBean;
import com.yanxiu.gphone.student.login.presenter.impl.LoginPresenterImpl;
import com.yanxiu.gphone.student.login.utils.EditTextManger;
import com.yanxiu.gphone.student.login.utils.WavesLayout;
import com.yanxiu.gphone.student.login.view.interf.LoginViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:23.
 * Function :
 */

public class LoginActivity extends YanxiuBaseActivity implements LoginViewChangedListener, View.OnClickListener {

    private LoginPresenterImpl presenter;
    private EditText mUserNameView;
    private ImageView mClearView;
    private EditText mPassWordView;
    private ImageView mCipherView;
    private TextView mLoginView;
    private TextView mForgetPassWordView;
    private TextView mFastRegisteredView;
    private ImageView mThirdQQView;
    private ImageView mThirdWXView;
    private RelativeLayout mTitleView;
    private WavesLayout mWavesView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenterImpl(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mUserNameView = (EditText) findViewById(R.id.ed_user_name);
        mClearView = (ImageView) findViewById(R.id.iv_clear);
        mPassWordView = (EditText) findViewById(R.id.ed_pass_word);
        mCipherView = (ImageView) findViewById(R.id.iv_cipher);
        mLoginView = (TextView) findViewById(R.id.tv_login);
        mForgetPassWordView = (TextView) findViewById(R.id.tv_forget_password);
        mFastRegisteredView = (TextView) findViewById(R.id.tv_fast_registered);
        mThirdQQView = (ImageView) findViewById(R.id.iv_third_qq);
        mThirdWXView = (ImageView) findViewById(R.id.iv_third_wx);
        mTitleView= (RelativeLayout) findViewById(R.id.in_title);
        mWavesView= (WavesLayout) findViewById(R.id.wl_login_waves);
    }

    private void initData() {
        mTitleView.setVisibility(View.INVISIBLE);
        mClearView.setEnabled(false);
        mLoginView.setEnabled(false);
        EditTextManger.getManager(mUserNameView).setInputNumberAndLetter().setTextChangedListener((view, value, isEmpty) -> presenter.setUserNameValue(value));
        EditTextManger.getManager(mPassWordView).setInputNumberAndLetter().setTextChangedListener((view, value, isEmpty) -> presenter.setPassWordValue(value));
    }

    private void initListener() {
        mClearView.setOnClickListener(this);
        mCipherView.setOnClickListener(this);
        mLoginView.setOnClickListener(this);
        mForgetPassWordView.setOnClickListener(this);
        mFastRegisteredView.setOnClickListener(this);
        mThirdQQView.setOnClickListener(this);
        mThirdWXView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestory();
            presenter = null;
        }
    }

    @Override
    public void onHttpStart(int uuid) {

    }

    @Override
    public void onReturntError(int uuid, LoginBean bean) {

    }

    @Override
    public void onSuccess(int uuid, LoginBean bean) {

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
    public void setEditUserNameClear(String text) {
        mUserNameView.setText(text);
    }

    @Override
    public void setEditUserNameIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            mClearView.setEnabled(false);
            mClearView.setVisibility(View.INVISIBLE);
        } else {
            mClearView.setEnabled(true);
            mClearView.setVisibility(View.VISIBLE);
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

    @Override
    public void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mWavesView.setCanShowWave(true);
            mLoginView.setEnabled(true);
            mLoginView.setBackgroundResource(R.drawable.shape_login_click);
        } else {
            mWavesView.setCanShowWave(false);
            mLoginView.setEnabled(false);
            mLoginView.setBackgroundResource(R.drawable.shape_login_unclick);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                presenter.setUserNameChange();
                break;
            case R.id.iv_cipher:
                presenter.setPassWorkChange();
                break;
            case R.id.tv_login:
                String userName = mUserNameView.getText().toString().trim();
                String passWord = mPassWordView.getText().toString().trim();

                int length = userName.length();
                if (length < 11 || length > 16) {
                    return;
                }

                presenter.LoginByAccount(userName, passWord);
                break;
            case R.id.tv_forget_password:
                ForgetPassWordActivity.LaunchActivity(LoginActivity.this);
                break;
            case R.id.tv_fast_registered:
                RegisterActivity.LaunchActivity(LoginActivity.this);
                break;
            case R.id.iv_third_qq:
                presenter.LoginByQQ();
                break;
            case R.id.iv_third_wx:
                presenter.LoginByWX();
                break;
        }
    }
}
