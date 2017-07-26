package com.yanxiu.gphone.student.user.setting.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.login.activity.LoginActivity;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

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

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SettingActivity.this;
        setContentView(R.layout.activity_setting);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);
        mMobileCodeView = (TextView) findViewById(R.id.tv_mobile_code);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                SettingActivity.this.finish();
                break;
            case R.id.ll_bind_mobile:
                String mobile=LoginInfo.getMobile();
                if (TextUtils.isEmpty(mobile)){
                    BindMobileActivity.LaunchActivity(mContext,BindMobileActivity.COME_TYPE_SETTING);
                }else {
                    CheckMobileActivity.LaunchActivity(mContext,mobile);
                }
                break;
            case R.id.ll_change_password:
                ToastManager.showMsg("2");
                break;
            case R.id.ll_check_updata:
                ToastManager.showMsg("3");
                break;
            case R.id.ll_about:
                ToastManager.showMsg("4");
                break;
            case R.id.wl_login_out:
                LoginInfo.LogOut();
                LoginActivity.LaunchActivity(mContext);
                break;
        }
    }
}
