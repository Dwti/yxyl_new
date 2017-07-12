package com.yanxiu.gphone.student.mistake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/12 11:23.
 * Function :
 */
public class MistakeListActivity extends YanxiuBaseActivity {

    private Context mContext;

    public static void LuanchActivity(Context context){
        Intent intent=new Intent(context,MistakeListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=MistakeListActivity.this;
        PublicLoadLayout rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_chooselocation);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {

    }

    private void listener() {

    }

    private void initData() {

    }
}
