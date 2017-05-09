package com.yanxiu.gphone.student.login.view.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.login.bean.LoginBean;
import com.yanxiu.gphone.student.login.presenter.impl.LoginPresenterImpl;
import com.yanxiu.gphone.student.login.view.interf.LoginViewChangedListener;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:23.
 * Function :
 */

public class LoginActivity extends Activity implements LoginViewChangedListener{

    private LoginPresenterImpl presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter=new LoginPresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null) {
            presenter.onDestory();
            presenter=null;
        }
    }

    @Override
    public void onHttpStart(int uuid) {

    }

    @Override
    public void onReturntError(int uuid, LoginBean bean) {

    }

    @Override
    public void onSuccess(int uuid, LoginBean bean) {

    }

    @Override
    public void onCancel(int uuid) {

    }

    @Override
    public void onNetWorkError(int uuid, String msg) {

    }

    @Override
    public void onDataError(int uuid, String msg) {

    }

    @Override
    public void onHttpFinished(int uuid) {

    }

    @Override
    public void setButtonFocusChanged(boolean hasFocus) {

    }
}
