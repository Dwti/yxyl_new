package com.yanxiu.gphone.student.user.setting.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/27 15:08.
 * Function :
 */
public class AboutActivity extends YanxiuBaseActivity implements View.OnClickListener{

    private Context mContext;
    private ImageView mBackView;
    private TextView mTitleView,mVersion;
    private ImageView mTopView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=AboutActivity.this;
        setContentView(R.layout.activity_about);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView= (ImageView) findViewById(R.id.iv_top);
        mVersion = (TextView) findViewById(R.id.tv_version);
    }

    private void listener() {
        mBackView.setOnClickListener(AboutActivity.this);
    }

    private void initData() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            mVersion.setText("学生端-V" + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mVersion.setText("版本未知");
        }
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText("");
        mBackView.setBackgroundResource(R.drawable.selector_white_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                AboutActivity.this.finish();
                break;
            case R.id.ll_pass_word:
                PrivacyActivity.LaunchActivity(mContext, Constants.PRIVACY_POLICY_URL);
                break;
        }
    }
}
