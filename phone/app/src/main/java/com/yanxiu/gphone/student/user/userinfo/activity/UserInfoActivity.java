package com.yanxiu.gphone.student.user.userinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/31 10:42.
 * Function :
 */
public class UserInfoActivity extends YanxiuBaseActivity implements View.OnClickListener{

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,UserInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=UserInfoActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_userinfo);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView= (ImageView) findViewById(R.id.iv_left);
        mTitleView= (TextView) findViewById(R.id.tv_title);
        mTopView=findViewById(R.id.include_top);
    }

    private void listener() {
        mBackView.setOnClickListener(UserInfoActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.user_message);
        mTitleView.setTextColor(ContextCompat.getColor(mContext,R.color.color_666666));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                finish();
                break;
        }
    }
}
