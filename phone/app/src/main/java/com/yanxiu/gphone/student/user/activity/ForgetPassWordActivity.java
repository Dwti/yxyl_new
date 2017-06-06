package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.user.response.ForgerPassWordResponse;
import com.yanxiu.gphone.student.user.response.VerCodeResponse;
import com.yanxiu.gphone.student.user.request.ForgetPassWordRequest;
import com.yanxiu.gphone.student.user.request.SendVerCodeRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.util.time.CountDownManager;
import com.yanxiu.gphone.student.customviews.WavesLayout;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:26.
 * Function :
 */

public class ForgetPassWordActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private static final String INTENT_MOBILE = "mobile";
    private static final String TYPE = "1";

    private Context mContext;

    private EditText mMobileView;
    private ImageView mClearView;
    private EditText mVerCodeView;
    private TextView mSendVerCodeView;
    private TextView mNextView;
    private WavesLayout mWavesView;

    private boolean isMobileReady = false;
    private boolean isVerCodeReady = false;
    private SendVerCodeRequest mSendVerCodeRequest;
    private ForgetPassWordRequest mForgetPassWordRequest;
    private PublicLoadLayout rootView;
    private ImageView mBackView;
    private TextView mTitleView;

    public static void LaunchActivity(Context context, String mobile) {
        Intent intent = new Intent(context, ForgetPassWordActivity.class);
        intent.putExtra(INTENT_MOBILE, mobile);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ForgetPassWordActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_forget_password);
        setContentView(rootView);
        String mobile = getIntent().getStringExtra(INTENT_MOBILE);
        initView();
        listener();
        initData(mobile);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendVerCodeRequest != null) {
            mSendVerCodeRequest.cancelRequest();
            mSendVerCodeRequest = null;
        }
        if (mForgetPassWordRequest != null) {
            mForgetPassWordRequest.cancelRequest();
            mForgetPassWordRequest = null;
        }
    }

    private void initView() {
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        mMobileView = (EditText) findViewById(R.id.ed_mobile);
        mClearView = (ImageView) findViewById(R.id.iv_clear);
        mVerCodeView = (EditText) findViewById(R.id.ed_ver_code);
        mSendVerCodeView = (TextView) findViewById(R.id.tv_send_verCode);
        mNextView = (TextView) findViewById(R.id.tv_next);
        mWavesView = (WavesLayout) findViewById(R.id.wl_forget_waves);
    }

    private void initData(String mobile) {
        mBackView.setVisibility(View.VISIBLE);
        mClearView.setEnabled(false);
        mSendVerCodeView.setEnabled(false);
        mNextView.setEnabled(false);
        mTitleView.setText(getText(R.string.forgetpassword));
        mMobileView.setText(mobile);
        if (mobile.length() > 0) {
            mMobileView.setSelection(mobile.length());
        }
    }

    private void listener() {
        mBackView.setOnClickListener(ForgetPassWordActivity.this);
        mClearView.setOnClickListener(ForgetPassWordActivity.this);
        mSendVerCodeView.setOnClickListener(ForgetPassWordActivity.this);
        mNextView.setOnClickListener(ForgetPassWordActivity.this);
        EditTextManger.getManager(mMobileView).setInputOnlyNumber().setTextChangedListener(ForgetPassWordActivity.this);
        EditTextManger.getManager(mVerCodeView).setInputOnlyNumber().setTextChangedListener(ForgetPassWordActivity.this);
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
            mNextView.setEnabled(true);
        } else {
            mWavesView.setCanShowWave(false);
            mNextView.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        String mobileCode;
        String verCode;
        switch (v.getId()) {
            case R.id.iv_left:
                ForgetPassWordActivity.this.finish();
                EditTextManger.getManager(mTitleView).hideSoftInput(mContext);
                break;
            case R.id.iv_clear:
                mMobileView.setText("");
                break;
            case R.id.tv_send_verCode:
                mobileCode = mMobileView.getText().toString().trim();
                if (mobileCode.length() != 11 || !mobileCode.substring(0, 1).equals("1")) {
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }
                sendVerCode(mobileCode);
                break;
            case R.id.tv_next:
                mobileCode = mMobileView.getText().toString().trim();
                verCode = mVerCodeView.getText().toString().trim();
                if (mobileCode.length() != 11 || !mobileCode.substring(0, 1).equals("1")) {
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }
                if (verCode.length() != 4) {
                    ToastManager.showMsg(getText(R.string.input_true_verCode));
                    return;
                }
                onNext(mobileCode, verCode);
                break;
        }
    }

    private void sendVerCode(String mobile) {
        rootView.showLoadingView();
        mSendVerCodeRequest = new SendVerCodeRequest();
        mSendVerCodeRequest.mobile = mobile;
        mSendVerCodeRequest.type = TYPE;
        mSendVerCodeRequest.startRequest(VerCodeResponse.class, new EXueELianBaseCallback<VerCodeResponse>() {

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

    private void onNext(final String mobile, String verCode) {
        rootView.showLoadingView();
        mForgetPassWordRequest = new ForgetPassWordRequest();
        mForgetPassWordRequest.mobile = mobile;
        mForgetPassWordRequest.code = verCode;
        mForgetPassWordRequest.type = TYPE;
        mForgetPassWordRequest.startRequest(ForgerPassWordResponse.class, new EXueELianBaseCallback<ForgerPassWordResponse>() {

            @Override
            protected void onResponse(RequestBase request, ForgerPassWordResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    LoginInfo.setMobile(mobile);
                    ResetPassWordActivity.LaunchActivity(mContext);
                    ForgetPassWordActivity.this.finish();
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

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        if (view == mMobileView) {
            if (isEmpty) {
                isMobileReady = false;
            } else {
                isMobileReady = true;
            }
            setSendVerCodeViewFocusChange(isMobileReady);
            setEditMobileIsEmpty(isEmpty);
        } else if (view == mVerCodeView) {
            if (isEmpty) {
                isVerCodeReady = false;
            } else {
                isVerCodeReady = true;
            }
        }
        setButtonFocusChange(isMobileReady && isVerCodeReady);
    }
}
