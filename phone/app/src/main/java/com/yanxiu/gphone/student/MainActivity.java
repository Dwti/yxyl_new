package com.yanxiu.gphone.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yanxiu.gphone.student.login.view.ui.LoginActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, LoginActivity.class));
    }
}
