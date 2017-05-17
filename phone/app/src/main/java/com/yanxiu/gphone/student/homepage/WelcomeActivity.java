package com.yanxiu.gphone.student.homepage;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.yanxiu.gphone.student.base.YanxiuBaseActivity;

/**
 * Created by 戴延枫 on 2017/5/9.
 */

public class WelcomeActivity extends YanxiuBaseActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Todo 退出程序

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

    }

}