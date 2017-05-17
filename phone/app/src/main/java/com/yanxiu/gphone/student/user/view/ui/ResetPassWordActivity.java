package com.yanxiu.gphone.student.user.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.user.bean.ResetPassWordBean;
import com.yanxiu.gphone.student.user.presenter.impl.ResetPassWordPresenterImpl;
import com.yanxiu.gphone.student.user.view.interf.ResetPassWordViewChangedListener;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.view.WavesLayout;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/16 15:41.
 * Function :
 */
public class ResetPassWordActivity extends YanxiuBaseActivity implements ResetPassWordViewChangedListener ,View.OnClickListener{

    private Context mContext;
    private TextView mResetPassWordView;
    private EditText mPassWordView;
    private EditText mPassWordAgainView;
    private ResetPassWordPresenterImpl presenter;
    private WavesLayout mWavesView;
    private String mobile;
    private String verCode;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,ResetPassWordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        mContext=ResetPassWordActivity.this;
        presenter=new ResetPassWordPresenterImpl(ResetPassWordActivity.this);
        initView();
        initData();
        Listener();
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
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener((view, value, isEmpty) -> presenter.setPassWordValue(value));
        EditTextManger.getManager(mPassWordAgainView).setInputAllNotHanzi().setTextChangedListener((view, value, isEmpty) -> presenter.setPassWordAgainValue(value));
    }

    private void Listener() {
        mResetPassWordView.setOnClickListener(ResetPassWordActivity.this);
    }

    @Override
    public void onHttpStart(int uuid) {

    }

    @Override
    public void onReturntError(int uuid, ResetPassWordBean resetPassWordBean) {

    }

    @Override
    public void onSuccess(int uuid, ResetPassWordBean resetPassWordBean) {

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
        String passWord;
        String passWordAgain;
        switch (v.getId()){
            case R.id.tv_reset_password:
                passWord=mPassWordView.getText().toString().trim();
                passWordAgain=mPassWordAgainView.getText().toString().trim();
                if (passWord.length()<6||passWord.length()>18){
                    return;
                }
                if(passWordAgain.length()<6||passWordAgain.length()>18){
                    return;
                }
                if (!passWord.endsWith(passWordAgain)){
                    return;
                }
                presenter.onResetPassWord(mobile,verCode,passWord);
                break;
        }
    }

    @Override
    public void onButtonFocusChange(boolean hasFocus) {
        if (hasFocus){
            mWavesView.setCanShowWave(true);
            mResetPassWordView.setEnabled(true);
            mResetPassWordView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_login_click));
        }else {
            mWavesView.setCanShowWave(false);
            mResetPassWordView.setEnabled(false);
            mResetPassWordView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_login_unclick));
        }
    }
}
