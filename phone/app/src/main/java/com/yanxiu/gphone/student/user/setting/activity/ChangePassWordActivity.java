package com.yanxiu.gphone.student.user.setting.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.user.setting.request.ChangePassWordRequest;
import com.yanxiu.gphone.student.user.setting.response.ChangePassWordResponse;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/27 11:58.
 * Function :
 */
public class ChangePassWordActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;

    private EditText mPassWordView;
    private ImageView mClearPassWordView;
    private EditText mPassWordNewView;
    private ImageView mClearPassWordNewView;
    private EditText mPassWordAgainView;
    private ImageView mClearPassWordAgainView;
    private WavesLayout mWavesView;
    private TextView mSureView;

    private boolean isPassWordReady = false;
    private boolean isPassWordNewReady = false;
    private boolean isPassWordAgainReady = false;

    private ChangePassWordRequest mChangePassWordRequest;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, ChangePassWordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ChangePassWordActivity.this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_changepassword);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChangePassWordRequest!=null){
            mChangePassWordRequest.cancelRequest();
            mChangePassWordRequest=null;
        }
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);

        mPassWordView = (EditText) findViewById(R.id.ed_pass_word);
        mClearPassWordView = (ImageView) findViewById(R.id.iv_clear_top);

        mPassWordNewView = (EditText) findViewById(R.id.ed_pass_word_new);
        mClearPassWordNewView = (ImageView) findViewById(R.id.iv_clear_center);

        mPassWordAgainView = (EditText) findViewById(R.id.ed_pass_word_again);
        mClearPassWordAgainView = (ImageView) findViewById(R.id.iv_clear_bottom);

        mWavesView = (WavesLayout) findViewById(R.id.wl_waves);
        mSureView = (TextView) findViewById(R.id.tv_ok);
    }

    private void listener() {
        mBackView.setOnClickListener(ChangePassWordActivity.this);
        mClearPassWordView.setOnClickListener(ChangePassWordActivity.this);
        mClearPassWordNewView.setOnClickListener(ChangePassWordActivity.this);
        mClearPassWordAgainView.setOnClickListener(ChangePassWordActivity.this);
        mWavesView.setOnClickListener(ChangePassWordActivity.this);

        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(ChangePassWordActivity.this);
        EditTextManger.getManager(mPassWordNewView).setInputAllNotHanzi().setTextChangedListener(ChangePassWordActivity.this);
        EditTextManger.getManager(mPassWordAgainView).setInputAllNotHanzi().setTextChangedListener(ChangePassWordActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.setting_change_password);
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));

        mSureView.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                EditTextManger.getManager(mBackView).hideSoftInput();
                ChangePassWordActivity.this.finish();
                break;
            case R.id.iv_clear_top:
                mPassWordView.setText("");
                break;
            case R.id.iv_clear_center:
                mPassWordNewView.setText("");
                break;
            case R.id.iv_clear_bottom:
                mPassWordAgainView.setText("");
                break;
            case R.id.wl_waves:
                String passWord=mPassWordView.getText().toString().trim();
                String passWordNew=mPassWordNewView.getText().toString().trim();
                String passWordAgain=mPassWordAgainView.getText().toString().trim();
                
                if (passWord.length()<6){
                    ToastManager.showMsg(getText(R.string.input_password_old_error));
                    return;
                }
                if (passWordNew.length()<6){
                    ToastManager.showMsg(getText(R.string.input_password_new_error));
                    return;
                }
                if (!passWordNew.equals(passWordAgain)){
                    ToastManager.showMsg(getText(R.string.input_password_not_same));
                    return;
                }
                
                changePassWord(passWord,passWordNew);
                break;
        }
    }

    private void changePassWord(String passWord,String passWordNew){
        rootView.showLoadingView();
        mChangePassWordRequest=new ChangePassWordRequest();
        mChangePassWordRequest.oldPass=passWord;
        mChangePassWordRequest.newPass=passWordNew;
        mChangePassWordRequest.startRequest(ChangePassWordResponse.class, new EXueELianBaseCallback<ChangePassWordResponse>() {
            @Override
            protected void onResponse(RequestBase request, ChangePassWordResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0){
                    ToastManager.showMsg(R.string.change_password_success);
                    LoginInfo.savePassWord(response.data.get(0).password);
                    LoginInfo.saveToken(response.data.get(0).token);
                    LoginInfo.saveUid(response.data.get(0).uid);
                    ChangePassWordActivity.this.finish();
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

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        if (view == mPassWordView) {
            isPassWordReady = !isEmpty;
            setClearButtonFocusChange(isPassWordReady,mPassWordView);
        } else if (view == mPassWordNewView) {
            isPassWordNewReady = !isEmpty;
            setClearButtonFocusChange(isPassWordNewReady,mPassWordNewView);
        } else if (view == mPassWordAgainView) {
            isPassWordAgainReady = !isEmpty;
            setClearButtonFocusChange(isPassWordAgainReady,mPassWordAgainView);
        }
        setSureButtonFocusChange(isPassWordReady&&isPassWordNewReady&&isPassWordAgainReady);
    }

    private void setClearButtonFocusChange(boolean hasFocus,View button){
        if (hasFocus){
            button.setEnabled(true);
            button.setVisibility(View.VISIBLE);
        }else {
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }
    }

    private void setSureButtonFocusChange(boolean hasFocus){
        if (hasFocus){
            mWavesView.setCanShowWave(true);
            mSureView.setEnabled(true);
        }else {
            mWavesView.setCanShowWave(false);
            mSureView.setEnabled(false);
        }
    }

}
