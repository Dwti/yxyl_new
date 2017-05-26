package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.user.response.ResetPassWordResponse;
import com.yanxiu.gphone.student.user.http.ResetPassWordRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
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
    private String mobile;
    private String verCode;
    /**
     * default passwords are empty
     * */
    private boolean isPassWordReady = false;
    private boolean isPassWordAgainReady = false;
    private ResetPassWordRequest mResetPassWordRequest;
    private PublicLoadLayout rootView;

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
        rootView.finish();
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
        mPassWordView= (EditText) findViewById(R.id.ed_pass_word);
        mPassWordAgainView= (EditText) findViewById(R.id.ed_pass_word_again);
        mResetPassWordView= (TextView) findViewById(R.id.tv_reset_password);
        mWavesView= (WavesLayout) findViewById(R.id.wl_reset_waves);
    }

    private void initData() {
        mWavesView.setCanShowWave(false);
        mResetPassWordView.setEnabled(false);
    }

    private void listener() {
        mResetPassWordView.setOnClickListener(ResetPassWordActivity.this);
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(ResetPassWordActivity.this);
        EditTextManger.getManager(mPassWordAgainView).setInputAllNotHanzi().setTextChangedListener(ResetPassWordActivity.this);
    }

    @Override
    public void onClick(View v) {
        String passWord;
        String passWordAgain;
        switch (v.getId()){
            case R.id.tv_reset_password:
                passWord=mPassWordView.getText().toString().trim();
                passWordAgain=mPassWordAgainView.getText().toString().trim();
                if (!passWord.endsWith(passWordAgain)){
                    ToastManager.showMsg(getText(R.string.input_password_not_same));
                    return;
                }
                if (passWord.length()<6||passWord.length()>18){
                    ToastManager.showMsg(getText(R.string.input_password_error));
                    return;
                }
                onResetPassWord(mobile,passWord);
                break;
        }
    }

    private void onResetPassWord(String mobile, String passWord) {
        rootView.showLoadingView();
        mResetPassWordRequest=new ResetPassWordRequest();
        mResetPassWordRequest.mobile=mobile;
        mResetPassWordRequest.password=passWord;
        mResetPassWordRequest.startRequest(ResetPassWordResponse.class, new HttpCallback<ResetPassWordResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResetPassWordResponse ret) {
                rootView.hiddenLoadingView();
                if (ret.status.getCode()==0){
                    ToastManager.showMsg(getText(R.string.reset_password_success));
                    LoginActivity.LaunchActivity(mContext);
                }else {
                    ToastManager.showMsg(ret.status.getDesc());
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
