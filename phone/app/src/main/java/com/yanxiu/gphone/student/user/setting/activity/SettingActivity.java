package com.yanxiu.gphone.student.user.setting.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.login.activity.LoginActivity;
import com.yanxiu.gphone.student.login.response.UserMessageBean;
import com.yanxiu.gphone.student.user.setting.bean.BindMobileMessage;
import com.yanxiu.gphone.student.util.ActivityManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.UpdateUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/25 16:47.
 * Function :
 */
public class SettingActivity extends YanxiuBaseActivity implements View.OnClickListener {

    private Context mContext;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private TextView mMobileCodeView;
    private LinearLayout mBindMobileLayout;
    private LinearLayout mChangePassWordLayout;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SettingActivity.this;
        EventBus.getDefault().register(mContext);
        setContentView(R.layout.activity_setting);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);
        mMobileCodeView = (TextView) findViewById(R.id.tv_mobile_code);
        mBindMobileLayout= (LinearLayout) findViewById(R.id.ll_bind_mobile);
        mChangePassWordLayout= (LinearLayout) findViewById(R.id.ll_change_password);
    }

    private void listener() {
        mBackView.setOnClickListener(SettingActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.setting);
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));

        String mobile = LoginInfo.getMobile();
        if (!TextUtils.isEmpty(mobile)) {
            mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        }
        mMobileCodeView.setText(mobile);

        if (!UserMessageBean.LOGIN_ACCOUNT.equals(LoginInfo.getLoginType())){
            mBindMobileLayout.setVisibility(View.GONE);
            mChangePassWordLayout.setVisibility(View.GONE);
        }

        mBackView.setBackgroundResource(R.drawable.selector_back);
    }

    public void onEventMainThread(BindMobileMessage message) {
        if (message != null) {
            SettingActivity.this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                SettingActivity.this.finish();
                break;
            case R.id.ll_bind_mobile:
                String mobile = LoginInfo.getMobile();
                if (TextUtils.isEmpty(mobile)) {
                    BindMobileActivity.LaunchActivity(mContext, BindMobileActivity.COME_TYPE_SETTING);
                } else {
                    CheckMobileActivity.LaunchActivity(mContext, mobile);
                }
                break;
            case R.id.ll_change_password:
                ChangePassWordActivity.LaunchActivity(mContext);
                break;
            case R.id.ll_check_updata:
                UpdateUtil.Initialize(this,true);
                break;
            case R.id.ll_about:
                AboutActivity.LaunchActivity(mContext);
                break;
            case R.id.wl_login_out:
                PushManager.getInstance().unBindAlias(this.getApplicationContext(), String.valueOf(LoginInfo.getUID()), true);
                LoginInfo.LogOut();
                ActivityManger.LogOut();
                LoginActivity.LaunchActivity(mContext);
                break;
        }
    }
}
