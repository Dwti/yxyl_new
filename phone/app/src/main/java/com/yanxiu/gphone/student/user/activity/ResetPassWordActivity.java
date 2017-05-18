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
import com.yanxiu.gphone.student.user.Response.ResetPassWordResponse;
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
public class ResetPassWordActivity extends YanxiuBaseActivity implements View.OnClickListener{

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

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,ResetPassWordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        mContext=ResetPassWordActivity.this;
        initView();
        initData();
        Listener();
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
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(new EditTextManger.onTextLengthChangedListener() {
            @Override
            public void onChanged(EditText view, String value, boolean isEmpty) {
                if (isEmpty){
                    isPassWordReady=false;
                }else {
                    isPassWordReady=true;
                }
                setButtonFocusChange(isPassWordReady&&isPassWordAgainReady);
            }
        });
        EditTextManger.getManager(mPassWordAgainView).setInputAllNotHanzi().setTextChangedListener(new EditTextManger.onTextLengthChangedListener() {
            @Override
            public void onChanged(EditText view, String value, boolean isEmpty) {
                if (isEmpty){
                    isPassWordAgainReady=false;
                }   else {
                    isPassWordAgainReady=true;
                }
                setButtonFocusChange(isPassWordReady&&isPassWordAgainReady);
            }
        });
    }

    private void Listener() {
        mResetPassWordView.setOnClickListener(ResetPassWordActivity.this);
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

    public void onResetPassWord(String mobile, String passWord) {
        mResetPassWordRequest=new ResetPassWordRequest();
        mResetPassWordRequest.mobile=mobile;
        mResetPassWordRequest.password=passWord;
        mResetPassWordRequest.startRequest(ResetPassWordResponse.class, new HttpCallback<ResetPassWordResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResetPassWordResponse ret) {
                if (ret.status.getCode()==0){
                    ToastManager.showMsg(getText(R.string.reset_password_success));
                    LoginActivity.LaunchActivity(mContext);
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

    public void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus){
            mWavesView.setCanShowWave(true);
            mResetPassWordView.setEnabled(true);
        }else {
            mWavesView.setCanShowWave(false);
            mResetPassWordView.setEnabled(false);
        }
    }
}
