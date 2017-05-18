package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/18 16:47.
 * Function :
 */
public class CompleteInfoActivity extends YanxiuBaseActivity {

    private Context mContext;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,CompleteInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeinfo);
        initView();
        initData();
        Listener();
    }

    private void initView() {

    }

    private void initData() {

    }

    private void Listener() {

    }


}
