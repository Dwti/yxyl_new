package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
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

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.homepage.MainActivity;
import com.yanxiu.gphone.student.user.Response.LoginResponse;
import com.yanxiu.gphone.student.user.http.LoginRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.customviews.WavesLayout;

import java.util.regex.Pattern;
@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:23.
 * Function :
 */

public class LoginActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private Context mContext;

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

    /**
     * The default username and password are empty
     */
    private boolean isUserNameReady = false;
    private boolean isPassWordReady = false;
    /**
     * The default password is cipher
     */
    private boolean isCipher = true;

    private Pattern pattern = Pattern.compile("[0-9]*");
    private LoginRequest mLoginRequest;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=LoginActivity.this;
        initView();
        listener();
        initData();
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
        mTitleView= (RelativeLayout) findViewById(R.id.include_top);
        mWavesView= (WavesLayout) findViewById(R.id.wl_login_waves);
    }

    private void initData() {
        mTitleView.setVisibility(View.INVISIBLE);
        mClearView.setEnabled(false);
        mLoginView.setEnabled(false);
    }

    private void listener() {
        mClearView.setOnClickListener(LoginActivity.this);
        mCipherView.setOnClickListener(LoginActivity.this);
        mLoginView.setOnClickListener(LoginActivity.this);
        mForgetPassWordView.setOnClickListener(LoginActivity.this);
        mFastRegisteredView.setOnClickListener(LoginActivity.this);
        mThirdQQView.setOnClickListener(LoginActivity.this);
        mThirdWXView.setOnClickListener(LoginActivity.this);
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(LoginActivity.this);
        EditTextManger.getManager(mUserNameView).setInputNumberAndLetter().setTextChangedListener(LoginActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoginRequest!=null){
            mLoginRequest.cancelRequest();
            mLoginRequest=null;
        }
    }

    private void setEditUserNameIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            mClearView.setEnabled(false);
            mClearView.setVisibility(View.INVISIBLE);
        } else {
            mClearView.setEnabled(true);
            mClearView.setVisibility(View.VISIBLE);
        }
    }

    private void setEditPassWordChange(boolean isCipher) {
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

    private void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mWavesView.setCanShowWave(true);
            mLoginView.setEnabled(true);
        } else {
            mWavesView.setCanShowWave(false);
            mLoginView.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        String userName;
        String passWord;
        switch (v.getId()) {
            case R.id.iv_clear:
                mUserNameView.setText("");
                break;
            case R.id.iv_cipher:
                this.isCipher = !isCipher;
                setEditPassWordChange(isCipher);
                break;
            case R.id.tv_login:
                userName = mUserNameView.getText().toString().trim();
                passWord = mPassWordView.getText().toString().trim();

                if (userName.length()<11||userName.length()>16){
                    ToastManager.showMsg(getText(R.string.input_true_account));
                    return;
                }

                if (passWord.length()<6||passWord.length()>18){
                    ToastManager.showMsg(getText(R.string.input_true_password));
                    return;
                }

                LoginByAccount(userName, passWord);
                break;
            case R.id.tv_forget_password:
                userName = mUserNameView.getText().toString().trim();
                if (userName.length()!=11||!userName.substring(0,1).equals("1")||!pattern.matcher(userName).matches()){
                    userName="";
                }
                ForgetPassWordActivity.LaunchActivity(mContext,userName);
                break;
            case R.id.tv_fast_registered:
//                RegisterActivity.LaunchActivity(mContext);
                ChooseLocationActivity.LaunchActivity(mContext);
                break;
            case R.id.iv_third_qq:
                LoginByQQ();
                break;
            case R.id.iv_third_wx:
                LoginByWX();
                break;
        }
    }

    private void LoginByAccount(String user_name, String pass_word) {
        mLoginRequest = new LoginRequest();
        mLoginRequest.mobile=user_name;
        mLoginRequest.password=pass_word;
        mLoginRequest.startRequest(LoginResponse.class, new HttpCallback<LoginResponse>() {
            @Override
            public void onSuccess(RequestBase request, LoginResponse ret) {
                if (ret.status.getCode()==0){
                    LoginInfo.savaCacheData(ret.data.get(0));
                    MainActivity.invoke(LoginActivity.this);
                    LoginActivity.this.finish();
                }else if (ret.status.getCode()==80){
                    JoinClassActivity.LaunchActivity(mContext);
                }else {
                    ToastManager.showMsg(ret.status.getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    private void LoginByWX() {
    }

    private void LoginByQQ() {
    }

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        if (view==mUserNameView){
            if (isEmpty){
                isUserNameReady = false;
            }else {
                isUserNameReady = true;
            }
            setEditUserNameIsEmpty(isEmpty);
        }else if (view==mPassWordView){
            if (isEmpty){
                isPassWordReady = false;
            }else {
                isPassWordReady = true;
            }
        }
        setButtonFocusChange(isUserNameReady&&isPassWordReady);
    }
}
