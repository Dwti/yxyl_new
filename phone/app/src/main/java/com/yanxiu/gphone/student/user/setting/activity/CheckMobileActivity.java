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
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.user.setting.bean.BindMobileMessage;
import com.yanxiu.gphone.student.user.setting.request.CheckMobileRequest;
import com.yanxiu.gphone.student.user.setting.request.SendVerCodeBindMobileRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.util.time.CountDownManager;

import de.greenrobot.event.EventBus;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/7/26 11:25.
 * Function :
 */
public class CheckMobileActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    private static final String MOBILE = "mobile";
    private static final String TYPE = "1";

    private Context mContext;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private PublicLoadLayout rootView;
    private EditText mMobileView;
    private EditText mVerCodeView;
    private TextView mSendVerCodeView;
    private WavesLayout mWavesView;
    private TextView mSureView;

    private boolean isMobileReady = false;
    private boolean isVerCodeReady = false;
    private SendVerCodeBindMobileRequest mVerCodeBindMobileRequest;
    private CheckMobileRequest mCheckMobileRequest;

    private String mMobileCode;

    public static void LaunchActivity(Context context, String mobile) {
        Intent intent = new Intent(context, CheckMobileActivity.class);
        intent.putExtra(MOBILE, mobile);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = CheckMobileActivity.this;
        EventBus.getDefault().register(mContext);
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_checkmobile);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);

        mMobileView = (EditText) findViewById(R.id.ed_mobile);
        mVerCodeView = (EditText) findViewById(R.id.ed_ver_code);
        mSendVerCodeView = (TextView) findViewById(R.id.tv_send_verCode);
        mWavesView = (WavesLayout) findViewById(R.id.wl_waves);
        mSureView = (TextView) findViewById(R.id.tv_ok);
    }

    private void listener() {
        mBackView.setOnClickListener(CheckMobileActivity.this);
        mSendVerCodeView.setOnClickListener(CheckMobileActivity.this);
        mSureView.setOnClickListener(CheckMobileActivity.this);

        EditTextManger.getManager(mMobileView).setInputOnlyNumber().setTextChangedListener(CheckMobileActivity.this);
        EditTextManger.getManager(mVerCodeView).setInputOnlyNumber().setTextChangedListener(CheckMobileActivity.this);
    }

    private void initData() {

        mMobileCode = getIntent().getStringExtra(MOBILE);

        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.check_mobile);
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));

        StringBuilder builder = new StringBuilder(mMobileCode);
        builder.insert(3, " ");
        builder.insert(8, " ");
        mMobileView.setText(builder);
        mMobileView.setEnabled(false);
        mSureView.setEnabled(false);

        mBackView.setBackgroundResource(R.drawable.selector_back);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
        if (mVerCodeBindMobileRequest != null) {
            mVerCodeBindMobileRequest.cancelRequest();
            mVerCodeBindMobileRequest = null;
        }
        if (mCheckMobileRequest != null) {
            mCheckMobileRequest.cancelRequest();
            mCheckMobileRequest = null;
        }
    }

    public void onEventMainThread(BindMobileMessage message) {
        if (message != null) {
            CheckMobileActivity.this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                EditTextManger.getManager(mSureView).hideSoftInput();
                CheckMobileActivity.this.finish();
                break;
            case R.id.tv_send_verCode:
                sendVerCode(mMobileCode);
                break;
            case R.id.tv_ok:
                String verCode = mVerCodeView.getText().toString().trim();
                if (verCode.length() != 4) {
                    ToastManager.showMsg(getText(R.string.input_true_verCode));
                    return;
                }
                checkMobile(mMobileCode, verCode);
                break;
        }
    }

    private void sendVerCode(String mobile) {
        rootView.showLoadingView();
        mVerCodeBindMobileRequest = new SendVerCodeBindMobileRequest();
        mVerCodeBindMobileRequest.mobile = mobile;
        mVerCodeBindMobileRequest.type = TYPE;
        mVerCodeBindMobileRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    startTiming();
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

    private void checkMobile(String mobile, String verCode) {
        rootView.showLoadingView();
        mCheckMobileRequest = new CheckMobileRequest();
        mCheckMobileRequest.mobile = mobile;
        mCheckMobileRequest.code = verCode;
        mCheckMobileRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode() == 0) {
                    BindMobileActivity.LaunchActivity(mContext, BindMobileActivity.COME_TYPE_CHECK_MOBILE);
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
        } else if (view == mVerCodeView) {
            if (isEmpty) {
                isVerCodeReady = false;
            } else {
                isVerCodeReady = true;
            }
        }
        setButtonFocusChange(isMobileReady && isVerCodeReady);
    }

    private void startTiming() {
        ToastManager.showMsg(getText(R.string.send_verCode_finish));
        CountDownManager.getManager().setScheduleListener(new CountDownManager.ScheduleListener() {
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

    private void setSendVerCodeViewFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mSendVerCodeView.setEnabled(true);
        } else {
            mSendVerCodeView.setEnabled(false);
        }
    }

    private void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mWavesView.setCanShowWave(true);
            mSureView.setEnabled(true);
        } else {
            mWavesView.setCanShowWave(false);
            mSureView.setEnabled(false);
        }
    }
}
