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
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.user.response.ResetPassWordResponse;
import com.yanxiu.gphone.student.user.request.ResetPassWordRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.customviews.WavesLayout;
@SuppressWarnings("all")

/**
 * Created by Canghaixiao.
 * Time : 2017/5/16 15:41.
 * Function :
 */
public class ResetPassWordActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private Context mContext;
    private TextView mResetPassWordView;
    private EditText mPassWordView;
    private EditText mPassWordAgainView;
    private WavesLayout mWavesView;
    private String verCode;
    /**
     * default passwords are empty
     * */
    private boolean isPassWordReady = false;
    private boolean isPassWordAgainReady = false;
    private ResetPassWordRequest mResetPassWordRequest;
    private PublicLoadLayout rootView;
    private ImageView mBackView;
    private TextView mTitleView;
    private ImageView mCipherView;
    private ImageView mCipherAgainView;

    /**
     * default they are cipher
     * */
    private boolean isPassWordCipher=true;
    private boolean isPassWordAgainCipher=true;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,ResetPassWordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=ResetPassWordActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_resetpassword);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mResetPassWordRequest!=null){
            mResetPassWordRequest.cancelRequest();
            mResetPassWordRequest=null;
        }
    }

    private void initView() {
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        mPassWordView= (EditText) findViewById(R.id.ed_pass_word);
        mPassWordAgainView= (EditText) findViewById(R.id.ed_pass_word_again);
        mResetPassWordView= (TextView) findViewById(R.id.tv_reset_password);
        mWavesView= (WavesLayout) findViewById(R.id.wl_reset_waves);
        mCipherView= (ImageView) findViewById(R.id.iv_cipher);
        mCipherAgainView= (ImageView) findViewById(R.id.iv_cipher_again);
    }

    private void initData() {
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(getText(R.string.resetpassword));
        mWavesView.setCanShowWave(false);
        mResetPassWordView.setEnabled(false);
    }

    private void listener() {
        mBackView.setOnClickListener(ResetPassWordActivity.this);
        mResetPassWordView.setOnClickListener(ResetPassWordActivity.this);
        mCipherView.setOnClickListener(ResetPassWordActivity.this);
        mCipherAgainView.setOnClickListener(ResetPassWordActivity.this);
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(ResetPassWordActivity.this);
        EditTextManger.getManager(mPassWordAgainView).setInputAllNotHanzi().setTextChangedListener(ResetPassWordActivity.this);
    }

    @Override
    public void onClick(View v) {
        String passWord;
        String passWordAgain;
        switch (v.getId()){
            case R.id.iv_left:
                ResetPassWordActivity.this.finish();
                EditTextManger.getManager(mTitleView).hideSoftInput(mContext);
                break;
            case R.id.tv_reset_password:
                passWord=mPassWordView.getText().toString().trim();
                passWordAgain=mPassWordAgainView.getText().toString().trim();
                if (!passWord.equals(passWordAgain)){
                    ToastManager.showMsg(getText(R.string.input_password_not_same));
                    return;
                }
                if (passWord.length()<6||passWord.length()>18){
                    ToastManager.showMsg(getText(R.string.input_password_error));
                    return;
                }
                onResetPassWord(passWord);
                break;
            case R.id.iv_cipher:
                this.isPassWordCipher=!isPassWordCipher;
                setEditPassWordChange(mPassWordView,mCipherView,isPassWordCipher);
                break;
            case R.id.iv_cipher_again:
                this.isPassWordAgainCipher=!isPassWordAgainCipher;
                setEditPassWordChange(mPassWordAgainView,mCipherAgainView,isPassWordAgainCipher);
                break;
        }
    }

    private void setEditPassWordChange(EditText editText,ImageView imageView,boolean isCipher) {
        if (isCipher) {
//            imageView.setBackgroundResource();
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        String text = editText.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            editText.setSelection(text.length());
        }
    }

    private void onResetPassWord(String passWord) {
        rootView.showLoadingView();
        mResetPassWordRequest=new ResetPassWordRequest();
        mResetPassWordRequest.mobile= LoginInfo.getMobile();
        mResetPassWordRequest.password=passWord;
        mResetPassWordRequest.startRequest(ResetPassWordResponse.class, new EXueELianBaseCallback<ResetPassWordResponse>() {

            @Override
            protected void onResponse(RequestBase request, ResetPassWordResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0){
                    ToastManager.showMsg(getText(R.string.reset_password_success));
                    LoginActivity.LaunchActivity(mContext);
                    ResetPassWordActivity.this.finish();
                }else {
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

    private void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus){
            mWavesView.setCanShowWave(true);
            mResetPassWordView.setEnabled(true);
        }else {
            mWavesView.setCanShowWave(false);
            mResetPassWordView.setEnabled(false);
        }
    }

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        switch (view.getId()){
            case R.id.ed_pass_word:
                if (isEmpty){
                    isPassWordReady=false;
                }else {
                    isPassWordReady=true;
                }
                break;
            case R.id.ed_pass_word_again:
                if (isEmpty){
                    isPassWordAgainReady=false;
                }   else {
                    isPassWordAgainReady=true;
                }
                break;
        }
        setButtonFocusChange(isPassWordReady&&isPassWordAgainReady);
    }
}
