package com.yanxiu.gphone.student.user.activity;

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

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.homepage.MainActivity;
import com.yanxiu.gphone.student.user.bean.LoginBean;
import com.yanxiu.gphone.student.user.http.LoginRequestTask;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.NetWorkUtils;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.util.view.WavesLayout;

import java.util.regex.Pattern;
@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:23.
 * Function :
 */

public class LoginActivity extends YanxiuBaseActivity implements View.OnClickListener {

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
     * Login using the account password
     */
    private static final int UUID_ACCOUNT = 0x000;
    /**
     * Login using the wx
     */
    private static final int UUID_WX = 0x001;
    /**
     * Login using the qq
     */
    private static final int UUID_QQ = 0x002;

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
    private LoginRequestTask request;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=LoginActivity.this;
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
        EditTextManger.getManager(mUserNameView).setInputNumberAndLetter().setTextChangedListener(new EditTextManger.onTextLengthChangedListener() {
            @Override
            public void onChanged(EditText view, String value, boolean isEmpty) {
                if (isEmpty){
                    isUserNameReady = false;
                }else {
                    isUserNameReady = true;
                }
                setEditUserNameIsEmpty(isEmpty);
                setButtonFocusChange(isUserNameReady&&isPassWordReady);
            }
        });
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(new EditTextManger.onTextLengthChangedListener() {
            @Override
            public void onChanged(EditText view, String value, boolean isEmpty) {
                if (isEmpty){
                    isPassWordReady = false;
                }else {
                    isPassWordReady = true;
                }
                setButtonFocusChange(isUserNameReady&&isPassWordReady);
            }
        });
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
        if (request!=null){
            request.cancelRequest();
            request=null;
        }
    }

    public void onHttpStart(int uuid) {

    }

    public void onReturntError(int uuid, LoginBean bean) {
        if (uuid==UUID_ACCOUNT){
            if (bean.status.getCode()==80){
                //to perfect information
                ToastManager.showMsg("to perfect information");
            }else {
                ToastManager.showMsg(bean.status.getDesc());
            }
        }
    }

    public void onHttpSuccess(int uuid, LoginBean bean) {
        if (uuid==UUID_ACCOUNT){
            LoginInfo.savaCacheData(bean.data.get(0));
            MainActivity.invoke(LoginActivity.this);
            LoginActivity.this.finish();
        }
    }

    public void onNetWorkError(int uuid, String msg) {
        ToastManager.showMsg(getText(R.string.net_null));
    }

    public void onDataError(int uuid, String msg) {
        ToastManager.showMsg(getText(R.string.data_error));
    }

    public void onHttpFinished(int uuid) {

    }

    public void setEditUserNameIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            mClearView.setEnabled(false);
            mClearView.setVisibility(View.INVISIBLE);
        } else {
            mClearView.setEnabled(true);
            mClearView.setVisibility(View.VISIBLE);
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

    public void setButtonFocusChange(boolean hasFocus) {
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
                RegisterActivity.LaunchActivity(mContext);
                break;
            case R.id.iv_third_qq:
                LoginByQQ();
                break;
            case R.id.iv_third_wx:
                LoginByWX();
                break;
        }
    }
    public void LoginByAccount(String user_name, String pass_word) {
        onHttpStart(UUID_ACCOUNT);
        request = new LoginRequestTask();
        request.mobile=user_name;
        request.password=pass_word;
        request.startRequest(LoginBean.class, new HttpCallback<LoginBean>() {
            @Override
            public void onSuccess(RequestBase request, LoginBean ret) {
                if (ret.status.getCode()==0){
                    onHttpSuccess(UUID_ACCOUNT,ret);
                }else {
                    onReturntError(UUID_ACCOUNT,ret);
                }
                onHttpFinished(UUID_ACCOUNT);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                if (NetWorkUtils.isNetAvailable()) {
                    onDataError(UUID_ACCOUNT,error.getMessage());
                }else {
                    onNetWorkError(UUID_ACCOUNT,error.getMessage());
                }
                onHttpFinished(UUID_ACCOUNT);
            }
        });
    }


    public void LoginByWX() {
    }

    public void LoginByQQ() {
    }

}
