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
import com.yanxiu.gphone.student.user.setting.request.BindMobileRequest;
import com.yanxiu.gphone.student.user.setting.request.SendVerCodeBindMobileRequest;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.util.time.CountDownManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/26 16:48.
 * Function :
 */
public class BindMobileActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener {

    public static final String COME_TYPE_CHECK_MOBILE = "check_mobile";
    public static final String COME_TYPE_SETTING = "setting";

    private static final String COME_TYPE_KEY = "come_type";
    private static final String TYPE = "0";

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private EditText mMobileView;
    private ImageView mClearMobileView;
    private EditText mVerCodeView;
    private TextView mSendVerCodeView;
    private WavesLayout mWavesView;
    private TextView mSureView;

    private boolean isMobileReady = false;
    private boolean isVerCodeReady = false;

    private SendVerCodeBindMobileRequest mVerCodeBindMobileRequest;
    private BindMobileRequest mBindMobileRequest;

    public static void LaunchActivity(Context context, String comeFrom) {
        Intent intent = new Intent(context, BindMobileActivity.class);
        intent.putExtra(COME_TYPE_KEY, comeFrom);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BindMobileActivity.this;
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
        mClearMobileView = (ImageView) findViewById(R.id.iv_clear);
        mVerCodeView = (EditText) findViewById(R.id.ed_ver_code);
        mSendVerCodeView = (TextView) findViewById(R.id.tv_send_verCode);
        mWavesView = (WavesLayout) findViewById(R.id.wl_waves);
        mSureView = (TextView) findViewById(R.id.tv_ok);
    }

    private void listener() {
        mBackView.setOnClickListener(BindMobileActivity.this);
        mSendVerCodeView.setOnClickListener(BindMobileActivity.this);
        mSureView.setOnClickListener(BindMobileActivity.this);
        mClearMobileView.setOnClickListener(BindMobileActivity.this);

        EditTextManger.getManager(mMobileView).setInputOnlyNumber().setTextChangedListener(BindMobileActivity.this);
        EditTextManger.getManager(mVerCodeView).setInputOnlyNumber().setTextChangedListener(BindMobileActivity.this);
    }

    private void initData() {

        String comeFrom = getIntent().getStringExtra(COME_TYPE_KEY);

        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        if (COME_TYPE_CHECK_MOBILE.equals(comeFrom)) {
            mTitleView.setText(R.string.bind_new_mobile);
        } else {
            mTitleView.setText(R.string.setting_bind_mobile);
        }
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));

        mSureView.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVerCodeBindMobileRequest != null) {
            mVerCodeBindMobileRequest.cancelRequest();
            mVerCodeBindMobileRequest = null;
        }
        if (mBindMobileRequest!=null){
            mBindMobileRequest.cancelRequest();
            mBindMobileRequest=null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                EditTextManger.getManager(mSureView).hideSoftInput();
                BindMobileActivity.this.finish();
                break;
            case R.id.iv_clear:
                mMobileView.setText("");
                break;
            case R.id.tv_send_verCode:
                String mobileCode = mMobileView.getText().toString().replace(" ", "");
                if (mobileCode.length() != 11 || !mobileCode.substring(0, 1).equals("1")) {
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }
                sendVerCode(mobileCode);
                break;
            case R.id.tv_ok:
                String mMobileCode = mMobileView.getText().toString().replace(" ", "");
                String verCode = mVerCodeView.getText().toString().trim();
                if (mMobileCode.length() != 11 || !mMobileCode.substring(0, 1).equals("1")) {
                    ToastManager.showMsg(getText(R.string.input_true_mobile));
                    return;
                }
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

    private void checkMobile(final String mobile, String verCode) {
        rootView.showLoadingView();
        mBindMobileRequest=new BindMobileRequest();
        mBindMobileRequest.code=verCode;
        mBindMobileRequest.newMobile=mobile;
        mBindMobileRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0){
                    LoginInfo.saveMobile(mobile);
                    EventBus.getDefault().post(new BindMobileMessage());
                    BindMobileActivity.this.finish();
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
        if (view == mMobileView) {
            isMobileReady=!isEmpty;
            if (isMobileReady) {
                setAddorRemoveSpace(value);
            }
            setEditMobileIsEmpty(isEmpty);
            setSendVerCodeViewFocusChange(isMobileReady);
        } else if (view == mVerCodeView) {
            isVerCodeReady = !isEmpty;
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

    private void setAddorRemoveSpace(String value) {
        boolean isShouldReset = false;
        if (value.length() > 3) {
            if (value.length() == 4) {
                isShouldReset = true;
                if (value.substring(3, 4).equals(" ")) {
                    value = value.trim();
                } else {
                    value = value.substring(0, 3) + " " + value.substring(3, value.length());
                }
            } else if (value.length() == 9) {
                isShouldReset = true;
                if (!value.substring(3, 4).equals(" ")) {
                    value = value.substring(0, 3) + " " + value.substring(3, value.length());
                }
                if (value.substring(8, 9).equals(" ")) {
                    value = value.trim();
                } else {
                    value = value.substring(0, 8) + " " + value.substring(8, value.length());
                }
            } else {
                if (!value.substring(3, 4).equals(" ")) {
                    isShouldReset = true;
                    value = value.substring(0, 3) + " " + value.substring(3, value.length());
                }
                if (value.length() > 9) {
                    if (!value.substring(8, 9).equals(" ")) {
                        isShouldReset = true;
                        value = value.substring(0, 8) + " " + value.substring(8, value.length());
                    }
                }
            }
        }
        if (isShouldReset) {
            mMobileView.setText(value);
            mMobileView.setSelection(mMobileView.length());
        }
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

    private void setEditMobileIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            mClearMobileView.setEnabled(false);
            mClearMobileView.setVisibility(View.INVISIBLE);
        } else {
            mClearMobileView.setEnabled(true);
            mClearMobileView.setVisibility(View.VISIBLE);
        }
    }

}
