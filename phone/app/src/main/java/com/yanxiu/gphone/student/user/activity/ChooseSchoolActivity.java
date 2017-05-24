package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/22 17:54.
 * Function :
 */
public class ChooseSchoolActivity extends YanxiuBaseActivity {

    private Context mContext;

    public static void LuanchActivity(Context context){
        Intent intent=new Intent(context,ChooseSchoolActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseschool);
        mContext=ChooseSchoolActivity.this;
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
