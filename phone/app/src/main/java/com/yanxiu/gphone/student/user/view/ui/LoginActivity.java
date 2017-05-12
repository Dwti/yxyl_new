package com.yanxiu.gphone.student.user.view.ui;

import android.content.Context;
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
import android.widget.Toast;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.user.bean.LoginBean;
import com.yanxiu.gphone.student.user.presenter.impl.LoginPresenterImpl;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.view.WavesLayout;
import com.yanxiu.gphone.student.user.view.interf.LoginViewChangedListener;

import java.util.regex.Pattern;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:23.
 * Function :
 */

public class LoginActivity extends YanxiuBaseActivity implements LoginViewChangedListener, View.OnClickListener {

    private Context mContext;

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

    private Pattern pattern = Pattern.compile("[0-9]*");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=LoginActivity.this;
        presenter = new LoginPresenterImpl(LoginActivity.this);
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
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener((view, value, isEmpty) -> presenter.setPassWordValue(value));
    }

    private void initListener() {
        mClearView.setOnClickListener(LoginActivity.this);
        mCipherView.setOnClickListener(LoginActivity.this);
        mLoginView.setOnClickListener(LoginActivity.this);
        mForgetPassWordView.setOnClickListener(LoginActivity.this);
        mFastRegisteredView.setOnClickListener(LoginActivity.this);
        mThirdQQView.setOnClickListener(LoginActivity.this);
        mThirdWXView.setOnClickListener(LoginActivity.this);
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
        String userName;
        String passWord;
        switch (v.getId()) {
            case R.id.iv_clear:
                presenter.setUserNameChange();
                break;
            case R.id.iv_cipher:
                presenter.setPassWorkChange();
                break;
            case R.id.tv_login:
                userName = mUserNameView.getText().toString().trim();
                passWord = mPassWordView.getText().toString().trim();

                if (userName.length()<11||userName.length()>16){
                    Toast.makeText(mContext,getText(R.string.input_true_account),Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passWord.length()<6||passWord.length()>18){
                    Toast.makeText(mContext,getText(R.string.input_true_password),Toast.LENGTH_SHORT).show();
                    return;
                }

                presenter.LoginByAccount(userName, passWord);
                break;
            case R.id.tv_forget_password:
                userName = mUserNameView.getText().toString().trim();
                if (userName.length()!=11||!userName.substring(0,1).equals("1")||!pattern.matcher(userName).matches()){
                    userName="";
                }
                ForgetPassWordActivity.LaunchActivity(mContext,userName);
                break;
            case R.id.tv_fast_registered:
                RegisterActivity.LaunchActivity(mContext);
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
